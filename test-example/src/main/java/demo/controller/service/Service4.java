package demo.controller.service;

import com.ls.framework.core.annotation.LSBean;
import demo.controller.exception.GlobalException;

@LSBean("service4")
public class Service4 implements IService {
    @Override
    public String test() {
        if (System.currentTimeMillis() % 2 == 1) {
            throw new GlobalException();
        }
        return "hello";
    }
}
