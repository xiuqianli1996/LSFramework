package com.ls.framework.web.template.impl;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.web.json.JsonEngine;
import com.ls.framework.web.json.impl.GsonJsonEngine;
import com.ls.framework.web.template.TemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LSBean("DEFAULT_JSON_TEMPLATE_ENGINE")
public class JsonTemplateEngine implements TemplateEngine {
    private JsonEngine jsonEngine = new GsonJsonEngine();

    public void setJsonEngine(JsonEngine jsonEngine) {
        this.jsonEngine = jsonEngine;
    }

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object result) {
        String jsonStr = null;
        if (result instanceof String) {
            jsonStr = (String) result;
        } else {
            jsonStr = jsonEngine.toJson(result);
        }
        response.setContentType("application/json");
        try {
            response.getWriter().write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
