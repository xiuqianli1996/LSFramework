package com.ls.framework.context;

import com.ls.framework.aop.Enhancer;
import com.ls.framework.ioc.BeanFactory;
import com.ls.framework.utils.PropKit;
import demo.controller.OneController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ApplicationContext {

    public ApplicationContext(String configLocation) {
        //加载配置文件
        PropKit.use(configLocation);

        //初始化Bean容器
        try {
            BeanFactory.getInstance().init(PropKit.get("app.scanPackage"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // AOP强化
        Enhancer.enhanceAll();
    }

    public <T> T getBean(Class<T> clazz) {
        return BeanFactory.getInstance().getBean(clazz);
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ApplicationContext applicationContext = new ApplicationContext("application.properties");
        OneController oneController = applicationContext.getBean(OneController.class);
//        oneController.test();
        Method testMethod = oneController.getClass().getMethod("test");
        testMethod.invoke(oneController);
//        Field[] fields = oneController.getClass().getDeclaredFields();
//        Method[] methods = oneController.getClass().getDeclaredMethods();
//        for (Field field : fields) {
//            System.out.println(field.getName());
//        }
//        for (Method method : methods) {
//            System.out.println(method.getName());
//        }

    }

}
