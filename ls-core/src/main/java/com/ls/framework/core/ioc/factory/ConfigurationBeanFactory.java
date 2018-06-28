package com.ls.framework.core.ioc.factory;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import com.ls.framework.core.annotation.LSParam;
import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.StringKit;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public class ConfigurationBeanFactory implements BeanFactory {

    @Override
    public void loadBean(Set<Class<?>> classSet) {
        //加载LSConfiguration注解的类
        ClassUtil.getClassesByAnnotation(classSet, LSConfiguration.class)
                .stream()
                .sorted((o1, o2) -> {
                    LSConfiguration configuration1 = o1.getAnnotation(LSConfiguration.class);
                    LSConfiguration configuration2 = o2.getAnnotation(LSConfiguration.class);
                    return configuration1.value() - configuration2.value();
                })
                .forEach(this::loadConfiguration);
    }

    private void loadConfiguration(Class<?> clazz) {
        try {
            Object configInstance = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            Arrays.sort(methods, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())); //根据名字排序控制加载顺序
            for (Method method : methods) {
                if (method.isAnnotationPresent(LSConfiguration.class)) {
                    method.invoke(configInstance); //LSConfiguration注解的方法直接执行
                    continue;
                }
                if (!method.isAnnotationPresent(LSBean.class))
                    continue;

                Object[] args = getInjectParams(method);
                Object beanInstance = method.invoke(configInstance, args);
                Class<?> beanClass = method.getReturnType();
                LSBean lsBean = method.getAnnotation(LSBean.class);
                BeanContainer.putBeanByAnnotation(beanClass, lsBean, beanInstance);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private Object[] getInjectParams(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> typeClass = parameter.getType();
            String beanName = typeClass.getName();
            if (parameter.isAnnotationPresent(LSAutowired.class)) {
                LSAutowired lsAutowired = parameter.getAnnotation(LSAutowired.class);
                if (StringKit.notBlank(lsAutowired.value())) {
                    beanName = lsAutowired.value();
                }
            }
            Object arg = BeanContainer.getBean(beanName);
            if (arg == null) {
                throw new LSException(String.format("In %s inject param is null: %s"
                        , ClassUtil.getFullMethodName(method), beanName));
            }
            args[i] = AopHelper.enhance(arg);
        }
        return args;
    }
}

