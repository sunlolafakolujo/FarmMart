package com.logicgate.access;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/farmmart")
@CrossOrigin
public class AccessController {

    @GetMapping("/adminAccess")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess(){
        return ResponseEntity.ok().body("This URL is only accessible to the Admin");
    }

    @GetMapping("/sellerAccess")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> sellerAccess(){
        return ResponseEntity.ok().body("This URL is only accessible to the Seller");
    }

    @GetMapping("/buyerAccess")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<String> buyerAccess(){
        return ResponseEntity.ok().body("This URL is only accessible to the Buyer");
    }

    @GetMapping("/employeeAccess")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<String> employeeAccess(){
        return ResponseEntity.ok().body("This URL is only accessible to the Employee");
    }

    @GetMapping("/financeAccess")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public ResponseEntity<String> financeAccess(){
        return ResponseEntity.ok().body("This URL is only accessible to the Accountant");
    }
}
