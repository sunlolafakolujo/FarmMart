package com.logicgate.appuser.model;


import com.logicgate.contact.model.Contact;
import com.logicgate.userrole.model.UserRole;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateAppUser {
    private Long id;
    private String password;
    private Contact contact;
    private Collection<UserRole> userRoles=new HashSet<>();
}
