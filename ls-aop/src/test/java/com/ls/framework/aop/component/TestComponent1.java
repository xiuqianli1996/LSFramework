package com.ls.framework.aop.component;

import com.ls.framework.aop.annotation.LSAround;
import com.ls.framework.aop.aspect.TestAspect4;
import com.ls.framework.ioc.annotation.LSBean;

@LSBean
public class TestComponent1 {

    @LSAround(TestAspect4.class)
    public void test() {
        System.out.println("hahhgfhfgha");
    }

}
