package com.ls.framework.ioc.factory;

import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.kit.BeanDefinitionKit;

public class LSBeanAnnotationBeanFactory extends AbstractAnnotationBeanFactory<LSBean> {

    @Override
    protected BeanDefinition getBeanDefinition(LSBean annotation, Class<?> clazz, ApplicationContext context, BeanContainer beanContainer) {
        String name = StrKit.orDefault(annotation.value(), () -> StrKit.firstCharToLowerCase(clazz.getSimpleName()));
        return BeanDefinitionKit.from(clazz, name, annotation.singleton());
    }
}
