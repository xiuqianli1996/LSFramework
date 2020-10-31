package com.ls.framework.aop.aspect;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.component.TestComponent;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.AopKit;
import com.ls.framework.aop.support.Invocation;
import com.ls.framework.common.kit.ClassKit;

@LSAspect(targetClasses = TestComponent.class)
public class TestAspect implements AopAction {
    @Override
    public Object around(Invocation invocation) throws Throwable {
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + " before1...");
        Object result = invocation.invoke();
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "after1...");
        return result;
    }


    @Override
    public boolean match(Invocation invocation) {
        return !AopKit.isObjectMethod(invocation.getMethod());
    }
}
