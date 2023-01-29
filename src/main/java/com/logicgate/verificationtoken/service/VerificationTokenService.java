package com.logicgate.verificationtoken.service;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.verificationtoken.exception.VerificationTokenNotFoundException;
import com.logicgate.verificationtoken.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    Optional<AppUser> findAppUserByToken(String token);
    VerificationToken generateNewVerificationToken(String token) throws VerificationTokenNotFoundException;
    void saveVerificationTokenToUser(AppUser appUser, String token);
    String validateVerificationToken(String token) throws VerificationTokenNotFoundException;
}
