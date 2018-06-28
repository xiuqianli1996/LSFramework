package com.ls.framework.core.config;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import com.ls.framework.core.bean.TestBean;
import com.ls.framework.core.bean.TestBean2;
import com.ls.framework.core.ioc.BeanContainer;

@LSConfiguration
public class TestConfig {

    @LSBean("qwer")
    public TestBean2 get1TestBean() {
        TestBean2 testBean2 = new TestBean2();
        testBean2.setVal1(4546);
        testBean2.setVal2("dsadasfsafs");
        return testBean2;
    }

    @LSConfiguration
    public void get2Config() {
        TestBean2 testBean2 = BeanContainer.getBean("qwer");
        testBean2.setVal2("cxcxczzxcczx");
    }

}
