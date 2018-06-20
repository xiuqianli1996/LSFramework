package demo.controller;

import com.ls.framework.annotation.*;
import demo.controller.service.Service;

@LSController
@LSRequestMapping("/one")
@LSAround({Action.class, Action3.class})
public class OneController {

    @LSAutowired
    Service service;

    @LSAround(Action2.class)
    @LSClear(Action3.class)
    public void test() {
        if (service == null) {
            System.out.println("is null");
            return;
        }
        service.test();
//        service.test2();
    }

    @LSAround(Action2.class)
    public void test2() {
        System.out.println("is test2");
    }

}
