package com.ls.framework.core.exception;

public class LSException extends RuntimeException {
    public LSException(String message) {
        super(message);
    }

    public LSException(Throwable cause) {
        super(cause);
    }
}
