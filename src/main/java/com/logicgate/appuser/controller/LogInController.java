package com.logicgate.appuser.controller;


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
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/logIn")
//    @PreAuthorize("hasAnyRole('SELLER','EMPLOYEE','BUYER')")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsernameOrEmailOrMobile(),
                        authRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(authentication);
        final String username= jwtUtil.extractUsername(token);
        return ResponseEntity.ok(new AuthResponse(username,token));
    }
}
