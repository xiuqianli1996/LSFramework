package com.ls.framework.web;

import com.ls.framework.core.annotation.LSController;
import com.ls.framework.core.context.ApplicationContext;
import com.ls.framework.core.utils.ClassUtil;

import java.util.List;

public class WebApplicationContext extends ApplicationContext {
    public WebApplicationContext(String configLocation) {
        super(configLocation);
    }

    private void initMapping() {
        List<Class<?>> controllerClassList = ClassUtil.getClassesByAnnotation(LSController.class);

    }
}
