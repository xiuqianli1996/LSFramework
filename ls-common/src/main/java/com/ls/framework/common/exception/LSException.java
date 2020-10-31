package com.ls.framework.common.exception;

public class LSException extends RuntimeException {
    public LSException(String message) {
        super(message);
    }

    public LSException(String message, Throwable cause) {
        super(message, cause);
    }

    public LSException(Throwable cause) {
        super(cause);
    }

}
