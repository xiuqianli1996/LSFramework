package com.ls.framework.jdbc.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LSColumn {
    String value() default "";

    /**
     * @return 标注非数据库字段
     */
    boolean ignore() default false;
}
