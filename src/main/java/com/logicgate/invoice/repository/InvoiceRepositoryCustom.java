package com.logicgate.invoice.repository;


import com.logicgate.invoice.model.Invoice;
import com.logicgate.order.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepositoryCustom {
    @Query("FROM Invoice i WHERE i.invoiceNo=?1")
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    @Query("FROM Invoice i WHERE i.order=?1")
    Invoice findInvoiceByOrder(Order order);

    @Query("FROM Invoice i WHERE i.order=?1")
    List<Invoice> findInvoiceByBuyer(List<Order> orders,Pageable pageable);
}
