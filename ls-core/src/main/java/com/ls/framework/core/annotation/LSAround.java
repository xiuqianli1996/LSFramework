package com.ls.framework.core.annotation;

import com.ls.framework.core.aop.AopAction;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSAround {
    Class<? extends AopAction>[] value();
}
