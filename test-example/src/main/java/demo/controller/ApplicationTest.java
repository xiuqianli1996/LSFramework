package demo.controller;


import com.ls.framework.core.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;

public class ApplicationTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ApplicationContext applicationContext = new ApplicationContext("application.properties");
//        OneController oneController = applicationContext.getBean(OneController.class);
//        oneController.test();
        TestBean2 testBean = applicationContext.getBean(TestBean2.class);
//        System.out.println(testBean);
        testBean.test();
//        System.out.println(oneController);
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
