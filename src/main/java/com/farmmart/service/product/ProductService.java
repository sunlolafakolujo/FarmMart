package com.farmmart.service.product;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.data.model.vendor.Vendor;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product saveProduct(Product product);
    Product findProductById(Long id) throws ProductNotFoundException;
    List<Product> findProductByName(String productName) throws ProductNotFoundException;
    List<Product> findProductByType(String productType) throws ProductNotFoundException;
    List<Product> findProductByBrand(String brand) throws ProductNotFoundException;
    List<Product> findProductByPriceLessThanOrEquals(BigDecimal price) throws ProductNotFoundException;
    List<Product> findProductByPriceGreaterThanOrEquals(BigDecimal price) throws ProductNotFoundException;
    List<Product> findProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws ProductNotFoundException;
    List<Product> findProductByPartNumber(String partNumber) throws ProductNotFoundException;
    List<Product> findProductByCategory(Category category, String categoryName) throws CategoryNotFoundException;
    List<Product> findProductByVendor(Vendor vendor, String vendorName);
    List<Product> findAllProduct(Integer limit);
    Product updateProduct(Product product, Long id) throws ProductNotFoundException;
    void deleteProductById(Long id) throws ProductNotFoundException;
    void deleteAllProduct();



}
