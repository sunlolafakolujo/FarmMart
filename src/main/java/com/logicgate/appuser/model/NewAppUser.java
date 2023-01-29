package com.logicgate.appuser.model;


import com.logicgate.contact.model.Contact;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.model.UserRole;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewAppUser {
    private String userCode;
    private UserType userType;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String mobile;
    private Contact contact;
    private Collection<UserRole> userRoles=new HashSet<>();
}
