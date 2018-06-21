package demo.controller;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.aop.AopAction;
import com.ls.framework.core.aop.Invocation;

@LSBean
public class Action2 extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
        System.out.println("aop2 before");
        invocation.invoke();
        System.out.println("aop2 after");
    }
}
