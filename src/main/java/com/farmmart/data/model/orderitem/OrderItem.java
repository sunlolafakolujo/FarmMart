package com.farmmart.data.model.orderitem;

import com.farmmart.data.model.order.CustomerOrder;
import com.farmmart.data.model.product.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdDate;

    private BigDecimal price;

    @Positive
    private Integer orderQuantity;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private CustomerOrder customerOrder;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Product product;






//    @Transient
//    public BigDecimal getTotalPrice(){
//        return getProduct().getPrice().multiply(new BigDecimal(orderQuantity));
//    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((orderItemPK == null) ? 0 : orderItemPK.hashCode());
//
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        OrderItem other = (OrderItem) obj;
//        if (orderItemPK == null) {
//            if (other.orderItemPK != null) {
//                return false;
//            }
//        } else if (!orderItemPK.equals(other.orderItemPK)) {
//            return false;
//        }
//
//        return true;
//    }
}
