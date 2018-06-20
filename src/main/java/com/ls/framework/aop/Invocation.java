package com.ls.framework.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    private Object returnVal = null;
    private List<AopAction> aopActionList;
    private int invokePos = 0;

    public Invocation(Object target, Method method, Object[] args, MethodProxy methodProxy, List<AopAction> aopActionList) {
        this.target = target;
        this.method = method;
        this.args = args;
        this.methodProxy = methodProxy;
        this.aopActionList = aopActionList;
    }

    public Invocation invoke() throws Throwable {
        if (invokePos < aopActionList.size()) {
            aopActionList.get(invokePos++).invoke(this);
        } else {
            returnVal = methodProxy.invoke(target, args);
        }

        return this;
    }

    public Object getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(Object returnVal) {
        this.returnVal = returnVal;
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
