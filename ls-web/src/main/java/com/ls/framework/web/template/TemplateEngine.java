package com.ls.framework.web.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TemplateEngine {
    void render(HttpServletRequest request, HttpServletResponse response, Object result);
}
