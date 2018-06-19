package demo.controller;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSAutowired;
import com.ls.framework.annotation.LSBean;

@LSBean
public class Service {

    @LSAutowired
    Service2 service2;
    @LSAutowired("service3")
    Service3 service3;

    @LSAround(Action2.class)
    public void test() {
        service2.test();
    }

    public void test2() {
        service3.test3();
    }
}
