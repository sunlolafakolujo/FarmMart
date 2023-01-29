package com.logicgate.passwordtoken.repository;


import com.logicgate.passwordtoken.model.PasswordVerificationToken;
import org.springframework.data.jpa.repository.Query;

public interface PasswordVerificationTokenCustom {
    @Query("FROM PasswordVerificationToken p WHERE p.token=?1")
    PasswordVerificationToken findByToken(String token);
}
