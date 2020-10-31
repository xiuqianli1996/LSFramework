package com.ls.framework.ioc.factory;

import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.definition.BeanDefinition;

import java.util.List;
import java.util.Set;

/**
 * 可以通过实现BeanFactory接口自定义bean加载, 在这里面不能使用依赖注入之类的功能
 */
public interface BeanFactory {
    List<BeanDefinition> loadBeanDefinition(ApplicationContext context, BeanContainer beanContainer, Set<Class<?>> classSet);
}
