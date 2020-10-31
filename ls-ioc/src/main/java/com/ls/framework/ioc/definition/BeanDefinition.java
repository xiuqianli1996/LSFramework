package com.ls.framework.ioc.definition;

import com.ls.framework.ioc.BeanContainer;

public interface BeanDefinition {
    String getName();

    /**
     * @param container bean 容器，用来获取构造bean实例时需要的依赖对象
     * @return
     */
    Object getBean(BeanContainer container);

    void setBean(Object bean);

    Class<?> getBeanClass();

    boolean isSingleton();
}
