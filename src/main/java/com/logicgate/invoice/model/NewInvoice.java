package com.logicgate.invoice.model;


import com.logicgate.order.model.Order;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class NewInvoice {
    private String invoiceNo;
    private Date invoiceDate;
    private BigDecimal vatAmount;
    private BigDecimal invoiceTotal;
    private Order order;
}
