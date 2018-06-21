package com.ls.framework.core.context;

import com.ls.framework.core.aop.AopHelper;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.ioc.DependencyInjector;
import com.ls.framework.core.ioc.factory.AnnotationBeanFactory;
import com.ls.framework.core.ioc.factory.BeanFactory;
import com.ls.framework.core.ioc.factory.JsonBeanFactory;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.core.utils.StringKit;

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
        String scanPackage = PropKit.get("app.scanPackage");
        if (StringKit.isBlank(scanPackage)) {
            throw new RuntimeException("scan package can not be null");
        }
        scanPackage = "com.ls.framework," + scanPackage; //默认扫描框架包名
		ClassUtil.init(scanPackage); //扫描配置包名下的所有类（递归）
	}



}
