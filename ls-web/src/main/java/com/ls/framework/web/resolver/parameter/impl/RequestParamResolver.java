package com.ls.framework.web.resolver.parameter.impl;

import com.ls.framework.web.annotation.LSRequestParam;
import com.ls.framework.web.exception.MissingRequestParamException;
import com.ls.framework.web.resolver.parameter.ParameterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class RequestParamResolver implements ParameterResolver {
    @Override
    public boolean filter(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        return parameter.isAnnotationPresent(LSRequestParam.class);
    }

    @Override
    public Object handle(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        LSRequestParam lsRequestParam = parameter.getAnnotation(LSRequestParam.class);
        String paramName = lsRequestParam.value();
        String param = request.getParameter(paramName);

        //判断是否为空 判断是抛出异常还是取默认值
        if (StringKit.isBlank(param)) {
            if (lsRequestParam.require()) {
                throw new MissingRequestParamException(ClassUtil.getFullMethodName(actionMethod), paramName);
            }
            // 取默认参数
            param = lsRequestParam.defaultValue();
        }
        return ConvertUtil.convert(param, parameter.getType());
    }
}
