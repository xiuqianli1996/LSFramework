package com.ls.framework.web.handler;

import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.loader.WebLoader;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlerHelper {

    private static final Logger logger = Logger.getLogger(HandlerHelper.class);

    /**
     * 装载路由
     * @param clazz
     */
    public static void initRoute(Class<?> clazz) {
//        System.out.println(clazz.getName());
        for (Method method : clazz.getMethods()){
            if (!method.isAnnotationPresent(LSRequestMapping.class)) {
                continue;
            }
            LSRequestMapping classRequestMapping = clazz.getAnnotation(LSRequestMapping.class);
            LSRequestMapping methodRequestMapping = method.getAnnotation(LSRequestMapping.class);
            String mappingUrl = "/" + classRequestMapping.value() + "/" + methodRequestMapping.value();
            mappingUrl = mappingUrl.replaceAll("/+", "/");
            buildMapping(clazz, method, mappingUrl);

            logger.info("mapping \"" + mappingUrl + "\" to " + clazz.getName() + "." + method.getName());

        }
    }

    private static void buildMapping(Class<?> controllerClass, Method actionMethod, String mappingUrl) {
        String regxUrl = mappingUrl.replaceAll("\\{\\w+\\}", "(\\\\w+)");
//        Pattern pattern = Pattern.compile(regxUrl);

        ActionHandler actionHandler = new ActionHandler();
        actionHandler.setActionMethod(actionMethod);
//        actionHandler.setActionPattern(pattern);
        actionHandler.setControllerClass(controllerClass);
        actionHandler.setControllerInstance(BeanContainer.getBean(controllerClass));
        actionHandler.setMappingUrl(mappingUrl);
        actionHandler.setRegxUrl(regxUrl);

        Pattern pathParamNamePattern = Pattern.compile("\\{(\\w+)\\}");
        Matcher matcher = pathParamNamePattern.matcher(mappingUrl);
        List<String> pathParamNames = new ArrayList<>();
        while (matcher.find()) {
            pathParamNames.add(matcher.group(1));
//            System.out.println(matcher.group(1));
        }

        actionHandler.setPathParamNames(pathParamNames);

        HandlerContainer.add(actionHandler);
    }
}
