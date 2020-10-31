package com.ls.framework.core.property;

import java.util.Properties;

public abstract class AbstractStringPropertySource implements PropertySource<String> {

    protected Properties properties;

    protected int order = Integer.MAX_VALUE;

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String getOrDefault(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }

    @Override
    public int order() {
        return order;
    }
}
