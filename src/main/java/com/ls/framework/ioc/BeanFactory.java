package com.ls.framework.ioc;

import com.alibaba.fastjson.JSON;
import com.ls.framework.annotation.LSBean;
import com.ls.framework.exception.DiException;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.ConvertUtil;
import com.ls.framework.utils.CollectionKit;
import com.ls.framework.utils.StringKit;
import com.ls.framework.annotation.LSController;
import org.junit.Test;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BeanFactory {

    private final static Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private final static Map<String, Object> enhancedBeanMap = new ConcurrentHashMap<>();
    private final static Pattern BEAN_REF_PATTERN = Pattern.compile("\\$\\{(\\w+)\\}");

    public static void initByConfig(String configPath) throws Exception {
        InputStream inputStream = ClassUtil.getClassLoader().getResourceAsStream(configPath);
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();

        List<BeanInfo> beanInfoList = JSON.parseArray(new String(buffer), BeanInfo.class);
        beanInfoList.forEach(BeanFactory::addBeanByBeanDefinition);
//        beanInfoList.forEach(beanDefinition -> System.out.println(beanDefinition.getConstructor()));
    }

    private static void addBeanByBeanDefinition(BeanInfo beanInfo) {
        String className = beanInfo.getClassName();
        String beanName = beanInfo.getName();
        int constructorParamCount = beanInfo.getConstructor() == null ? 0 : beanInfo.getConstructor().size();
        Map<String, String> propertiesMap = beanInfo.getProperties();
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = null;
            //尝试进行构造函数注入，构造函数参数个数必须与配置文件中的参数个数相同
            Constructor[] constructors = clazz.getDeclaredConstructors();
            if (constructorParamCount > 0 && constructors.length > 0) {
                Constructor targetConstructor = null;
                for (Constructor constructor : constructors) {
                    if (constructor.getParameterCount() == constructorParamCount) {
                        targetConstructor = constructor;
                        break;
                    }
                }
                if (targetConstructor != null) {
                    Parameter[] params = targetConstructor.getParameters();
                    Object[] args = new Object[constructorParamCount];
                    int pos = 0;
                    for (Parameter parameter : params) { //填充构造函数参数
                        String name = parameter.getName();
                        Class type = parameter.getType();
                        String value = beanInfo.getConstructor().get(pos);

                        args[pos++] = getPropertyObj(value, type);
                    }

                    instance = targetConstructor.newInstance(args);
                }

            }

            if (instance == null) {
                instance = clazz.newInstance();
            }

            //进行成员变量注入
            if (propertiesMap != null && propertiesMap.size() > 0) {
                for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                    String name = entry.getKey();
                    String value = entry.getValue();
                    Field field = clazz.getDeclaredField(name);
                    if (Modifier.isFinal(field.getModifiers())) {
                        throw new DiException(clazz.getName() + ":" + field.getName());
                    }
                    field.setAccessible(true);
                    field.set(instance, getPropertyObj(value, field.getType()));
                }
            }

            beanMap.put(beanName, instance); // 按定义的名字存一遍
            beanMap.put(className, instance);// 再按类名存一遍

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getPropertyObj(String value, Class type) {
        if (StringKit.isBlank(value))
            return null;
        Matcher matcher = BEAN_REF_PATTERN.matcher(value);
        if (matcher.find()) {
            String argName = matcher.group(1);
            Object arg = beanMap.get(argName);
            if (arg == null) {
                throw new RuntimeException("can not found bean by name is " + argName);
            }
            if (arg.getClass() != type) {
                throw new RuntimeException("can not found bean by name is " + argName + ",type is wrong");
            }
            return arg;
        } else {
            return ConvertUtil.convert(value, type);
        }
    }

    public static void initByAnnotation() throws IllegalAccessException, InstantiationException {
        System.out.println("------- Bean Container init running ---------");
        List<Class<?>> classes = ClassUtil.getAllClasses();
        if (CollectionKit.isEmptyList(classes)) {
            return;
        }
        for (Class<?> clazz : classes) {
            String className = clazz.getName();
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

                if (!CollectionKit.isEmptyArray(interfaces)) {//再按实现的接口的类名存一次
                    for (Class interfaceClass : interfaces) {
                        beanMap.put(interfaceClass.getName(), instance);
                    }
                }
            }
        }
        injectDependencies();
        System.out.println("------- Bean Container init success ---------");
    }

    public static <T> T getBean(String key) {
        return (T) beanMap.get(key);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getBean(clazz.getName());
    }

    private static void injectDependencies() {
        for (Object obj : beanMap.values()) {
            DependencyInjector.inject(obj);
        }
    }

    public static Map<String, Object> getBeanMap() {
        return beanMap;
    }

    public static void setBean(String key, Object obj) {
        beanMap.put(key, obj);
    }

    public static boolean containsKey(String key) {
        return beanMap.containsKey(key);
    }

    @Test
    public void testRefPattern() {
        Matcher matcher = BEAN_REF_PATTERN.matcher("${test}");
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));
    }
}
