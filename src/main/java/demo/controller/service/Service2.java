package demo.controller.service;

import com.ls.framework.annotation.LSAutowired;
import com.ls.framework.annotation.LSBean;

@LSBean
public class Service2 {
    @LSAutowired
    Service service;
    public void test() {
        System.out.println("IOC test success");
    }

    public void test2() {
        service.test2();
    }
}
