package com.ls.framework.web.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext {
    private static ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    public static HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    public static void setRequest(HttpServletRequest value) {
        requestThreadLocal.set(value);
    }

    public static HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    public static void setResponse(HttpServletResponse value) {
        responseThreadLocal.set(value);
    }
}
