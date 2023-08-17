package com.logicgate.event.listener;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.verificationtoken.service.VerificationTokenService;
import com.logicgate.emailconfiguration.EmailConfiguration;
import com.logicgate.event.RegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private EmailConfiguration emailConfiguration;

    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost(emailConfiguration.getHost());
        javaMailSender.setPort(emailConfiguration.getPort());
        javaMailSender.setUsername(emailConfiguration.getUsername());
        javaMailSender.setPassword(emailConfiguration.getPassword());

        String token= UUID.randomUUID().toString();
        AppUser appUser=event.getAppUser();
        verificationTokenService.saveVerificationTokenToUser(appUser,token);
        SimpleMailMessage simpleMailMessage=registrationVerificationEmail(event, appUser,token);
        javaMailSender.send(simpleMailMessage);
    }

    private SimpleMailMessage registrationVerificationEmail(RegistrationEvent event, AppUser appUser, String token){
        String to= appUser.getEmail();
        String from="fakolujos@gmail.com";
        String subject="Verify Registration";
        String link=event.getApplicationUrl()+"/api/farmmart/verifyRegistration?token="+token;
        String body="Dear "+appUser.getUsername()+",\n\n"+"Thanks for registering an account with Farm Mart!\n" +
                "Before you get started, we'll need you to verify your email "+link +
                "\n\nIgnore this email if you did not signup for registration.\n Regards,\n Team FarmMart";

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        return simpleMailMessage;
    }
}
