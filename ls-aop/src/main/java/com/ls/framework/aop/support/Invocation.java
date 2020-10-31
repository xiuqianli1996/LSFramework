package com.ls.framework.aop.support;

import lombok.*;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
public class Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private MethodProxy methodProxy;
    @Setter
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

    public Object invoke() throws Throwable {
        // 当某个拦截器已经给returnVal赋值了就不再往下执行

        if (invokePos < aopActionList.size()) {
            AopAction action = aopActionList.get(invokePos++);
            if (!action.match(this)) {
                // 如果不需要执行就直接跳过这个切面
                return invoke();
            }
            return action.around(this);
        }
        return methodProxy.invoke(target, args);
    }

}
