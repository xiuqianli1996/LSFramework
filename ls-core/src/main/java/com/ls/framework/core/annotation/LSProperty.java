package com.ls.framework.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSProperty {
    String location() default "";
    String prefix();
}
