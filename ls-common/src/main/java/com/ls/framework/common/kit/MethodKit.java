package com.ls.framework.common.kit;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MethodKit {

    private static final Set<String> OBJECT_METHODS = new HashSet<>();

    static {
        for (Method method : Object.class.getDeclaredMethods()) {
            OBJECT_METHODS.add(method.getName());
        }
    }

    public static boolean isObjectMethod(Method method) {
        // 取巧做法，直接根据名字判断，还可以加上参数个数及类型是否匹配的判断
        return OBJECT_METHODS.contains(method.getName());
    }

}
