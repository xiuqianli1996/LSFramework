package com.ls.framework.web.loader;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.ioc.BeanHelper;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.ObjectKit;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.handler.HandlerContainer;
import com.ls.framework.web.resolver.ParameterResolverContainer;
import com.ls.framework.web.resolver.ViewResolversContainer;
import com.ls.framework.web.resolver.argument.ParameterResolver;
import com.ls.framework.web.resolver.view.ViewResolver;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebLoader implements Loader {

    private static final Logger logger = Logger.getLogger(WebLoader.class);

    @Override
    public void doLoad(Set<Class<?>> classSet) {

        //装载视图处理器
        ClassUtil.getClassesByInterface(classSet, ViewResolver.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(BeanHelper::getInstance)
                .filter(ObjectKit::notNull)
                .map(o -> (ViewResolver)o)
                .forEach(ViewResolversContainer::add);

        //装载参数处理器
        ClassUtil.getClassesByInterface(classSet, ParameterResolver.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(BeanHelper::getInstance)
                .filter(ObjectKit::notNull)
                .map(o -> (ParameterResolver)o)
                .forEach(ParameterResolverContainer::add);

        //扫描被LSBean和LSRequestMapping修饰的类
        ClassUtil.getClassesByAnnotation(classSet, LSRequestMapping.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(LSBean.class))
                .forEach(WebLoader::initRoute);

    }

    /**
     * 装载路由
     * @param clazz
     */
    private static void initRoute(Class<?> clazz) {
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
        String regx = mappingUrl.replaceAll("\\{\\w+\\}", "(\\\\w+)");
        Pattern pattern = Pattern.compile(regx);

        ActionHandler actionHandler = new ActionHandler();
        actionHandler.setActionMethod(actionMethod);
        actionHandler.setActionPattern(pattern);
        actionHandler.setControllerClass(controllerClass);
        actionHandler.setControllerInstance(BeanContainer.getBean(controllerClass));
        actionHandler.setMappingUrl(mappingUrl);

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

    @Override
    public int getLevel() {
        return 2;
    }
}
