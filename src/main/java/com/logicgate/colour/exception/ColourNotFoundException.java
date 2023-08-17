package com.logicgate.colour.exception;


public class ColourNotFoundException extends RuntimeException{
    public ColourNotFoundException() {
        super();
    }

    public ColourNotFoundException(String message) {
        super(message);
    }

    public ColourNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ColourNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ColourNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
