package com.ls.framework.core.annotation;

import java.lang.annotation.*;

/**
 * 通过value的值可控制loader执行顺序，从小到大执行
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSLoader {
    int value() default Integer.MAX_VALUE;
}
