package com.ls.framework.context;

import com.ls.framework.aop.AopHelper;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.DependencyInjector;
import com.ls.framework.ioc.factory.AnnotationBeanFactory;
import com.ls.framework.ioc.factory.BeanFactory;
import com.ls.framework.ioc.factory.JsonBeanFactory;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.PropKit;

public class ApplicationContext {

    private BeanFactory annotationBeanFactory, jsonBeanFactory;

    public ApplicationContext(String configLocation) {
        //加载配置文件
        PropKit.use(configLocation);
        
        //初始化工具类
        initUtils();

        //初始化Bean容器
        annotationBeanFactory = new AnnotationBeanFactory();
        jsonBeanFactory = new JsonBeanFactory(PropKit.get("app.beansConfig"));
        try {
            annotationBeanFactory.loadBean();//扫描class根据注解加载
            jsonBeanFactory.loadBean(); //从json文件加载bean
        } catch (Exception e) {
            e.printStackTrace();
        }
        DependencyInjector.injectAll(); //依赖注入

        // AOP强化
        AopHelper.enhanceAll();
    }

    public <T> T getBean(Class<T> clazz) {
        return BeanContainer.getBean(clazz);
    }

    public <T> T getBean(String name) {
        return BeanContainer.getBean(name);
    }
    
    private void initUtils() {
		ClassUtil.init(PropKit.get("app.scanPackage")); //扫描配置包名下的所有类（递归）
	}



}
