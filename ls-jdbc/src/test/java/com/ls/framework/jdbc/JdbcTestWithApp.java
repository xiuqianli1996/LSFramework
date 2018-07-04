package com.ls.framework.jdbc;

import com.ls.framework.core.context.ApplicationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import sun.awt.windows.WEmbeddedFrame;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void testTransaction() {
        ResultService resultService = app.getBean(ResultService.class);
        try {
            resultService.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInParam() {
        ResultService resultService = app.getBean(ResultService.class);
        List<ResultBean> list = resultService.list(new String[]{"2017-5-23", "2017-5-22"});
        assertEquals(2, list.size());
    }
}
