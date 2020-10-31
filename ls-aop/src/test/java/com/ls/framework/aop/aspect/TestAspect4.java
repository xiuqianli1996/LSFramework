package com.ls.framework.aop.aspect;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.Invocation;
import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.ioc.annotation.LSBean;

@LSBean
public class TestAspect4 implements AopAction {
    @Override
    public Object around(Invocation invocation) throws Throwable {
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "before4...");
        Object result = invocation.invoke();
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "after4...");
        return result;
    }
}
