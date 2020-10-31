package com.ls.framework.aop.aspect;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.AopKit;
import com.ls.framework.aop.support.Invocation;
import com.ls.framework.common.kit.ClassKit;

@LSAspect
public class TestAspect3 implements AopAction {
    @Override
    public Object around(Invocation invocation) throws Throwable {
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "before3...");
        Object result = invocation.invoke();
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "after3...");
        return result;
    }


    @Override
    public boolean match(Invocation invocation) {
        return !AopKit.isObjectMethod(invocation.getMethod());
    }
}
