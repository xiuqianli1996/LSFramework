package com.ls.framework.core.context;

import com.ls.framework.core.annotation.LSLoader;
import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Modifier;
import java.util.*;

public class ApplicationContext {

    private static final Logger logger = Logger.getLogger(ApplicationContext.class);

    public ApplicationContext(String configLocation) {
        //加载配置文件
        PropKit.use(configLocation);
    }

    public void init() {

        Set<Class<?>> classSet = initClassSet();
        initLoader(classSet);

    }

    private Set<Class<?>> initClassSet() {
        String scanPackage = PropKit.get(Constants.CONFIG_SCAN_PACKAGE);
        if (StringKit.isBlank(scanPackage)) {
            throw new RuntimeException("scan package can not be null");
        }
        scanPackage = "com.ls.framework," + scanPackage; //默认扫描框架包名
        Set<Class<?>> classSet = new HashSet<>(); //扫描配置包名下的所有类（递归）
        String[] pkgNames = scanPackage.split(",");
        for (String packageName : pkgNames) {
            Set<Class<?>> list = ClassUtil.scanClassListByPkg(packageName);
            if (!CollectionKit.isEmptyCollection(list))
                classSet.addAll(list);
        }
        return classSet;
    }

    private void initLoader(Set<Class<?>> classSet) {
        ClassUtil.getClassesByInterface(classSet, Loader.class)
                .stream()
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers())) //排除抽象类
                .filter(clazz -> clazz.isAnnotationPresent(LSLoader.class)) //只加载LSLoader注解的类
                .sorted((clazz1, clazz2) -> {
                    LSLoader lsLoader1 = clazz1.getAnnotation(LSLoader.class);
                    LSLoader lsLoader2 = clazz2.getAnnotation(LSLoader.class);
                    return lsLoader1.value() - lsLoader2.value();
                }) //对所有loader根据level升序排序
                .map(ObjectKit::getInstance)
                .filter(Objects::nonNull) //过滤掉创建实例失败的Loader
                .map(o -> (Loader) o)
                .forEach(loader -> {
                    Logger log = Logger.getLogger(loader.getClass());
                    log.info("loading...");
                    loader.doLoad(classSet);
                    log.info("load success");
        });

    }

    public <T> T getBean(Class<T> clazz) {
        return BeanContainer.getBean(clazz);
    }

    public <T> T getBean(String name) {
        return BeanContainer.getBean(name);
    }

}
