package com.logicgate.configuration.security.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthResponse {
    private String username;
    private String jwtToken;
}
