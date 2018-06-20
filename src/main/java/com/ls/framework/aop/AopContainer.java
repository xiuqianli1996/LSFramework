package com.ls.framework.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 维护Aop拦截链缓存
 */
public class AopContainer {
    private final static Map<Class<?>, List<AopAction>> classAopActionChainMap = new HashMap<>();
    private final static Map<Method, List<AopAction>> aopActionCache = new ConcurrentHashMap<>();

    public static List<AopAction> getAopActionChain(Method key) {
        return aopActionCache.get(key);
    }

    public static void putAopActionChain(Method key, List<AopAction> value) {
        aopActionCache.put(key, value);
    }

    public static List<AopAction> getClassAopActionChain(Class<?> key) {
        return classAopActionChainMap.get(key);
    }

    public static List<AopAction> getClassAopActionChainOrNew(Class<?> key) {
        List<AopAction> actionList = getClassAopActionChain(key);
        if (actionList == null) {
            actionList = new ArrayList<>();
        }
        return actionList;
    }

    public static List<AopAction> putClassAopActionChain(Class<?> key, List<AopAction> value) {
        return classAopActionChainMap.put(key, value);
    }
}
