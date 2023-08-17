package com.logicgate.order.model;


import com.logicgate.buyer.model.Buyer;
import com.logicgate.shoppingcart.model.ShoppingCart;
import com.logicgate.staticdata.OrderStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderCode;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Buyer buyer;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ShoppingCart> shoppingCarts;

    public Order(String orderCode, LocalDateTime orderDate, LocalDateTime deliveryDate,
                 Buyer buyer,OrderStatus orderStatus, List<ShoppingCart> shoppingCarts) {
        this.orderCode = orderCode;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.orderStatus=orderStatus;
        this.buyer = buyer;
        this.shoppingCarts = shoppingCarts;
    }
}
