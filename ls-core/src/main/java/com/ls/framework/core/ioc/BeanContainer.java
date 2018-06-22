package com.ls.framework.core.ioc;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.StringKit;

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

    public static void putBeanByAnnotation(Class<?> clazz, LSBean lsBean, Object instance) {
        put(clazz.getName(), instance); //先按类名存一次
        String name = lsBean.value().trim();
        if (StringKit.notBlank(name)) {
            put(name, instance); //再按注解名存一次
        }
        Class<?>[] interfaces = clazz.getInterfaces();

        if (!CollectionKit.isEmptyArray(interfaces)) {//再按实现的接口的类名存一次
            for (Class interfaceClass : interfaces) {
                put(interfaceClass.getName(), instance);
            }
        }
    }
}
