package com.logicgate.passwordtoken.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordTokenModel {
    private String usernameOrMobileOrEmail;
    private String token;
    private String oldPassword;
    private String newPassword;
}
