package com.farmmart.data.model.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.orderitem.OrderItem;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime orderDate;

    private BigDecimal vatAmount;

    private BigDecimal amountTotal;

    @OneToOne
    private AppUser appUser;

    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

    @Transient
    public BigDecimal getSubTotalAmount(){
        BigDecimal total=BigDecimal.ZERO;

        List<OrderItem> orderItemList= orderItems;

        for (OrderItem o:orderItemList){
            total=total.multiply(o.getTotalPrice());
        }

        return total;
    }

    @Transient
    public BigDecimal totalVatAmount(){

        return BigDecimal.valueOf(0.075).multiply(getSubTotalAmount());
    }

    @Transient
    public BigDecimal getTotalAmount(){
        return getSubTotalAmount().add(totalVatAmount());
    }

    @Transient
    public Integer getNumberOfProductPurchased(){

        return this.getOrderItems().size();
    }
}
