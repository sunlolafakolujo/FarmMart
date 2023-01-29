package com.logicgate.appuser.repository;


import com.logicgate.appuser.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long>, AppUserRepositoryCustom {
}
