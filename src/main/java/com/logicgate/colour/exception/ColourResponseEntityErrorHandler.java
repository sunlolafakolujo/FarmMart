package com.logicgate.colour.exception;


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
public class ColourResponseEntityErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus
    @ExceptionHandler(ColourNotFoundException.class)
    public ResponseEntity<ValidateErrorMessage> colourExceptionHandler(ColourNotFoundException colourNotFoundException,
                                                                       WebRequest request){

        ValidateErrorMessage validateErrorMessage=new ValidateErrorMessage( HttpStatus.NOT_FOUND,
                colourNotFoundException.getMessage(),
                request.getDescription(false),
                new Date());

        return new ResponseEntity<>(validateErrorMessage,HttpStatus.BAD_REQUEST);
    }
}
