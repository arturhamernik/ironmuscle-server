package com.muscle.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequestDto {
    private final Optional<String> name;
    private final Optional<String> lastName;
    private final String username;
    private final String email;
    private final String password;
    private final List<String> roles;
}
