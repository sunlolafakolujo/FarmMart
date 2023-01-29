package com.logicgate.passwordtoken.repository;


import com.logicgate.passwordtoken.model.PasswordVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordVerificationTokenRepository extends JpaRepository<PasswordVerificationToken,Long>, PasswordVerificationTokenCustom {
}
