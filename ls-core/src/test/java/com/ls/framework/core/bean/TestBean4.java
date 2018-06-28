package com.ls.framework.core.bean;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;

@LSBean
public class TestBean4 {

    @LSAutowired
    public TestBean testBean;
    public TestBean2 testBean2;

    @LSAutowired
    public void setTestBean2(TestBean2 testBean2) {
        this.testBean2 = testBean2;
    }

}
