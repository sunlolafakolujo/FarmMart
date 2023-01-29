package com.logicgate.userrole.repository;


import com.logicgate.userrole.model.UserRole;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepositoryCustom {
    @Query("From UserRole u Where u.roleName=?1 ORDER By u.id")
    Optional<UserRole> findByUserRoleName(String roleName);
}
