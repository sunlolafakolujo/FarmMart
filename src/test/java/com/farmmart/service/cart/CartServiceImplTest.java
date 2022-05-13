package com.farmmart.service.cart;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.cart.Cart;
import com.farmmart.data.model.cart.CartNotFoundException;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.cart.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@Slf4j
@Sql(scripts = {"classpath:db/insert.sql"})
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private CartService cartService=new CartServiceImpl();

    Cart cart;
    AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cart=new Cart();
        appUser=new AppUser();
    }

    @Test
    void testThatYouCanMockAddToCartMethod() {
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);

        cartService.addToCart(cart);

        ArgumentCaptor<Cart> cartArgumentCaptor=ArgumentCaptor.forClass(Cart.class);

        Mockito.verify(cartRepository,Mockito.times(1)).save(cartArgumentCaptor.capture());

        Cart capturedCart=cartArgumentCaptor.getValue();

        assertEquals(capturedCart,cart);
    }

    @Test
    void testThatYouCanMockFindCartByIdMethod() {

        Long id =2L;
        Mockito.when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        cartService.findCartById(id);

        Mockito.verify(cartRepository, Mockito.times(1)).findById(id);
    }

//    @Test
//    void testThatYouCanListCartItemsMethod() {
//        String username="BukolaFako";
//
//        appUser=appUserRepository.findByUserName(username);
//
//        List<Cart> carts=new ArrayList<>();
//
//        Mockito.when(cartRepository.findCartByAppUser(appUser)).thenReturn(carts);
//
//        cartService.listCartItems(appUser);
//
//        Mockito.verify(cartRepository, times(1)).findCartByAppUser(appUser);
//
//    }

    @Test
    void testThatYouCanMockUpdateCartMethod() throws CartNotFoundException {

        Long id =1L;
        Mockito.when(cartRepository.findById(id)).thenReturn(Optional.of(cart));

        cartService.updateCart(cart, id);

        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    }

    @Test
    void testThatYouCanMockFindAllCartsMethod() {

        List<Cart>  carts=new ArrayList<>();

        Mockito.when(cartRepository.findAll()).thenReturn(carts);

        cartService.findAllCarts();

        Mockito.verify(cartRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testThatYouCanMockDeleteCartByAppUserMethod() throws CartNotFoundException {
        String username ="BukolaFako";
        appUser=appUserRepository.findByUserName(username);

        List<Cart> carts=cartRepository.findCartByAppUser(appUser);

        doNothing().when(cartRepository).deleteAll(carts);

        cartService.deleteCartByAppUser(username);

        verify(cartRepository,times(1)).deleteAll(carts);
    }

    @Test
    void testThatYouCanDeleteCartById(){
        Long id=2L;

        doNothing().when(cartRepository.findById(id));

        cartService.findCartById(id);

        verify(cartRepository, times(1)).deleteById(id);
    }

    @Test
    void testThatYouCanMockDeleteAllCarts() {

        doNothing().when(cartRepository).deleteAll();

        cartService.deleteAllCarts();

        verify(cartRepository,times(1)).deleteAll();
    }
}