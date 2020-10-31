package com.ls.framework.web.handler;

import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.web.annotation.LSRequestMapping;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerHelper {

    private static final String pathParamPlaceholderRegx = "\\{(\\w+)\\}"; //LSRequestMapping注解的path参数名正则
    private static final String pathParamRegx = "([0-9a-zA-Z\\\\-_]+)";//真正的参数正则，把占位符换成这玩意

//    private static final Logger logger = Logger.getLogger(HandlerHelper.class);

    /**
     * 装载路由
     * @param clazz
     */
    public static void initRoute(Class<?> clazz) {
//        System.out.println(clazz.getName());
        LSRequestMapping classRequestMapping = clazz.getAnnotation(LSRequestMapping.class);
        for (Method method : clazz.getMethods()){
            if (!method.isAnnotationPresent(LSRequestMapping.class)) {
                continue;
            }
            LSRequestMapping methodRequestMapping = method.getAnnotation(LSRequestMapping.class);
            String mappingUrl = "/" + classRequestMapping.value() + "/" + methodRequestMapping.value();
            mappingUrl = mappingUrl.replaceAll("/+", "/");//干掉多余的斜杠
            buildMapping(clazz, method, mappingUrl);

//            logger.info("mapping \"" + mappingUrl + "\" to " + clazz.getName() + "." + method.getName());

        }
    }

    private static void buildMapping(Class<?> controllerClass, Method actionMethod, String mappingUrl) {
        String regxUrl = mappingUrl.replaceAll(pathParamPlaceholderRegx, pathParamRegx);
//        Pattern pattern = Pattern.compile(regxUrl);

        ActionHandler actionHandler = new ActionHandler();
        actionHandler.setActionMethod(actionMethod);
//        actionHandler.setActionPattern(pattern);
        actionHandler.setControllerClass(controllerClass);
        actionHandler.setControllerInstance(BeanContainer.getBean(controllerClass));
        actionHandler.setMappingUrl(mappingUrl);
        actionHandler.setRegxUrl(regxUrl);

        Pattern pathParamNamePattern = Pattern.compile(pathParamPlaceholderRegx);
        Matcher matcher = pathParamNamePattern.matcher(mappingUrl);
        List<String> pathParamNames = new ArrayList<>();
        while (matcher.find()) {
            pathParamNames.add(matcher.group(1));
//            System.out.println(matcher.group(1));
        }

        actionHandler.setPathParamNames(pathParamNames);

        HandlerContainer.add(actionHandler);
    }

//    public static void main(String[] args) {
//        String url = "/get/{day}";
//        Pattern pathParamNamePattern = Pattern.compile(pathParamPlaceholderRegx);
//        Matcher matcher = pathParamNamePattern.matcher(url);
//        if (matcher.find()) {
//            System.out.println(matcher.group(1));
//        }
//        url = url.replaceAll(pathParamPlaceholderRegx, pathParamRegx);
//        Pattern paramPattern = Pattern.compile(url);
//        String realurl = "/get/2017-5-22";
//        matcher = paramPattern.matcher(realurl);
//        if (matcher.find()) {
//            System.out.println(matcher.group(1));
//        }
//    }
}
