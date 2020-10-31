package com.ls.framework.web.loader;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSLoader;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.handler.HandlerHelper;
import com.ls.framework.web.resolver.ExceptionResolverContainer;
import com.ls.framework.web.resolver.ParameterResolverContainer;
import com.ls.framework.web.resolver.ViewResolversContainer;
import com.ls.framework.web.resolver.parameter.ParameterResolver;
import com.ls.framework.web.resolver.exception.ExceptionResolver;
import com.ls.framework.web.resolver.view.ViewResolver;
import org.apache.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.Set;

@LSLoader(1000)
public class WebLoader implements Loader {

    private static final Logger logger = Logger.getLogger(WebLoader.class);

    @Override
    public void doLoad(Set<Class<?>> classSet) {

        //装载视图处理器
        ClassUtil.getClassesByInterface(classSet, ViewResolver.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(ObjectKit::getInstance)
                .filter(ObjectKit::notNull)
                .map(o -> (ViewResolver)o)
                .forEach(ViewResolversContainer::add);

        //装载参数处理器
        ClassUtil.getClassesByInterface(classSet, ParameterResolver.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(ObjectKit::getInstance)
                .filter(ObjectKit::notNull)
                .map(o -> (ParameterResolver)o)
                .forEach(ParameterResolverContainer::add);

        //装载异常处理器
        ClassUtil.getClassesByInterface(classSet, ExceptionResolver.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(ObjectKit::getInstance)
                .filter(ObjectKit::notNull)
                .map(o -> (ExceptionResolver)o)
                .forEach(ExceptionResolverContainer::add);

        //扫描被LSBean和LSRequestMapping修饰的类，装载路由
        ClassUtil.getClassesByAnnotation(classSet, LSRequestMapping.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(LSBean.class))
                .forEach(HandlerHelper::initRoute);

    }

}
