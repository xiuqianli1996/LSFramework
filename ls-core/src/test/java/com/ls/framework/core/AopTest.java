package com.ls.framework.core;

import com.ls.framework.core.aop.Service;
import com.ls.framework.core.aop.Service1;
import com.ls.framework.core.context.ApplicationContext;
import org.junit.Before;
import org.junit.Test;

public class AopTest {
    private ApplicationContext app;
    @Before
    public void init() {
        app = new ApplicationContext("application.properties");
        app.init();
    }

    @Test
    public void testAop() {
        Service service = app.getBean(Service.class);
        Service1 service1 = app.getBean(Service1.class);

        service.test();
        service.test2();
        service1.test();
    }

}
