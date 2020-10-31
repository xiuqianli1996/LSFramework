package com.ls.framework.ioc.definition;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.ioc.BeanContainer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Constructor;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AnnotationBeanDefinition extends BaseBeanDefinition {

    @Getter
    private final Constructor<?> constructor;

    public AnnotationBeanDefinition(String name, Class<?> beanClass, boolean singleton, Constructor<?> constructor) {
        super(name, beanClass, singleton);
        this.constructor = constructor;
    }

    @Override
    protected Object newBeanInstance(BeanContainer container) {
        int paramCount = constructor.getParameterCount();
        if (paramCount == 0){
            //无参构造函数直接反射构造对象
            return ObjectKit.getInstanceByConstructor(constructor);
        }
        List<Object> params = container.resolveDependencyParams(constructor.getParameters());
        return ObjectKit.getInstanceByConstructor(constructor, params.toArray());
    }
}
