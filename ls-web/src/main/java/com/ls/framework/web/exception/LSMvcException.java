package com.ls.framework.web.exception;

public class LSMvcException extends RuntimeException {

    public LSMvcException() {
    }

    public LSMvcException(String message) {
        super(message);
    }

    public LSMvcException(Throwable cause) {
        super(cause);
    }
}
