package com.logicgate.invoice.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.invoice.exception.InvoiceNotFoundException;
import com.logicgate.invoice.model.Invoice;
import com.logicgate.order.exception.OrderNotFoundException;

import java.util.List;

public interface InvoiceService {
    Invoice addInvoiceToOrder(Invoice invoice);
    Invoice fetchByInvoiceNumber(String invoiceNumber) throws InvoiceNotFoundException;
    Invoice fetchInvoiceByOrder(String orderCode) throws OrderNotFoundException;
    List<Invoice> fetchInvoiceByBuyer(String searchKey, Integer pageNumber) throws AppUserNotFoundException;
    List<Invoice> fetchAllInvoices(Integer pageNumber);
    void cancelInvoice(String invoiceNumber) throws InvoiceNotFoundException;

}
