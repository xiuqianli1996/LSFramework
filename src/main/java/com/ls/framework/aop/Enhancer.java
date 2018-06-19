package com.ls.framework.aop;

import com.ls.framework.ioc.BeanFactory;

import java.util.Map;

public class Enhancer {

    public static <T> T enhance(T obj) {
        return (T) net.sf.cglib.proxy.Enhancer.create(obj.getClass(), new AopCallback(obj));
    }

    public static void enhanceAll() {
        Map<String, Object> beanMap = BeanFactory.getInstance().getBeanMap();
        System.out.println("---------- Aop enhancing ------------");
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object enhanceInstance = enhance(entry.getValue());
            beanMap.put(entry.getKey(), enhanceInstance);
//            BeanFactory.getInstance().getBean(entry.getKey());
        }
        System.out.println("---------- Aop enhancing success ------------");
    }

}
