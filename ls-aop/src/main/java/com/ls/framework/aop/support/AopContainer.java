package com.ls.framework.aop.support;

import com.ls.framework.common.kit.CollectionKit;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 维护Aop拦截链缓存
 */
public class AopContainer {
    // 类级的会进行排序
    private final static Map<Class<?>, Set<AopAction>> classAopActionChainMap = new ConcurrentHashMap<>();
    // 方法级的按照注册顺序（通过LinkedHashSet实现）
    private final static Map<Method, Set<AopAction>> methodAopActionCache = new ConcurrentHashMap<>();

    public static Set<AopAction> getAopActionChain(Method key) {
        return Collections.unmodifiableSet(methodAopActionCache.getOrDefault(key, Collections.emptySet()));
    }

    public static Set<AopAction> getAopActionChain(Class<?> key) {
        return Collections.unmodifiableSet(classAopActionChainMap.getOrDefault(key, Collections.emptySet()));
    }

    public static void putAopAction(Method method, AopAction action) {
        methodAopActionCache.computeIfAbsent(method, k -> new LinkedHashSet<>()).add(action);
    }

    public static void putAopAction(Class<?> clazz, AopAction action) {
        classAopActionChainMap.computeIfAbsent(clazz, k -> new LinkedHashSet<>()).add(action);
    }

}
