package com.ls.framework.web;

import com.ls.framework.core.context.ApplicationContext;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.core.utils.StringKit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

public class LSContextListner implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        String configLocation = servletContext.getInitParameter("contextConfigLocation");
        ApplicationContext applicationContext = new ApplicationContext(configLocation);
        applicationContext.init();

        registerDefaultServlet(servletContext);
    }

    private void registerDefaultServlet(ServletContext context) {
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping(PropKit.get("app.favicon", "/favicon.ico"));
        String wwwPath = PropKit.get("app.resources", "/static/");
        if (StringKit.notBlank(wwwPath)) {
            defaultServlet.addMapping(wwwPath + "*");
        }
    }
}
