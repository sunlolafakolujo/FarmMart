package com.logicgate.userrole.model;

import lombok.Data;

@Data
public class AddRoleToUser {
    private String usernameOrEmailOrPassword;
    private String roleName;
}
