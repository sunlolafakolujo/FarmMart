package com.logicgate.farmservices.exception;



public class FarmServiceNotFoundException extends RuntimeException {
    public FarmServiceNotFoundException() {
        super();
    }

    public FarmServiceNotFoundException(String message) {
        super(message);
    }

    public FarmServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FarmServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    protected FarmServiceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
