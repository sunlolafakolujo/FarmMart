package com.logicgate.verificationtoken.controller;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.verificationtoken.exception.VerificationTokenNotFoundException;
import com.logicgate.verificationtoken.model.VerificationToken;
import com.logicgate.verificationtoken.service.VerificationTokenService;
import com.logicgate.event.RegistrationEvent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class VerificationTokenController {

    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher publisher;

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) throws VerificationTokenNotFoundException, VerificationTokenNotFoundException {
        String result=verificationTokenService.validateVerificationToken(token);

        if (result.equalsIgnoreCase("Valid token")){
            return "Registration successful";
        }
        return "Bad user";
    }

    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String token, HttpServletRequest request) throws VerificationTokenNotFoundException {
        VerificationToken verificationToken=verificationTokenService.generateNewVerificationToken(token);
        AppUser appUser=verificationToken.getAppUser();
        publisher.publishEvent(new RegistrationEvent(appUser,applicationUrl(request)));
        return "Click on the link in the email in your inbox to verify registration";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+ ":"+request.getServerPort()+request.getContextPath();
    }
}
