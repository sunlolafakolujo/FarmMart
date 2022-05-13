package com.farmmart.controller.staterestcontroller;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.service.country.CountryServiceImpl;
import com.farmmart.service.state.StateServiceImpl;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class StateRestControllerTest {

    @Autowired
    private StateServiceImpl stateService;

    @Autowired
    private CountryServiceImpl countryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    States state;

    Country country;

    @BeforeEach
    void setUp() {
        state=new States();
        country=new Country();
    }

    @Test
    void testThatWhenYooCallSaveStateMethod_ThenStateIsSaved() throws CountryNotFoundException, Exception {
        String countryName="Nigeria";
        country=countryService.findCountryByName(countryName);
        state.setCountry(country);
        state.setStateName("Nasarawa");

        this.mockMvc.perform(post("/api/state/saveState")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(state)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.stateName").value("Nasarawa"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetStateById() throws Exception {
        this.mockMvc.perform(get("/api/state/findStateById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.stateName").value("Lagos"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetStateByNameMethod_ThenStateIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/state/findStateByName/osun")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stateName").value("Osun"))
                .andReturn();
    }

    @Test
    void testThatWhenYoCallGetAllStatesMethod_ThenStatesAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/state/findAllStates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(6)))
                .andExpect(jsonPath("$[5].stateName").value("Ondo"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCountryStatesMethod_ThenStatesAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/state/findCountryStates/Nigeria")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(6)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallModifyStateMethod_ThenStateIsUpdated() throws StateNotFoundException, Exception {
        Long id=4L;
        state=stateService.findStateById(id);
        state.setStateName("Sun Shine");

        stateService.updateState(state,id);

        this.mockMvc.perform(put("/api/state/updateState/4")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(state)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(state.getId()))
                .andExpect(jsonPath("$.stateName").value("Sun Shine"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteStateById_ThenStateIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/state/deleteStateById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI2NTYzfQ.5c5dYuMtSsVDT6R4wc_Vf6FWZBMZ4gK2C0h6bHTtH1o")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllStatesMethod_ThenAllStatesAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/state/deleteAllStates")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3MTg2fQ.KTGC3pYqeHA8wm8fnLCfkZwMhnN-n3s46aZAFN0Z0xo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}