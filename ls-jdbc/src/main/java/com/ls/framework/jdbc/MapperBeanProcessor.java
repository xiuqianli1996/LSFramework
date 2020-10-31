package com.ls.framework.jdbc;

import com.ls.framework.common.kit.ClassKit;
import com.ls.framework.ioc.BeanContainer;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.ioc.listener.BeanLifeCircle;
import com.ls.framework.jdbc.annotation.LSMapper;
import com.ls.framework.jdbc.binding.MapperProxy;
import com.ls.framework.jdbc.session.LazySqlSession;
import com.ls.framework.jdbc.session.SqlSession;
import net.sf.cglib.proxy.Enhancer;

@LSBean
public class MapperBeanProcessor implements BeanLifeCircle {

    @Override
    public Object beforeInitialize(String name, Class<?> clazz, boolean singleton, BeanContainer container) {
        if (!ClassKit.hasAnnotation(clazz, LSMapper.class)) {
            // 不是Mapper类不做处理
            return null;
        }

        LSMapper annotation = clazz.getAnnotation(LSMapper.class);
        String sqlSessionName = annotation.sqlSessionName();

        SqlSession sqlSession = container.getBean(sqlSessionName, SqlSession.class);

        return sqlSession.getMapper(clazz);
    }
}
