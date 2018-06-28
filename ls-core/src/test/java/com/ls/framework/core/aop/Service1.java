package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSBean;

@LSBean
public class Service1 {

    @LSAround(Action2.class)
    public void test() {
        System.out.println("Service1 test");
    }

}
