package com.samsonan.counters.service.exception;

/**
 * 
 * To be used when counter not found or user doesn't have permission to access it
 */
public class RemoteNotFoundException extends Exception {

    private static final long serialVersionUID = -5896497679750230569L;

    public RemoteNotFoundException() {
    }

    public RemoteNotFoundException(String message) {
        super(message);
    }

    public RemoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteNotFoundException(Throwable cause) {
        super(cause);
    }

    public RemoteNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
