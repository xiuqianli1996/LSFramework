package com.ls.framework.jdbc;


import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.definition.BaseBeanDefinition;
import com.ls.framework.ioc.definition.BeanDefinition;
import com.ls.framework.ioc.factory.AbstractAnnotationBeanFactory;
import com.ls.framework.ioc.kit.BeanDefinitionKit;
import com.ls.framework.jdbc.annotation.LSMapper;
import com.ls.framework.jdbc.binding.MapperProxy;
import com.ls.framework.jdbc.session.LazySqlSession;
import com.ls.framework.jdbc.session.SqlSession;
import net.sf.cglib.proxy.Enhancer;

/**
 * 加载@LSMapper注解的类到ioc容器中，接口在MapperBeanProcessor使用动态代理生成实例，类通过AnnotationBeanDefinition让ioc容器控制实例化
 */
public class MapperBeanFactory extends AbstractAnnotationBeanFactory<LSMapper> {

    @Override
    protected boolean filterAbstractClass() {
        return false;
    }

    @Override
    protected BeanDefinition getBeanDefinition(LSMapper annotation, Class<?> clazz, ApplicationContext context, BeanContainer beanContainer) {
        String beanName = StrKit.orDefault(annotation.value(), clazz::getSimpleName);

        if (ClassKit.notAbstract(clazz)) {
            return BeanDefinitionKit.from(clazz, beanName, true);
        }


        return new BaseBeanDefinition(beanName, clazz, true);
    }
}
