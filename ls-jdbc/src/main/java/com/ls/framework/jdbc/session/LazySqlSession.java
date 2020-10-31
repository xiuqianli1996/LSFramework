package com.ls.framework.jdbc.session;

import java.util.List;
import java.util.function.Supplier;

public class LazySqlSession implements SqlSession {

    private Supplier<SqlSession> sqlSessionSupplier;

    private LazySqlSession(Supplier<SqlSession> sqlSessionSupplier) {
        this.sqlSessionSupplier = sqlSessionSupplier;
    }

    public static LazySqlSession from(Supplier<SqlSession> sqlSessionSupplier) {
        return new LazySqlSession(sqlSessionSupplier);
    }

    private SqlSession getSession() {
        return sqlSessionSupplier.get();
    }

    @Override
    public <E> E getMapper(Class<?> clazz) {
        return getSession().getMapper(clazz);
    }

    @Override
    public <T> T selectOne(Class<T> clazz, String sql, Object[] params) {
        return getSession().selectOne(clazz, sql, params);
    }

    @Override
    public <T> List<T> selectList(Class<T> clazz, String sql, Object[] params) {
        return getSession().selectList(clazz, sql, params);
    }

    @Override
    public long executeUpdate(String sql, Object[] params) {
        return getSession().executeUpdate(sql, params);
    }

    @Override
    public long queryLong(String sql, Object[] params) {
        return getSession().queryLong(sql, params);
    }
}
