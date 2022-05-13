package com.farmmart.controller.userrolerestcontrol;

import com.farmmart.data.model.userrole.UserRole;
import com.farmmart.data.model.userrole.UserRoleNotFoundException;
import com.farmmart.service.userrole.UserRoleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@AutoConfigureMockMvc
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class UserRoleRestControllerTest {

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserRole userRole;

    @BeforeEach
    void setUp() {
        userRole=new UserRole();
    }

    @Test
    void testThatWhenYouCallSaveNewRoleMethod_ThenRoleIsSaved() throws Exception {
        @NotBlank String roleName="ROLE_MANAGER";
        userRole.setRoleName(roleName);

        this.mockMvc.perform(post("/api/userRole/saveRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userRole)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name").value(roleName))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetRoleByIdMethod_ThenRoleIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/userRole/findRoleById/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name").value("SUPPLIER_ROLE"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetRoleByNameMethod_ThenRoleISReturned() throws Exception {

        this.mockMvc.perform(get("/api/userRole/findRoleByName/EMPLOYEE_ROLE"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name").value("EMPLOYEE_ROLE"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllRolesMethod_ThenAllRolesAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/userRole/findAllRoles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(6)))
                .andExpect(jsonPath("$[2].name").value("DEFAULT_ROLE"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateRoleMethod_ThenRoleIsUpdated() throws UserRoleNotFoundException, Exception {
        Long id=3L;
        userRole=userRoleService.findUserRoleById(id);
        userRole.setRoleName("ROLE_INTERN");
        userRoleService.updateUserRole(userRole,id);

        this.mockMvc.perform(put("/api/userRole/updateRoleById/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userRole)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userRole.getId()))
                .andExpect(jsonPath("$.name").value("INTERN"))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteRoleByIdMethod_ThenRoleIsDeleted() throws Exception {
        this.mockMvc.perform(delete("/api/userRole/deleteRoleById/6"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYoCalDeleteAllRolesMethod_ThenAllRolesAreDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/userRole/deleteAllRoles"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}