package com.ls.framework.web.handler;

import com.ls.framework.web.exception.MissingRequestParamException;
import com.ls.framework.web.resolver.ParameterResolverContainer;
import com.ls.framework.web.resolver.ViewResolversContainer;
import com.ls.framework.web.utils.PathParamKit;
import org.apache.log4j.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ActionHandler {

    private static Logger logger = Logger.getLogger(ActionHandler.class);

    private Object controllerInstance;
    private Class controllerClass;
    private Method actionMethod;
    private Pattern actionPattern;
    private String mappingUrl;
    private String regxUrl;
    private List<String> pathParamNames;

    public Object invoke(String url, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info(request.getMethod() + " === " + request.getRequestURL());
        Map<String, String> pathParamMap = PathParamKit.buildPathParam(url, actionPattern, pathParamNames);
        Object[] args = new Object[actionMethod.getParameterCount()];
        int pos = 0;
        for (Parameter parameter : actionMethod.getParameters()) {
            args[pos++] = ParameterResolverContainer.handle(actionMethod, parameter, request, response, pathParamMap);
        }
        try {
            return actionMethod.invoke(controllerInstance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw (Exception) e.getTargetException();
        }
        return null;
    }

    public boolean match(String url) {
        return actionPattern.matcher(url).matches();
    }

    public String getRegxUrl() {
        return regxUrl;
    }

    public void setRegxUrl(String regxUrl) {
        this.actionPattern = Pattern.compile(regxUrl);
        this.regxUrl = regxUrl;
    }

    public Class getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Pattern getActionPattern() {
        return actionPattern;
    }

    public String getMappingUrl() {
        return mappingUrl;
    }

    public void setMappingUrl(String mappingUrl) {
        this.mappingUrl = mappingUrl;
    }

    public List<String> getPathParamNames() {
        return pathParamNames;
    }

    public void setPathParamNames(List<String> pathParamNames) {
        this.pathParamNames = pathParamNames;
    }

    public void setActionPattern(Pattern actionPattern) {
        this.actionPattern = actionPattern;
    }

    public Object getControllerInstance() {
        return controllerInstance;
    }

    public void setControllerInstance(Object controllerInstance) {
        this.controllerInstance = controllerInstance;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    /**
     * 替换掉对比对象的path参数占位符后尝试通过当前对象的正则表达式匹配，成功即认为映射路径重复
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionHandler that = (ActionHandler) o;
        return actionPattern.matcher(that.mappingUrl.replaceAll("\\{\\w+\\}", "a")).matches();
//        return Objects.equals(controllerInstance, that.controllerInstance) &&
//                Objects.equals(controllerClass, that.controllerClass) &&
//                Objects.equals(actionMethod, that.actionMethod) &&
//                Objects.equals(actionPattern, that.actionPattern) &&
//                Objects.equals(mappingUrl, that.mappingUrl) &&
//                Objects.equals(pathParamNames, that.pathParamNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controllerInstance, controllerClass, actionMethod, actionPattern, mappingUrl, pathParamNames);
    }

}
