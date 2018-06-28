package com.ls.framework.jdbc;

import com.ls.framework.core.context.ApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JdbcTestWithApp {
    private ApplicationContext app;

    @Before
    public void init() {
        app = new ApplicationContext("application.properties");
        app.init();
    }

    @Test
    public void testSelect() {
        ResultService resultService = app.getBean(ResultService.class);
        ResultBean resultBean = resultService.get();
        System.out.println(resultBean);
        assertNotNull(resultBean);
    }
}
