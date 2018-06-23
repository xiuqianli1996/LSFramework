package com.ls.framework.web.resolver;

import com.ls.framework.web.handler.ActionHandler;
import com.ls.framework.web.resolver.view.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

public class ViewResolversContainer {
    private static List<ViewResolver> viewResolverList = new LinkedList<>();

    public static void add(ViewResolver viewResolver) {
        viewResolverList.add(viewResolver);
    }

    public static void handle(ActionHandler actionHandler, Object result, HttpServletRequest request, HttpServletResponse response) {

        for (ViewResolver viewResolver : viewResolverList) {
            if (viewResolver.filter(actionHandler, result, request, response)) {
                viewResolver.handle(actionHandler, result, request, response);
                break;
            }
        }
    }
}
