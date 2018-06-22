package demo.controller;

import demo.controller.service.IService;
import demo.controller.service.Service2;
import demo.controller.service.Service3;

public class TestConfigDI {

    private IService service2;
    private IService service3;

    public TestConfigDI(IService service2, IService service3) {
        this.service2 = service2;
        this.service3 = service3;
    }

    public void test() {
        service2.test();
        service3.test();
    }
}
