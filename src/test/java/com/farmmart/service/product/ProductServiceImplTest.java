package com.farmmart.service.product;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.model.vendor.VendorNotFoundException;
import com.farmmart.data.repository.category.CategoryRepository;
import com.farmmart.data.repository.product.ProductRepository;
import com.farmmart.data.repository.vendor.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private CategoryRepository categoryRepository;

//    @Mock
//    private ColourRepository colourRepository;
//
    @InjectMocks
    private ProductService productService=new ProductServiceImpl();

    Vendor vendor;
    Product product;
    Category category;
//    Colour colour;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        product=new Product();
        category=new Category();
        vendor=new Vendor();
    }

    @Test
    void testThatYouCanMockSaveProductMethod() {

        Mockito.when(productRepository.save(product)).thenReturn(product);

        productService.saveProduct(product);

        ArgumentCaptor<Product> productArgumentCaptor=ArgumentCaptor.forClass(Product.class);

        Mockito.verify(productRepository, Mockito.times(1)).save(productArgumentCaptor.capture());

        Product capturedProduct=productArgumentCaptor.getValue();

        assertEquals(capturedProduct,product);
    }

    @Test
    void testThatYouCanMockFindProductByIdMethod() throws ProductNotFoundException {

        Long id =2L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.findProductById(id);

        Mockito.verify(productRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testThatYoyCanMockFindProductByType() throws ProductNotFoundException {
        String productType ="Pepper";

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByType(productType)).thenReturn(products);

        productService.findProductByType(productType);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByType(productType);


    }

    @Test
    void testThatYouCanMockFindProductByNameMethod() throws ProductNotFoundException {

        String name= "Sun Gold";

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByName(name)).thenReturn(products);

        productService.findProductByName(name);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByName(name);
    }

    @Test
    void testThatYouCanMockFindProductByBrandMethod() throws ProductNotFoundException {

        String brand= "Hoss";

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByBrand(brand)).thenReturn(products);

        productService.findProductByBrand(brand);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByBrand(brand);
    }

    @Test
    void testThatYouCAnMockFindProductByPriceLessThanOrEqualsMethod() throws ProductNotFoundException {

        BigDecimal price =BigDecimal.valueOf(6000);

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByPriceLessThanOrEqualsTo(price)).thenReturn(products);

        productService.findProductByPriceLessThanOrEquals(price);

        Mockito.verify(productRepository,Mockito.times(1)).findProductByPriceLessThanOrEqualsTo(price);
    }

    @Test
    void testThatYouCanMockFindProductByPriceGreaterThanOrEqualsMethod() throws ProductNotFoundException {

        BigDecimal price=BigDecimal.valueOf(200);

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByPriceGreaterThanOrEqualsTo(price)).thenReturn(products);

        productService.findProductByPriceGreaterThanOrEquals(price);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByPriceGreaterThanOrEqualsTo(price);
    }

    @Test
    void findProductByPriceRange() throws ProductNotFoundException {

        BigDecimal minPrice=BigDecimal.valueOf(200);
        BigDecimal maxPrice = BigDecimal.valueOf(6000);

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByPriceRange(minPrice, maxPrice)).thenReturn(products);

        productService.findProductByPriceRange(minPrice, maxPrice);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByPriceRange(minPrice, maxPrice);
    }

    @Test
    void testThatYouCanMockFindProductByPartNumberMethod() throws ProductNotFoundException {

        String partNumber= "H2346316";

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByPartNumber(partNumber)).thenReturn(products);

        productService.findProductByPartNumber(partNumber);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByPartNumber(partNumber);

    }

    @Test
    void testThatYouCanMockFindProductByCategoryMethod() throws CategoryNotFoundException {

        String categoryName="Seeds and Seedlings";

        category=categoryRepository.findByCategoryName(categoryName);

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findProductByCategory(category)).thenReturn(products);

        productService.findProductByCategory(category, categoryName);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByCategory(category);
    }

    @Test
    void testThatYouCanMockFindProductByVendorMethod() throws VendorNotFoundException {

        String vendorName="Zero Waste Farms";

        List<Product> products=new ArrayList<>();

        vendor=vendorRepository.findByName(vendorName);

        Mockito.when(productRepository.findProductByVendor(vendor)).thenReturn(products);

        productService.findProductByVendor(vendor, vendorName);

        Mockito.verify(productRepository, Mockito.times(1)).findProductByVendor(vendor);
    }

    @Test
    void testThatYouCanMockFindAllProductMethod() {

        List<Product> products=new ArrayList<>();

        Mockito.when(productRepository.findAll()).thenReturn(products);

        productService.findAllProduct(10);

        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockUpdateProductMethod() throws ProductNotFoundException {

        Long id =2L;

        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));

        productService.updateProduct(product, id);

        Mockito.verify(productRepository, Mockito.times(1)).save(product);
    }

    @Test
    void deleteProductById() throws ProductNotFoundException {

        Long id =1L;

        doNothing().when(productRepository).deleteById(id);

        productService.deleteProductById(id);

        Mockito.verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAllProduct() {

        doNothing().when(productRepository).deleteAll();

        productService.deleteAllProduct();

        verify(productRepository, times(1)).deleteAll();
    }
}