package com.ls.framework.core.annotation;

import com.ls.framework.core.aop.AopAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSClear {
    Class<? extends AopAction>[] value() default {};
}
