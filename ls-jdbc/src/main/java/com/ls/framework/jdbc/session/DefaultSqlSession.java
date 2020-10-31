package com.ls.framework.jdbc.session;

import com.ls.framework.jdbc.binding.MapperProxy;
import com.ls.framework.jdbc.config.LSDbConfiguration;
import com.ls.framework.jdbc.exception.LSJdbcException;
import com.ls.framework.jdbc.executor.DefaultJdbcExecutor;
import com.ls.framework.jdbc.executor.JdbcExecutor;
import net.sf.cglib.proxy.Enhancer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class DefaultSqlSession extends AbstractSqlSession {

    private LSDbConfiguration configuration;
    private JdbcExecutor executor;

    public DefaultSqlSession(LSDbConfiguration configuration, JdbcExecutor jdbcExecutor) {
        super(configuration.getDataSources());
        this.configuration = configuration;
        this.executor = jdbcExecutor;
    }

    @Override
    public <T> T selectOne(Class<T> clazz, String sql, Object[] params) {
        return executor.queryBean(getConnection(), clazz, sql, params);
    }

    @Override
    public <T> List<T> selectList(Class<T> clazz, String sql, Object[] params) {
        return executor.queryBeanList(getConnection(), clazz, sql, params);

    }

    @Override
    public long executeUpdate(String sql, Object[] params) {
        return executor.executeUpdate(getConnection(), sql, params);
    }

    @Override
    public long queryLong(String sql, Object[] params) {
        return executor.queryLong(getConnection(), sql, params);
    }


}
