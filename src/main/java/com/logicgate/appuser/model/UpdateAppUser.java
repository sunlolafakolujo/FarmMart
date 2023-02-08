package com.logicgate.appuser.model;


import com.logicgate.contact.model.Contact;
import com.logicgate.userrole.model.UserRole;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdateAppUser {
    private Long id;
    private String password;
    private Contact contact;
    private List<UserRole> userRoles;
}
