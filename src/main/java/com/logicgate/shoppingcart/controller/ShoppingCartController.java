package com.logicgate.shoppingcart.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.service.AppUserService;
import com.logicgate.buyer.service.BuyerService;
import com.logicgate.shoppingcart.exception.ShoppingCartNotFoundException;
import com.logicgate.shoppingcart.model.NewShoppingCart;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.shoppingcart.model.ShoppingCartDto;
import com.logicgate.shoppingcart.model.UpdateShoppingCart;
import com.logicgate.shoppingcart.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final ModelMapper modelMapper;
    private final AppUserService appUserService;
    private final BuyerService buyerService;

    @PostMapping("/addProductToCart")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<NewShoppingCart> addProductToCart(@RequestBody NewShoppingCart newShoppingCart) throws AppUserNotFoundException {
        ShoppingCart shoppingCart=modelMapper.map(newShoppingCart,ShoppingCart.class);
        ShoppingCart post=shoppingCartService.addProductToShoppingCart(shoppingCart);
        NewShoppingCart posted=modelMapper.map(post,NewShoppingCart.class);
        return new ResponseEntity<>(posted, CREATED);
    }

    @GetMapping("/findCart")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShoppingCartDto> getCartById(@RequestParam("id") Long id) throws ShoppingCartNotFoundException {
        ShoppingCart shoppingCart=shoppingCartService.fetchCartById(id);
        return new ResponseEntity<>(convertShoppingCartToDto(shoppingCart),OK);
    }

    @GetMapping("/findCartByCode")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShoppingCartDto> getCartByCode(@RequestParam("code") String shoppingCartCode) throws ShoppingCartNotFoundException {
        ShoppingCart shoppingCart=shoppingCartService.fetchCartByCode(shoppingCartCode);
        return new ResponseEntity<>(convertShoppingCartToDto(shoppingCart),OK);
    }

    @GetMapping("/findBuyerShoppingCart")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<ShoppingCartDto>> getAllCartsOrFetchByUsernameOrEmailOrMobile() throws AppUserNotFoundException {
        return new ResponseEntity<>(shoppingCartService.fetchBuyerShoppingCart()
                .stream()
                .map(this::convertShoppingCartToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findAllShoppingCarts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShoppingCartDto>> getAllShoppingCart(@RequestParam("pageNumber") Integer pageNumber){
        return new ResponseEntity<>(shoppingCartService.fetchAllShoppingCart(pageNumber)
                .stream()
                .map(this::convertShoppingCartToDto)
                .collect(Collectors.toList()),OK);
    }

    @PutMapping("/updateShoppingCart")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<UpdateShoppingCart> editShoppingCart(@RequestBody UpdateShoppingCart updateShoppingCart,
                                                               @RequestParam("id") Long id) throws ShoppingCartNotFoundException {
        ShoppingCart shoppingCart=modelMapper.map(updateShoppingCart,ShoppingCart.class);
        ShoppingCart post=shoppingCartService.updateShoppingCart(shoppingCart,id);
        UpdateShoppingCart posted=modelMapper.map(post,UpdateShoppingCart.class);
        return new ResponseEntity<>(posted,OK);
    }

    @DeleteMapping("/deleteCartByBuyer")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> deleteCartByBuyer() throws AppUserNotFoundException {
        shoppingCartService.deleteCartByBuyer();
        return ResponseEntity.ok().body("Buyer cart has being deleted");
    }

    @DeleteMapping("/deleteCart")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<?> deleteCartById(@RequestParam("id") Long id) throws ShoppingCartNotFoundException {
        shoppingCartService.deleteShoppingCart(id);
        return ResponseEntity.ok().body("Cart ID "+id+" has being deleted");
    }

    @DeleteMapping("/deleteAllCarts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAllCarts(){
        shoppingCartService.deleteAllShoppingCarts();
        return ResponseEntity.ok().body("Carts has being deleted");
    }

    private ShoppingCartDto convertShoppingCartToDto(ShoppingCart shoppingCart){
        ShoppingCartDto shoppingCartDto=new ShoppingCartDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setShoppingCartCode(shoppingCart.getShoppingCartCode());
        shoppingCartDto.setOrderQuantity(shoppingCart.getOrderQuantity());
        shoppingCartDto.setProductPrice(shoppingCart.getProduct().getPrice());
        shoppingCartDto.setProductName(shoppingCart.getProduct().getProductName());
        shoppingCartDto.setProductDescription(shoppingCart.getProduct().getProductDescription());
        shoppingCartDto.setCartTotal(shoppingCart.getCartTotal());
        return shoppingCartDto;
    }
}
