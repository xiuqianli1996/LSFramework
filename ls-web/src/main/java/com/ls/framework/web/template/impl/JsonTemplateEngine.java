package com.ls.framework.web.template.impl;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.web.context.RequestContext;
import com.ls.framework.web.json.Json;
import com.ls.framework.web.json.impl.GsonJson;
import com.ls.framework.web.template.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LSBean("DEFAULT_JSON_TEMPLATE_ENGINE")
public class JsonTemplateEngine implements TemplateEngine {
    private Json json = new GsonJson();

    public void setJson(Json json) {
        this.json = json;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object result) {
        String jsonStr = json.toJson(result);
        response.setContentType("application/json");
        try {
            response.setContentLength(jsonStr.length());
            response.getWriter().write(jsonStr);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
