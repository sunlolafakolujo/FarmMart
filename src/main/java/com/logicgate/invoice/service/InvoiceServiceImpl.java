package com.logicgate.invoice.service;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.appuser.model.AppUser;
import com.logicgate.appuser.repository.AppUserRepository;
import com.logicgate.buyer.model.Buyer;
import com.logicgate.buyer.repository.BuyerRepository;
import com.logicgate.invoice.exception.InvoiceNotFoundException;
import com.logicgate.invoice.model.Invoice;
import com.logicgate.invoice.repository.InvoiceRepository;
import com.logicgate.order.exception.OrderNotFoundException;
import com.logicgate.order.model.Order;
import com.logicgate.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService{
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public Invoice addInvoiceToOrder(Invoice invoice) {
        invoice.setInvoiceNo("INVOICE".concat(String.valueOf(new Random().nextInt(100000000))));
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice fetchByInvoiceNumber(String invoiceNumber) throws InvoiceNotFoundException {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice number "+invoiceNumber+" Not Found"));
    }

    @Override
    public Invoice fetchInvoiceByOrder(String orderCode) throws OrderNotFoundException {
        Order order=orderRepository.findOrderByCode(orderCode)
                .orElseThrow(()->new OrderNotFoundException("Order Code "+orderCode+" Not Found"));
        return invoiceRepository.findInvoiceByOrder(order);
    }

    @Override//check the return
    public List<Invoice> fetchInvoiceByBuyer(String searchKey, Integer pageNumber) throws AppUserNotFoundException {
        Pageable pageable= PageRequest.of(pageNumber,10);
        AppUser appUser=appUserRepository.findUserByUsernameOrEmailOrMobile(searchKey,searchKey,searchKey)
                .orElseThrow(()->new AppUserNotFoundException("User "+searchKey+" Not Found"));
        Buyer buyer=buyerRepository.findBuyerByUsernameOrEmailOrPassword(appUser);
        List<Order> orders=orderRepository.findOrderByBuyer(buyer);
        return invoiceRepository.findInvoiceByBuyer(orders,pageable);
    }

    @Override
    public List<Invoice> fetchAllInvoices(Integer pageNumber) {
        Pageable pageable=PageRequest.of(pageNumber,10);
        return invoiceRepository.findAll(pageable).toList();
    }

    @Override
    public void cancelInvoice(String invoiceNumber) throws InvoiceNotFoundException {
        Invoice invoice=invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(()->new InvoiceNotFoundException("Invoice number "+invoiceNumber+" Not Found"));
        invoiceRepository.delete(invoice);
    }
}
