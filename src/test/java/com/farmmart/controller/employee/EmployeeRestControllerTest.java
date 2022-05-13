package com.farmmart.controller.employeerestcontroller;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.employee.Employee;
import com.farmmart.data.model.employee.EmployeeNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.staticdata.Gender;
import com.farmmart.data.model.staticdata.RelationshipWithNextOfKin;
import com.farmmart.service.address.AddressServiceImpl;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.employee.EmployeeServiceImpl;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class EmployeeRestControllerTest {

    @Autowired
    private EmployeeServiceImpl employeeService;

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

    Employee employee;

    AppUser appUser;

    Address address;

    LocalGovernment localGovernment;

    @BeforeEach
    void setUp() {
        employee=new Employee();
        appUser=new AppUser();
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatWhenYouCallRegisterEmployeeMethod_thenEmployeeIsRegistered() throws LocalGovernmentNotFoundException, Exception {
        Long id=8L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);

        address.setLandMark("Bar Beach");
        address.setPostZipCode("110001");
        address.setCity("Victoria Island");
        address.setStreetName("Nnamdi Azikwe Road");
        address.setStreetNumber("100");
        address.setLocalGovernment(localGovernment);

        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        appUser.setUsername("KunleAdebayo");
        appUser.setPassword("Butty_1234");
        appUser.setEmail("sanmi.adebayo@yahoo.com");
        appUser.setPhone("09075654325");
        appUser.setAddresses(addresses);

        employee.setNextOfKin("Bukola Adebayo");
        employee.setOtherNames("Sanmi");
        employee.setLastName("Adebayo");
        employee.setFirstName("Kunle");
        employee.setGender(Gender.MALE);
        employee.setAppUser(appUser);
        employee.setDob(LocalDate.parse("1970-05-01"));
        employee.setRelationshipWithNextOfKin(RelationshipWithNextOfKin.SISTER);
        employee.setHiredDate(LocalDate.parse("2005-02-10"));

        this.mockMvc.perform(post("/api/employee/registerEmployee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI4NzY5fQ.HI1L3A_BtJ9x2ImDOw0Ceiy0qQVJkvsQUc0_82G4Slk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(employee)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.nextOfKin",is("Bukola Adebayo")))
                .andExpect(jsonPath("$.lastName",is("Adebayo")))
                .andExpect(jsonPath("$.firstName",is("Kunle")))
                .andExpect(jsonPath("$.gender").value("MALE"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetEmployeeByIdMethod_thenEmployeeIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findEmployeeById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI4NzY5fQ.HI1L3A_BtJ9x2ImDOw0Ceiy0qQVJkvsQUc0_82G4Slk")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.lastName").value("Fakolujo"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetEmployeeByFirstNameMethod_thenEmployeeIsORAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findEmployeeByFirstName/Hephz")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andExpect(jsonPath("$[0].lastName").value("Fakolujo"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetEmployeeByLastNameMethod_thenEmployeeIsOrAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findEmployeeByLastName/Fakolujo")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI4NzY5fQ.HI1L3A_BtJ9x2ImDOw0Ceiy0qQVJkvsQUc0_82G4Slk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andExpect(jsonPath("$[0].otherNames").value("Oluwapamilerin"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetEmployeeByGenderMethod_thenEmployeeIsOrAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findEmployeeByGender/FEMALE")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetEmployeeByUsernameMethod_thenEmployeeIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findEmployeeByUsername/AkinSquare")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName",is("Akinwunmi")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllEmployeesMethod_thenEmployeeAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/employee/findAllEmployees")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("$[1].otherNames",is("Aderagbemi")))
                .andReturn();


    }

    @Test
    void testThatWhenYouCallUpdateEmployeeDetailsByIdMethod_thenEmployeeRecordIsUpdated() throws EmployeeNotFoundException, Exception {
        Long id=2L;
        employee=employeeService.findEmployeeById(id);
        employee.setOtherNames("Aderobaki");

        employeeService.updateEmployeeDetails(employee,id);

        this.mockMvc.perform(put("/api/employee/updateEmployeeById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
//                .andExpect(jsonPath("$.otherNames").value("Aderobaki"))
                .andReturn();
    }

    @Test
    void testUpdateEmployeeDetailsByIdAdminMethod_thenEmployeeRecordIsUpdated() throws EmployeeNotFoundException, Exception {
        Long id=1L;
        employee=employeeService.findEmployeeById(id);
        employee.setFirstName("Oluwamakemilaugh");

        employeeService.updateEmployeeDetails(employee,id);
        this.mockMvc.perform(put("/api/employee/updateEmployee/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(employee)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employee.getId()))
//                .andExpect(jsonPath("$.otherNames").value("Oluwapamilerin"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteEmployeeByIdMethod_thenEmployeeIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/employee/deleteEmployeeById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllEmployeeMethod_thenAllEmployeeAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/employee/deleteAllEmployee")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODI5NTM4fQ.KQYTOceoXEK0Ii6Cqo48Byck-_bG-dcQX93eiczt5G0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}