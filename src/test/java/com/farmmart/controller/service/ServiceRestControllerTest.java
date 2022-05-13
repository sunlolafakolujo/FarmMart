package com.farmmart.controller.servicerestcontroller;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.services.Service;
import com.farmmart.data.model.services.ServiceNotFoundException;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.service.category.CategoryServiceImp;
import com.farmmart.service.service.ServiceImpl;
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
class ServiceRestControllerTest {

    @Autowired
    private ServiceImpl serviceImpl;

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @Autowired
    private VendorServiceImpl vendorService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Service service;
    Category category;
    Vendor vendor;

    @BeforeEach
    void setUp() {
        service=new Service();
        category=new Category();
        vendor=new Vendor();
    }

    @Test
    void testThatWhenYouCallSaveServiceMethod_thenServiceIsSaved() throws CategoryNotFoundException, VendorNotFoundException, Exception {
        Long id =40L;
        category=categoryServiceImp.findCategoryById(id);

        Long vid= 3L;
        vendor=vendorService.findVendorById(vid);

        service.setServiceName("Haulage");
        service.setServiceDescription("Transportation of goods from one location to another");
        service.setCategory(category);
        service.setVendor(vendor);

        this.mockMvc.perform(post("/api/service/saveService")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODE1NTkyfQ.4ztErFTrXZytvgSgTONisyLmY-ZDRhmy0uXPc6pY4TQ")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(service)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.serviceName", is("Haulage")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetServiceByIdMethod_thenServiceIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/service/findServiceById/3")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ2Njg5fQ.CmCb3I9bMTPLFVgVqb56SmXYRI9geSSb5fzu0-GXLS4")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.serviceName", is("Composting")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetServiceByNameMethod_thenServiceIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/service/findServiceByName/Animal Grading")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ0MDk4fQ.ewPzSZ_wkWS1Ldw2AJma4ZRtH3xTNjdDPGYKyOyXluE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serviceName", is("Animal Grading")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllServicesMethod_thenServicesAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/service/findAllServices")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ0MDk4fQ.ewPzSZ_wkWS1Ldw2AJma4ZRtH3xTNjdDPGYKyOyXluE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[1].serviceName", is("Animal Grading")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetServiceByCategoryMethod_thenServiceIsOrAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/service/findServiceByCategory/Livestock Breeder")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ0MDk4fQ.ewPzSZ_wkWS1Ldw2AJma4ZRtH3xTNjdDPGYKyOyXluE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetServiceByVendorMethod_thenServiceIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/service/findServiceByVendor/zero w")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ0MDk4fQ.ewPzSZ_wkWS1Ldw2AJma4ZRtH3xTNjdDPGYKyOyXluE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateServiceMethod_thenServiceIsUpdated() throws ServiceNotFoundException, Exception {
        Long id =1L;
        service=serviceImpl.findServiceById(id);

        service.setServiceName("Animal Breeding");

        serviceImpl.updateService(service, id);

        this.mockMvc.perform(put("/api/service/updateService/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ1OTkyfQ.KFeVc-A7-FZoXYhctjcD_5i-1S-uOJ1R_XiQoxqLClk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(service)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(service.getId()))
//                .andExpect(jsonPath("$.serviceName").value("Animal Breeding"))
                .andReturn();


    }

    @Test
    void testThatWhenYouCallDeleteServiceByIdMethod_thenAServiceIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/service/deleteServiceById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ1OTkyfQ.KFeVc-A7-FZoXYhctjcD_5i-1S-uOJ1R_XiQoxqLClk"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllServices_thenServicesAreDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/service/deleteAllServices")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ3ODQ2Njg5fQ.CmCb3I9bMTPLFVgVqb56SmXYRI9geSSb5fzu0-GXLS4"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}