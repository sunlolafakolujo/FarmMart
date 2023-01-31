package com.logicgate.passwordtoken.controller;

import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.service.AppUserService;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.event.PasswordResetEvent;
import com.logicgate.passwordtoken.exception.PasswordVerificationTokenNotFoundException;
import com.logicgate.passwordtoken.model.PasswordTokenModel;
import com.logicgate.passwordtoken.service.PasswordVerificationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordVerificationTokenController {
    private final PasswordVerificationTokenService passwordVerificationTokenService;
    private final ApplicationEventPublisher publisher;
    private final AppUserService appUserService;

    @PostMapping("/resetPassword")
    @PreAuthorize("hasAnyRole('EMPLOYEE','SELLER','BUYER')")
    public String resetPassword(@RequestBody PasswordTokenModel passwordTokenModel,
                                HttpServletRequest request) throws AppUserNotFoundException {
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(searchKey);
//        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(passwordTokenModel.getUsernameOrMobileOrEmail());
        if (appUser!=null){
            publisher.publishEvent(new PasswordResetEvent(applicationUrl(request),appUser));
        }
        return "An email has been sent to your email to reset your password";
    }

    @PostMapping("/savePassword")
    @PreAuthorize("hasAnyRole('EMPLOYEE','SELLER','BUYER')")
    public String savePassword(@RequestBody PasswordTokenModel passwordTokenModel, @RequestParam("token") String token) throws AppUserNotFoundException, PasswordVerificationTokenNotFoundException {
        Optional<AppUser> appUser=passwordVerificationTokenService.findUserByPasswordToken(token);
        if (appUser.isPresent()){
            passwordVerificationTokenService.changeUserPassword(appUser.get(),passwordTokenModel.getNewPassword());
        }
        return "Password Reset Successfully";
    }

    @PostMapping("/changePassword")
    @PreAuthorize("hasAnyRole('EMPLOYEE','SELLER','BUYER')")
    public String changePassword(@RequestBody PasswordTokenModel passwordTokenModel) throws AppUserNotFoundException,
            PasswordVerificationTokenNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(searchKey);
//        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(passwordTokenModel.getUsernameOrMobileOrEmail());
        if (!passwordVerificationTokenService.checkIfOldPasswordExist(appUser, passwordTokenModel.getOldPassword())){
            throw new PasswordVerificationTokenNotFoundException("Invalid old password");
        }
        passwordVerificationTokenService.changeUserPassword(appUser,passwordTokenModel.getNewPassword());
        return "Password changed successfully";
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+ ":"+request.getServerPort()+request.getContextPath();
    }

}
