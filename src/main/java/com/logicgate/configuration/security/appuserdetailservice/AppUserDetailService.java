package com.logicgate.configuration.security.appuserdetailservice;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.configuration.security.auth.AuthRequest;
import com.logicgate.configuration.security.auth.AuthResponse;
import com.logicgate.configuration.security.jwtutil.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class AppUserDetailService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

//    public AuthResponse createJwtToken(AuthRequest authRequest) throws Exception {
//        String searchKey=authRequest.getUsernameOrEmailOrMobile();
//        String password= authRequest.getPassword();
//        authenticate(searchKey,password);
//        final UserDetails userDetails=loadUserByUsername(searchKey);
//        String generateToken= jwtUtil.generateToken(userDetails);
//        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
//                .orElseThrow(()->new UsernameNotFoundException(searchKey +" Not Found"));
//        return new AuthResponse(appUser.getUsername(),appUser.getUserRoles(),generateToken);
//    }

    @Override
    public UserDetails loadUserByUsername(String searchKey) {
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,
                        searchKey,searchKey)
                .orElseThrow(()->new UsernameNotFoundException(searchKey +" Not Found"));

            return new org
                    .springframework
                    .security.core.userdetails
                    .User(appUser.getUsername(), appUser.getPassword(), appUser.getIsEnabled(), true,
                    true, true, getAuthority(appUser));
    }

    private Set<SimpleGrantedAuthority> getAuthority(AppUser appUser) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        appUser.getUserRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

//    private void authenticate(String usernameOrEmailOrMobile, String password) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmailOrMobile,password));
//        }catch (DisabledException de){
//            throw new Exception("User is disabled");
//        }catch (BadCredentialsException be){
//            throw new Exception("Bad Credentials from user");
//        }
//
//    }
}
