package com.logicgate.category.exception;


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
public class CategoryRestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ValidateErrorMessage> categoryNotFoundException(CategoryNotFoundException categoryNotFoundException,
                                                                          WebRequest request){

        ValidateErrorMessage errorMessage =new ValidateErrorMessage(HttpStatus.NOT_FOUND,
                categoryNotFoundException.getMessage(),
                request.getDescription(false),
                new Date());

        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }
}
