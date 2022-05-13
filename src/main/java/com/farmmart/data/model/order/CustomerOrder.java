package com.farmmart.data.model.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.orderitem.OrderItem;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "orderItems")
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private String sessionId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderDate;

//    private BigDecimal vatAmount;

    private Status status;

    @OneToOne
    private AppUser appUser;

    @OneToMany(mappedBy = "customerOrder",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems=new ArrayList<>();

    private BigDecimal totalAmount;

//    @Transient
//    public BigDecimal getSubTotalAmount(){
//        BigDecimal total=BigDecimal.ZERO;
//
//        List<OrderItem> orderItemList= orderItems;
//
//        for (OrderItem o:orderItemList){
//            total=total.multiply(o.getTotalPrice());
//        }
//
//        return total;
//    }
//
//    @Transient
//    public BigDecimal totalVatAmount(){
//
//        return BigDecimal.valueOf(0.075).multiply(getSubTotalAmount());
//    }
//
//    @Transient
//    public BigDecimal getTotalAmount(){
//        return getSubTotalAmount().add(totalVatAmount());
//    }
//
//    @Transient
//    public Integer getNumberOfProductPurchased(){
//
//        return this.getOrderItems().size();
//    }
}
