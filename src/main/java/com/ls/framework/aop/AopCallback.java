package com.ls.framework.aop;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.ioc.BeanFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AopCallback implements MethodInterceptor {

    private Object target;

    public AopCallback(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        if (!method.isAnnotationPresent(LSAround.class)) {
            return methodProxy.invoke(target, args);
        }
        LSAround lsAround = method.getAnnotation(LSAround.class);
        Class<? extends AopAction> aopActionClass = lsAround.value();
        AopAction aopAction = BeanFactory.getInstance().getBean(aopActionClass);
        return aopAction.invoke(new Invocation(target, method, args, methodProxy));
    }
}
