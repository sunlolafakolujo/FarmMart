package com.farmmart.controller.localgovernmentrestcontroller;

import com.farmmart.data.model.localgovernment.LocalGovernment;
import com.farmmart.data.model.localgovernment.LocalGovernmentNotFoundException;
import com.farmmart.data.model.state.StateNotFoundException;
import com.farmmart.data.model.state.States;
import com.farmmart.service.localgovernment.LocalGovernmentServiceImpl;
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

import javax.validation.constraints.NotBlank;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/insert.sql"})
class LocalGovernmentRestControllerTest {

    @Autowired
    private LocalGovernmentServiceImpl localGovernmentService;

    @Autowired
    private StateServiceImpl stateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    LocalGovernment localGovernment;

    States state;

    @BeforeEach
    void setUp() {
        localGovernment=new LocalGovernment();
        state=new States();
    }

    @Test
    void testThatWhenYouCallSaveLocalGovernmentMethod_ThenLocalGovernmentIsSaved() throws StateNotFoundException, Exception {
        String stateName="Lagos";
        state=stateService.findStateByName(stateName);

        localGovernment.setState(state);
        @NotBlank(message = "Add Local Government Name") String localGovtName="Surulere";
        localGovernment.setLocalGovernmentName(localGovtName);

        this.mockMvc.perform(post("/api/localGovernment/saveLocalGovernment")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3NjI0fQ.or2Vmkb5ka-WxHTp4fApbS0qalt9HZlpr9s833SvU1k")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(localGovernment)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.localGovernmentName").value(localGovtName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetLocalGovernmentByIdMethod_ThenLocalGovernmentIsReturned() throws Exception {
        Object localGovernmentName="Amuwo Odofin";
        this.mockMvc.perform(get("/api/localGovernment/findLocalGovernmentById/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3NjI0fQ.or2Vmkb5ka-WxHTp4fApbS0qalt9HZlpr9s833SvU1k")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.localGovernmentName").value(localGovernmentName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetLocalGovernmentByNameMethod_ThenLocalGovernmentIsReturned() throws Exception {
        Object localGovernmentName ="Epe";
        this.mockMvc.perform(get("/api/localGovernment/findLocalGovernmentByName/epe")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3NjI0fQ.or2Vmkb5ka-WxHTp4fApbS0qalt9HZlpr9s833SvU1k")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.localGovernmentName",notNullValue()))
                .andExpect(jsonPath("$.localGovernmentName").value(localGovernmentName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllLocalGovernmentMethod_ThenLocalGovernmentsAreReturned() throws Exception {
        Object localGovernmentName="Amuwo Odofin";
        this.mockMvc.perform(get("/api/localGovernment/findAllLocalGovernments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3NjI0fQ.or2Vmkb5ka-WxHTp4fApbS0qalt9HZlpr9s833SvU1k")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(8)))
                .andExpect(jsonPath("$[2].localGovernmentName").value(localGovernmentName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetLocalGovernmentsByStateMethod_ThenLocalGovernmentAreReturned() throws StateNotFoundException, Exception {
        this.mockMvc.perform(get("/api/localGovernment/findStateLocalGovernments/lagos")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI3NjI0fQ.or2Vmkb5ka-WxHTp4fApbS0qalt9HZlpr9s833SvU1k")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(8)))
                .andReturn();
    }

    @Test
    void testThatWHenYouCallUpdateLocalGovernmentMethod_ThenLocalGovernmentIsUpdated() throws LocalGovernmentNotFoundException, Exception {
        Long id=3L;
        localGovernment=localGovernmentService.findLocalGovernmentById(id);
        @NotBlank(message = "Add Local Government Name") String localGovernmentName="Amuwo";
        localGovernment.setLocalGovernmentName(localGovernmentName);

        localGovernmentService.updateLocalGovernment(localGovernment,id);

        this.mockMvc.perform(put("/api/localGovernment/updateLocalGovernmentById/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI4MjUyfQ.vO18UnEpo2UN4fhVTCsABJh-TIuwz2BvRWLt_bhyWuE")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(localGovernment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(localGovernment.getId()))
                .andExpect(jsonPath("$.localGovernmentName").value(localGovernmentName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteLocalGovernmentByIdMethod_ThenLocalGovernmentIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/localGovernment/deleteLocalGovernmentById/5")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI4MjUyfQ.vO18UnEpo2UN4fhVTCsABJh-TIuwz2BvRWLt_bhyWuE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllLocalGovernmentMethod_ThenAllLocalGovernmentAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/localGovernment/deleteAllLocalGovernments")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1NzI4MjUyfQ.vO18UnEpo2UN4fhVTCsABJh-TIuwz2BvRWLt_bhyWuE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}