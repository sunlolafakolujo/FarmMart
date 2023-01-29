package com.logicgate.product.repository;


import com.logicgate.category.model.Category;
import com.logicgate.product.model.Product;
import com.logicgate.seller.model.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryCustom {
    @Query("FROM Product p WHERE p.productCode=?1")
    Optional<Product> findByProductCode(String productCode);

    @Query("FROM Product p WHERE p.productType=?1 OR p.productName=?2 OR p.brand=?3")
    List<Product> findProductByTypeOrNameOrBrandOrPartNumber(String productType, String productName,
                                                             String brand, Pageable pageable);

    @Query("FROM Product p WHERE p.price >= ?1")
    List<Product> findProductByPriceWithinPriceLimit(BigDecimal price, Pageable pageable);

    @Query("FROM Product p WHERE p.price BETWEEN  ?1 And ?2")
    List<Product> findProductByPriceWithinRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

//    @Query("FROM Product p WHERE p.category=?1")
//    List<Product> findProductByCategory(Category category);

    @Query("FROM Product p WHERE p.seller=?1")
    List<Product> findProductBySeller(Seller seller,Pageable pageable);
}
