package com.ls.framework.common.kit;

import com.ls.framework.common.exception.LSException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ObjectKit {

    public static <T> T getInstance(Class<T> clazz, Object... params) {
        List<Class<?>> types = Arrays.stream(params)
                .map(Object::getClass)
                .collect(Collectors.toList());
        try {
            return getInstanceByConstructor(clazz.getDeclaredConstructor(types.toArray(new Class<?>[0])), params);
        } catch (NoSuchMethodException e) {
            throw new LSException("getConstructor error, class name: " + clazz.getName());
        }
    }

    public static <T> T getInstanceByConstructor(Constructor<T> constructor, Object... params) {
        try {
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("get new instance error, class name: " + constructor.getDeclaringClass().getName(), e);
        }
    }

    public static <T> List<T> getInstancesByInterface(Set<Class<?>> classSet, Class<T> interfaceClass) {
        return classSet.stream()
                .filter(interfaceClass::isAssignableFrom)
                .filter(ClassKit::notAbstract) //排除抽象类
                .map(ObjectKit::getInstance)
                .filter(Objects::nonNull) //过滤掉创建实例失败的Loader
                .map(interfaceClass::cast)
                .collect(Collectors.toList());
    }

    public static <T> T orDefault(T t, Supplier<T> defaultValGetter) {
        return t == null ? defaultValGetter.get() : t;
    }
}
