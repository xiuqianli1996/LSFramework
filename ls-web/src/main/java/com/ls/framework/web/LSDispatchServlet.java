package com.ls.framework.web;


import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.context.ApplicationContext;
import com.ls.framework.web.context.RequestContext;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.handler.HandlerContainer;
import com.ls.framework.web.resolver.ExceptionResolverContainer;
import com.ls.framework.web.resolver.ViewResolversContainer;
import com.ls.framework.web.resolver.exception.ExceptionResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LSDispatchServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        RequestContext.setRequest(req);
        RequestContext.setResponse(resp);
        String formatUrl = req.getRequestURI().replaceAll("/+", "/");
        ActionHandler actionHandler = HandlerContainer.get(formatUrl);
//        System.out.println(req.getRequestURI());
        if (actionHandler != null) {
            Object result = null;
            Exception exception = null;
            try {
                result = actionHandler.invoke(formatUrl, req, resp);
            } catch (Exception e) {
                exception = e;
            }
            try {
                processDispatchResult(actionHandler, result, req, resp, exception);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e);
            }
        } else {
            resp.sendError(404);
        }

        resp.flushBuffer();
    }

    private void processDispatchResult(ActionHandler actionHandler, Object result, HttpServletRequest request
            , HttpServletResponse response, Exception exception) throws Exception {
        if (exception != null) {
            result = ExceptionResolverContainer.handle(actionHandler, exception, request, response);
            if (result == null) {
                throw exception;
            }
        }

        if (result != null) {
            ViewResolversContainer.handle(actionHandler, result, request, response);
        }
    }
}
