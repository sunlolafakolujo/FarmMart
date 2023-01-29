package com.logicgate.configuration.security.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthRequest {
    private String usernameOrEmailOrMobile;
    private String password;
}
