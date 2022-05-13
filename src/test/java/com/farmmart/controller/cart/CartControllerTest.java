package com.farmmart.controller.cart;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.cart.Cart;
import com.farmmart.data.model.cart.CartNotFoundException;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.cart.CartServiceImpl;
import com.farmmart.service.product.ProductServiceImpl;
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
@Transactional
@AutoConfigureMockMvc
@Sql(scripts = {"classpath:db/insert.sql"})
@Slf4j
class CartControllerTest {
    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private AppUserDetailService appUserDetailService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Cart cart;

    AppUser appUser;

    Product product;

    @BeforeEach
    void setUp() {
        cart=new Cart();
        appUser=new AppUser();
        product=new Product();
    }

    @Test
    void testThatWhenYouCallAddToCartMethod_thenCartIsSaved() throws AppUserNotFoundException, ProductNotFoundException, Exception {
        String username="AkinEmma";
        appUser=appUserDetailService.findUserByUsername(username);

        Long id=4L;
        product=productService.findProductById(id);

        Integer orderQuantity=2;

        cart.setOrderQuantity(orderQuantity);
        cart.setAppUser(appUser);
        cart.setProduct(product);

        this.mockMvc.perform(post("/api/cart/addToCart")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYyODczfQ.k8MEtyK4pK-cYx244Ne4bqbliTlfrmh5ZVYlvVP1pX8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cart)))
                .andDo(print())
                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.OrderQuantity").value(2))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallFindCartByIdMethod_thenCartIsReturned() throws Exception {
        this.mockMvc.perform(get("/api/cart/findCartById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYyODczfQ.k8MEtyK4pK-cYx244Ne4bqbliTlfrmh5ZVYlvVP1pX8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallListCartItems_thenUserCartISOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/cart/listCartItemsByUsername/BukolaFako")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYzNjUzfQ.7qBnfD6zz8uqrad7YKpy6Hkb3zgPbgBFuAOFno5wsTM"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
//                .andExpect(jsonPath("$[0].orderQuantity").value(2))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllCartsMethod_thenCartsAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/cart/findAllCarts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYzNjUzfQ.7qBnfD6zz8uqrad7YKpy6Hkb3zgPbgBFuAOFno5wsTM"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[1].orderQuantity").value(3))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateCartMethod_thenCartIsUpdated() throws CartNotFoundException, Exception {

        Long id=1L;

        cart=cartService.findCartById(id);

        Integer orderQuantity=6;
        cart.setOrderQuantity(orderQuantity);

        cart=cartService.updateCart(cart,id);

        this.mockMvc.perform(put("/api/cart/updateCartById/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYzNjUzfQ.7qBnfD6zz8uqrad7YKpy6Hkb3zgPbgBFuAOFno5wsTM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(cart)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cart.getId()))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteCartByIdMethod_thenCartIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/cart/deleteCartById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDYzNjUzfQ.7qBnfD6zz8uqrad7YKpy6Hkb3zgPbgBFuAOFno5wsTM"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteCartByAppUser_thenUserCartIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/cart/deleteCartByAppUser/AkinEmma")
                .header(HttpHeaders.AUTHORIZATION, "Bearer "))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllCarts_thenCartIsOrAreDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/cart/deleteAllCarts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ4NDY0NDIzfQ.1A-7SB5UqVEOYKnUW20T6_4_dXPHmcveYSj2I2lLXK8"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}