package com.ls.framework.core.ioc.factory;

import java.util.Set;

public interface BeanFactory {
    void loadBean(Set<Class<?>> classSet);
    void loadBean(String configPath);
}
