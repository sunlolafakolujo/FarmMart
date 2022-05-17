package com.farmmart.data.repository.wishlist;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.data.model.wishlist.WishList;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.product.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Slf4j
@Transactional
@Sql(scripts = {"classpath:db/insert.sql"})
class WishListRepositoryTest {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    WishList wishList;

    Product product;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        wishList=new WishList();
        product=new Product();
        appUser=new AppUser();
    }

    @Test
    void testThatYouCanSaveWishList() throws ProductNotFoundException {
        Long id=1L;
        product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        String username ="HephzibahPam";
        appUser=appUserRepository.findByUserName(username);

        wishList.setCreatedDate(LocalDate.now());
        wishList.setAppUser(appUser);
        wishList.setProduct(product);

        log.info("Wishlist repo before saving: {}",wishList);

        assertDoesNotThrow(()->wishListRepository.save(wishList));
    }
}