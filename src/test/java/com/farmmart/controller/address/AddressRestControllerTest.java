package com.farmmart.controller.addressrestcontroller;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.address.AddressNotFoundException;
import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.service.address.AddressServiceImpl;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Slf4j
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/insert.sql"})
class AddressRestControllerTest {

    @Autowired
    private AddressServiceImpl addressService;

    @Autowired
    private LocalGovernmentServiceImpl localGovernmentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Address address;

    LocalGovernment localGovernment;
    @BeforeEach
    void setUp() {
        address=new Address();
        localGovernment=new LocalGovernment();
    }

    @Test
    void testThatWhenYouCallSaveAddressMethod_ThenAddressIsSaved() throws LocalGovernmentNotFoundException, Exception {
        Long id=3L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);

        address.setLocalGovernment(localGovernment);
        address.setStreetNumber("Block 202, Flat 2");
        address.setStreetName("Amuwo Odofin Housing Estate");
        address.setCity("Mile 2");
        address.setPostZipCode("100111");

        this.mockMvc.perform(post("/api/address/saveAddress")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(address)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.streetNumber").value("Block 202, Flat 2"))
                .andExpect(jsonPath("$.streetName").value("Amuwo Odofin Housing Estate"))
                .andExpect(jsonPath("$.city").value("Mile 2"))
                .andExpect(jsonPath("$.postZipCode").value("100111"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAddressByIdMethod_ThenAddressIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/address/findAddressById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.streetNumber").value("Block 202, Flat 2"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllAddressesMethod_ThenAddressesAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/address/findAllAddresses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(6)))
                .andExpect(jsonPath("$[1].streetNumber").value("17"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAddressesByLocalGovernmentNameMethod_ThenAddressesAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/address/findAddressesByLocalGovernment/amuwo odofin")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateAddressMethod_ThenAddressIsUpdated() throws AddressNotFoundException, Exception {
        Long id=1L;
        address=addressService.findAddressById(id);

        address.setStreetNumber("Block 433, Flat 1");

        addressService.updateAddress(address,id);

        this.mockMvc.perform(put("/api/address/updateAddressById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(address)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(address.getId()))
                .andExpect(jsonPath("$.streetNumber").value("Block 433, Flat 1"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAddressByIdMethod_ThenAddressIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/address/deleteAddressById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllAddressesMethod_ThenAllAddressesAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/address/deleteAllAddresses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1Nzg5MjgwfQ.8bXipZwydPO_iCsqFV2W3VVDqOf2HnA9ramdEEPSMYw"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}