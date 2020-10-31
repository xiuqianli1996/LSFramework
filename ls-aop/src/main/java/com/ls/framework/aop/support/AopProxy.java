package com.ls.framework.aop.support;

import com.ls.framework.aop.annotation.LSClear;
import com.ls.framework.common.kit.CollectionKit;
import com.ls.framework.common.kit.ReflectKit;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Data
public class AopProxy implements MethodInterceptor {

    private final Object target;

    private Map<Method, List<AopAction>> aopActionsMap = new ConcurrentHashMap<>();

    private AopProxy(Object target) {
        this.target = target;
    }

    public static Object getProxy(Object target) {
        AopProxy proxy = new AopProxy(target);

        try {
            return Enhancer.create(target.getClass(), proxy);
        } catch (Exception e) {
            log.debug("create proxy by default constructor error, fallback to add default constructor", e);
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(proxy);
        Class<?> proxyClass = enhancer.createClass();
        ReflectKit.addDefaultConstructor(proxyClass);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<AopAction> aopActions = getAopActionChain(method);
        if (CollectionKit.isEmpty(aopActions)) {
            return methodProxy.invoke(target, args);
        }
        return new Invocation(target, method, args, methodProxy, aopActions).invoke();
    }
    
    private List<AopAction> getAopActionChain(Method method) {
        return aopActionsMap.computeIfAbsent(method, k -> {
            //获取类切面action
            List<AopAction> actionList = new ArrayList<>(AopContainer.getAopActionChain(target.getClass()));

            if (method.isAnnotationPresent(LSClear.class)) {
                //方法上有LSClear注解就清除切面
                Class<? extends AopAction>[] clearClasses = method.getAnnotation(LSClear.class).value();
                if (CollectionKit.isEmpty(clearClasses)) {
                    //清除所有切面
                    actionList.clear();
                } else {
                    //清除指定切面
                    actionList = actionList.stream()
                            .filter(aopAction -> !CollectionKit.in(clearClasses, aopAction.getClass()))
                            .collect(Collectors.toList());
                }
            }
            // 对类级切面排序
            actionList.sort((a, b) -> {
                if (a.order() == b.order()) {
                    // 如果order相等就按类名排序
                    return a.getClass().getSimpleName().compareToIgnoreCase(b.getClass().getSimpleName());
                }
                return Integer.compare(a.order(), b.order());
            });
            // 获取注解在方法上的切面action
            actionList.addAll(AopContainer.getAopActionChain(method));
            return actionList;
        });

    }
}
