package com.logicgate.passwordtoken.exception;



public class PasswordTokenNotFoundException extends RuntimeException{
    public PasswordTokenNotFoundException() {
        super();
    }

    public PasswordTokenNotFoundException(String message) {
        super(message);
    }

    public PasswordTokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordTokenNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PasswordTokenNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
