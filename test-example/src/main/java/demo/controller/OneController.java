package demo.controller;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSClear;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.web.annotation.LSPathParam;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.annotation.LSRequestParam;
import com.ls.framework.web.annotation.LSResponseBody;
import demo.controller.exception.GlobalException;
import demo.controller.exception.GlobalException2;
import demo.controller.service.IService;
import demo.controller.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@LSBean
@LSRequestMapping("////one")
@LSAround({Action.class, Action3.class})
public class OneController {

    @LSAutowired
    Service service;

    private IService service4;

    @LSAround(Action2.class)
    @LSClear(Action3.class)
    public void test() {
        if (service == null) {
            System.out.println("is null");
            return;
        }
        service.test();
//        service.test2();
    }

    @LSAround(Action2.class)
    public void test2() {
        System.out.println("is test2");
    }

    @LSRequestMapping("//////test/{id}")
    public void testAction(HttpServletResponse response) throws IOException {
        response.getWriter().write("123");
    }

    @LSRequestMapping("/te/{qwe}")
    public void testAction2(HttpServletResponse response, @LSRequestParam("a") int a
            , @LSRequestParam("b") int b, @LSPathParam("qwe") String qwe) throws IOException {

        response.getWriter().write(a + b + " === " + service4.test() + qwe);
    }

    @LSRequestMapping("/testJson")
    @LSResponseBody
    public TestBean testJson(@LSRequestParam("name") String name, HttpServletResponse response) throws IOException {
//        response.getWriter().write("hello," + name);
        TestBean testBean = BeanContainer.getBean(TestBean.class);
        System.out.println(testBean);
//        TestBean testBean = new TestBean();
//        testBean.setVal1(12);
//        testBean.setVal2(name);
        return testBean;
    }

    @LSRequestMapping("/testJsp")
    public String testJsp(@LSRequestParam(value = "name", require = false) String name, HttpServletRequest request) {
        request.setAttribute("name", name);
        System.out.println(name);
        return "test";
    }

    @LSAutowired("service4")
    public void setService4(IService service4) {
        this.service4 = service4;
    }
}
