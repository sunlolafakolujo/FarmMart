package com.farmmart.controller.countryrestcontroller;

import com.farmmart.data.model.country.Country;
import com.farmmart.data.model.country.CountryNotFoundException;
import com.farmmart.service.country.CountryServiceImpl;
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

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class CountryResControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CountryServiceImpl countryService;

    Country country;

    @BeforeEach
    void setUp() {
        country=new Country();
    }

    @Test
    void testThatWhenYouCallSaveCountryMethod_ThenCountryIsSaved() throws Exception {
        country.setCountryName("South Africa");

        this.mockMvc.perform(post("/api/country/saveCountry")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(country)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.countryName").value("South Africa"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCountryByIdMethod_ThenCountryIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/country/findCountryById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.countryName").value("Republic of Benin"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCountryByNameMethod_ThenCountryIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/country/findCountryByName/ghana")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.countryName").value("Ghana"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllCountryMethod_ThenAllCountriesAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/country/findAllCountries")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(3)))
                .andExpect(jsonPath("$[1].countryName").value("Republic of Benin"))//test to fail
                .andReturn();
    }

    @Test
    void testThatWhenYouCallModifyCountryMethod_ThenCountryIsModified() throws CountryNotFoundException, Exception {
        Long id=1L;
        country=countryService.findCountryById(id);
        country.setCountryName("Federal Republic of Nigeria");

        countryService.updateCountry(country,id);

        this.mockMvc.perform(put("/api/country/updateCountryById/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(country)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(country.getId()))
                .andExpect(jsonPath("$.countryName").value("Federal Republic of Nigeria"))
                .andReturn();
    }

    @Test
    void testThatWhenCallDeleteCountryByIdMethod_ThenDeleteCountry() throws Exception {
        this.mockMvc.perform(delete("/api/country/deleteCountryById/2")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllCountriesMethod_ThenDeleteAll() throws Exception {
        this.mockMvc.perform(delete("/api/country/deleteAllCountries")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI1ODEyfQ.GOSfWcLZyOtFbo_0NzNA-GiebVP9VQwKQoeJHfsoafs"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}