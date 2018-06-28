package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAspect;
import com.ls.framework.core.annotation.LSBean;

@LSBean
public class Action2 extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {

        invocation.invoke();
        System.out.println(" Action2 after");
    }
}
