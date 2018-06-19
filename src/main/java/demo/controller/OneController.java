package demo.controller;

import com.ls.framework.annotation.LSAround;
import com.ls.framework.annotation.LSAutowired;
import com.ls.framework.annotation.LSController;
import com.ls.framework.annotation.LSRequestMapping;

@LSController
@LSRequestMapping("/one")
public class OneController {

    @LSAutowired
    Service service;

    @LSAround(Action.class)
    public void test() {
        if (service == null) {
            System.out.println("is null");
            return;
        }
        service.test();
        service.test2();
    }

}
