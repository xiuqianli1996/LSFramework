package com.ls.framework.core.ioc;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.exception.DiException;
import com.ls.framework.core.utils.StringKit;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DependencyInjector {

    public static void injectAll() {
        for (Object obj : BeanContainer.allBeans()) {
            inject(obj);
        }
    }


    public static void inject(Object obj) {
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(LSAutowired.class)) {
                continue;
            }
            if (Modifier.isFinal(field.getModifiers())) {
                throw new DiException(clazz.getName() + ":" + field.getName());
            }

            LSAutowired lsAutowired = field.getAnnotation(LSAutowired.class);
            String beanName = lsAutowired.value();
            if (StringKit.isBlank(beanName)) {
                beanName = field.getType().getName();
            }
            Object val = BeanContainer.getBean(beanName);
            if (val == null) {
                throw new DiException(beanName + "is not found in bean container");
            }
            field.setAccessible(true);
            try {
                field.set(obj, AopHelper.enhance(val)); //注入Aop强化后的对象
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new DiException(e.getMessage());
            }
        }
    }
}
