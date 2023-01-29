package com.logicgate.passwordtoken.service;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.passwordtoken.exception.PasswordTokenNotFoundException;
import com.logicgate.passwordtoken.model.PasswordVerificationToken;
import com.logicgate.passwordtoken.repository.PasswordVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;

@Service
@Transactional
public class PasswordVerificationTokenServiceImpl implements PasswordVerificationTokenService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordVerificationTokenRepository passwordVerificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PasswordVerificationToken findByToken(String token) throws PasswordTokenNotFoundException {
        PasswordVerificationToken passwordVerificationToken=passwordVerificationTokenRepository.findByToken(token);
        if (passwordVerificationToken==null){
            throw new PasswordTokenNotFoundException("Password token Not Found");
        }
        return passwordVerificationToken;
    }

    @Override
    public Optional<AppUser> findUserByPasswordToken(String token) {
        return Optional.ofNullable(passwordVerificationTokenRepository.findByToken(token).getAppUser());
    }

    @Override
    public PasswordVerificationToken createPasswordRestTokenForUser(String token, AppUser appUser) {
        PasswordVerificationToken passwordToken=new PasswordVerificationToken(token, appUser);
        return passwordVerificationTokenRepository.save(passwordToken);
    }

    @Override
    public String validatePasswordToken(String token) throws PasswordTokenNotFoundException {
        PasswordVerificationToken passwordToken=passwordVerificationTokenRepository.findByToken(token);
        if (passwordToken==null){
            throw new PasswordTokenNotFoundException("Invalid token");
        }
        Calendar calendar=Calendar.getInstance();
        if ((passwordToken.getExpectedExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            passwordVerificationTokenRepository.delete(passwordToken);
            throw new PasswordTokenNotFoundException("Password token expired");
        }else return "Valid token";
    }

    @Override
    public void changeUserPassword(AppUser appUser, String newPassword) {
        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
    }

    @Override
    public Boolean checkIfOldPasswordExist(AppUser appUser, String oldPassword) {
        return passwordEncoder.matches(appUser.getPassword(), oldPassword);
    }
}
