package com.ls.framework.web.template.impl;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.web.context.RequestContext;
import com.ls.framework.web.exception.LSMvcException;
import com.ls.framework.web.template.TemplateEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LSBean("DEFAULT_TEMPLATE_ENGINE")
public class JspTemplateEngine implements TemplateEngine {

    private String viewPrefix = "/WEB-INF";
    private String viewSuffix = ".jsp";

    public void setViewPrefix(String viewPrefix) {
        this.viewPrefix = viewPrefix;
    }

    public void setViewSuffix(String viewSuffix) {
        this.viewSuffix = viewSuffix;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object result) {
        String viewPath = getViewPath(String.valueOf(result));
        try {
            request.getRequestDispatcher(viewPath).forward(request, response);
        } catch (ServletException | IOException e) {
            throw new LSMvcException(e.getCause());
        }

    }

    private String getViewPath(String path) {
        String fullPath = "/" + viewPrefix + "/" + path + viewSuffix;
        return fullPath.replaceAll("/+", "/");
    }
}
