package com.ls.framework.ioc;

import java.util.List;
import java.util.Map;

public class BeanInfo {


    /**
     * className : demo.controller.TestBean
     * properties : {"val1":10,"val2":"123"}
     * constructor : {"val1":10,"val2":"123"}
     */

    private String name;
    private String className;
    private Map<String, String> properties;
    private List<String> constructor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public List<String> getConstructor() {
        return constructor;
    }

    public void setConstructor(List<String> constructor) {
        this.constructor = constructor;
    }
}
