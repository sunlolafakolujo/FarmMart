package com.logicgate.userrole.service;


import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.UserRole;

import java.util.List;

public interface UserRoleService {
    UserRole addRole(UserRole userRole) throws UserRoleNotFoundException;
    UserRole fetchRoleById(Long id) throws UserRoleNotFoundException;
    UserRole fetchRoleByName(String roleName) throws UserRoleNotFoundException;
    List<UserRole> fetchAllRoles(Integer pageNumber);
    UserRole updateUserRole(UserRole userRole, Long id) throws UserRoleNotFoundException;
    void deleteRoleById(Long id) throws UserRoleNotFoundException;
    void deleteAllRoles();
}
