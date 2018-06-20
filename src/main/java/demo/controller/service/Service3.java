package demo.controller.service;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSBean;
import demo.controller.Action;

@LSBean("service3")
public class Service3 {
//    @LSAround(Action.class)
    public void test3() {
        System.out.println("IOC2 test success");
    }
}
