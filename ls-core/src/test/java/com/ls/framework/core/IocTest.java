package com.ls.framework.core;

import com.ls.framework.core.bean.*;
import com.ls.framework.core.context.ApplicationContext;
import com.ls.framework.core.ioc.BeanContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IocTest {

    private ApplicationContext app;

    @Before
    public void init() {
        app = new ApplicationContext("application.properties");
        app.init();
    }

    @Test
    public void testIocByConfig() {
        TestBean testBean = app.getBean("testBean");
        assertNotNull(testBean);
        TestBean testBean1 = app.getBean("testBean1");
        assertNotNull(testBean1);
        TestBean2 testBean2 = app.getBean("testBean2");
        assertNotNull(testBean2);
        assertEquals(testBean2.getTestBean(), testBean);
        CircleA circleA = app.getBean(CircleA.class);
        CircleB circleB = app.getBean(CircleB.class);
        CircleC circleC = app.getBean(CircleC.class);
        assertNotNull(circleA);
        assertNotNull(circleB);
        assertNotNull(circleC);
        assertEquals(circleA.o, circleC);
        assertEquals(circleB.o, circleA);
        assertEquals(circleC.o, circleB);
    }

    @Test
    public void testIocByAnnotation() {
        TestBean2 testBean2 = app.getBean(TestBean2.class);
        TestBean testBean = app.getBean(TestBean.class);
        TestBean3 testBean3 = app.getBean(TestBean3.class);
        assertNotNull(testBean3);
        assertNotNull(testBean3.getTestBean2());
        assertEquals(testBean2, testBean3.getTestBean2()); //这里因为cglib强化了两个不同的对象所以测不过，修改不加强的过滤条件，测试通过

        TestBean4 testBean4 = app.getBean(TestBean4.class);
        assertNotNull(testBean4);
//        System.out.println(testBean4);
        assertNotNull(testBean4.testBean);
        assertNotNull(testBean4.testBean2);
    }

    @Test
    public void testIocByConfigurationAnnotation() {
        TestBean2 testBean2 = BeanContainer.getBean("qwer");
        assertEquals("cxcxczzxcczx", testBean2.getVal2());

    }

}
