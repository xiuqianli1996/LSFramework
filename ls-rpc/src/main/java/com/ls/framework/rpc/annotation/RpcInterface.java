package com.ls.framework.rpc.annotation;

import java.lang.annotation.*;

/**
 * 标注一个类是rpc接口
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RpcInterface {

    /**
     * @return 接口名，如果是空串默认用clazz.getSimpleName();
     */
    String value() default "";

}
