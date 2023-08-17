package com.logicgate.shoppingcart.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.configuration.security.jwtrequestfilter.JwtRequestFilter;
import com.logicgate.product.model.Product;
import com.logicgate.product.repository.ProductRepository;
import com.logicgate.shoppingcart.exception.ShoppingCartNotFoundException;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.shoppingcart.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService{
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ShoppingCart addProductToShoppingCart(ShoppingCart shoppingCart)  {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart fetchCartById(Long id) throws ShoppingCartNotFoundException {
        return shoppingCartRepository.findById(id)
                .orElseThrow(()->new ShoppingCartNotFoundException("Cart ID "+id+" Not Found"));
    }

    @Override
    public ShoppingCart fetchCartByCode(String shoppingCartCode) throws ShoppingCartNotFoundException {
        return shoppingCartRepository.findByShoppingCartCode(shoppingCartCode)
                .orElseThrow(()->new ShoppingCartNotFoundException("Shopping Cart "+shoppingCartCode+" Not Found"));
    }

    @Override
    public List<ShoppingCart> fetchBuyerShoppingCart() throws AppUserNotFoundException {
//        Pageable pageable= PageRequest.of(pageNumber,10);
        String searchKey= JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()-> new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        return shoppingCartRepository.findShoppingCartByBuyer(buyer);
    }

    @Override
    public List<ShoppingCart> fetchAllShoppingCart(Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        return shoppingCartRepository.findAll(pageable).toList();
    }

    @Override
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart, Long id) throws ShoppingCartNotFoundException {
        ShoppingCart savedCart=shoppingCartRepository.findById(id)
                .orElseThrow(()->new ShoppingCartNotFoundException("Cart ID "+id+" Not Found"));
        if (Objects.nonNull(shoppingCart.getOrderQuantity())){
            savedCart.setOrderQuantity(shoppingCart.getOrderQuantity());
        }
        return shoppingCartRepository.save(savedCart);
    }

    @Override
    public BigDecimal getShoppingCartAmount(List<ShoppingCart> shoppingCarts) throws AppUserNotFoundException, ShoppingCartNotFoundException {
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        BigDecimal cartAmount=BigDecimal.ZERO;
        BigDecimal singleCartAmount;
        Integer availableProductQuantity=0;
        for (ShoppingCart shoppingCart:shoppingCarts){
            Optional<Product> product=productRepository.findById(shoppingCart.getProduct().getId());
            if (product.isPresent()){
                Product p= product.get();
                if (p.getAvailableStockQuantity()<shoppingCart.getOrderQuantity()){
                    singleCartAmount=(p.getPrice().multiply(new BigDecimal(p.getAvailableStockQuantity())))
                            .setScale(2, RoundingMode.UP);
                    shoppingCart.setOrderQuantity(p.getAvailableStockQuantity());
                }else {
                    singleCartAmount=(p.getPrice().multiply(new BigDecimal(shoppingCart.getOrderQuantity())))
                            .setScale(2, RoundingMode.UP);
                    availableProductQuantity=p.getAvailableStockQuantity()-shoppingCart.getOrderQuantity();
                }
                shoppingCarts=shoppingCartRepository.findShoppingCartByBuyer(buyer);
                List<ShoppingCart> filteredCarts=shoppingCarts.stream().filter(x->x.getProduct().getId().equals(p.getId())).collect(Collectors.toList());
                if (filteredCarts.size()>0){
                    throw new ShoppingCartNotFoundException("Product already exist in the cart. For additional product quantity, update in your cart");
                }
                cartAmount=cartAmount.add(singleCartAmount);
                p.setAvailableStockQuantity(availableProductQuantity);
                availableProductQuantity=0;
                shoppingCart.setBuyer(buyer);
                shoppingCart.setShoppingCartCode("CART".concat(String.valueOf(new Random().nextInt(100000000))));
                shoppingCart.setProduct(p);
                shoppingCart.setCartTotal(singleCartAmount);
                productRepository.save(p);
            }
        }
        return cartAmount;
    }

    @Override
    public void deleteCartByBuyer() throws AppUserNotFoundException {
//        Pageable pageable=PageRequest.of(pageNumber,10);
        String searchKey=JwtRequestFilter.CURRENT_USER;
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobileIgnoreCase(searchKey,searchKey,searchKey)
                .orElseThrow(()-> new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        List<ShoppingCart> shoppingCarts=shoppingCartRepository.findShoppingCartByBuyer(buyer);
        shoppingCartRepository.deleteAll(shoppingCarts);
    }

    @Override
    public void deleteShoppingCart(Long id) throws ShoppingCartNotFoundException {
        if (shoppingCartRepository.existsById(id)){
            shoppingCartRepository.deleteById(id);
        }else {
            throw new ShoppingCartNotFoundException("Shopping Cart ID "+id+" Not Found");
        }
    }

    @Override
    public void deleteAllShoppingCarts() {
        shoppingCartRepository.deleteAll();
    }
}
