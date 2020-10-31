package com.ls.framework.aop.support;


import com.ls.framework.common.intf.Ordered;

public interface AopAction extends Ordered {

    /**
     * 切面逻辑
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object around(Invocation invocation) throws Throwable;

    /**
     * 判断是否需要执行当前切面, 主要用于类级切面
     * @param invocation
     * @return
     */
    default boolean match(Invocation invocation) {
        return true;
    }

}
