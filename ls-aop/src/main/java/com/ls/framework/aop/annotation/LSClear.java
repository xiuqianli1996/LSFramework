package com.ls.framework.aop.annotation;

import com.ls.framework.aop.support.AopAction;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSClear {
    Class<? extends AopAction>[] value() default {};
}
