package com.ls.framework.ioc.kit;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.definition.AnnotationBeanDefinition;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.definition.MethodBeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;


public class BeanDefinitionKit {

    /**
     * 只能有一个被LSAutowired注解的构造函数
     */
    private static final int MAX_ANNOTATED_CONSTRUCTOR_SIZE = 1;

    public static <T> BeanDefinition from(Class<T> clazz, String name, boolean singleton) {
        List<Constructor> annotatedConstructors = ReflectKit.getAllConstructors(clazz, constructor -> constructor.isAnnotationPresent(LSAutowired.class));
        if (annotatedConstructors.size() > MAX_ANNOTATED_CONSTRUCTOR_SIZE) {
            throw new LSException("@LSAutowired constructor count can not great than " + MAX_ANNOTATED_CONSTRUCTOR_SIZE + ", class name: " + clazz.getName());
        }
        if (annotatedConstructors.size() == MAX_ANNOTATED_CONSTRUCTOR_SIZE) {
            // 发现一个被注解的构造函数，之后就用这个构造函数进行构造
            return new AnnotationBeanDefinition(name, clazz, singleton, annotatedConstructors.get(0));
        }
        name = name.trim();
        // 没有发现被注解的构造函数，找出所需参数最短的构造函数 todo: 是否要用参数最长的？
        List<Constructor> constructors = ReflectKit.getAllConstructors(clazz);
        constructors.sort(Comparator.comparingInt(Constructor::getParameterCount));

        return new AnnotationBeanDefinition(name, clazz, singleton, constructors.get(0));
    }

    public static BeanDefinition from(Class<?> configurationClass, Method createInstanceMethod, String name, boolean singleton) {
        Class<?> beanClass = createInstanceMethod.getReturnType();
        if (beanClass == Void.class) {
            throw new LSException("bean method can not return void, name: " + ClassKit.getFullMethodName(createInstanceMethod));
        }
        name = name.trim();
        return new MethodBeanDefinition(name, beanClass, singleton, configurationClass, createInstanceMethod);
    }
}
