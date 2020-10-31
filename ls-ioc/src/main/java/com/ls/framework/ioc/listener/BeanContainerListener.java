package com.ls.framework.ioc.listener;

import com.ls.framework.common.intf.Ordered;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.definition.BeanDefinition;

public interface BeanContainerListener extends Ordered {

    /**
     * 处理发现通过BeanFactory发现的BeanDefinition，返回值不为null的时候使用返回值进行注册
     * @param beanDefinition
     * @return
     */
    default BeanDefinition processBeanDefinition(BeanDefinition beanDefinition){
        return null;
    }

    default void afterLoadBeanDefinition(BeanContainer beanContainer) {}

}
