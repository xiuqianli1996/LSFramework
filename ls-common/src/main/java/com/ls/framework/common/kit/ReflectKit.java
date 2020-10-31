package com.ls.framework.common.kit;

import com.ls.framework.common.exception.LSException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ReflectKit {

    public static void doWithFiled(Class<?> clazz, Consumer<Field> consumer) {
        doWithFiled(clazz, consumer, field -> true);
    }

    /**
     * @param clazz 目标class
     * @param consumer 执行操作的消费函数
     * @param predicate 过滤条件
     */
    public static void doWithFiled(Class<?> clazz, Consumer<Field> consumer, Predicate<Field> predicate) {
        Arrays.stream(clazz.getDeclaredFields())
                .filter(predicate)
                .forEach(consumer);
        if (clazz.getSuperclass() != Object.class) {
            doWithFiled(clazz.getSuperclass(), consumer, predicate);
        }
    }

    public static void doWithMethod(Class<?> clazz, Consumer<Method> consumer) {
        doWithMethod(clazz, consumer, method -> true);
    }

    /**
     * @param clazz 目标class
     * @param consumer 消费函数
     * @param predicate 过滤条件
     */
    public static void doWithMethod(Class<?> clazz, Consumer<Method> consumer, Predicate<Method> predicate) {
        if (clazz == null) {
            return;
        }
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(predicate)
                .forEach(consumer);
        if (clazz.getSuperclass() != Object.class) {
            doWithMethod(clazz.getSuperclass(), consumer, predicate);
        }
    }

    public static List<Constructor> getAllConstructors(Class<?> clazz) {
        return getAllConstructors(clazz, constructor -> true);
    }

    /**
     * @param clazz 目标class
     * @param predicate 过滤条件
     * @return 所有构造函数，不递归父类
     */
    public static List<Constructor> getAllConstructors(Class<?> clazz, Predicate<Constructor> predicate) {
        return Arrays.stream(clazz.getConstructors())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static Object invokeMethod(Object instance, Method method, Object... params) {
        try {
            return method.invoke(instance, params);
        } catch (Exception e) {
            throw new LSException(e);
        }
    }

    public static void setAccessible(AccessibleObject accessible) {
        accessible.setAccessible(true);
    }

    public static void setFieldValue(Object instance, Field field, Object value) {
        setAccessible(field);
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new LSException(e);
        }
    }

    /**
     * 通过ReflectionFactory添加默认构造方法， 一般只在cglib动态代理生成的类没有默认构造方法时使用
     * @param clazz
     */
    public static void addDefaultConstructor(Class<?> clazz) {
        Class<?> reflectionFactoryClass;

        try {
            reflectionFactoryClass = Class.forName("sun.reflect.ReflectionFactory");
        } catch (ClassNotFoundException e) {
            log.info("can not find reflectionFactoryClass by sun.reflect.ReflectionFactory, try to use jdk.internal.reflect.ReflectionFactory");
            try {
                reflectionFactoryClass = Class.forName("jdk.internal.reflect.ReflectionFactory");
            } catch (ClassNotFoundException ex) {
                throw new LSException("can not find reflectionFactoryClass by jdk.internal.reflect.ReflectionFactory");
            }
        }

        try {
            Method getReflectionFactoryMethod = reflectionFactoryClass.getMethod("getReflectionFactory");
            Object reflectionFactory = getReflectionFactoryMethod.invoke(null);
            Method newConstructorForSerializationMethod = reflectionFactoryClass.getMethod("newConstructorForSerialization", Class.class, Constructor.class);

            newConstructorForSerializationMethod.invoke(reflectionFactory, clazz, Object.class.getConstructor());
        } catch (Exception e) {
            throw new LSException("can not add default constructor to class: " + clazz.getName(), e);
        }
    }

    public static Class<?>[] resolveParameterizedType(Class clazz){
        return resolveParameterizedType(clazz.getGenericSuperclass());
    }

    public static Class<?>[] resolveParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>[]) ((ParameterizedType)type).getActualTypeArguments();
        }
        return new Class<?>[]{(Class<?>) type};
    }

}
