package com.ls.framework.core.ioc.factory;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.ObjectKit;

import java.util.Set;

public class AnnotationBeanFactory implements BeanFactory {

    @Override
    public void loadBean(Set<Class<?>> classSet) {
        if (CollectionKit.isEmptyCollection(classSet)) {
            return;
        }

        //加载LSBean注解的类
        ClassUtil.getClassesByAnnotation(classSet, LSBean.class)
                .forEach(clazz -> {
                    Object instance = ObjectKit.getInstance(clazz);
                    LSBean lsBean = clazz.getAnnotation(LSBean.class);
                    BeanContainer.putBeanByAnnotation(clazz, lsBean, instance);
                });

    }

}
