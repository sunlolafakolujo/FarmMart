package com.farmmart.data.model.order;

import com.farmmart.data.model.appuser.AppUser;
import com.farmmart.data.model.orderitem.OrderItem;
import com.farmmart.data.model.staticdata.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
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
public class CustomerOrder implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate orderDate;

//    private BigDecimal vatAmount;

    private Status status;

    @OneToOne(fetch =FetchType.EAGER )
    private AppUser appUser;

    @OneToMany(mappedBy = "customerOrder",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems=new ArrayList<>();

    private BigDecimal totalAmount;
}
