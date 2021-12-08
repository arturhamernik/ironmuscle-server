package com.muscle.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muscle.user.dto.*;
import com.muscle.user.entity.IronUser;
import com.muscle.user.response.BadgeResponse;
import com.muscle.user.response.IronUserResponse;
import com.muscle.user.service.BadgeService;
import com.muscle.user.service.UserService;
import com.muscle.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.muscle.user.util.JwtUtil.generateErrorBody;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BadgeService badgeService;

    /**
     * Refresh token
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String refresh_token = authorizationHeader.substring("Bearer ".length());
                    String username = jwtUtil.extractUsername(authorizationHeader);

                    UserDetails userDetails = userService.loadUserByUsername(username);
                    String access_token = jwtUtil.createToken(request, userDetails, 1000 * 60 * 60 * 24);

                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", access_token);
                    tokens.put("refresh_token", refresh_token);
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);

                } catch (Exception e) {
                    response.setStatus(UNAUTHORIZED.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), jwtUtil.generateErrorResponse(UNAUTHORIZED, e.getMessage()));
                }
            } else {
                throw new RuntimeException("Refresh token is missing");
            }
    }

    /**
     * Show user badges
     * @return
     */
    @GetMapping("/badges")
    public List<BadgeResponse> getBadges() {
        return badgeService.getBadges();
    }

    /**
     * Show user badges
     * @param header
     * @return
     */
    @GetMapping("/user/badges")
    public List<BadgeResponse> getUserBadges(@RequestHeader("Authorization") String header) {
        return badgeService.getUserBadges(header);
    }

    /**
     * Show info about currently logged user
     * @param header
     * @return
     */
    @GetMapping("/myself")
    public IronUserResponse getMyself(@RequestHeader("Authorization") String header) {
        return userService.getMyself(header);
    }

/*    *//**
     * Change my details
     * @param header
     * @param changeUserDetailsDto
     *//*
    @PutMapping("/myself")
    public void changeMyDetails(@RequestHeader("Authorization") String header, @RequestBody ChangeUserDetailsDto changeUserDetailsDto) {
        userService.changeMyDetails(header, changeUserDetailsDto);
    }*/

    /**
     * Change email
     * @param header
     * @param changeUserDetails
     */
    @PutMapping("/myself")
    public ResponseEntity changeEmail(@RequestHeader("Authorization") String header, @RequestBody ChangeUserDetails changeUserDetails) {
        try {
            userService.changeEmail(header, changeUserDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(generateErrorBody(400, e));
        }
    }

    /**
     * Lock user
     * @param id
     * @param changeUserDetails
     */
    @PutMapping("/user/lock")
    public void lockUser(@RequestParam Long id, @RequestBody ChangeUserDetails changeUserDetails) {
        userService.lockUser(id, changeUserDetails);
    }

    /**
     * Send reset password email
     * @param email
     */
    @PostMapping("/password/reset")
    public void requestPasswordReset(@RequestParam("email") String email) {
        userService.requestPasswordChange(email);
    }
    /**
     * Change password
     * @param resetPasswordDto
     */
    @PutMapping("/password/reset")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        userService.resetPassword(resetPasswordDto);
    }

    /**
     * Change password
     * @param header
     * @param changePasswordDto
     */
    @PostMapping("/password/change")
    public void changePassword(@RequestHeader("Authorization") String header, @RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(header, changePasswordDto);
    }

    /**
     * Get all users
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/users")
    Map<String, Object> getUsers(@RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "100") Integer size,
                                 @RequestParam(defaultValue = "") String query) {
        Pageable paging = PageRequest.of(page, size);
        Page<IronUser> userPage = userService.getPaginatedUsers(paging, query);

        List<IronUserDto> usersList = userPage.getContent()
                .stream().map(IronUser::dtoResponse).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", usersList);
        response.put("currentPage", userPage.getNumber());
        response.put("totalItems", userPage.getTotalElements());
        response.put("totalPages", userPage.getTotalPages());

        return response;
    }

    @PostMapping("/user/icon")
    public ResponseEntity changeUserImage(@RequestHeader("Authorization") String header, @RequestBody MultipartFile file) {
        try {
            userService.changeUserIcon(header, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(generateErrorBody(500, e));
        }
    }
}
