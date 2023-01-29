package com.logicgate.verificationtoken.repository;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.verificationtoken.model.VerificationToken;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VerificationTokenRepositoryCustom {
    @Query("From VerificationToken v Where v.token=?1")
    VerificationToken findByToken(String token);

    @Query("From VerificationToken v Where v.appUser=?1")
    Optional<AppUser> findByUser(AppUser appUser);
}
