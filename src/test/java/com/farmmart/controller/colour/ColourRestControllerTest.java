package com.farmmart.controller.colourrestcontroller;

import com.farmmart.data.model.colour.Colour;
import com.farmmart.data.model.colour.ColourNotFoundException;
import com.farmmart.service.colour.ColourServiceImpl;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ColourRestControllerTest {

    @Autowired
    private ColourServiceImpl colourService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Colour colour;

    @BeforeEach
    void setUp() {
        colour=new Colour();
    }

    @Test
    void testThatWhenYouCallSaveColourMethod_thenColourIsSaved() throws Exception {
        colour.setColourName("Gold");

        this.mockMvc.perform(post("/api/colour/saveColour")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MTQ3NDkxfQ.wxa71R5EucvKrS28hF9j3H88d9__fvtFAwD4PK0QW5M")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(colour)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.colourName").value("Gold"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetColourByIdMethod_thenColourIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/colour/findColourById/4")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.colourName", is("Yellow")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetColourByNameMethod_thenColourIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/colour/findColourByName/blue")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.colourName", is("Blue")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllColoursMethod_thenColoursAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/colour/findAllColours")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$[3].colourName", is("Yellow")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateColourMethod_thenColourIsUpdated() throws ColourNotFoundException, Exception {

        Long id=1L;
        colour=colourService.findColourById(id);

        colour.setColourName("Army Green");

        colourService.updateColour(colour, id);

        this.mockMvc.perform(put("/api/colour/updateColourById/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(colour)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(colour.getId()))
                .andExpect(jsonPath("$.colourName", is("Army Green")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteColourMethod_thenColourIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/colour/deleteColourById/2")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllColoursMethod_thenColoursAreDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/colour/deleteAllColours")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2MDcyNjIyfQ.7Ka6qCLIZUPjUiy1dg4D72nAYKXRc5-RbPMwni2_blo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}