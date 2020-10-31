package com.ls.framework.aop;


import com.ls.framework.aop.annotation.LSAround;
import com.ls.framework.aop.support.AopAction;
import com.ls.framework.aop.support.AopContainer;
import com.ls.framework.aop.support.AopProxy;
import com.ls.framework.common.bean.Holder;
import com.ls.framework.common.kit.CollectionKit;
import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.listener.BeanLifeCircle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@LSBean
public class AopEnhanceBeanProcessor implements BeanLifeCircle {

    @Override
    public Object afterInitialize(String name, Object bean, boolean singleton, BeanContainer container) {
        Class<?> beanClass = bean.getClass();
        Holder<Boolean> needProxy = new Holder<>(false);
        // 尝试处理方法上注解的action  （递归父类处理）
        ReflectKit.doWithMethod(beanClass, method -> {
            LSAround around = method.getAnnotation(LSAround.class);
            if (CollectionKit.notEmpty(around.value())) {
                needProxy.setValue(true);
                Arrays.stream(around.value()).map(container::getBean).forEach(action -> AopContainer.putAopAction(method, (AopAction) action));
            }
        }, method -> method.isAnnotationPresent(LSAround.class));
        if (AopLoader.NEED_AOP_CLASSES.containsKey(beanClass)) {
            needProxy.setValue(true);
            // 尝试注册类注解的AopAction
            AopLoader.NEED_AOP_CLASSES.get(beanClass).forEach(actionClass -> {
                AopContainer.putAopAction(beanClass, (AopAction) container.getBean(actionClass));
            });
        }
        if (needProxy.getValue()) {
            // 需要代理强化
            return AopProxy.getProxy(bean);
        }

        return bean;
    }
}
