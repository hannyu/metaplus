package com.outofstack.metaplus.server.storage;


public class EsRuntimeException extends RuntimeException {
    public EsRuntimeException(String message) {
        super(message);
    }

    public EsRuntimeException(Throwable cause) {
        super(cause);
    }

    public EsRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
