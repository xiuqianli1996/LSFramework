package com.ls.framework.core.ioc;

public class BeanHelper {
    public static Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
