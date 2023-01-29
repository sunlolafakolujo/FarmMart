package com.logicgate.verificationtoken.service;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.verificationtoken.exception.VerificationTokenNotFoundException;
import com.logicgate.verificationtoken.model.VerificationToken;
import com.logicgate.verificationtoken.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VerificationTokenServiceImpl implements VerificationTokenService{
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public Optional<AppUser> findAppUserByToken(String token) {
        return Optional.ofNullable(verificationTokenRepository.findByToken(token).getAppUser());
    }

    @Override
    public VerificationToken generateNewVerificationToken(String token) throws VerificationTokenNotFoundException {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
        if (verificationToken==null){
            throw new VerificationTokenNotFoundException("Token Not Found");
        }
        verificationToken.setToken(UUID.randomUUID().toString());
        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void saveVerificationTokenToUser(AppUser appUser, String token) {
        VerificationToken verificationToken=new VerificationToken(token,appUser);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateVerificationToken(String token) throws VerificationTokenNotFoundException {
        VerificationToken verificationToken=verificationTokenRepository.findByToken(token);
        if (verificationToken==null){
            throw new VerificationTokenNotFoundException("Invalid token");
        }
        AppUser appUser=verificationToken.getAppUser();
        Calendar calendar=Calendar.getInstance();
        if ((verificationToken.getExpectedExpirationTime().getTime()-calendar.getTime().getTime())<=0){
            verificationTokenRepository.delete(verificationToken);
            throw new VerificationTokenNotFoundException("Token expired");
        }
        appUser.setIsEnabled(true);
        appUserRepository.save(appUser);

        return "Valid token";
    }
}
