package com.ls.framework.core;

import com.ls.framework.core.constant.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplicationContextTests {

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = ApplicationContext.run(TestApplication.class);
    }

    @Test
    public void testGetProperty() {
        String jdbcUser = applicationContext.getProperty("jdbc.user");
        Assert.assertEquals(jdbcUser, "root");

        String profile = applicationContext.getProperty(Constants.CONFIG_ACTIVE_PROFILE);
        Assert.assertEquals(profile, "test");

        // profile覆盖默认application.properties
        String test = applicationContext.getProperty("test");
        Assert.assertEquals(test, "456");

        System.out.println("jdbc.user: " + jdbcUser);
        System.out.println("user.home: " + applicationContext.getProperty("user.home"));

    }
}
