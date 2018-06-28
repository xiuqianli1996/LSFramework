package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSClear;

@LSBean
public class Service {

    public void test() {
        System.out.println("Service test");
    }

    @LSClear
    @LSAround(Action2.class)
    public void test2() {
        System.out.println("Service test2");
    }

}
