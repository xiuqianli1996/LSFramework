package com.ls.framework.web.resolver.parameter.impl;

import com.ls.framework.web.resolver.parameter.ParameterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class ServletRequestParamResolver implements ParameterResolver {
    @Override
    public boolean filter(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        return parameter.getType() == HttpServletRequest.class; //判断是否由这个类处理这个参数
    }

    @Override
    public Object handle(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        return request;
    }
}
