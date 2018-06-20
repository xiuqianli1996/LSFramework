package demo.controller;

import com.ls.framework.annotation.LSBean;
import com.ls.framework.aop.AopAction;
import com.ls.framework.aop.Invocation;

@LSBean
public class Action extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
        System.out.println("aop before");
        invocation.invoke();
        System.out.println("aop after");
    }
}
