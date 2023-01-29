package com.logicgate.userrole.repository;


import com.logicgate.userrole.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>, UserRoleRepositoryCustom {
}
