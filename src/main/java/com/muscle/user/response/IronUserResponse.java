package com.muscle.user.response;

import lombok.*;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class IronUserResponse {
    private Long id;
    private String name;
    private String lastName;
    private String username;
    private String email;
}