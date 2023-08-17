package com.logicgate.buyer.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BuyerNotFoundException extends RuntimeException{
    public BuyerNotFoundException() {
        super();
    }

    public BuyerNotFoundException(String message) {
        super(message);
    }

    public BuyerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuyerNotFoundException(Throwable cause) {
        super(cause);
    }

    protected BuyerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
