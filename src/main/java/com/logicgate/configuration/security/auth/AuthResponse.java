package com.logicgate.configuration.security.auth;


import com.logicgate.userrole.model.UserRole;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@Getter
@Setter
@ToString
public class AuthResponse {
//    private AppUser appUser;
    private String usernameOrEmailOrMobile;
    private List<UserRole> userRoles;
    private String jwtToken;
}
