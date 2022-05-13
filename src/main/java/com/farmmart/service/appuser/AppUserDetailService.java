package com.farmmart.service.appuser;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.userrole.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
public class AppUserDetailService implements AppUserService, UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=appUserRepository.findByUserName(username);

        if (appUser==null){
            log.error("App user does not exist", appUser.getUsername());

            throw new UsernameNotFoundException("Username not found in the database");
        }else {
            log.info("App user {} exist in the database",username);
        }

        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();

        appUser.getUserRoles()
                .forEach(userRole -> {authorities.add(new SimpleGrantedAuthority(userRole.getRoleName()));});

        return new User(appUser.getUsername(),
                appUser.getPassword(),
                authorities);
    }

    @Override
    public AppUser saveAppUser(AppUser appUser) throws AppUserNotFoundException {
        AppUser username= appUserRepository.findByUserName(appUser.getUsername());

        AppUser email= appUserRepository.findByEmail(appUser.getEmail());

        AppUser phoneNumber= appUserRepository.findByPhone(appUser.getPhone());

        if (Objects.nonNull(username)||Objects.nonNull(email)||Objects.nonNull(phoneNumber)){
            throw new AppUserNotFoundException("User already exist");
        }else if (!validatePhoneNumber(appUser)){
                throw new AppUserNotFoundException("Incorrect Phone Number");
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));

        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser userLogIn(AppUser appUser) throws AppUserNotFoundException {
        AppUser savedAppUser= appUserRepository.findByUserName(appUser.getUsername());

        if(!appUser.getUsername().equals(savedAppUser.getUsername()) || !appUser.getPassword().equals(savedAppUser.getPassword())){

            throw new AppUserNotFoundException("Invalid credentials");

        }else log.info("Successfully signed in");

        return savedAppUser;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {

        AppUser appUser=appUserRepository.findByUserName(username);

        UserRole userRole=userRoleRepository.findByRoleName(roleName);

        appUser.getUserRoles().add(userRole);
    }

    @Override
    public AppUser findUserById(Long id) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findById(id).orElseThrow(()->new AppUserNotFoundException("User Not Found"));

        return appUser;
    }

    @Override
    public AppUser findUserByUsername(String username) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findByUserName(username);

        if (Objects.isNull(appUser)){
            throw new AppUserNotFoundException("User Not Found");
        }

        return appUser;
    }

    @Override
    public AppUser findUserByPhoneNumber(String phone) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findByPhone(phone);

        if (Objects.isNull(appUser)){
            throw new AppUserNotFoundException("User Not Found");
        }

        return appUser;
    }

    @Override
    public AppUser findUserByEmail(String email) throws AppUserNotFoundException {
        AppUser appUser=appUserRepository.findByEmail(email);

        if (Objects.isNull(appUser)){
            throw new AppUserNotFoundException("User Not Found");
        }

        return appUser;
    }

    @Override
    public List<AppUser> findUserByType(UserType userType) {

        return appUserRepository.findByUserType(userType);
    }

    @Override
    public List<AppUser> findAllUsers() {

        return appUserRepository.findAll();
    }

    @Override
    public AppUser updateUser(AppUser appUser, Long id) throws AppUserNotFoundException {

        AppUser savedUser=appUserRepository.findById(id).orElseThrow(()->new AppUserNotFoundException("User Not Found"));

        if (Objects.nonNull(appUser.getPhone()) && !"".equalsIgnoreCase(appUser.getPhone())){
            savedUser.setPhone(appUser.getPhone());
        }if (Objects.nonNull(appUser.getUserRoles())){
            savedUser.setUserRoles(appUser.getUserRoles());
        }if (Objects.nonNull(appUser.getPassword()) && !"".equalsIgnoreCase(appUser.getPassword())){
            savedUser.setPassword(appUser.getPassword());
        }if (Objects.nonNull(appUser.getAddresses())){
            savedUser.setAddresses(appUser.getAddresses());
        }

        return appUserRepository.save(savedUser);
    }

    @Override
    public void deleteUserById(Long id) throws AppUserNotFoundException {
        appUserRepository.deleteById(id);

        Optional<AppUser> optionalAppUser=appUserRepository.findById(id);

        if (optionalAppUser.isPresent()){
            throw new AppUserNotFoundException("User is Deleted");
        }
    }

    @Override
    public void deleteAllUsers() {

        appUserRepository.deleteAll();
    }

    private  boolean validatePhoneNumber(AppUser appUser) {
        if (appUser.getPhone().matches("\\d{10}")){
            return true;
        } else if(appUser.getPhone().matches("\\d{11}")){
            return true;
        } else if(appUser.getPhone().matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        } else if(appUser.getPhone().matches("\\d{4}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) {
            return true;
        }else if(appUser.getPhone().matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) {
            return true;
        } else if(appUser.getPhone().matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")){
            return true;
        }
        else return false;
    }

}
