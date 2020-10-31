package com.ls.framework.web.exception;

import com.ls.framework.web.handler.ActionHandler;

public class DuplicateMappingException extends RuntimeException {

    public DuplicateMappingException(ActionHandler a1, ActionHandler a2) {
        super(String.format("%s is Duplicate in %s -- %s and %s -- %s", a1.getRegxUrl()
                , ClassUtil.getFullMethodName(a1.getActionMethod()), a1.getMappingUrl()
                , ClassUtil.getFullMethodName(a2.getActionMethod()), a2.getMappingUrl()));
    }
}
