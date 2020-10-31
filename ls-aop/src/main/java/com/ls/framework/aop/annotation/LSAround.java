package com.ls.framework.aop.annotation;

import com.ls.framework.aop.support.AopAction;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSAround {
    Class<? extends AopAction>[] value();
}
