package com.logicgate.product.service;



import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.product.exception.ProductNotFoundException;
import com.logicgate.product.model.Product;
import com.logicgate.seller.exception.SellerNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product) throws AppUserNotFoundException;
    Product fetchProductById(Long id) throws ProductNotFoundException;
    List<Product> fetchAllProductsOrByTypeOrNameOrBrandOrPartNumber(String searchKey, Integer pageNumber);
    List<Product> fetchAllProductsOrByPriceWithinRange(BigDecimal minPrice,BigDecimal maxPrice, Integer pageNumber);
    List<Product> fetchAllProductsOrByPriceWithinLimit(BigDecimal price, Integer pageNumber);
    List<Product> fetchProductByCategory(String searchKey) throws ProductNotFoundException;
    List<Product> fetchProductBySeller(Integer pageNumber) throws SellerNotFoundException, AppUserNotFoundException;
    List<Product> fetchProductDetails(Boolean isSingleProductCheckout, Long productId) throws ProductNotFoundException, AppUserNotFoundException;
    Product updateProduct(Product product, Long id) throws ProductNotFoundException;
    void deleteProduct(Long id) throws ProductNotFoundException;
    void deleteProductByCode(String productCode) throws ProductNotFoundException, AppUserNotFoundException;
    void deleteAllProducts();
}
