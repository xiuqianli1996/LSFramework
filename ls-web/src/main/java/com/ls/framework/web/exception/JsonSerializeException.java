package com.ls.framework.web.exception;

public class JsonSerializeException extends RuntimeException {

    public JsonSerializeException(String className) {
        super(String.format("Can not serialize this class: %s", className));
    }
}
