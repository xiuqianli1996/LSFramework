package com.ls.framework.ioc.component;

import com.ls.framework.core.annotation.LSValue;
import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.config.TestProperties;

@LSBean
public class TestComponent1 {

    @LSAutowired
    private TestComponent2 testComponent2;

    private TestComponent3 testComponent3;

    @LSAutowired
    private TestProperties testProperties;

    public TestComponent1(TestComponent3 testComponent3) {
        this.testComponent3 = testComponent3;
    }

    public String test() {
        return testComponent2.test();
    }

    public String test2() {
        return testComponent3.test();
    }

    public String test3() {
        return "msg:" + testProperties.getMsg() + ", count: " + testProperties.getCount();
    }


}
