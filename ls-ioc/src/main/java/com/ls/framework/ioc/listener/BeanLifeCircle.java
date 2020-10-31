package com.ls.framework.ioc.listener;

import com.ls.framework.common.intf.Ordered;
import com.ls.framework.ioc.BeanContainer;

/**
 * ioc bean生命周期切入处理
 */
public interface BeanLifeCircle extends Ordered {

    /**
     * bean调用实例化之前调用，返回不为null时直接将返回值放到bean容器
     * 会取第一个返回值不为null的listener的值作为最终值，排序靠后的非null返回值会被抛弃
     * @param name
     * @param clazz
     * @param singleton
     * @param container
     * @return
     */
    default Object beforeInitialize(String name, Class<?> clazz, boolean singleton, BeanContainer container) {
        return null;
    }

    /**
     * 这个跟beforeInitialize不同，会将最后一个listener的非null返回值作为最终结果
     * @param name
     * @param bean
     * @param singleton
     * @return
     */
    default Object afterInitialize(String name, Object bean, boolean singleton, BeanContainer container) {
        return bean;
    }

    default Object postPropertiesInject(String name, Object bean, BeanContainer container) {
        return bean;
    }

    default Object afterPropertiesInject(String name, Object bean, BeanContainer container) {
        return bean;
    }
}
