package com.ls.framework.web.utils;

import com.ls.framework.web.annotation.LSResponseBody;
import com.ls.framework.web.handler.ActionHandler;

import java.lang.reflect.Method;

public class ControllerKit {

    public static boolean isResponseBody(ActionHandler actionHandler) {
        Method actionMethod = actionHandler.getActionMethod();
        Class<?> controllerClass = actionHandler.getControllerClass();
        return actionMethod.isAnnotationPresent(LSResponseBody.class)
                || controllerClass.isAnnotationPresent(LSResponseBody.class);
    }

}
