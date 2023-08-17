package com.logicgate.configuration.security.auth;

import lombok.*;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthRequest {

    private String usernameOrEmailOrMobile;
    private String password;
}
