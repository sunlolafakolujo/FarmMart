package com.farmmart.data.repository.appuser;

import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.address.AddressNotFoundException;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.repository.address.AddressRepository;
import com.farmmart.data.repository.userrole.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    AppUser appUser;

    Address address;

    UserRole userRole;

    @BeforeEach
    void setUp() {
        appUser=new AppUser();
        address=new Address();
        userRole=new UserRole();
    }

    @Test
    void testThatYouCanSaveAppUser() throws AddressNotFoundException {
        Long id=1L;
        address=addressRepository.findById(id).orElseThrow(()-> new AddressNotFoundException("Address Not Found"));
        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        userRole.setRoleName("INTERN");
        Set<UserRole> userRoles=new HashSet<>();
        userRoles.add(userRole);

        appUser.setUsername("JohnFak");
        appUser.setUserType(UserType.EMPLOYEE);
        appUser.setPassword(passwordEncoder.encode("1234"));
        appUser.setPhone("08096107651");
        appUser.setEmail("john.fakolujo@yahoo.com");
        appUser.setUserRoles(userRoles);
        appUser.setAddresses(addresses);

        assertDoesNotThrow(()->appUserRepository.save(appUser));

        log.info("App User repo: {}",appUser);
    }

    @Test
    void testThatYouCanAddRoleToUser(){
        String username="HephzibahPam";

        appUser=appUserRepository.findByUserName(username);

        String role="MANAGER";

        UserRole userRole=userRoleRepository.findByRoleName(role);

        appUser.getUserRoles().add(userRole);

        assertDoesNotThrow(()->appUserRepository.save(appUser));

        log.info("App User: {}",appUser);
    }

    @Test
    void testThatYouCanFindAppUserById() throws AppUserNotFoundException {
        Long id=2L;
        appUser=appUserRepository.findById(id).orElseThrow(()->new AppUserNotFoundException("User NotFound"));

        assertEquals(2, appUser.getId());

        log.info("fetch User by Id: {} ",appUser);
    }

    @Test
    void testThatYouCanFindAppUserByEmail() throws AppUserNotFoundException {
        String email="fakolujos@gmail.com";

        appUser=appUserRepository.findByEmail(email);

        if (appUser==null){
            throw new AppUserNotFoundException("User Not Found");
        }

        assertEquals(email,appUser.getEmail());

        log.info("fetch User by email {}",appUser);
    }

    @Test
    void testThatYouCanFindUserByPhoneNumber() throws AppUserNotFoundException {
        String phone="08080472478";

        appUser=appUserRepository.findByPhone(phone);

        if (appUser==null){
            throw new AppUserNotFoundException("User Not Found");
        }

        log.info("Fetch user by Phone Number{}",appUser);
    }

    @Test
    void testThatYouCanFindUserByUsername() throws AppUserNotFoundException {
        String username="AkinEmma";

        appUser=appUserRepository.findByUserName(username);

        if (appUser==null){
            throw new AppUserNotFoundException("User Not Found");
        }

        log.info("Fetch user by username {}",appUser);
    }

    @Test
    void testThatYouCanFindUserByType(){
        UserType userType=UserType.CUSTOMER;
        List<AppUser> appUsers=appUserRepository.findByUserType(userType);

        log.info("User with type Customer {}",appUsers);
    }

    @Test
    void testThatYouCanFindAllUsers() throws AppUserNotFoundException {
        List<AppUser> appUsers=appUserRepository.findAll();

        if (appUsers.isEmpty()){
            throw new AppUserNotFoundException("App User repo is Null");
        }

        log.info("Fetch All Users {}",appUsers);

        assertEquals(6,appUsers.size());
    }

    @Test
    void testThatYouCanUpdateUser() throws AppUserNotFoundException {
        String username="BukolaFako";
        appUser=appUserRepository.findByUserName(username);

        if (appUser==null){
            throw new AppUserNotFoundException("User Not Found");
        }
        String password="12345";
        appUser.setPassword(password);

        assertDoesNotThrow(()->appUserRepository.save(appUser));

        assertEquals(password,appUser.getPassword());

        log.info("Updated User {}",appUser);
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteUserById() throws AppUserNotFoundException {
        Long id=2L;
        appUserRepository.deleteById(id);

        Optional<AppUser> optionalAppUser=appUserRepository.findById(id);

        if (optionalAppUser.isPresent()){
            throw new AppUserNotFoundException("User Not Deleted");
        }

        log.info("Deleting User by Id");
    }

    @Rollback(value = false)
    @Test
    void testThatYouCanDeleteAllUsers(){
        appUserRepository.deleteAll();

        log.info("Deleting all Users");
    }
}