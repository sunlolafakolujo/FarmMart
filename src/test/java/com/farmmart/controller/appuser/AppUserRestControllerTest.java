package com.farmmart.controller.appuserrestcontroller;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.userrole.UserRoleNotFoundException;
import com.farmmart.service.address.AddressServiceImpl;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@Transactional
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/insert.sql"})
class AppUserRestControllerTest {

    @Autowired
    private AppUserDetailService appUserService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private LocalGovernmentServiceImpl localGovernmentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    AppUser appUser;

    UserRole userRole;

    Address address;

    LocalGovernment localGovernment;

    @BeforeEach
    void setUp() {
        appUser=new AppUser();
        userRole=new UserRole();
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatWhenYouCallSaveAppUserMethod_ThenUserIsSaved() throws LocalGovernmentNotFoundException, Exception, UserRoleNotFoundException {
        Long id=6L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);

        address.setStreetNumber("312");
        address.setStreetName("Herbert Macaulay Way");
        address.setCity("Sabo");
        address.setPostZipCode("");
        address.setLandMark("Sabo BusStop");
        address.setLocalGovernment(localGovernment);
        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        userRole=userRoleService.findUserRoleByName("ROLE_ADMIN");
        List<UserRole> userRoles=new ArrayList<>();
        userRoles.add(userRole);

        appUser.setUsername("LaoluAkins");
        appUser.setPassword("1234");
        appUser.setEmail("lolu@gmail.com");
        appUser.setPhone("08024678713");
        appUser.setUserRoles(userRoles);
        appUser.setAddresses(addresses);

        this.mockMvc.perform(post("/api/appUser/registerUser")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA0NzIzfQ.xbP2mkCXWoZELQAAsMFAVCHya9V5Tdo_d-5QXYBojmE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(appUser)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.username").value("LaoluAkins"))
                .andExpect(jsonPath("$.email").value("lolu@gmail.com"))
                .andExpect(jsonPath("$.phone").value("08024678713"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallAddRoleToUserMethod_thenRoleIsAddedToUser() throws Exception {
        String username="HephzibahPam";
        String role="MANAGER";

        appUserService.addRoleToUser(username,role);

        this.mockMvc.perform(post("/api/appUser/addRoleToUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(appUser)))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetUserById_ThenUserIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/appUser/findUserById/1")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.email").value("adebukolafaolujo@gmail.com"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetUserByUsernameMethod_ThenUserIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/appUser/findUserByUsername/AkinEmma")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("AkinEmma"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetUserByEmailMethod_ThenUserIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/appUser/findUserByEmail/adebukolafaolujo@gmail.com")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("BukolaFako"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetUserByPhoneNumberMethod_ThenUserIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/appUser/findUserByPhoneNumber/08080472478")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("BukolaFako"))
                .andReturn();
    }

    @Test
    void testThatWhenYouMockGetUserByType_ThenUsersAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/appUser/findUserByType/CUSTOMER")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("$[0].userType").value("CUSTOMER"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllUsersMethod_ThenAllUsersAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/appUser/findAllUsers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA1NjY0fQ.lSh7PM0b-xEadKHyJuP3KZV9tG-7vkVrKosqBx9Pw60")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(6)))
                .andExpect(jsonPath("$[0].username").value("BukolaFako"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateUserMethod_TheUserIsUpdated() throws AppUserNotFoundException, Exception {
        Long id=1L;
        appUser=appUserService.findUserById(id);
        String password="Akinpelumi@04071974";
        appUser.setPassword(password);

        appUserService.updateUser(appUser,id);

        this.mockMvc.perform(put("/api/appUser/updateUserById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA2MzM1fQ.-ekyWUYVFrkmZUYfZf-TLx2O8i1J5WZwwA_6YWDz2ZI")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(appUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appUser.getId()))
                .andExpect(jsonPath("$.password").value(password))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteUserByIdMethod_ThenUserIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/appUser/deleteUserById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA2MzM1fQ.-ekyWUYVFrkmZUYfZf-TLx2O8i1J5WZwwA_6YWDz2ZI"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllUsers_ThenAllUsersAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/appUser/deleteAllUsers")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA2MzM1fQ.-ekyWUYVFrkmZUYfZf-TLx2O8i1J5WZwwA_6YWDz2ZI"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}