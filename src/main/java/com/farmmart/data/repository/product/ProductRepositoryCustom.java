package com.farmmart.data.repository.product;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.vendor.Vendor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {

    @Query("From Product p Where p.productType Like %?#{[0].toUpperCase()}%")
    List<Product> findProductByType(String productType);

    @Query("From Product p Where p.productName Like %?#{[0].toUpperCase()}%")
    List<Product> findProductByName(String productName);

    @Query("From Product p Where p.brand Like %?#{[0].toUpperCase()}%")
    List<Product> findProductByBrand(String brand);

    @Query("From Product p Where p.price <= ?1")
    List<Product> findProductByPriceLessThanOrEqualsTo(BigDecimal price);

    @Query("From Product p Where p.price >= ?1")
    List<Product> findProductByPriceGreaterThanOrEqualsTo(BigDecimal price);

    @Query("From Product p Where p.price Between  ?1 And ?2")
    List<Product> findProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("From Product p Where p.partNumber=?1")
    List<Product> findProductByPartNumber(String partNumber);

    @Query("From Product p Where p.category=?1")
    List<Product> findProductByCategory(Category category);

    @Query("FROM Product p WHERE p.vendor=?1")
    List<Product> findProductByVendor(Vendor vendor);
}
