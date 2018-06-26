package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSClear;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.CollectionKit;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AopCallback implements MethodInterceptor {

    private Object target;

    public AopCallback(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<AopAction> actionList = getAopActionChain(method);
    	if (CollectionKit.isEmptyCollection(actionList)) {
            return methodProxy.invoke(target, args);
        }
        return new Invocation(target, method, args, methodProxy, actionList).invoke().getReturnVal();
    }
    
    private List<AopAction> getAopActionChain(Method method) {
        List<AopAction> actionList = AopContainer.getAopActionChain(method);
    	if (actionList != null) {
    		return actionList;
    	}

        actionList = AopContainer.getClassAopActionChain(target.getClass());//获取类拦截器
    	
    	if (!method.isAnnotationPresent(LSAround.class)) {
    		return actionList;
    	}
    	
    	if (actionList == null)
            actionList = new ArrayList<>();
    	if (method.isAnnotationPresent(LSClear.class)) {
    	    //方法上有LSClear注解就清除切面
            Class<? extends AopAction>[] clearClasses = method.getAnnotation(LSClear.class).value();
            if (CollectionKit.isEmptyArray(clearClasses)) {
                actionList.clear(); //清除所有切面
            } else {
                //清除指定切面
                actionList = actionList.stream()
                        .filter(aopAction -> !CollectionKit.inArray(clearClasses, aopAction.getClass()))
                        .collect(Collectors.toList());
            }
        }
    	LSAround lsAround = method.getAnnotation(LSAround.class);
        for (Class<? extends AopAction> aopActionClass : lsAround.value()) {
            AopAction aopAction = BeanContainer.getBean(aopActionClass);
            actionList.add(aopAction);
        }
        AopContainer.putAopActionChain(method, actionList);
    	return actionList;
    }
}
