package com.ls.framework.ioc.definition;

import com.ls.framework.common.exception.LSException;
import com.ls.framework.ioc.BeanContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Data
@RequiredArgsConstructor
public class BaseBeanDefinition implements BeanDefinition {
    private Object bean;

    private final String name;

    private final Class<?> beanClass;

    private final boolean singleton;

    private ThreadLocal<Boolean> initializing = ThreadLocal.withInitial(() -> false);

    public BaseBeanDefinition(Object bean, String name, Class<?> beanClass, boolean singleton) {
        this.bean = bean;
        this.name = name;
        this.beanClass = beanClass;
        this.singleton = singleton;
    }

    @Override
    public Object getBean(BeanContainer container) {
        if (singleton) {
            // 如果是单例
            if (bean == null) {
                bean = initialize(container);
            }
            return bean;
        }
        // 否则每次获取新实例
        return initialize(container);
    }

    private Object initialize(BeanContainer container) {
        if (initializing.get()) {
            // 如果还在实例化的时候又被调用就认为是发生了循环依赖， todo: 确认是否存在某些边界情况？
            throw new LSException("circle dependency, name: " + name);
        }
        try {
            initializing.set(true);
            Object instance = container.beforeInitialize(this);
            if (instance == null) {
                instance = newBeanInstance(container);
            }
            instance = container.postPropertiesInject(this, instance);
            instance = container.afterPropertiesInject(this, instance);
            instance = container.afterInitialize(this, instance);
            return instance;
        } finally {
            initializing.remove();
        }
    }

    protected Object newBeanInstance(BeanContainer container) {
        return null;
    }

}
