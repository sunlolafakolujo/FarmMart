package com.farmmart.controller.cartcontroller;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.appuser.AppUserNotFoundException;
import com.farmmart.data.model.cart.*;
import com.farmmart.service.appuser.AppUserDetailService;
import com.farmmart.service.cart.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    private final AppUserDetailService appUserDetailService;

    private final ModelMapper modelMapper;

    @PostMapping("/addToCart")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<NewCart> addToCart(@RequestBody NewCart newCart){
        Cart cart=modelMapper.map(newCart,Cart.class);

        Cart postCart=cartService.addToCart(cart);

        NewCart postedCart=modelMapper.map(postCart, NewCart.class);

        return new ResponseEntity<>(postedCart, HttpStatus.CREATED);
    }

    @GetMapping("/findCartById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<CartDt> findCartById(@PathVariable(value = "id") Long id){

        Cart cart=cartService.findCartById(id);

        CartDt cartDt=convertCartToDto(cart);

        return ResponseEntity.ok().body(cartDt);
    }

    @GetMapping("/listCartItemsByUsername/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<CartDto> listCartItems(@PathVariable(value = "username") String username) throws AppUserNotFoundException {

        AppUser appUser=appUserDetailService.findUserByUsername(username);

//        CartDto cartDto= cartService.listCartItems(appUser);

        CartDto cartDto= cartService.listCartItems(appUser,username);

        return ResponseEntity.ok().body(cartDto);
    }

    @GetMapping("/findAllCarts")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<List<CartDt>> getAllCarts(){

        return ResponseEntity.ok().body(cartService.findAllCarts()
                .stream()
                .map(this::convertCartToDto)
                .collect(Collectors.toList()));
    }

    @PutMapping("updateCartById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<ModifyCart> updateCart(@RequestBody ModifyCart modifyCart,@PathVariable(value = "id") Long id) throws CartNotFoundException {
        Cart cart=modelMapper.map(modifyCart, Cart.class);

        Cart updatedCart=cartService.updateCart(cart, id);

        ModifyCart postCart=modelMapper.map(updatedCart, ModifyCart.class);

        return ResponseEntity.ok().body(postCart);
    }

    @DeleteMapping("/deleteCartByAppUser/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteCartByAppUser(@PathVariable(value = "username") String username) throws CartNotFoundException {
        cartService.deleteCartByAppUser(username);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteCartById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteCartById(@PathVariable(value = "id") Long id) throws CartNotFoundException {
        cartService.deleteCartById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAllCarts")
    @PreAuthorize("hasAnyAuthority('ROLE_CUSTOMER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteAllCart(){
        cartService.deleteAllCarts();

        return ResponseEntity.noContent().build();
    }

    private CartDt convertCartToDto(Cart cart){
        CartDt cartDt=new CartDt();

        cartDt.setId(cart.getId());
        cartDt.setOrderQuantity(cart.getOrderQuantity());
        cartDt.setProductName(cart.getProduct().getProductName());
        cartDt.setProductPrice(cart.getProduct().getPrice());
        cartDt.setProductType(cart.getProduct().getProductType());
        cartDt.setProductDescription(cart.getProduct().getProductDescription());
        cartDt.setImageURL(cart.getProduct().getImageURL());

        return cartDt;
    }
}
