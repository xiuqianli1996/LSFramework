package com.ls.framework.core.ioc;

import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.factory.AnnotationBeanFactory;
import com.ls.framework.core.ioc.factory.ConfigurationBeanFactory;
import com.ls.framework.core.ioc.factory.JsonBeanFactory;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.core.utils.StringKit;

import java.util.Set;

public class IocLoader implements Loader {
    @Override
    public void doLoad(Set<Class<?>> classSet) {
        //初始化Bean容器
        try {
            new AnnotationBeanFactory().loadBean(classSet);//扫描class根据注解加载
            new ConfigurationBeanFactory().loadBean(classSet); //根据LSConfiguration注解的类里被LSBean修饰的方法加载bean
            String beansConfig = PropKit.get("app.beansConfig");
            if (StringKit.isBlank(beansConfig)) {
                throw new LSException("beansConfig path is null, can not load bean");
            }
            new JsonBeanFactory().loadBean(beansConfig); //从json文件加载bean
        } catch (Exception e) {
            e.printStackTrace();
        }
        DependencyInjector.injectAll(); //依赖注入
    }

    @Override
    public int getLevel() {
        return Constants.IOC_LOADER_LEVEL;
    }
}
