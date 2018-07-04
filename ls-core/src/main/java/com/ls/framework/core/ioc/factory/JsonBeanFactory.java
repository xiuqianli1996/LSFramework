package com.ls.framework.core.ioc.factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.ioc.BeanInfo;
import com.ls.framework.core.exception.DiException;
import com.ls.framework.core.utils.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonBeanFactory implements BeanFactory {

    private List<BeanInfo> beanInfoList = null;
    private final static Pattern BEAN_REF_PATTERN = Pattern.compile("\\$\\{(\\w+)\\}"); //对象关系占位符正则匹配

    private void initBeanInfoList(String configPath) {
        InputStream inputStream = ClassUtil.getClassLoader().getResourceAsStream(configPath);
        if (inputStream == null) {
            return;
        }
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (buffer.length > 0) {
            beanInfoList = new Gson().fromJson(new String(buffer), new TypeToken<List<BeanInfo>>(){}.getType()); // gson解析BeanInfo集合
        }

    }

    @Override
    public void loadBean(Set<Class<?>> classSet) {
        String configPath = PropKit.get(Constants.CONFIG_BEANS_CONFIG);
        if (StringKit.isBlank(configPath)) {
            throw new LSException("beansConfig path is null, can not load bean");
        }
        initBeanInfoList(configPath);
        if (CollectionKit.isEmptyCollection(beanInfoList))
            return;
        beanInfoList.forEach(this::addBeanByBeanInfo);
    }

    private void addBeanByBeanInfo(BeanInfo beanInfo) {
        String className = beanInfo.getClassName();
        String beanName = beanInfo.getName();

        try {
            Class<?> clazz = Class.forName(className);

            Object instance = injectProperty(beanInfo, clazz);

            BeanContainer.put(beanName, instance); // 按定义的名字存一遍
            BeanContainer.put(className, instance);// 再按类名存一遍

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 根据properties的内容进行成员变量注入
     * @param beanInfo
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    private Object injectProperty(BeanInfo beanInfo, Class<?> clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        Object instance = getInstanceByConstructor(beanInfo.getConstructor(), clazz);

        Map<String, String> propertiesMap = beanInfo.getProperties();

        if (instance == null) {
            instance = clazz.newInstance(); //尝试根据构造函数新建实例失败直接调用无参构造函数
        }

        //进行成员变量注入
        if (propertiesMap != null && propertiesMap.size() > 0) {
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                if (Modifier.isFinal(field.getModifiers())) {
                    throw new DiException(clazz.getName() + ":" + field.getName());
                }
                field.set(instance, getPropertyObj(value, field.getType()));
            }
        }

        return instance;
    }

    /**
     * 尝试根据constructor内容进行构造函数注入并返回实例，constructor内参数长度、顺序必须和某一构造函数一致
     * @param constructorParamList
     * @param clazz
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Object getInstanceByConstructor(List<String> constructorParamList, Class<?> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        int constructorParamCount = constructorParamList == null ? 0 : constructorParamList.size();
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
                    Class type = parameter.getType();
                    String value = constructorParamList.get(pos);
                    args[pos++] = getPropertyObj(value, type);
                }

                instance = targetConstructor.newInstance(args);
            }
        }
        return instance;
    }

    /**
     * 获取属性的对象，根据参数占位符判断是否引用其他对象对象，基本类型直接调用工具类转换成相应的类型
     * @param value
     * @param type
     * @return
     */
    private Object getPropertyObj(String value, Class type) {
        if (StringKit.isBlank(value))
            return null;
        Matcher matcher = BEAN_REF_PATTERN.matcher(value);
        if (matcher.find()) {
            String argName = matcher.group(1);
            Object arg = BeanContainer.getBean(argName);
            if (arg == null) {
                throw new RuntimeException("can not found bean by name is " + argName);
            }
            if (arg.getClass() != type) {
                throw new RuntimeException("can not found bean by name is " + argName + ",type is wrong");
            }
            return AopHelper.enhance(arg);
        } else {
            return ConvertUtil.convert(value, type);
        }
    }

}
