package com.ls.framework.aop.support;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AopKit {

    private static final Set<String> OBJECT_METHOD_NAMES = Arrays.stream(Object.class.getMethods()).map(Method::getName).collect(Collectors.toSet());

    public static Class<?> getTargetClass(Object proxyBean) {
        return null;
    }

    /**
     * 判断是否定义在object里的方法，这里只是简单根据名字判断
     * @param method
     * @return
     */
    public static boolean isObjectMethod(Method method) {
        return OBJECT_METHOD_NAMES.contains(method.getName());
    }

}
