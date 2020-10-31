package com.ls.framework.aop.component;

import com.ls.framework.aop.annotation.LSAround;
import com.ls.framework.aop.annotation.LSClear;
import com.ls.framework.aop.aspect.TestAspect2;
import com.ls.framework.aop.aspect.TestAspect3;
import com.ls.framework.aop.aspect.TestAspect4;
import com.ls.framework.ioc.annotation.LSBean;

@LSBean
@LSAround(TestAspect3.class)
public class TestComponent {

    public void test() {
        System.out.println("hahha");
    }

    @LSClear
    @LSAround(TestAspect4.class)
    public void testClearAllClassAction() {
        // 清除所有类级切面
        System.out.println("testClearAllClassAction");
    }

    @LSClear(TestAspect2.class)
    public void testClearAspect2() {
        // 清除所有类级切面
        System.out.println("testClearAspect2");
    }

}
