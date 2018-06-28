package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAspect;
import com.ls.framework.core.annotation.LSBean;

@LSBean
@LSAspect(value = "com.ls.framework.core.aop")
public class Action1 extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
        System.out.println("before");
        invocation.invoke();
    }
}
