package com.ls.framework.jdbc.session;

import java.util.List;

public interface SqlSession {

    <E> E getMapper(Class<?> clazz);

    <T> T selectOne(Class<T> clazz, String sql, Object[] params);

    <T> List<T> selectList(Class<T> clazz, String sql, Object[] params);

    long executeUpdate(String sql, Object[] params);

    long queryLong(String sql, Object[] params);

}
