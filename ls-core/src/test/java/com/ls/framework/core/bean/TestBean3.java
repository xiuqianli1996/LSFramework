package com.ls.framework.core.bean;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;

@LSBean
public class TestBean3 {
    @LSAutowired
    private TestBean2 testBean2;

    public void test() {
        testBean2.test();
    }

    public TestBean2 getTestBean2() {
        return testBean2;
    }

    public void setTestBean2(TestBean2 testBean2) {
        this.testBean2 = testBean2;
    }
}
