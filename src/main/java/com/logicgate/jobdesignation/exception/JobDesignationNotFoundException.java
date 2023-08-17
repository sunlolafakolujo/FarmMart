package com.logicgate.jobdesignation.exception;


public class JobDesignationNotFoundException extends RuntimeException{
    public JobDesignationNotFoundException() {
        super();
    }

    public JobDesignationNotFoundException(String message) {
        super(message);
    }

    public JobDesignationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobDesignationNotFoundException(Throwable cause) {
        super(cause);
    }

    protected JobDesignationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
