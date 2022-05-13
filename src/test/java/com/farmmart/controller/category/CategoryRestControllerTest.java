package com.farmmart.controller.categoryrestcontroller;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.service.category.CategoryServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql(scripts = {"classpath:db/insert.sql "})
@AutoConfigureMockMvc
class CategoryRestControllerTest {

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Category category;

    @BeforeEach
    void setUp() {
        category=new Category();
    }

    @Test
    @WithMockUser
    void testThatWhenYouCallSaveCategoryMethod_thenProductCategoryIsSaved() throws Exception {

        String name="Chinchin";

        category.setCategoryName(name);

        this.mockMvc.perform(post("/api/category/saveCategory")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg3Njk1fQ.YtSJrFJqCf86SwRwi7wd4mDu8oeFBnFvGIqGGckbXQA")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(category)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.categoryName").value("Chinchin"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCategoryByIdMethod_thenCategoryIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/category/findCategoryById/34")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg3Njk1fQ.YtSJrFJqCf86SwRwi7wd4mDu8oeFBnFvGIqGGckbXQA")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.categoryName").value("Cassava Processing"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetCategoryByNameMethod_thenCategoryIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/category/findCategoryByName/Agro Chemical")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg3Njk1fQ.YtSJrFJqCf86SwRwi7wd4mDu8oeFBnFvGIqGGckbXQA")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllCategoryMethod_thenAllProductCategoriesAreReturned() throws Exception {
        this.mockMvc.perform(get("/api/category/findAllCategories")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg3Njk1fQ.YtSJrFJqCf86SwRwi7wd4mDu8oeFBnFvGIqGGckbXQA")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(46)))
                .andExpect(jsonPath("$[0].categoryName").value("Horticulture"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateCategoryMethod_thenCategoryIsUpdated() throws CategoryNotFoundException, Exception {
        Long id =7L;
        category=categoryServiceImp.findCategoryById(id);

        category.setCategoryName("Cereal");

        categoryServiceImp.updateCategory(category, id);

        this.mockMvc.perform(put("/api/category/updateCategoryById/7")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg3Njk1fQ.YtSJrFJqCf86SwRwi7wd4mDu8oeFBnFvGIqGGckbXQA")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(category)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.categoryName", is("Cereal")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteCategoryByIdMethod_thenCategoryIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/category/deleteCategoryById/40")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg4MzkxfQ.gEWJqnFC6Bn6zzhSHd3xA1zbSp0dbGPpPmoe-C-XIoc")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllCategoriesMethod_thenCategoriesAreDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/category/deleteAllCategories")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ1OTg4MzkxfQ.gEWJqnFC6Bn6zzhSHd3xA1zbSp0dbGPpPmoe-C-XIoc")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}