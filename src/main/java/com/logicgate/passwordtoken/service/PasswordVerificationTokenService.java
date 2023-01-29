package com.logicgate.passwordtoken.service;



import com.logicgate.appuser.model.AppUser;
import com.logicgate.passwordtoken.exception.PasswordTokenNotFoundException;
import com.logicgate.passwordtoken.model.PasswordVerificationToken;

import java.util.Optional;

public interface PasswordVerificationTokenService {
    PasswordVerificationToken findByToken(String token) throws PasswordTokenNotFoundException;
    Optional<AppUser> findUserByPasswordToken(String token);
    PasswordVerificationToken createPasswordRestTokenForUser(String token, AppUser appUser);
    String validatePasswordToken(String token) throws PasswordTokenNotFoundException;
    void changeUserPassword(AppUser appUser, String password);
    Boolean checkIfOldPasswordExist(AppUser appUser, String oldPassword);
}
