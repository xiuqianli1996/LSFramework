package com.ls.framework.web.resolver.view.impl;

import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.web.annotation.LSResponseBody;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.resolver.view.ViewResolver;
import com.ls.framework.web.template.TemplateEngine;
import com.ls.framework.web.template.impl.JsonTemplateEngine;
import com.ls.framework.web.utils.ControllerKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class JsonViewResolver implements ViewResolver {

    private TemplateEngine jsonTemplateEngine = BeanContainer.getBean("DEFAULT_JSON_TEMPLATE_ENGINE");

    @Override
    public boolean filter(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response) {
        return ControllerKit.isResponseBody(actionHandler);
    }

    @Override
    public void handle(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response) {
        jsonTemplateEngine.render(request, response, result);
    }
}
