package com.farmmart.controller.customerrestcontroller;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.customer.Customer;
import com.farmmart.data.model.customer.CustomerNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.staticdata.AgeRange;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.service.address.AddressServiceImpl;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.customer.CustomerServiceImpl;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class CustomerRestControllerTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private AppUserDetailService appUserService;

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private LocalGovernmentServiceImpl localGovernmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Customer customer;

    AppUser appUser;

    Address address;

    LocalGovernment localGovernment;

    @BeforeEach
    void setUp() {
        customer=new Customer();
        appUser=new AppUser();
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatWhenYouCallRegisterCustomerMethod_thenCustomerIsRegistered() throws LocalGovernmentNotFoundException, Exception {
        Long id=3L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);
        address.setLocalGovernment(localGovernment);
        address.setStreetNumber("Block 433 Flat 1");
        address.setStreetName("Amuwo Odofin House Estate");
        address.setCity("Mile 2");
        address.setPostZipCode("100111");
        address.setLandMark("Crystal Place Estate");

        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        appUser.setAddresses(addresses);
        appUser.setUsername("Mac_Fem");
        appUser.setPassword("1234");
        appUser.setEmail("femimacaulay@yahoo.com");
        appUser.setPhone("0709765432");

        customer.setAppUser(appUser);
        customer.setFirstName("Femi");
        customer.setLastName("Macaulay");
        customer.setGender(Gender.MALE);
        customer.setAgeRange(AgeRange.FORTY_TO_FIFTY_NINE);

        this.mockMvc.perform(post("/api/customer/registerCustomer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(customer)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.firstName").value("Femi"))
                .andExpect(jsonPath("$.lastName").value("Macaulay"))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByIdMethod_thenCustomerIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findCustomerById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.firstName").value("Adebukola"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByFirstNameLikeMethod_thenCustomerIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findCustomerByFirstName/Akin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByLastNameLikeMethod_thenCustomerIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/customer/findCustomerByLastName/fakolujo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByGenderMethod_thenCustomersAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findCustomerByGender/MALE")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByAgeRangeMethod_thenCustomersAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findCustomerByAgeRange/FORTY_TO_FIFTY_NINE")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllCustomersMethod_thenCustomersAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findAllCustomer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("$[0].lastName",is("Fakolujo")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCustomerByUsernameMethod_thenCustomerIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/customer/findCustomerByUsername/AkinEmma")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4MTc5fQ.f0RwzOsTuBUHqQNPGaBT0rv6sDC9YTZQXo1k7sE1vEw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.firstName",is("Akin")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateCustomerByIdMethod_thenCustomerDetailsAreUpdated() throws CustomerNotFoundException, Exception {
        Long id=1L;
        customer=customerService.findCustomerById(id);
        customer.setFirstName("Abosede");

        customerService.updateCustomer(customer,id);

        this.mockMvc.perform(put("/api/customer/updateCustomerById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCdWtvbGFGYWtvIiwicm9sZXMiOlsiUk9MRV9DVVNUT01FUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA4ODc5fQ.gBeRTqYunw1046VN-91pVqnYPo1dyp-1GRbxc1JuurU")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.firstName",is("Abosede")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteCustomerByIdMethod_thenCustomerIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/customer/deleteCustomerById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA5MDc5fQ.fryIYmFVLOSyUW_rVEQayG6jnNxkjTs5nqwrDM3X39I")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllCustomers_thenAllCustomersAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/customer/deleteAllCustomer")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODA5MDc5fQ.fryIYmFVLOSyUW_rVEQayG6jnNxkjTs5nqwrDM3X39I")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}