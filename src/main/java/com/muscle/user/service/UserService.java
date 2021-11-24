package com.muscle.user.service;

import com.muscle.email.service.EmailSender;
import com.muscle.user.dto.*;
import com.muscle.user.entity.ConfirmationToken;
import com.muscle.user.entity.IronUser;
import com.muscle.user.entity.PasswordToken;
import com.muscle.user.repository.UserRepository;
import com.muscle.user.response.IronUserResponse;
import com.muscle.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.muscle.email.service.impl.EmailService.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Value("${email.confirm.link}")
    public String confirmEmailBaseLink;
    @Value("${password.reset.link}")
    public String resetPasswordBaseLink;
    @Value("${user.create.link}")
    public String createUserBaseLink;
    private final static String  USER_NOT_FOUND_MSG = "User %s not found in database";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final PasswordTokenService passwordTokenService;
    private final EmailSender emailSender;
    private final EmailValidator emailValidator;
    private final JwtUtil jwtUtil;

    public IronUser getUserFromHeader(String header) {
        return userRepository.findByUsername(jwtUtil.extractUsername(header))
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    public IronUserResponse getMyself(String header) {
        return getUserFromHeader(header).response();
    }

    public String getWelcomeMsg(String header) {
        return "Welcome " + getUserFromHeader(header).getUsername();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<IronUser> user = userRepository.findByUsername(username);


        if(!user.isPresent()) {
            user = userRepository.findByEmail(username);
        }
        IronUserDto ironUserDto = user.orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username))).dto();
        return IronUserDetails.builder()
                .id(ironUserDto.getId())
                .name(ironUserDto.getName())
                .lastName(ironUserDto.getLastName())
                .username(ironUserDto.getUsername())
                .email(ironUserDto.getEmail())
                .password(ironUserDto.getPassword())
                .locked(ironUserDto.getLocked())
                .enabled(ironUserDto.getEnabled())
                .authorities(ironUserDto.getRoles().stream().map(roleDto -> new SimpleGrantedAuthority(roleDto.getName())).collect(Collectors.toList()))
                .build();
    }

    public String signUpUser(IronUser ironUser){
        boolean usernameTaken = userRepository
                .findByUsername(ironUser.getUsername())
                .isPresent();

        boolean emailTaken = userRepository
                .findByEmail(ironUser.getEmail())
                .isPresent();

        if(usernameTaken || emailTaken) {
            throw new IllegalStateException("User already exists");
        }

        String encodedPassword = passwordEncoder.encode(ironUser.getPassword());

        ironUser.setPassword(encodedPassword);

        userRepository.save(ironUser);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .ironUser(ironUser)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableUser(String email) {
        IronUser ironUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        ironUser.setEnabled(true);

        userRepository.save(ironUser);
    }

    public Page<IronUser> getPaginatedUsers(Pageable pageable, String query) {
        return userRepository.findByUsernameContainsOrderByUsernameAsc(pageable, query);
    }

    public void requestPasswordChange(String email) {
        IronUser user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("User with this email doesn't exist. Try different email"));


        String token = UUID.randomUUID().toString();

        PasswordToken passwordToken = PasswordToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .ironUser(user)
                .build();

        passwordTokenService.savePasswordToken(passwordToken);

        String link = resetPasswordBaseLink + token;
        emailSender.send(user.getEmail(), buildPasswordEmail(user.getUsername(), link));
    }

    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        PasswordToken passwordToken = passwordTokenService.getToken(resetPasswordDto.getToken())
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        IronUser user = userRepository.findByUsername(passwordToken.getIronUser().getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (passwordToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Link expired");
        }

        LocalDateTime expiredAt = passwordToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Link expired");
        }

        validatePassword(resetPasswordDto.getPassword());

        String encodedPassword = passwordEncoder.encode(resetPasswordDto.getPassword());
        if(encodedPassword.equals(user.getPassword())) {
            throw new IllegalStateException("Your new password can not be the same as your old password");
        }
        user.setPassword(encodedPassword);
        passwordTokenService.setConfirmedAt(resetPasswordDto.getToken());

        userRepository.save(user);
    }

    public void changePassword(String header, ChangePasswordDto changePasswordDto) {
        IronUser user = getUserFromHeader(header);

        if(!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Old password you provided is incorrect");
        }

        String newPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());

        if(newPassword.equals(user.getPassword())) {
            throw new IllegalStateException("Your new password can not be the same as your old password");
        }

        validatePassword(changePasswordDto.getNewPassword());
        user.setPassword(newPassword);

        userRepository.save(user);
    }

    public void changeMyDetails(String header, ChangeUserDetailsDto changed) {
        IronUser user = getUserFromHeader(header);

        if(userRepository.findByEmail(changed.getEmail()).isPresent())
            throw new IllegalStateException("Email already taken");

        if(!emailValidator.test(changed.getEmail()))
            throw new IllegalArgumentException("New email is not valid");

        user.setEnabled(false);
        user.setEmail(changed.getEmail());
        userRepository.save(user);

        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .ironUser(user)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = confirmEmailBaseLink + token;
        emailSender.send(user.getEmail(), buildUserEmail(user.getUsername(), link));
    }

    public void changeUserDetails(Long id, ChangeUserDetailsDto changed) {
        IronUser user = userRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("User not found"));

        if(user.getLocked() == changed.isLock()) {
            if(user.getLocked()){
                throw new IllegalStateException("User is already locked");
            } else {
                throw new IllegalStateException("User is already unlocked");
            }
        } else {
            user.setLocked(changed.isLock());
            userRepository.save(user);
        }
    }

    public void validatePassword(String password) {
        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
            throw new IllegalStateException("Password must contain minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
        }
    }
}
