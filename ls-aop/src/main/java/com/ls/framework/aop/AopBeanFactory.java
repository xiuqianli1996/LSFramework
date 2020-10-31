package com.ls.framework.aop;

import com.ls.framework.aop.annotation.LSAspect;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.common.exception.LSException;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.factory.AbstractAnnotationBeanFactory;
import com.ls.framework.ioc.kit.BeanDefinitionKit;

/**
 * 加载只有LSAspect注解的类
 */
public class AopBeanFactory extends AbstractAnnotationBeanFactory<LSAspect> {

    @Override
    protected BeanDefinition getBeanDefinition(LSAspect annotation, Class<?> clazz, ApplicationContext context, BeanContainer beanContainer) {
        if (!AopAction.class.isAssignableFrom(clazz)) {
            throw new LSException("The class is not assignable from AopAction, can not annotated by LSAspect, class:" + clazz.getName());
        }
        if (clazz.isAnnotationPresent(LSBean.class)) {
            // 如果已经被LSBean注解的忽略
            return null;
        }
        return BeanDefinitionKit.from(clazz, clazz.getSimpleName(), true);
    }
}
