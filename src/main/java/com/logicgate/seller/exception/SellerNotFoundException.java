package com.logicgate.seller.exception;


public class SellerNotFoundException extends RuntimeException{
    public SellerNotFoundException() {
        super();
    }

    public SellerNotFoundException(String message) {
        super(message);
    }

    public SellerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SellerNotFoundException(Throwable cause) {
        super(cause);
    }

    protected SellerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
