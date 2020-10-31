package com.ls.framework.ioc.support;

import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.listener.BeanLifeCircle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LSBean
public class DependencyInjectBeanProcessor implements BeanLifeCircle {

    @Override
    public Object postPropertiesInject(String name, Object bean, BeanContainer container) {
        ReflectKit.doWithFiled(bean.getClass(), field -> {
            LSAutowired autowired = field.getAnnotation(LSAutowired.class);
            Object obj = null;
            if (StrKit.notBlank(autowired.value())) {
                obj = container.getBean(autowired.value());
                log.debug("find autowired bean by name: {}, bean class name: {}", autowired.value(), obj);
            } else {
                obj = container.getBean(field.getType());
                log.debug("find autowired bean by class: {}, bean: {}", field.getType(), obj);
            }
            try {
                ReflectKit.setFieldValue(bean, field, obj);
            } catch (Exception e) {
                log.error("please check autowired field type, class: " + bean.getClass().getName() + ", field: " + field.getName());
                throw e;
            }
        }, field -> field.isAnnotationPresent(LSAutowired.class));
        return bean;
    }

}
