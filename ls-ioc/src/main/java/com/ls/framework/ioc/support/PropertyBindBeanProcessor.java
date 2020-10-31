package com.ls.framework.ioc.support;

import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.annotation.LSProperty;
import com.ls.framework.core.annotation.LSValue;
import com.ls.framework.core.listener.ApplicationContextListener;
import com.ls.framework.core.property.FilePropertySource;
import com.ls.framework.core.property.PropertySource;
import com.ls.framework.core.property.PropertySourceFactory;
import com.ls.framework.core.property.binder.PropertyBinderService;
import com.ls.framework.core.resource.Resource;
import com.ls.framework.core.resource.ResourceFactory;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.listener.BeanLifeCircle;

@LSBean
public class PropertyBindBeanProcessor implements BeanLifeCircle, ApplicationContextListener {

    private static PropertySourceFactory propertySourceFactory;

    @Override
    public Object postPropertiesInject(String name, Object bean, BeanContainer container) {
        Class<?> clazz = bean.getClass();
        String prefix = "";
        if (clazz.isAnnotationPresent(LSProperty.class)) {
            processWithLSPropertyAnnotation(clazz, bean);
            return bean;
        }
        PropertySource propertySource = propertySourceFactory;
        PropertyBinderService.sharedBinder().bind(bean, propertySource, prefix, field -> field.isAnnotationPresent(LSValue.class));

        return bean;
    }

    /**
     * 处理LSProperty注解的类，一般是绑定配置的pojo类
     * @param clazz
     * @param bean
     */
    private void processWithLSPropertyAnnotation(Class<?> clazz, Object bean) {
        LSProperty property = clazz.getAnnotation(LSProperty.class);
        PropertySource propertySource = propertySourceFactory;

        if (!StrKit.isBlank(property.location())) {
            Resource resource = ResourceFactory.getResource(property.location());
            propertySource = new FilePropertySource(resource);
        }

        PropertyBinderService.sharedBinder().bind(bean, propertySource, property.prefix(), field -> true);
    }

    @Override
    public void setPropertySourceFactory(PropertySourceFactory propertySourceFactory) {
        this.propertySourceFactory = propertySourceFactory;
    }

    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }


}
