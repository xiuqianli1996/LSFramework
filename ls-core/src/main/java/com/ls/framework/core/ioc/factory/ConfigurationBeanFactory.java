package com.ls.framework.core.ioc.factory;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import com.ls.framework.core.annotation.LSParam;
import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Set;

public class ConfigurationBeanFactory extends BaseBeanFactory {

    @Override
    public void loadBean(Set<Class<?>> classSet) {
        //加载LSConfiguration注解的类
        ClassUtil.getClassesByAnnotation(classSet, LSConfiguration.class)
                .forEach(clazz -> {
                    try {
                        Object configInstance = clazz.newInstance();
                        for (Method method : clazz.getMethods()) {
                            if (!method.isAnnotationPresent(LSBean.class))
                                continue;
                            Parameter[] parameters = method.getParameters();
                            Object[] args = new Object[parameters.length];
                            for (int i = 0; i < parameters.length; i++) {
                                Parameter parameter = parameters[i];
                                Class<?> typeClass = parameter.getType();
                                String beanName = typeClass.getName();
                                if (parameter.isAnnotationPresent(LSParam.class)) {
                                    LSParam lsParam = parameter.getAnnotation(LSParam.class);
                                    beanName = lsParam.value();
                                }
                                Object arg = BeanContainer.getBean(beanName);
                                if (arg == null) {
                                    throw new LSException(String.format("In %s.%s inject param is null: %s"
                                            , clazz.getName(), method.getName(), beanName));
                                }
                                args[i] = AopHelper.enhance(arg);
                            }
                            Object beanInstance = method.invoke(configInstance, args);
                            Class<?> beanClass = method.getReturnType();
                            LSBean lsBean = method.getAnnotation(LSBean.class);
                            BeanContainer.putBeanByAnnotation(beanClass, lsBean, beanInstance);
                        }
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }


                });
    }
}
