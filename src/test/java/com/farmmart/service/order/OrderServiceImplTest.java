package com.farmmart.service.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.repository.appuser.AppUserRepository;
import com.farmmart.data.repository.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private OrderService orderService=new OrderServiceImpl();

    CustomerOrder order;

    AppUser appUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order=new CustomerOrder();
        appUser=new AppUser();
    }

//    @Test
//    void testThatYouCanMockPlaceOrderMethod() {
//        Mockito.when(orderRepository.save(order)).thenReturn(order);
//
//        orderService.placeOrder(order);
//
//        ArgumentCaptor<CustomerOrder> orderArgumentCaptor=ArgumentCaptor.forClass(CustomerOrder.class);
//
//        Mockito.verify(orderRepository,Mockito.times(1)).save(orderArgumentCaptor.capture());
//
//        CustomerOrder capturedOrder=orderArgumentCaptor.getValue();
//
//        assertEquals(capturedOrder,order);
//    }

    @Test
    void testThatYouCanMockFindOrderById() {
    }

    @Test
    void findOrderByUser() {
    }

    @Test
    void findAllOrder() {
    }

    @Test
    void deleteOrderById() {
    }

    @Test
    void deleteOrderByUser() {
    }

    @Test
    void deleteAllOrder() {
    }
}