package com.ls.framework.web.annotation;

import java.lang.annotation.*;

/**
 * defaultValue必须在require为false的情况下生效
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSRequestParam {
    String value();
    boolean require() default true;
    String defaultValue() default "";
}
