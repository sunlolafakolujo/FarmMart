package com.logicgate.invoice.repository;


import com.logicgate.invoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice,Long>,InvoiceRepositoryCustom {
}
