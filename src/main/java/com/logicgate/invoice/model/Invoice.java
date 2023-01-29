package com.logicgate.invoice.model;


import com.logicgate.order.model.Order;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invoiceNo;
    private Date invoiceDate;
    private BigDecimal totalBeforeTax;
    private BigDecimal vatAmount;
    private BigDecimal invoiceTotal;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Order order;
}
