package com.ls.framework.ioc.factory;

import com.ls.framework.annotation.LSBean;
import com.ls.framework.annotation.LSController;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.CollectionKit;
import com.ls.framework.utils.StringKit;

import java.util.List;

public class AnnotationBeanFactory implements BeanFactory {
    @Override
    public void loadBean() {
        System.out.println("------- Bean Container init running ---------");
        List<Class<?>> classes = ClassUtil.getAllClasses();
        if (CollectionKit.isEmptyList(classes)) {
            return;
        }
        for (Class<?> clazz : classes) {
            String className = clazz.getName();
            if (clazz.isAnnotationPresent(LSController.class)) {
                BeanContainer.put(className, getInstance(clazz));
            } else if (clazz.isAnnotationPresent(LSBean.class)) {
                Object instance = getInstance(clazz);
                BeanContainer.put(className, instance); //先按类名存一次
                LSBean lsBean = clazz.getAnnotation(LSBean.class);
                String name = lsBean.value().trim();
                if (StringKit.notBlank(name)) {
                    BeanContainer.put(name, instance); //再按注解名存一次
                }

                Class<?>[] interfaces = clazz.getInterfaces();

                if (!CollectionKit.isEmptyArray(interfaces)) {//再按实现的接口的类名存一次
                    for (Class interfaceClass : interfaces) {
                        BeanContainer.put(interfaceClass.getName(), instance);
                    }
                }
            }
        }
        System.out.println("------- Bean Container init success ---------");
    }

    private Object getInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
