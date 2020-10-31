package com.ls.framework.aop.aspect;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.component.TestComponent;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.AopKit;
import com.ls.framework.aop.support.Invocation;
import com.ls.framework.common.kit.ClassKit;

@LSAspect(packages = "com.ls.framework.aop.component")
public class TestAspect2 implements AopAction {
    @Override
    public Object around(Invocation invocation) throws Throwable {
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "before2...");
        Object result = invocation.invoke();
        System.out.println(ClassKit.getFullMethodName(invocation.getMethod()) + "after2...");
        return result;
    }

    @Override
    public boolean match(Invocation invocation) {
        return !AopKit.isObjectMethod(invocation.getMethod());
    }
}
