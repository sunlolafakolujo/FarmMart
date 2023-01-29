package com.logicgate.configuration.appuserdetailservice;


import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import lombok.AllArgsConstructor;
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

    @Override
    public UserDetails loadUserByUsername(String searchKey) {
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,
                        searchKey,searchKey)
                .orElseThrow(()->new UsernameNotFoundException(searchKey +" Not Found"));

        return new org
                .springframework
                .security.core.userdetails
                .User(appUser.getUsername(),appUser.getPassword(), appUser.getIsEnabled(),true,
                true, true,getAuthority(appUser));
    }

    private Set<SimpleGrantedAuthority> getAuthority(AppUser appUser) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        appUser.getUserRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }
}
