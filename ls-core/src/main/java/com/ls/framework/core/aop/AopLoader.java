package com.ls.framework.core.aop;

import com.ls.framework.core.annotation.LSAround;
import com.ls.framework.core.annotation.LSAspect;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSLoader;
import com.ls.framework.core.constant.Constants;
import com.ls.framework.core.ioc.BeanContainer;
import com.ls.framework.core.loader.Loader;
import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.core.utils.CollectionKit;
import com.ls.framework.core.utils.StringKit;

import java.util.Map;
import java.util.Set;

@LSLoader(100)
public class AopLoader implements Loader {
    @Override
    public void doLoad(Set<Class<?>> classSet) {
        // AOP强化
        createChainsByAopAction(classSet);
        createChainsByAnnotation(classSet);
        enhanceBeanContainer();
    }

    private void enhanceBeanContainer() {
        Map<String, Object> beanMap = BeanContainer.getBeanMap();
        //对bean容器里的bean进行强化（生成代理对象）
        for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
            Object obj = entry.getValue();
            // || !obj.getClass().isAnnotationPresent(LSBean.class)
            Class<?> clazz = obj.getClass();
            if (clazz.isAssignableFrom(AopAction.class)) {
                continue; //不强化Aop拦截类
            }
            if (!clazz.isAnnotationPresent(LSBean.class)
                    && CollectionKit.isEmptyCollection(AopContainer.getClassAopActionChain(clazz))) {
                continue;//不是LSBean注解的类，且切面列表为空的类不加强
            }
            Object enhanceInstance = AopHelper.enhance(obj);
            beanMap.put(entry.getKey(), enhanceInstance);
        }
    }

    private void createChainsByAnnotation(Set<Class<?>> classSet) {
        //扫描LSAround注解的类
        Set<Class<?>> classes = ClassUtil.getClassesByAnnotation(classSet, LSAround.class);
        for (Class<?> clazz : classes) {
            if (!BeanContainer.containsKey(clazz.getName()))
                continue;
            LSAround lsAround = clazz.getAnnotation(LSAround.class);
            for (Class<?> aopActionClass : lsAround.value())
                AopHelper.addClassAopAction(clazz, aopActionClass);
        }
    }

    private void createChainsByAopAction(Set<Class<?>> classSet) {
        //扫描所有继承AopAction的类，如果有LSAspect注解就添加到缓存
        Set<Class<?>> aopActionClasses = ClassUtil.getClassesBySuper(classSet, AopAction.class);
        for (Class<?> clazz : aopActionClasses) {
            if (!clazz.isAnnotationPresent(LSAspect.class) || !clazz.isAnnotationPresent(LSBean.class)) {
                continue;
            }

            LSAspect aspect = clazz.getAnnotation(LSAspect.class);
            String pkg = aspect.value();
            String cls = aspect.cls();

            if (StringKit.notBlank(cls)) {
                String clsName = pkg + "." + cls;
                try {
                    Class<?> targetClass = Class.forName(clsName);
                    AopHelper.addClassAopAction(targetClass, clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                ClassUtil.getClassesByPkg(classSet, pkg).forEach(targetClass -> {
                    AopHelper.addClassAopAction(targetClass, clazz);
                });
            }
        }
    }


}
