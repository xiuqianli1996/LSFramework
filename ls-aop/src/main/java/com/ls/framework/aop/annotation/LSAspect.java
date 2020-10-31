package com.ls.framework.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LSAspect {

	/**
	 * @return 需要代理的目标类
	 */
	Class[] targetClasses() default {};

	/**
	 * @return 需要代理的包名，会给指定包下的所有类加上代理切面，暂不支持模糊匹配
	 */
	String[] packages() default {};

	/**
	 * @return 给类或方法上有指定注解的类加上代理切面
	 */
	Class<? extends Annotation>[] targetAnnotations() default {};


}
