package com.farmmart.service.appuser;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.staticdata.UserType;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.userrole.UserRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private AppUserService appUserService=new AppUserDetailService();

    AppUser appUser;

    UserRole userRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        appUser=new AppUser();
        userRole=new UserRole();
    }

    @Test
    void testThatYouCanMockSaveAppUserMethod() throws AppUserNotFoundException {
        Mockito.when(appUserRepository.save(appUser)).thenReturn(appUser);

        appUserService.saveAppUser(appUser);

        ArgumentCaptor<AppUser> appUserArgumentCaptor=ArgumentCaptor.forClass(AppUser.class);

        Mockito.verify(appUserRepository,Mockito.times(1)).save(appUserArgumentCaptor.capture());

        AppUser capturedAppUser=appUserArgumentCaptor.getValue();

        assertEquals(capturedAppUser,appUser);
    }

//    @Test
//    void testThatYouCanMockSignInUserMethod(){
//        String username="HephzibahPam";
//        Mockito.when((appUserRepository.findByUserName(username))).thenReturn(appUser);
//
//        appUserService.signInUser(appUser);
//
//        Mockito.verify(appUserRepository,times(1)).findByUserName(username);
//    }

//    @Test
//    void testThatYouCanMockAddRoleToUser(){
//        String username="HephzibahPam";
//
//        appUser=appUserRepository.findByUserName(username);
//
//        Role role=Role.ADMIN;
//
//        userRole=userRoleRepository.findByName(role);
//
//        appUser.getUserRoles().add(userRole);
//
//        Mockito.doNothing().when(appUserRepository).save(appUser);
//
//        appUserService.addRoleToUser(username,role);
//
//        verify(appUserRepository,times(1)).save(appUser);
//    }

    @Test
    void testThatYouCanMockFindUserByIdMethod() throws AppUserNotFoundException {
        Long id=2L;
        Mockito.when(appUserRepository.findById(id)).thenReturn(Optional.of(appUser));

        appUserService.findUserById(id);

        Mockito.verify(appUserRepository,Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYouCanMockFindUserByUserName() throws AppUserNotFoundException {
        String username="BukolaFako";

        Mockito.when(appUserRepository.findByUserName(username)).thenReturn(appUser);

        appUserService.findUserByUsername(username);

        Mockito.verify(appUserRepository,Mockito.times(1)).findByUserName(username);
    }

    @Test
    void testThatYouCanMockFindByPhoneNumber() throws AppUserNotFoundException {
        String phone="08028424682";

        Mockito.when(appUserRepository.findByPhone(phone)).thenReturn(appUser);

        appUserService.findUserByPhoneNumber(phone);

        Mockito.verify(appUserRepository,Mockito.times(1)).findByPhone(phone);
    }

    @Test
    void testThatYouCanMockFindUserByEmailMethod() throws AppUserNotFoundException {
        String email="fakolujos@gmail.com";

        Mockito.when(appUserRepository.findByEmail(email)).thenReturn(appUser);

        appUserService.findUserByEmail(email);

        Mockito.verify(appUserRepository,Mockito.times(1)).findByEmail(email);
    }

    @Test
    void testThatYouCanMockFindUserByTypeMethod(){
        List<AppUser> appUsers=new ArrayList<>();

        UserType userType=UserType.CUSTOMER;

        Mockito.when(appUserRepository.findByUserType(userType)).thenReturn(appUsers);

        appUserService.findUserByType(userType);

        Mockito.verify(appUserRepository,times(1)).findByUserType(userType);
    }

    @Test
    void testThatYouCanMockFindAllUserMethod() {
        List<AppUser> appUsers=new ArrayList<>();

        Mockito.when(appUserRepository.findAll()).thenReturn(appUsers);

        appUserService.findAllUsers(10);

        Mockito.verify(appUserRepository,Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockUpdateUserMethod() throws AppUserNotFoundException {
        Long id=1L;
        Mockito.when(appUserRepository.findById(id)).thenReturn(Optional.of(appUser));

        appUserService.updateUser(appUser,id);

        Mockito.verify(appUserRepository,Mockito.times(1)).save(appUser);
    }

    @Test
    void testThatYouCanMockDeleteUserByIdMethod() throws AppUserNotFoundException {
        Long id=2L;
        doNothing().when(appUserRepository).deleteById(id);

        appUserService.deleteUserById(id);

        verify(appUserRepository,times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllUsersMethod() {
        doNothing().when(appUserRepository).deleteAll();

        appUserService.deleteAllUsers();

        verify(appUserRepository,times(1)).deleteAll();
    }
}