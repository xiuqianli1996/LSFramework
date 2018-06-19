package demo.controller;

import com.ls.framework.annotation.LSBean;
import com.ls.framework.aop.AopAction;
import com.ls.framework.aop.Invocation;

@LSBean
public class Action extends AopAction {
    @Override
    public Object invoke(Invocation invocation) {
        System.out.println("aop before");
        Object o = invocation.invoke();
        System.out.println("aop after");
        return o;
    }
}
