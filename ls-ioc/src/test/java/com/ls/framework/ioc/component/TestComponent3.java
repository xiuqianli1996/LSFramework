package com.ls.framework.ioc.component;


import com.ls.framework.core.annotation.LSValue;
import com.ls.framework.ioc.annotation.LSBean;

@LSBean
public class TestComponent3 {

    @LSValue("test.msg")
    private String msg;

    public String test() {
        return msg;
    }

}
