package com.ls.framework.jdbc.annotation;

import com.ls.framework.jdbc.config.Constants;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LSMapper {

    String value() default "";

    String sqlSessionName() default Constants.DEFAULT_SESSION_BEAN_NAME;

    String dataSourceName() default Constants.DEFAULT_DATASOURCE_NAME;

}
