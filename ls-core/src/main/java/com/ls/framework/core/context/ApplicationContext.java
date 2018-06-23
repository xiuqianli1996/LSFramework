package com.ls.framework.core.context;

import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.aop.AopLoader;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.ioc.BeanHelper;
import com.ls.framework.core.ioc.DependencyInjector;
import com.ls.framework.core.ioc.IocLoader;
import com.ls.framework.core.ioc.factory.AnnotationBeanFactory;
import com.ls.framework.core.ioc.factory.BeanFactory;
import com.ls.framework.core.ioc.factory.JsonBeanFactory;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.core.utils.StringKit;
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
        String scanPackage = PropKit.get("app.scanPackage");
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
                .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                .map(BeanHelper::getInstance)
                .filter(Objects::nonNull) //过滤掉创建实例失败的Loader
                .map(o -> (Loader) o)
                .sorted(Comparator.comparingInt(Loader::getLevel)) //对所有loader根据level升序排序
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
