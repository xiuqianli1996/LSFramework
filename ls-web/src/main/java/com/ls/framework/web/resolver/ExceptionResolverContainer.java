package com.ls.framework.web.resolver;

import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.resolver.exception.ExceptionResolver;
import com.ls.framework.web.resolver.view.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class ExceptionResolverContainer {
    private static List<ExceptionResolver> exceptionResolverList = new LinkedList<>();

    public static boolean add(ExceptionResolver exceptionResolver) {
        return exceptionResolverList.add(exceptionResolver);
    }

    public static Object handle(ActionHandler actionHandler, Exception exception, HttpServletRequest request
            , HttpServletResponse response) {
        for (ExceptionResolver exceptionResolver : exceptionResolverList) {
            Object result = exceptionResolver.handle(actionHandler, exception, request, response);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
