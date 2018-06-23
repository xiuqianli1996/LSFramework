package com.ls.framework.web.resolver.view.impl;

import com.ls.framework.web.annotation.LSResponseBody;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.resolver.view.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 需要模板引擎渲染的视图处理器
 */
public class TemplateViewResolver implements ViewResolver {


    @Override
    public boolean filter(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response) {
        Method actionMethod = actionHandler.getActionMethod();
        Class<?> controllerClass = actionHandler.getControllerClass();
        //返回值为String并且类和方法上都没有LSResponseBody注解的才处理
        return result.getClass() == String.class
                && !actionMethod.isAnnotationPresent(LSResponseBody.class)
                && !controllerClass.isAnnotationPresent(LSResponseBody.class);
    }

    @Override
    public void handle(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response) {
//        if (!filter(actionHandler, result)) {
//            return  null;
//        }

    }
}
