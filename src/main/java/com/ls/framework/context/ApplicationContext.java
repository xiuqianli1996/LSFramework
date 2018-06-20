package com.ls.framework.context;

import com.ls.framework.aop.AopHelper;
import com.ls.framework.ioc.BeanFactory;
import com.ls.framework.utils.ClassUtil;
import com.ls.framework.utils.PropKit;

public class ApplicationContext {

    public ApplicationContext(String configLocation) {
        //加载配置文件
        PropKit.use(configLocation);
        
        //初始化工具类
        initUtils();

        //初始化Bean容器
        try {
            BeanFactory.initByAnnotation();//扫描class根据注解加载
            BeanFactory.initByConfig(PropKit.get("app.beansConfig")); //从json文件加载bena
        } catch (Exception e) {
            e.printStackTrace();
        }

        // AOP强化
        AopHelper.enhanceAll();
    }

    public <T> T getBean(Class<T> clazz) {
        return BeanFactory.getBean(clazz);
    }
    
    private void initUtils() {
		ClassUtil.init(PropKit.get("app.scanPackage")); //扫描配置包名下的所有类（递归）
	}



}
