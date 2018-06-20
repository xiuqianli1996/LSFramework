package com.ls.framework.annotation;

import com.ls.framework.aop.AopAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSClear {
    Class<? extends AopAction>[] value() default {};
}
