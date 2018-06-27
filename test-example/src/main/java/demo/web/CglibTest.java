package demo.web;

import demo.web.service.IService;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class CglibTest {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        TestBean target = new TestBean();
//        TestBean testBean = AopHelper.enhance(target);
//        Class<?> clazz = testBean.getClass();
//        System.out.println(clazz);
//        for (Method method : clazz.getDeclaredMethods())  {
//            System.out.println(method.getName());
//        }
//        System.out.println("=====");
//        for (Field field : clazz.getDeclaredFields())  {
//            System.out.println(field.getName());
//        }
//        System.out.println("=======");
//        Field field = clazz.getDeclaredField("CGLIB$CALLBACK_0");
//        field.setAccessible(true);
//        AopCallback aopCallback = (AopCallback) field.get(testBean);
//
//        System.out.println(aopCallback.getTarget());

        IService service = (IService) Enhancer.create(IService.class, new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return "dsadsa";
            }
        });
        System.out.println(service.test());
    }

}
