package com.ls.framework.web.exception;

import java.lang.reflect.Method;

public class MissingRequestParamException extends RuntimeException {

    public MissingRequestParamException(String methodName, String paramName) {
        super(String.format("In %s, miss param name: %s", methodName, paramName));
    }
}
