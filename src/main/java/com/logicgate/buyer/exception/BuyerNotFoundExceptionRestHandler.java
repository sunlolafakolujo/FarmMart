package com.logicgate.buyer.exception;


import com.logicgate.erromessage.ValidateErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class BuyerNotFoundExceptionRestHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus
    @ExceptionHandler(BuyerNotFoundException.class)
    public ResponseEntity<ValidateErrorMessage> buyerNotFoundExceptionHandler(BuyerNotFoundException bnfe, WebRequest request){
        ValidateErrorMessage vem=new ValidateErrorMessage(HttpStatus.NOT_FOUND,
                bnfe.getMessage(),
                request.getDescription(false),
                new Date());
        return new ResponseEntity<>(vem,HttpStatus.BAD_REQUEST);
    }
}
