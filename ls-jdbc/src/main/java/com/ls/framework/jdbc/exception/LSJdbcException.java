package com.ls.framework.jdbc.exception;

public class LSJdbcException extends RuntimeException {

    public LSJdbcException(String message) {
        super(message);
    }

    public LSJdbcException(Throwable cause) {
        super(cause);
    }
}
