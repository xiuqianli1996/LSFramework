package com.ls.framework.core.annotation;

import java.lang.annotation.*;

/**
 * 作用于类上标注为配置类，作用于方法上可执行无参忽略返回值的方法
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSConfiguration {
    int value() default 0;
}
