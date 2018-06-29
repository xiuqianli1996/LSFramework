package com.ls.framework.core.ioc;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.exception.DiException;
import com.ls.framework.core.utils.StringKit;

import java.lang.reflect.*;

public class DependencyInjector {

    /**
     * 遍历bean容器里的所有对象，依次注入
     */
    public static void injectAll() {
        DependencyInjector injector = new DependencyInjector();
        for (Object obj : BeanContainer.allBeans()) {
            injector.inject(obj);
        }
    }


    public void inject(Object obj) {
        Class<?> clazz = obj.getClass();
        injectByField(clazz, obj);
        injectBySetMethod(clazz, obj);
    }

    /**
     * 遍历成员变量进行注入
     * @param clazz
     * @param obj
     */
    private void injectByField(Class<?> clazz, Object obj) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(LSAutowired.class)) {
                continue;
            }

            if (Modifier.isFinal(field.getModifiers())) {
                throw new DiException(clazz.getName() + ":" + field.getName());
            }


            try {
                field.setAccessible(true);
//                if (field.get(obj) != null) {
//                    continue;//已经有值不覆盖注入
//                }

                LSAutowired lsAutowired = field.getAnnotation(LSAutowired.class);
                Object val = getInjectVal(lsAutowired, field.getType().getName());
                field.set(obj, val); //注入Aop强化后的对象
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new DiException(e.getMessage());
            }
        }
    }

    /**
     * 必须是public set开头 只有一个参数 LSAutowired注解的方法才会调用注入
     * @param clazz
     * @param obj
     */
    private void injectBySetMethod(Class<?> clazz, Object obj) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.getName().startsWith("set") || method.getParameterCount() != 1) {
                continue;
            }
            if (!method.isAnnotationPresent(LSAutowired.class)) {
                continue;
            }

            LSAutowired lsAutowired = method.getAnnotation(LSAutowired.class);
            Class<?> paramClass = method.getParameterTypes()[0];
            Object val = getInjectVal(lsAutowired, paramClass.getName());

            try {
                method.invoke(obj, val);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new DiException(e.getMessage());
            }
        }
    }

    /**
     * 尝试根据LSAutowired给的beanName从容器里获取对象，为空就按类名获取
     * @param lsAutowired
     * @param className
     * @return
     */
    private Object getInjectVal(LSAutowired lsAutowired, String className) {
        String beanName = lsAutowired.value();
        if (StringKit.isBlank(beanName)) {
            beanName = className;
        }
        Object val = BeanContainer.getBean(beanName);
        if (val == null) {
//            throw new DiException(beanName + "is not found in bean container");
            return null;
        }
        return AopHelper.enhance(val);
    }
}
