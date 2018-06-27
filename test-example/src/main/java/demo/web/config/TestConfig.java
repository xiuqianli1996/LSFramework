package demo.web.config;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import demo.web.TestConfigDI;
import demo.web.service.IService;
import demo.web.service.Service3;

@LSConfiguration
public class TestConfig {

    @LSBean
    public TestConfigDI getTestConfigDI(@LSAutowired("service3") IService service2, Service3 service3) {
        return new TestConfigDI(service2, service3);
    }
}
