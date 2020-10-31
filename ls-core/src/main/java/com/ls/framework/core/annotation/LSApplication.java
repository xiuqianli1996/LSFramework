package com.ls.framework.core.annotation;

import com.ls.framework.core.constant.Constants;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSApplication {
    /**
     * @return 配置文件路径
     */
    String value() default Constants.DEFAULT_PROPERTIES_LOCATION;

    /**
     * @return 新增需要扫描的包名
     */
    String[] includePackage() default "";

    /**
     * @return 需要排除扫描的包名
     */
    String[] excludePackage() default "";
}
