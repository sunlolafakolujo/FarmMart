package com.logicgate.image.exception;



public class PictureNotFoundException extends RuntimeException {
    public PictureNotFoundException() {
        super();
    }

    public PictureNotFoundException(String message) {
        super(message);
    }

    public PictureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PictureNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PictureNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
