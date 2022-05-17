package com.farmmart.service.product;

import com.farmmart.data.model.category.Category;
import com.farmmart.data.model.category.CategoryNotFoundException;
import com.farmmart.data.model.product.Product;
import com.farmmart.data.model.product.ProductNotFoundException;
import com.farmmart.data.model.vendor.Vendor;
import com.farmmart.data.repository.category.CategoryRepository;
import com.farmmart.data.repository.product.ProductRepository;
import com.farmmart.data.repository.vendor.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private VendorRepository vendorRepository;


    @Override
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductNotFoundException {

        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        return product;
    }

    @Override
    public List<Product> findProductByName(String productName) throws ProductNotFoundException {

        List<Product> products= productRepository.findProductByName(productName);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByType(String productType) throws ProductNotFoundException {
        List<Product> products= productRepository.findProductByType(productType);

        if (products==null){
            throw new ProductNotFoundException("Product Type Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByBrand(String brand) throws ProductNotFoundException {

        List<Product> products=productRepository.findProductByBrand(brand);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByPriceLessThanOrEquals(BigDecimal price) throws ProductNotFoundException {

        List<Product> products=productRepository.findProductByPriceLessThanOrEqualsTo(price);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByPriceGreaterThanOrEquals(BigDecimal price) throws ProductNotFoundException {

        List<Product> products=productRepository.findProductByPriceGreaterThanOrEqualsTo(price);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) throws ProductNotFoundException {

        List<Product> products=productRepository.findProductByPriceRange(minPrice, maxPrice);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByPartNumber(String partNumber) throws ProductNotFoundException {

        List<Product> products=productRepository.findProductByPartNumber(partNumber);

        if (products==null){
            throw new ProductNotFoundException("Product Not Found");
        }

        return products;
    }

    @Override
    public List<Product> findProductByCategory(Category category, String categoryName) throws CategoryNotFoundException {

        category=categoryRepository.findByCategoryName(categoryName);

        if (category==null){
            throw new CategoryNotFoundException("Product Category Not Found");
        }

        List<Product> products=productRepository.findProductByCategory(category);

        return products;
    }

    @Override
    public List<Product> findProductByVendor(Vendor vendor, String vendorName) {

        vendor=vendorRepository.findByName(vendorName);

        List<Product> products=productRepository.findProductByVendor(vendor);

        return products;
    }

    @Override
    public List<Product> findAllProduct(Integer limit) {

        return productRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Product updateProduct(Product product, Long id) throws ProductNotFoundException {

        Product savedProduct=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));

        if (Objects.nonNull(product.getColours())){
            savedProduct.setColours(product.getColours());
        }if (Objects.nonNull(product.getPrice())){
            savedProduct.setPrice(product.getPrice());
        }if (Objects.nonNull(product.getStockQuantity())){
            savedProduct.setStockQuantity(product.getStockQuantity());
        }

        return productRepository.save(savedProduct);
    }

    @Override
    public void deleteProductById(Long id) throws ProductNotFoundException {
            productRepository.deleteById(id);

        Optional<Product> optionalProduct=productRepository.findById(id);

        if (optionalProduct.isPresent()){
            throw new ProductNotFoundException("Product id "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllProduct() {

        productRepository.deleteAll();

    }
}
