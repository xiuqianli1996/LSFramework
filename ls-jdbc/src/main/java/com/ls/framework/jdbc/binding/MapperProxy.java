package com.ls.framework.jdbc.binding;

import com.ls.framework.core.utils.ClassUtil;
import com.ls.framework.jdbc.annotation.LSModifying;
import com.ls.framework.jdbc.annotation.LSQuery;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.session.SqlSession;
import com.sun.org.apache.regexp.internal.RE;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class MapperProxy implements MethodInterceptor {

    private SqlSession sqlSession;

    public MapperProxy(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        MapperData mapperData = getMapperData(method);
        mapperData.sortParams(objects);
        String sql = mapperData.buildSql(objects);
//        System.out.println(sql);
//        for (Object obj : objects) {
//            System.out.println(obj);
//        }
//        return sql;
        if (mapperData.isModifying()) {
            return sqlSession.executeUpdate(sql, objects);
        }
        Class<?> returnType = method.getReturnType();
        if (List.class.isAssignableFrom(returnType)) {
            //获取返回值的类型
            Type type = method.getGenericReturnType();
            Class<?> realType;
            if (type instanceof ParameterizedType) {
                realType = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                realType = (Class)type;
            }
            return sqlSession.selectList(realType, sql, objects);
        }
        if (returnType.isArray()) {
//            System.out.println(returnType.getComponentType());
            Class<?> realType = returnType.getComponentType();
            List list = sqlSession.selectList(realType, sql, objects);
            Object[] result = (Object[]) Array.newInstance(realType, list.size());
            return list.toArray(result);
        }
        if (returnType == long.class || returnType == Long.class) {
            return sqlSession.queryLong(sql, objects);
        }
        return sqlSession.selectOne(returnType, sql, objects);
    }

    private MapperData getMapperData(Method method) {
        String fullMethodName = ClassUtil.getFullMethodName(method);
        MapperData mapperData = MapperContainer.mapperMethodMap.get(fullMethodName);
        if (mapperData == null) {
            if (!method.isAnnotationPresent(LSQuery.class)) {
                //如果没有找到对应的SQL映射又没有LSQuery注解修饰就抛出异常
                throw new LSJdbcException(String.format("can not find mapper: %s", fullMethodName));
            }
            LSQuery lsQuery = method.getAnnotation(LSQuery.class);
            String sql = lsQuery.value();
            boolean modifying = method.isAnnotationPresent(LSModifying.class);
            mapperData = new MapperData(method, sql, modifying);
            MapperContainer.mapperMethodMap.put(fullMethodName, mapperData);
        }
        return mapperData;
    }
}
