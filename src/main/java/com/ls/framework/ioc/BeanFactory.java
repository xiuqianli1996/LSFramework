package com.ls.framework.ioc;

import com.ls.framework.annotation.LSBean;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.ObjectKit;
import com.ls.framework.utils.StringKit;
import com.ls.framework.annotation.LSController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {

    private Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private Map<String, Object> enhancedBeanMap = new ConcurrentHashMap<>();
    private static final BeanFactory INSTANCE = new BeanFactory();

    private BeanFactory() {

    }

    public static BeanFactory getInstance() {
        return INSTANCE;
    }

    public void init(String basePackage) throws IllegalAccessException, InstantiationException {
        System.out.println("------- Bean Container init running ---------");
        List<String> classNames = ClassUtil.scanClassNamesByPkg(basePackage);
        if (ObjectKit.isEmptyList(classNames)) {
            return;
        }
        for (String className : classNames) {
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (Exception e) {
//                e.printStackTrace();
                continue;
            }
            if (clazz.isAnnotationPresent(LSController.class)) {
                beanMap.put(className, clazz.newInstance());
            } else if (clazz.isAnnotationPresent(LSBean.class)) {
                Object instance = clazz.newInstance();
                beanMap.put(className, instance); //先按类名存一次
                LSBean lsBean = clazz.getAnnotation(LSBean.class);
                String name = lsBean.value().trim();
                if (StringKit.notBlank(name)) {
                    beanMap.put(name, instance); //再按注解名存一次
                }

                Class<?>[] interfaces = clazz.getInterfaces();

                if (!ObjectKit.isEmptyArray(interfaces)) {//再按实现的接口的类名存一次
                    for (Class interfaceClass : interfaces) {
                        beanMap.put(interfaceClass.getName(), instance);
                    }
                }
            }
        }
        injectDependencies();
        System.out.println("------- Bean Container init success ---------");
    }

    public <T> T getBean(String key) {
        return (T) beanMap.get(key);
    }

    public <T> T getBean(Class<T> clazz) {
        return getBean(clazz.getName());
    }

    private void injectDependencies() {
        for (Object obj : beanMap.values()) {
            DependencyInjector.inject(obj);
        }
    }

    public Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public void setBean(String key, Object obj) {
        beanMap.put(key, obj);
    }
}
