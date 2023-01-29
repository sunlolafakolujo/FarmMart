package com.logicgate.contact.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicgate.contact.exception.ContactNotFoundException;
import com.logicgate.contact.model.Contact;
import com.logicgate.contact.service.ContactService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = {"classpath:db/insert.sql"})
@Slf4j
@AutoConfigureMockMvc
class ContactControllerTest {
    @Autowired
    private ContactService contactService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    Contact contact;

    @BeforeEach
    void setUp() {
        contact=new Contact();
    }

    @Test
    void testThatYouWhenYouCallAddContactMethod_thenContactIsAdded() throws Exception {
        contact.setHouseNumber("11");
        contact.setStreetName("Kasabubu Street, Igando");
        contact.setCity("Ojo");
        contact.setLandmark("Igando Bus stop");
        contact.setStateProvince("Lagos");
        contact.setCountry("Nigeria");

        this.mockMvc.perform(post("/api/farmmart/addContact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(contact))
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxPRmFrb2x1am8iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9FTVBMT1lFRSIsImlzcyI6IkxvZ2ljR2F0ZSIsImlhdCI6MTY3NDA0NzY5NSwiZXhwIjoxNzEwMDQ3Njk1fQ.j8OOmZ1vrtSuLWhtuBZ0JN4qw8toN5iWl8-SJVLmqNM"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.streetName",is("Kasabubu Street, Igando")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetContactByIdMethod_thenContactIsReturned() throws Exception {
        Long id=1L;
        this.mockMvc.perform(get("/api/farmmart/findContactById?id=1")
                        .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxPRmFrb2x1am8iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9FTVBMT1lFRSIsImlzcyI6IkxvZ2ljR2F0ZSIsImlhdCI6MTY3NDA0NzY5NSwiZXhwIjoxNzEwMDQ3Njk1fQ.j8OOmZ1vrtSuLWhtuBZ0JN4qw8toN5iWl8-SJVLmqNM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.streetName",is("Road N Sparklight Estate, OPIC Isheri")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllContactsMethod_thenContactsAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/farmmart/findAllContacts?pageNumber=0")
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxPRmFrb2x1am8iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9FTVBMT1lFRSIsImlzcyI6IkxvZ2ljR2F0ZSIsImlhdCI6MTY3NDA0NzY5NSwiZXhwIjoxNzEwMDQ3Njk1fQ.j8OOmZ1vrtSuLWhtuBZ0JN4qw8toN5iWl8-SJVLmqNM"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(10)))
                .andExpect(jsonPath("$[3].streetName",is("Molete Road, Ibadan")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateContactMethod_thenContactIsUpdated() throws ContactNotFoundException, Exception {
        contact=contactService.fetchContactById(1L);
        contact.setHouseNumber("2A");
        contactService.updateContact(contact,1L);
        this.mockMvc.perform(put("/api/farmmart/updateContact?id=1")
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxPRmFrb2x1am8iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9FTVBMT1lFRSIsImlzcyI6IkxvZ2ljR2F0ZSIsImlhdCI6MTY3NDA0NzY5NSwiZXhwIjoxNzEwMDQ3Njk1fQ.j8OOmZ1vrtSuLWhtuBZ0JN4qw8toN5iWl8-SJVLmqNM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(contact)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.houseNumber",is("2A")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteContactByIdMethod_thenContactIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/farmmart/deleteContact?id=126")
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkYW5pZWxPRmFrb2x1am8iLCJyb2xlcyI6IlJPTEVfQURNSU4sUk9MRV9FTVBMT1lFRSIsImlzcyI6IkxvZ2ljR2F0ZSIsImlhdCI6MTY3NDA0NzY5NSwiZXhwIjoxNzEwMDQ3Njk1fQ.j8OOmZ1vrtSuLWhtuBZ0JN4qw8toN5iWl8-SJVLmqNM"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$",notNullValue()))
                .andReturn();
    }

    @Test
    void deleteAllContacts() {
    }
}