package com.ls.framework.ioc;

import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.annotation.LSApplication;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.component.TestComponent1;
import com.ls.framework.ioc.component.TestComponent2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@LSApplication
public class IocTestApplication {

    private ApplicationContext applicationContext;

    @LSBean
    public TestComponent2 testComponent2() {
        return new TestComponent2("world");
    }

    @Before
    public void init() {
        applicationContext = ApplicationContext.run(IocTestApplication.class);
    }

    @Test
    public void testIoc() {
        TestComponent1 testComponent1 = BeanContainer.sharedContainer().getBean(TestComponent1.class);
        assertEquals(testComponent1.test(), "world");
        assertEquals(testComponent1.test2(), "hahaha");
        assertEquals(testComponent1.test3(), "msg:hahaha, count: 123");
    }

}
