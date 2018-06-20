package demo.controller;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSAspect;
import com.ls.framework.annotation.LSBean;
import com.ls.framework.aop.AopAction;
import com.ls.framework.aop.Invocation;

@LSBean
@LSAspect(value = "demo.controller", cls = "TestBean2")
public class Action3 extends AopAction {
    @Override
    public void invoke(Invocation invocation) throws Throwable {
//        if (invocation.getMethod().isAnnotationPresent(LSAround.class)){
//            return;
//        }
        System.out.println("aop3 before");
        invocation.invoke();
        System.out.println("aop3 after");
    }

}
