package com.logicgate.invoice.model;

import com.logicgate.shoppingcart.model.ShoppingCart;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvoiceDto {
    private String invoiceNo;
    private Date invoiceDate;
    private String buyerName;
    private String mobile;
    private String houseNumber;
    private String streetName;
    private String city;
    private String stateProvince;
    private List<ShoppingCart> shoppingCarts;
    private BigDecimal totalBeforeTax;
    private BigDecimal vatAmount;
    private BigDecimal invoiceTotal;
}
