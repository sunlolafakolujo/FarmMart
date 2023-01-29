package com.logicgate.event.listener;


import com.logicgate.appuser.service.AppUserService;
import com.logicgate.buyer.service.BuyerService;
import com.logicgate.emailconfiguration.EmailConfiguration;
import com.logicgate.event.InvoiceEvent;
import com.logicgate.invoice.model.Invoice;
import com.logicgate.invoice.service.InvoiceService;
import com.logicgate.order.exception.OrderNotFoundException;
import com.logicgate.order.model.Order;
import com.logicgate.order.service.OrderService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InvoiceEventListener implements ApplicationListener<InvoiceEvent> {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private EmailConfiguration emailConfiguration;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BuyerService buyerService;

    @SneakyThrows
    @Override
    public void onApplicationEvent(InvoiceEvent event) {
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        mailSender.setHost(emailConfiguration.getHost());
        mailSender.setPort(emailConfiguration.getPort());
        mailSender.setUsername(emailConfiguration.getUsername());
        mailSender.setPassword(emailConfiguration.getPassword());

        String invoiceNumber="INVOICE".concat(String.valueOf(new Random().nextInt(100000000)));//check
        Order order= event.getOrder();
//        Invoice invoice=new Invoice(invoiceNumber,order);//check
        Invoice invoice=invoiceService.fetchInvoiceByOrder(order.getOrderCode());
        invoiceService.addInvoiceToOrder(invoice);
        SimpleMailMessage mailMessage=sendOrderInvoice(event,order,invoice);
        mailSender.send(mailMessage);
    }
        //replace invoice number in sendorderinvoice with invoice.
    private SimpleMailMessage sendOrderInvoice(InvoiceEvent event, Order order, Invoice invoice) throws OrderNotFoundException {
        Order savedOrder=orderService.fetchOrderByCode(order.getOrderCode());//check
        String to=savedOrder.getBuyer().getAppUser().getEmail();
        String link=event.getApplicationUrl()+"/api/farmmart/findByInvoiceNumber?invoiceNumber="+invoice.getInvoiceNo();
//        String link=event.getApplicationUrl()+"/api/farmmart/findByInvoiceNumber?invoiceNumber="+invoice.getInvoiceNumber;//test this
        String from ="fakolujos@gmail.com";
        String subject="Order Invoice";
        String body="Dear ".concat(savedOrder.getBuyer().getFirstName())+",\n\n Click on the link to view your order invoice "+link;

        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);
        return mailMessage;
    }
}
