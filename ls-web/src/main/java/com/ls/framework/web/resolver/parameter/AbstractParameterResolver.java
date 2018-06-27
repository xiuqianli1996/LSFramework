package com.ls.framework.web.resolver.parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public abstract class AbstractParameterResolver implements ParameterResolver {

    @Override
    public boolean filter(Method actionMethod, Parameter parameter, HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParamMap) {
        return true;
    }
}
