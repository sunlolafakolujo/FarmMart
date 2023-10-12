package com.logicgate.appuser.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.staticdata.UserType;
import com.logicgate.userrole.exception.UserRoleNotFoundException;
import com.logicgate.userrole.model.UserRole;
import com.logicgate.userrole.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService{
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public AppUser addAppUser(AppUser appUser) throws AppUserNotFoundException {
        appUser.setUserCode("USER".concat(String.valueOf(new Random().nextInt(100000000))));
        Optional<AppUser> user=appUserRepository
                .findUserByUsernameOrEmailOrMobileIgnoreCase(appUser.getUsername(),appUser.getEmail(),appUser.getMobile());
        if (user.isPresent()){
            throw new AppUserNotFoundException("User already exist");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    @Override
    public void addRoleToUser(String searchKey, String roleName) throws UserRoleNotFoundException, AppUserNotFoundException {
        UserRole userRole=userRoleRepository.findByUserRoleName(roleName)
                .orElseThrow(()->new UserRoleNotFoundException("Role Not found"));
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User Not Found"));
        appUser.getUserRoles().add(userRole);
        appUserRepository.save(appUser);
    }

    @Override
    public AppUser fetchById(Long id) throws AppUserNotFoundException {
        return appUserRepository.findById(id).orElseThrow(()->new AppUserNotFoundException("User ID: "+id+" Not Found"));
    }

    @Override
    public AppUser fetchByUsernameOrEmailOrMobile(String searchKey) throws AppUserNotFoundException {
        return appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
    }

    @Override
    public List<AppUser> fetchAllUsers(Integer pageNumber, UserType userType) {
        Pageable pageable= PageRequest.of(pageNumber,10);
        if (userType==null){
            return appUserRepository.findAll(pageable).toList();
        }else {
            return appUserRepository.findByUserType(userType);
        }
    }

    @Override
    public AppUser updateAppUser(AppUser appUser, Long id) throws AppUserNotFoundException {
        AppUser savedUser=appUserRepository.findById(id)
                .orElseThrow(()->new AppUserNotFoundException("User ID: "+id+" Not Found"));
        if (Objects.nonNull(appUser.getPassword()) && !"".equalsIgnoreCase(appUser.getPassword())){
            savedUser.setPassword(appUser.getPassword());
        }if (Objects.nonNull(appUser.getContact()) && !"".equals(appUser.getContact())){
            savedUser.setContact(appUser.getContact());
        }
        return appUserRepository.save(savedUser);
    }

    @Override
    public void deleteAppUser(Long id) throws AppUserNotFoundException {
        if (appUserRepository.existsById(id)){
            appUserRepository.deleteById(id);
        }else {
            throw new AppUserNotFoundException("User with ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllUsers() {
        appUserRepository.deleteAll();
    }
}
