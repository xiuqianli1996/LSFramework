package demo.controller;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSClear;
import com.ls.framework.web.annotation.LSPathParam;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.annotation.LSRequestParam;
import demo.controller.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@LSBean
@LSRequestMapping("////one/{asd}")
@LSAround({Action.class, Action3.class})
public class OneController {

    @LSAutowired
    Service service;

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

    @LSRequestMapping("/{qwe}")
    public void testAction2(HttpServletResponse response, @LSRequestParam("a") int a
            , @LSRequestParam("b") int b, @LSPathParam("qwe") String qwe) throws IOException {
        response.getWriter().write(a + b + " === " + qwe);
    }

}
