package demo.web;


import com.ls.framework.core.utils.ClassUtil;

import java.lang.reflect.InvocationTargetException;

public class ApplicationTest {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (Class<?> clazz : ClassUtil.scanClassListByPkg("com.ls.framework")) {
            System.out.println(clazz.getName());
        }
//        Pattern pattern1 = Pattern.compile("/*");
//        Pattern pattern2 = Pattern.compile("/*");
//        System.out.println(pattern1.equals(pattern2));
//        TestBean testBean = new TestBean();
//        testBean.setVal2("dasdasdsa");
//        testBean.setVal1(45644);
//
//        ApplicationContext applicationContext = new ApplicationContext("application.properties");
//        applicationContext.init();
//        JsonEngine jsonEngine = applicationContext.getBean("DEFAULT_JSON");
//        System.out.println(new Gson().toJson(testBean));
//        OneController oneController = applicationContext.getBean(OneController.class);
////        oneController.test();
//        TestConfigDI testConfigDI = applicationContext.getBean(TestConfigDI.class);
//        testConfigDI.test();
//        TestBean2 testBean = applicationContext.getBean(TestBean2.class);
////        System.out.println(testBean);
//        testBean.test();
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
