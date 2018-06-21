package demo.controller.service;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSClear;
import demo.controller.Action;
import demo.controller.Action2;

@LSBean
public class Service {

    @LSAutowired
    Service2 service2;
    @LSAutowired("service3")
    Service3 service3;

    @LSClear
    @LSAround({Action.class, Action2.class})
    public void test() {
        service2.test2();
    }

    public void test2() {
        service3.test3();
    }
}
