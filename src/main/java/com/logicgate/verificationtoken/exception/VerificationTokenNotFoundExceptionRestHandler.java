package com.logicgate.verificationtoken.exception;


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
public class VerificationTokenNotFoundExceptionRestHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus
    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<ValidateErrorMessage> verificationTokenExceptionHandler(VerificationTokenNotFoundException vtnfe,
                                                                                  WebRequest request){
        ValidateErrorMessage vem=new ValidateErrorMessage(HttpStatus.NOT_FOUND,
                vtnfe.getMessage(),
                request.getDescription(false),
                new Date());
        return new ResponseEntity<>(vem,HttpStatus.BAD_REQUEST);
    }
}
