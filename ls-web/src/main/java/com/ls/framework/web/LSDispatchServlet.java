package com.ls.framework.web;


import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.context.ApplicationContext;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.handler.HandlerContainer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LSDispatchServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String formatUrl = req.getRequestURI().replaceAll("/+", "/");
        ActionHandler actionHandler = HandlerContainer.get(formatUrl);
        System.out.println(req.getRequestURI());
        if (actionHandler != null) {
            actionHandler.invoke(formatUrl, req, resp);
        }
    }
}
