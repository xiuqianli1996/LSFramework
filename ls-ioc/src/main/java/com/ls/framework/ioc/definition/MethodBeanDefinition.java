package com.ls.framework.ioc.definition;

import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.ioc.BeanContainer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MethodBeanDefinition extends BaseBeanDefinition {

    @Getter
    private final Class<?> configurationClass;

    @Getter
    private final Method createInstanceMethod;

    public MethodBeanDefinition(String name, Class<?> beanClass, boolean singleton, Class<?> configurationClass, Method createInstanceMethod) {
        super(name, beanClass, singleton);
        this.configurationClass = configurationClass;
        this.createInstanceMethod = createInstanceMethod;
    }

    @Override
    protected Object newBeanInstance(BeanContainer container) {
        int paramCount = createInstanceMethod.getParameterCount();
        Object configuration = container.getBean(configurationClass);
        if (paramCount == 0){
            //无参构造函数直接反射执行方法构造对象
            return ReflectKit.invokeMethod(configuration, createInstanceMethod);
        }
        List<Object> params = container.resolveDependencyParams(createInstanceMethod.getParameters());
        return ReflectKit.invokeMethod(configuration, createInstanceMethod,  params.toArray());
    }
}
