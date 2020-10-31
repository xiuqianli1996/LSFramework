package com.ls.framework.web.resolver.parameter.impl;

import com.ls.framework.web.annotation.LSPathParam;
import com.ls.framework.web.resolver.parameter.ParameterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class PathParamResolver implements ParameterResolver {
    @Override
    public boolean filter(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        return parameter.isAnnotationPresent(LSPathParam.class);
    }

    @Override
    public Object handle(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        String paramName = parameter.getAnnotation(LSPathParam.class).value();
        String param = pathParamMap.get(paramName);
        if (StringKit.isBlank(param))
            return null;
        return ConvertUtil.convert(param, parameter.getType());
    }
}
