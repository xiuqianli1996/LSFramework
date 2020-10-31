package com.ls.framework.jdbc;

import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JdbcTestWithApp {
    private ApplicationContext app;

    @Before
    public void init() {
        app = new ApplicationContext("application.properties", Collections.singleton("com.ls.framework"), Collections.emptySet());
        app.init();
    }

    @Test
    public void testSelect() {
        ResultService resultService = BeanContainer.sharedContainer().getBean(ResultService.class);
        ResultBean resultBean = resultService.get();
        System.out.println(resultBean);
        assertNotNull(resultBean);
    }

    @Test
    public void testTransaction() {
        ResultService resultService = BeanContainer.sharedContainer().getBean(ResultService.class);
        try {
            resultService.update();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testInParam() {
        ResultService resultService = BeanContainer.sharedContainer().getBean(ResultService.class);
        List<ResultBean> list = resultService.list(new String[]{"2017-5-23", "2017-5-22"});
        assertEquals(1, list.size());
    }
}
