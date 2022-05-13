package com.farmmart.controller.vendor;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.staticdata.BusinessEntity;
import com.farmmart.data.model.staticdata.Facility;
import com.farmmart.data.model.staticdata.MeansOfIdentification;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.service.address.AddressServiceImpl;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
import com.farmmart.service.vendor.VendorServiceImpl;
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
@Slf4j
@Transactional
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/insert.sql"})
class VendorRestControllerTest {

    @Autowired
    private VendorServiceImpl vendorService;

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

    Vendor vendor;

    AppUser appUser;

    Address address;

    LocalGovernment localGovernment;

    @BeforeEach
    void setUp() {
        vendor=new Vendor();
        appUser=new AppUser();
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatWhenYouCallRegisterVendorMethod_thenVendorIsRegistered() throws LocalGovernmentNotFoundException, Exception {
        Long id=8L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);
        address.setLocalGovernment(localGovernment);
        address.setStreetNumber("3A");
        address.setStreetName("Oko Awo Street");
        address.setCity("Victoria Island");
        address.setPostZipCode("110001");
        address.setLandMark("Eko Hotel Roundabout");
        List<Address> addresses=new ArrayList<>();
        addresses.add(address);

        appUser.setAddresses(addresses);
        appUser.setUsername("BotroMarine");
        appUser.setPassword("65567");
        appUser.setEmail("info@botromaring.ng");
        appUser.setPhone("07065379981");

        vendor.setAppUser(appUser);
        vendor.setBusinessEntity(BusinessEntity.BUSINESS);
        vendor.setRepresentative("Robert Oduayo");
        vendor.setName("Botro Marine Nigeria Limited");
        vendor.setNatureOfBusiness("Supplies of PVC pipes and fittings");
        vendor.setFacility(Facility.OWN);
        vendor.setMeansOfIdentification(MeansOfIdentification.NIN);
        vendor.setMeansOfIdNumber("299768");
        vendor.setMeansOfIdIssueDate(LocalDate.parse("2021-03-25"));
        vendor.setMeansOfIdExpiryDate(LocalDate.parse("2026-03-24"));
        vendor.setTaxId("25875");
        vendor.setRcNumber("RC 58906");

        this.mockMvc.perform(post("/api/vendor/registerVendor")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcwNzA1fQ.umx4mvzzWAUVjPGamkGhYLdt-uQ4lYusx5wB1fAR7lo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(vendor)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.businessEntity",is("BUSINESS")))
                .andExpect(jsonPath("$.name",is("Botro Marine Nigeria Limited")))
                .andExpect(jsonPath("$.rcNumber",is("RC 58906")))
                .andReturn();

    }

    @Test
    void testThatWhenYouCallGetVendorByIdMethod_thenVendorIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/vendor/findVendorById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcwNzA1fQ.umx4mvzzWAUVjPGamkGhYLdt-uQ4lYusx5wB1fAR7lo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is("Logic Gate Integrated Services Limited")))
                .andExpect(jsonPath("$.rcNumber",is("158854")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetVendorByNameMethod_thenVendorIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/vendor/findVendorByName/Zero Waste Farms & Agro Business Limited")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcwNzA1fQ.umx4mvzzWAUVjPGamkGhYLdt-uQ4lYusx5wB1fAR7lo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.businessEntity",is("BUSINESS")))
                .andExpect(jsonPath("$.rcNumber",is("19889")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetVendorByBusinessEntityMethod_thenVendorsAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/vendor/findVendorByBusinessEntity/BUSINESS")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcwNzA1fQ.umx4mvzzWAUVjPGamkGhYLdt-uQ4lYusx5wB1fAR7loeyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcwNzA1fQ.umx4mvzzWAUVjPGamkGhYLdt-uQ4lYusx5wB1fAR7lo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andReturn();
    }

//    @Test
//    void testThatWhenYouCallGetVendorByStatusMethod_thenVendorISReturned() throws Exception {
//        this.mockMvc.perform(get("/api/vendor/findVendorByStatus/ACTIVE")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$",notNullValue()))
//                .andReturn();
//    }

    @Test
    void testThatWhenYouCallGetAllVendorsMethod_thenVendorsAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/vendor/findAllVendors")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcxMzY1fQ.7itk7OpztM1AsemYWvcnfq5irZMRi0Acij5jmPbMZFY")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(2)))
                .andExpect(jsonPath("$[1].name",is("Zero Waste Farms & Agro Business Limited")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetVendorByUsernameMethod_thenVendorIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/vendor/findVendorByUsername/ZeroWaste")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcxMzY1fQ.7itk7OpztM1AsemYWvcnfq5irZMRi0Acij5jmPbMZFY")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateVendorByIdMethod_thenVendorsDetailsIsUpdated() throws VendorNotFoundException, Exception {
        Long id=1L;
        vendor=vendorService.findVendorById(id);
        vendor.setRepresentative("Ganiu Afuwape");

        vendorService.updateVendorById(vendor,id);

        this.mockMvc.perform(put("/api/vendor/updateVendorById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMb2dpY0dhdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODcxNjkxfQ.MU3PhoNw4_X6vdiupHdHCTio6fyB7na9lcXKODJnz1Y")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(vendor)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vendor.getId()))
                .andExpect(jsonPath("$.representative",is("Ganiu Afuwape")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteVendorByIdMethod_thenVendorIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/vendor/deleteVendorById/1")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODc3OTY4fQ.ZNTNrXhnoFAjY5DUKS9SySdvsbZ-Wvo5A9v-iapNzuQ")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllVendorsMethod_thenAllVendorsAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/vendor/deleteAllVendors")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1ODc3OTY4fQ.ZNTNrXhnoFAjY5DUKS9SySdvsbZ-Wvo5A9v-iapNzuQ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}