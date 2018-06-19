package com.ls.framework.exception;

public class DiException extends RuntimeException {

    public DiException(String info) {
        super("final field can not be inject: " + info);
    }
}
