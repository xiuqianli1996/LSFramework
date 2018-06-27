package demo.web.service;


import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;

@LSBean("service2")
public class Service2 implements IService {
    @LSAutowired
    Service service;
    public String test() {
        System.out.println("IOC test success");
        return "";
    }

    public void test2() {
        service.test2();
    }
}
