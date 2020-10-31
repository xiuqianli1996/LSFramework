package com.demo.controller;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSClear;
import com.ls.framework.web.annotation.LSPathParam;
import com.ls.framework.web.annotation.LSRequestMapping;
import com.ls.framework.web.annotation.LSRequestParam;
import com.ls.framework.web.annotation.LSResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@LSBean
@LSRequestMapping("////one")
public class OneController {

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

        response.getWriter().write(a + b + " === " + qwe);
    }

    @LSRequestMapping("/testJson")
    @LSResponseBody
    public Map<String, String> testJson(@LSRequestParam("name") String name, HttpServletResponse response) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("user", "1232");
        return map;
    }

    @LSRequestMapping("/testJsp")
    public String testJsp(@LSRequestParam(value = "name", require = false) String name, HttpServletRequest request) {
        request.setAttribute("name", name);
        System.out.println(name);
        return "test";
    }

}
