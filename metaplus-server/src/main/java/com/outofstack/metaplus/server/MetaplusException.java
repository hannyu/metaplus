package com.outofstack.metaplus.server;


public class MetaplusException extends RuntimeException {
    public MetaplusException(String message) {
        super(message);
    }

    public MetaplusException(Throwable cause) {
        super(cause);
    }

    public MetaplusException(String message, Throwable cause) {
        super(message, cause);
    }
}
