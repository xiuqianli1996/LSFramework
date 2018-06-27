package com.ls.framework.jdbc.executor;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface JdbcExecutor {

    <T> List<T> queryBeanList(Connection connection, Class<T> clazz, String sql, Object[] paras);

    <T> T queryBean(Connection connection, Class<T> clazz, String sql, Object[] paras);

    long executeUpdate(Connection connection, String sql, Object[] params);

    long queryLong(Connection connection, String sql, Object[] params);

}
