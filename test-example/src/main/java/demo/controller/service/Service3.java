package demo.controller.service;


import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSBean;
import demo.controller.Action;

@LSBean("service3")
public class Service3 implements IService {

    @LSAround(Action.class)
    @Override
    public void test() {
        System.out.println("IOC2 test success");
    }
}
