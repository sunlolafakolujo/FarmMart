package com.logicgate.appuser.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.service.AppUserService;
import com.logicgate.configuration.security.appuserdetailservice.AppUserDetailService;
import com.logicgate.configuration.security.auth.AuthRequest;
import com.logicgate.configuration.security.auth.AuthResponse;
import com.logicgate.configuration.security.jwtutil.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class LogInController {
    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

//    @PostMapping({"/logIn"})
//    public AuthResponse createJwtToken(@RequestBody AuthRequest authRequest) throws Exception {
//        return appUserDetailService.createJwtToken(authRequest);
//    }

    @PostMapping("/logIn")
    public ResponseEntity<?> signIn(@RequestBody AuthRequest authRequest) throws AppUserNotFoundException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsernameOrEmailOrMobile(),
                        authRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        AppUser appUser=appUserService.fetchByUsernameOrEmailOrMobile(authRequest.getUsernameOrEmailOrMobile());
        return ResponseEntity.ok(new AuthResponse(appUser.getUsername(), appUser.getUserRoles(),token));

    }
}
