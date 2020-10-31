package com.ls.framework.jdbc.annotation;

import com.ls.framework.jdbc.config.Constants;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSQuery {

    String value();

    String dataSourceName() default "";

}
