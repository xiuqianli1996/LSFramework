package com.ls.framework.ioc.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSBean {
    String value() default "";
    boolean singleton() default true;
}
