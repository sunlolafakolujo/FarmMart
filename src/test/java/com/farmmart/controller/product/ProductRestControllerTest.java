package com.farmmart.controller.productrestcontroller;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.colour.Colour;
import com.farmmart.data.model.colour.ColourNotFoundException;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.data.model.staticdata.ProductAvailability;
import com.farmmart.data.model.staticdata.ProductCondition;
import com.farmmart.data.model.staticdata.UnitOfMeasure;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.service.category.CategoryServiceImp;
import com.farmmart.service.colour.ColourServiceImpl;
import com.farmmart.service.product.ProductServiceImpl;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ProductRestControllerTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private VendorServiceImpl vendorService;

    @Autowired
    private CategoryServiceImp categoryServiceImp;

    @Autowired
    private ColourServiceImpl colourService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Product product;

    Vendor vendor;

    Category category;

    Colour colour;

    @BeforeEach
    void setUp() {
        product=new Product();
        vendor=new Vendor();
        category=new Category();
        colour=new Colour();
    }

    @Test
    void testThatWhenYouCallSaveProductMethod_thenProductIsSaved() throws VendorNotFoundException,
            CategoryNotFoundException,
            ColourNotFoundException, Exception {

        vendor=vendorService.findVendorById(2L);

        category=categoryServiceImp.findCategoryById(11L);

        colour=colourService.findColourById(1L);
        List<Colour> colours=new ArrayList<>();
        colours.add(colour);

        product.setVendor(vendor);
        product.setCategory(category);
        product.setColours(colours);
        product.setProductCondition(ProductCondition.NEW);
        product.setProductDimension("45 × 17 × 9 in");
        product.setProductType("Planter");
        product.setProductSKU("HGS051");
        product.setProductDescription("The Hoss Garden Seeder allows you to easily plant your vegetable garden with accuracy and precision. " +
                "Our innovative seed plate design allows you to easily customize seed plates to fit the variety of seed you’re planting. " +
                "Seed plates lie flat in the hopper and can be easily changed without wasting excess seed.\n" +
                "\n" +
                "The Hoss Garden Seeder has a rolling coulter or rolling disk that opens a furrow for the dropping seed. " +
                "This double disk furrow opener works great in soils that have heavy organic residue from prior vegetable " +
                "or cover crops. Once the seed drops into the planting furrow, the drag chain covers the seed and the rear wheel " +
                "packs the soil to ensure optimal germination.\n" +
                "\n" +
                "The Hoss Garden Seeder is constructed with Amish-crafted hardwood handles, 15″ steel wheels, and a powder-coated " +
                "steel frame. The handles are adjustable to the users height. The steel frame ensures the unit is stable and easy to " +
                "push in a straight line along the row.\n" +
                " Hoss Garden Seeder features:\n" +
                "Removable hopper for filling and emptying seed\n" +
                "Adjustable planting depth from 1/4″ – 1 1/2″\n" +
                "Stand-alone design with back kickstand\n" +
                "Plant small to large seeds with accuracy and precision\n" +
                "Easily change seed plates with simple wing nut design\n" +
                "Includes Seed Plates #1-6; additional plates available here\n" +
                "Optional Row Marker available\n" +
                "Included:\n" +
                "Seed Plate #1 (HS4-1001) – broccoli, cauliflower, cabbage, turnips, mustard, rutabaga, and other fine seeds\n" +
                "Seed Plate #2 (HS4-1002) – onion, small coated seeds\n" +
                "Seed Plate #3 (HS4-1003) – okra, pelleted: carrots, lettuce, beets, chard\n" +
                "Seed Plate #4 (HS4-1004) – small sweet corn, popcorn\n" +
                "Seed Plate #5 (HS4-1005) – small peas and beans\n" +
                "Seed Plate #6 (HS4-1006) – medium beans, peas, large sweet corn, field corn");
        product.setProductAvailability(ProductAvailability.IN_STOCK);
        product.setProductStyle("");
        product.setProductName("Garden Seeder");
        product.setWeight("38 lbs");
        product.setPrice(BigDecimal.valueOf(251993));
        product.setUnitOfMeasure(UnitOfMeasure.EA);
        product.setStockQuantity(10);
        product.setPartNumber("");
        product.setBrand("Hoss");

        this.mockMvc.perform(post("/api/product/saveProduct")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQwNTkzfQ.AmUwAXZrsPcTowqU12_PsXP8uJ9PBzoYivmxG64_cng")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(product)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.productName", is("Garden Seeder")))
                .andExpect(jsonPath("$.brand", is("Hoss")))
                .andExpect(jsonPath("$.stockQuantity", is(10)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByIdMethod_thenProductIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ2NjQ4fQ.dq7byDhEDGue2Ve5u19XWqOLJFTFZnjokgIOFIBHVXE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName", is("National Pickling")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByNameMethod_thenProductIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByName/National Pickling")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQxMDg3fQ.3CiIycc67NoAA386iC-Y57A-JS99Dg-sTYktpDEK2Ik")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByTypeMethod_thenProductIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByType/watermelon")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQxMDg3fQ.3CiIycc67NoAA386iC-Y57A-JS99Dg-sTYktpDEK2Ik")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByBrandMethod_thenProductIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByBrand/hoss")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQxMDg3fQ.3CiIycc67NoAA386iC-Y57A-JS99Dg-sTYktpDEK2Ik")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$[0].brand", is("Hoss")))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductPriceLessThanOrEqualsToMethod_thenProductIsReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByPriceLessThan/6000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ2NjQ4fQ.dq7byDhEDGue2Ve5u19XWqOLJFTFZnjokgIOFIBHVXE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].price", is(5000.00)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductPriceGreaterThanOrEqualsToMethod_thenProductIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByPriceGreaterThan/7000")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQxODQ5fQ.a8on4TRUe4hMqwGw6KtX3CxLOo4_rr88mNDVoItiRqo")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
//                .andExpect(jsonPath("$[3].price", is(15000)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByPriceRangeMethod_thenProductIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByPriceRange/5500,16500")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjUzNTg3fQ.9pWS8iBnF2kTI2QTDz2MXMec2btk2syhJOznKv_H09A")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[2].price", is(6000)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByPartNumberMethod_thenProductIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByPartNumber/H2346316")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ0MTc1fQ.yhq-jdnCYczJ4XJscLKduA0gpsopiJ0hBmGB0IRk6gM")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize(1)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByCategoryMethod_thenProductIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByCategory/Seeds and Seedlings")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ0MTc1fQ.yhq-jdnCYczJ4XJscLKduA0gpsopiJ0hBmGB0IRk6gM")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetProductByVendorMethod_thenProductIsOrAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findProductByVendor/zero waste")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ0MTc1fQ.yhq-jdnCYczJ4XJscLKduA0gpsopiJ0hBmGB0IRk6gM")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallGetAllProductsMethod_thenProductsAreReturned() throws Exception {

        this.mockMvc.perform(get("/api/product/findAllProducts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ0MTc1fQ.yhq-jdnCYczJ4XJscLKduA0gpsopiJ0hBmGB0IRk6gM")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallUpdateProductMethod_thenProductIsUpdated() throws ProductNotFoundException, Exception {
        Long id =2L;
        product=productService.findProductById(id);

        product.setStockQuantity(100);

        productService.updateProduct(product, id);

        this.mockMvc.perform(put("/api/product/updateProductById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ0MTc1fQ.yhq-jdnCYczJ4XJscLKduA0gpsopiJ0hBmGB0IRk6gM")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.stockQuantity", is(100)))
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteProductByIdMethod_thenProductIsDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/product/deleteProductById/2")
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJaZXJvV2FzdGUiLCJyb2xlcyI6WyJST0xFX1ZFTkRPUiJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ2NDIzfQ.f7Cu6_8sIeEjHgPLTg-DkN8ZOqB7bhuH5IvllDoaN_o")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    void testThatWhenYouCallDeleteAllProductsMethod_thenAllProductsAreDeleted() throws Exception {

        this.mockMvc.perform(delete("/api/product/deleteAllProducts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJIZXBoemliYWhQYW0iLCJyb2xlcyI6WyJST0xFX0FETUlOIiwiUk9MRV9FTVBMT1lFRSJdLCJpc3MiOiIvYXBpL2xvZ2luIiwiZXhwIjoxNjQ2NjQ2NjQ4fQ.dq7byDhEDGue2Ve5u19XWqOLJFTFZnjokgIOFIBHVXE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }
}