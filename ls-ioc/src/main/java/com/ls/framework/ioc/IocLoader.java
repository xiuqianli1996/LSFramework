package com.ls.framework.ioc;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.definition.BaseBeanDefinition;
import com.ls.framework.ioc.factory.BeanFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class IocLoader implements Loader {

    public static final int ORDER = 0;

    @Override
    public void doLoad(ApplicationContext context, Set<Class<?>> classSet) {

        BeanContainer container = BeanContainer.sharedContainer();

        // 先把所有ApplicationContext注册到容器中
        container.register(new BaseBeanDefinition(context, "applicationContext", ApplicationContext.class, true));

        // 尝试把启动类sourceClass注册到容器中
        if (context.getSourceClass() != null) {
            Class<?> sourceClass = context.getSourceClass();
            Object sourceClassInstance = ObjectKit.getInstance(sourceClass);
            container.register(new BaseBeanDefinition(sourceClassInstance, StrKit.firstCharToLowerCase(sourceClass.getSimpleName()),sourceClass, true));
        }
//        context.getApplicationContextListeners().forEach(listener -> {
//            BaseBeanDefinition beanDefinition = new BaseBeanDefinition(listener, listener.getClass().getName(), listener.getClass(), true);
//            container.register(beanDefinition);
//        });
        // 加载bean信息
        loadBeanDefinition(container, context, classSet);
        // 注册listener
        container.initBeanLifeCircles();
        initBeanInstance(container);
    }


    private void loadBeanDefinition(BeanContainer container, ApplicationContext context, Set<Class<?>> classSet) {
        List<BeanDefinition> beanDefinitions = new LinkedList<>();

        ObjectKit.getInstancesByInterface(classSet, BeanFactory.class)
                .forEach(beanFactory -> {
                    beanDefinitions.addAll(beanFactory.loadBeanDefinition(context, container, classSet));
                });
        beanDefinitions.forEach(container::register);
    }

    /**
     * 初始化所有单例bean实例
     */
    private void initBeanInstance(BeanContainer container) {
        container.getBeanDefinitionSet().forEach(beanDefinition -> {
            beanDefinition.getBean(container);
        });
    }

    @Override
    public int order() {
        return ORDER;
    }

}
