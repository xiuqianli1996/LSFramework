package com.ls.framework.web;


import com.ls.framework.web.context.RequestContext;
import com.ls.framework.web.exception.LSMvcException;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.handler.HandlerContainer;
import com.ls.framework.web.resolver.ExceptionResolverContainer;
import com.ls.framework.web.resolver.ViewResolversContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
                result = actionHandler.invoke(formatUrl, req, resp);//执行action方法
            } catch (Exception e) {
                exception = e;
            }
            try {
                processDispatchResult(actionHandler, result, req, resp, exception);//处理结果,包括异常处理
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServletException(e);//没有相应的异常处理器，接着抛给servlet容器
            }
        } else {
            resp.sendError(404);
        }

        resp.flushBuffer();
    }

    private void processDispatchResult(ActionHandler actionHandler, Object result, HttpServletRequest request
            , HttpServletResponse response, Exception exception) throws Exception {
        if (exception != null) {
            result = ExceptionResolverContainer.handle(actionHandler, exception, request, response);//遍历执行所有异常处理器，找到第一个有处理结果的
            if (result == null) {
                throw exception;//遍历完之后异常没有被处理掉就直接往上抛
            }
        }

        // 开始处理action执行结果
        if (result != null) {
            ViewResolversContainer.handle(actionHandler, result, request, response);
        } else {
            throw new LSMvcException("The action handle result is null: " + ClassUtil.getFullMethodName(actionHandler.getActionMethod()));
        }
    }
}
