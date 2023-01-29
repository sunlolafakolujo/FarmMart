package com.logicgate.shoppingcart.exception;


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
public class ShoppingCartNotFoundExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(ShoppingCartNotFoundException.class)
    public ResponseEntity<ValidateErrorMessage> cartItemValidation(ShoppingCartNotFoundException cartItemNotFoundException,
                                                                   WebRequest request){
        ValidateErrorMessage vem=new ValidateErrorMessage(HttpStatus.NOT_FOUND,
                cartItemNotFoundException.getMessage(),
                request.getDescription(false),
                new Date());
        return new ResponseEntity<>(vem, HttpStatus.BAD_REQUEST);

    }
}
