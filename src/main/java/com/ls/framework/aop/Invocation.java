package com.ls.framework.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;

    public Invocation(Object target, Method method, Object[] args, MethodProxy methodProxy) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
    }

    public Object invoke() {
        try {
            return methodProxy.invoke(target, args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public void setMethodProxy(MethodProxy methodProxy) {
        this.methodProxy = methodProxy;
    }
}
