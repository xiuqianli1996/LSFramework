package com.ls.framework.ioc.factory;

import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.definition.BeanDefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractAnnotationBeanFactory<A extends Annotation> implements BeanFactory {

    protected abstract BeanDefinition getBeanDefinition(A annotation, Class<?> clazz, ApplicationContext context, BeanContainer beanContainer);

    /**
     * @return 是否过滤抽象类（包括接口）
     */
    protected boolean filterAbstractClass() {
        return true;
    }

    private Class<A> getTargetAnnotationClass() {
        return (Class<A>) ClassKit.getGenericTypeClass(this.getClass());
    }

    @Override
    public List<BeanDefinition> loadBeanDefinition(ApplicationContext context, BeanContainer beanContainer, Set<Class<?>> classSet) {
        Class<A> annotationClass = Objects.requireNonNull(getTargetAnnotationClass(), "target annotation class can not be null!");
        return ClassKit.getClassesByAnnotation(classSet, annotationClass)
                .stream()
                .filter(clazz -> {
                    if (filterAbstractClass()) {
                        // 过滤抽象类
                        return ClassKit.notAbstract(clazz);
                    }
                    return true;
                } )
                .map(clazz -> {
                    A annotation = clazz.getAnnotation(annotationClass);
                    return getBeanDefinition(annotation, clazz, context, beanContainer);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
