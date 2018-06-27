package com.ls.framework.web.resolver;

import com.ls.framework.web.resolver.parameter.ParameterResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ParameterResolverContainer {

    private static List<ParameterResolver> parameterResolverList = new LinkedList<>();

    public static void add(ParameterResolver parameterResolver) {
        parameterResolverList.add(parameterResolver);
    }

    public static Object handle(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {

        for (ParameterResolver parameterResolver : parameterResolverList) {
            if (parameterResolver.filter(actionMethod, parameter, request, response, pathParamMap)) {
                return parameterResolver.handle(actionMethod, parameter, request, response, pathParamMap);
            }
        }

        return null;
    }
}
