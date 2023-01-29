package com.logicgate.event.listener;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.emailconfiguration.EmailConfiguration;
import com.logicgate.event.PasswordResetEvent;
import com.logicgate.passwordtoken.service.PasswordVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordVerificationEventListener implements ApplicationListener<PasswordResetEvent> {
    @Autowired
    private PasswordVerificationTokenService passwordVerificationTokenService;
    @Autowired
    private EmailConfiguration emailConfiguration;

    @Override
    public void onApplicationEvent(PasswordResetEvent event) {
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost(emailConfiguration.getHost());
        javaMailSender.setPort(emailConfiguration.getPort());
        javaMailSender.setUsername(emailConfiguration.getUsername());
        javaMailSender.setPassword(emailConfiguration.getPassword());

        String token= UUID.randomUUID().toString();
        AppUser appUser= event.getAppUser();
        passwordVerificationTokenService.createPasswordRestTokenForUser(token,appUser);
        SimpleMailMessage simpleMailMessage=passwordVerificationEmail(event,appUser,token);
        javaMailSender.send(simpleMailMessage);
    }

    private SimpleMailMessage passwordVerificationEmail(PasswordResetEvent event, AppUser appUser, String token) {
        String to= appUser.getEmail();
        String from="fakolujos@gmail.com";
        String subject="Password Reset";
        String link= event.getApplicationUrl()+"/api/farmmart/savePassword?token="+token;
        String text="Dear "+appUser.getUsername()+",\n\n"+"Click on the link in your inbox to reset your password "+link;

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(to);
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(text);
        return simpleMailMessage;
    }


}
