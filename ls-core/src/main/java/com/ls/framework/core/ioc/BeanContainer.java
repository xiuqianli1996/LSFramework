package com.ls.framework.core.ioc;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContainer {

    private final static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    public static boolean containsKey(String key) {
        return beanMap.containsKey(key);
    }

    public static <T> T getBean(String key) {
        return (T) beanMap.get(key);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz.getName());
    }

    public static Object put(String key, Object value) {
        return beanMap.put(key, value);
    }

    public static Collection<Object> allBeans() {
        return beanMap.values();
    }

    public static Map<String, Object> getBeanMap() {
        return beanMap;
    }
}
