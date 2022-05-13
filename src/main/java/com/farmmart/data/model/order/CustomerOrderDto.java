package com.farmmart.data.model.order;

import com.farmmart.data.model.address.Address;
import com.farmmart.data.model.orderitem.OrderItem;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderDto {

    @JsonIgnore
    private Long id;

    private LocalDate orderDate;

    private Status status;

//    private AppUser appUser;
    private String customerName;

    private String phone;

    private List<Address> addresses;

    private List<OrderItem> orderItems;

    private BigDecimal totalAmount;
}
