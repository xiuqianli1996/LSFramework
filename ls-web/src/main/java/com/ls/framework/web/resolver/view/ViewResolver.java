package com.ls.framework.web.resolver.view;

import com.ls.framework.web.handler.ActionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ViewResolver {

    boolean filter(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response);
    void handle(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response);

}
