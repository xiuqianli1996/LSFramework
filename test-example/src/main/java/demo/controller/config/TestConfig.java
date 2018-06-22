package demo.controller.config;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import com.ls.framework.core.annotation.LSParam;
import demo.controller.OneController;
import demo.controller.TestConfigDI;
import demo.controller.service.IService;
import demo.controller.service.Service2;
import demo.controller.service.Service3;

@LSConfiguration
public class TestConfig {

    @LSBean
    public TestConfigDI getTestConfigDI(@LSParam("service3") IService service2, Service3 service3) {
        return new TestConfigDI(service2, service3);
    }
}
