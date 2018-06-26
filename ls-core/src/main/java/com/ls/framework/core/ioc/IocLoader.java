package com.ls.framework.core.ioc;

import com.ls.framework.core.annotation.LSLoader;
import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.exception.LSException;
import com.ls.framework.core.ioc.factory.AnnotationBeanFactory;
import com.ls.framework.core.ioc.factory.ConfigurationBeanFactory;
import com.ls.framework.core.ioc.factory.JsonBeanFactory;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.core.utils.StringKit;

import java.util.Set;

@LSLoader(10)
public class IocLoader implements Loader {
    @Override
    public void doLoad(Set<Class<?>> classSet) {
        //初始化Bean容器
        try {
            new AnnotationBeanFactory().loadBean(classSet);//扫描class根据注解加载
            new ConfigurationBeanFactory().loadBean(classSet); //根据LSConfiguration注解的类里被LSBean修饰的方法加载bean
            new JsonBeanFactory().loadBean(classSet); //从json文件加载bean
        } catch (Exception e) {
            e.printStackTrace();
        }
        DependencyInjector.injectAll(); //依赖注入
    }

}
