package com.ls.framework.web.resolver.exception;

import com.ls.framework.web.handler.ActionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionResolver {

    Object handle(ActionHandler actionHandler, Exception exception, HttpServletRequest request
            , HttpServletResponse response);

}
