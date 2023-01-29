package com.logicgate.product.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.category.model.Category;
import com.logicgate.category.repository.CategoryRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.product.exception.ProductNotFoundException;
import com.logicgate.product.model.Product;
import com.logicgate.product.repository.ProductRepository;
import com.logicgate.seller.exception.SellerNotFoundException;
import com.logicgate.seller.model.Seller;
import com.logicgate.seller.repository.SellerRepository;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.shoppingcart.repository.ShoppingCartRepository;
import com.logicgate.staticdata.ProductAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public Product addProduct(Product product) throws AppUserNotFoundException {
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+"Not Found"));
        Seller seller=sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
        product.setSeller(seller);
        product.setProductCode("ITEM".concat(String.valueOf(new Random().nextInt(100000000))));
        product.setProductAvailability(product.getAvailableStockQuantity()<=0?
                ProductAvailability.OUT_OF_STOCK:ProductAvailability.IN_STOCK);
        return productRepository.save(product);
    }

    @Override
    public Product fetchProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product Not Found"));
    }

    @Override
    public List<Product> fetchAllProductsOrByTypeOrNameOrBrandOrPartNumber(String searchKey, Integer pageNumber) {
        Pageable pageable= PageRequest.of(pageNumber,20);
        if (searchKey.equals("")){
            return productRepository.findAll(pageable).toList();
        }else {
            return productRepository.findProductByTypeOrNameOrBrandOrPartNumber(searchKey, searchKey,
                    searchKey, pageable);
        }
    }

    @Override
    public List<Product> fetchAllProductsOrByPriceWithinRange(BigDecimal minPrice, BigDecimal maxPrice, Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (maxPrice==null && minPrice==null){
            return productRepository.findAll(pageable).toList();
        }else {
            return productRepository.findProductByPriceWithinRange(minPrice, maxPrice, pageable);
        }
    }

    @Override
    public List<Product> fetchAllProductsOrByPriceWithinLimit(BigDecimal price, Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        if (price==null){
            return productRepository.findAll(pageable).toList();
        }else {
            return productRepository.findProductByPriceWithinPriceLimit(price, pageable);
        }
    }

    @Override
    public List<Product> fetchProductByCategory(String searchKey) throws ProductNotFoundException {
//        Pageable pageable=PageRequest.of(pageNumber,10);
        Category category=categoryRepository.findCategoryByCodeOrName(searchKey,searchKey)
                .orElseThrow(()->new ProductNotFoundException("Product "+searchKey+" Not Found"));
        List<Product>products=category.getProducts();
        return products;
    }

    @Override
    public List<Product> fetchProductBySeller(Integer pageNumber) throws SellerNotFoundException, AppUserNotFoundException {
        Pageable pageable=PageRequest.of(pageNumber,10);
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Seller seller=sellerRepository.findSellerByUsernameOrEmailOMobile(appUser);
        return productRepository.findProductBySeller(seller,pageable);
    }

    @Override
    public List<Product> fetchProductDetails(Boolean isSingleProductCheckout, Long productId) throws ProductNotFoundException, AppUserNotFoundException {
        if (isSingleProductCheckout && productId!=0){
            List<Product> products=new ArrayList<>();
            Product product=productRepository.findById(productId)
                    .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
            products.add(product);
            return products;
        }else {
            String searchKey= JwtRequestFilter.CURRENT_USER;
            AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                    .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
            Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
            List<ShoppingCart> shoppingCarts=shoppingCartRepository.findShoppingCartByBuyer(buyer);
            return shoppingCarts.stream().map(p ->p.getProduct()).collect(Collectors.toList());
        }
    }

    @Override
    public Product updateProduct(Product product, Long id) throws ProductNotFoundException {
        Product savedProduct=productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found"));
        if (Objects.nonNull(product.getPrice())&& !"".equals(product.getPrice())){
            savedProduct.setPrice(product.getPrice());
        }if (Objects.nonNull(product.getAvailableStockQuantity()) && !"".equals(product.getAvailableStockQuantity())){
            savedProduct.setAvailableStockQuantity(product.getAvailableStockQuantity());
        }if (Objects.nonNull(product.getProductSKU()) && !"".equalsIgnoreCase(product.getProductSKU())){
            savedProduct.setProductSKU(product.getProductSKU());
        }if (Objects.nonNull(product.getPictures()) && !"".equals(product.getPictures())){
            savedProduct.setPictures(product.getPictures());
        }
        return productRepository.save(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
        }else {
            throw new ProductNotFoundException("Product ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }
}
