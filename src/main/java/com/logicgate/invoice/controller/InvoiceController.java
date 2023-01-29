package com.logicgate.invoice.controller;


import com.logicgate.appuser.exception.AppUserNotFoundException;
import com.logicgate.invoice.exception.InvoiceNotFoundException;
import com.logicgate.invoice.model.Invoice;
import com.logicgate.invoice.model.InvoiceDto;
import com.logicgate.invoice.service.InvoiceService;
import com.logicgate.order.exception.OrderNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "api/farmmart")
@AllArgsConstructor
@CrossOrigin
public class InvoiceController {
    private InvoiceService invoiceService;

    @GetMapping("/findByInvoiceNumber")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<InvoiceDto> getInvoiceById(@RequestParam("invoiceNumber")String invoiceNumber)
                                                                        throws  InvoiceNotFoundException {
        Invoice invoice=invoiceService.fetchByInvoiceNumber(invoiceNumber);
        return new ResponseEntity<>(convertInvoiceToDto(invoice),OK);
    }

    @GetMapping("/findInvoiceByOrderCode")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<InvoiceDto> getInvoiceByOrderCode(@RequestParam("orderCode") String orderCode) throws OrderNotFoundException {
        Invoice invoice=invoiceService.fetchInvoiceByOrder(orderCode);
        return new ResponseEntity<>(convertInvoiceToDto(invoice),OK);
    }

    @GetMapping("/findBuyerInvoiceByEmailOrMobileOrUsername")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<List<InvoiceDto>> getBuyerInvoice(@RequestParam("pageNumber")Integer pageNumber,
                                                            @RequestParam("searchKey") String searchKey)
                                                            throws AppUserNotFoundException {
        return new ResponseEntity<>(invoiceService.fetchInvoiceByBuyer(searchKey,pageNumber)
                .stream()
                .map(this::convertInvoiceToDto)
                .collect(Collectors.toList()), OK);
    }

    @GetMapping("/findAllInvoices")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<List<InvoiceDto>> getAllInvoices(@RequestParam("pageNumber")Integer pageNumber){
        return new ResponseEntity<>(invoiceService.fetchAllInvoices(pageNumber)
                .stream()
                .map(this::convertInvoiceToDto)
                .collect(Collectors.toList()), OK);
    }

    @DeleteMapping("/cancelInvoice")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<?> cancelInvoice(@RequestParam("invoiceNumber")String invoiceNumber)
                                                                        throws InvoiceNotFoundException {
        invoiceService.cancelInvoice(invoiceNumber);
        return ResponseEntity.ok().body("Invoice number "+invoiceNumber+" has been cancelled");
    }

    private InvoiceDto convertInvoiceToDto(Invoice invoice){
        InvoiceDto invoiceDto=new InvoiceDto();
        invoiceDto.setInvoiceNo(invoice.getInvoiceNo());
        invoiceDto.setInvoiceDate(invoice.getInvoiceDate());
        invoiceDto.setBuyerName(invoice.getOrder().getBuyer().getFirstName()
                .concat(" ").concat(invoice.getOrder().getBuyer().getLastName()));
        invoiceDto.setMobile(invoice.getOrder().getBuyer().getAppUser().getMobile());
        invoiceDto.setHouseNumber(invoice.getOrder().getBuyer().getAppUser().getContact().getHouseNumber());
        invoiceDto.setStreetName(invoice.getOrder().getBuyer().getAppUser().getContact().getStreetName());
        invoiceDto.setCity(invoice.getOrder().getBuyer().getAppUser().getContact().getCity());
        invoiceDto.setStateProvince(invoice.getOrder().getBuyer().getAppUser().getContact().getStateProvince());
        invoiceDto.setShoppingCarts(invoice.getOrder().getShoppingCarts());
        invoiceDto.setTotalBeforeTax(invoice.getTotalBeforeTax());
        invoiceDto.setVatAmount(invoice.getVatAmount());
        invoiceDto.setInvoiceTotal(invoice.getInvoiceTotal());
        return invoiceDto;
    }
}
