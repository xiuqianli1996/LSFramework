package com.ls.framework.jdbc.session;

import com.ls.framework.jdbc.binding.MapperProxy;
import com.ls.framework.jdbc.exception.LSJdbcException;
import lombok.AllArgsConstructor;
import net.sf.cglib.proxy.Enhancer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
public abstract class AbstractSqlSession implements SqlSession {

    private Map<String, DataSource> dataSources;

    @Override
    public <E> E getMapper(Class<?> clazz) {
        return (E) Enhancer.create(clazz, new MapperProxy(this));
    }

    protected Connection getConnection() {
        Connection connection = ConnectionContext.get();
        if (connection == null) {
            String dataSourceName = ConnectionContext.getDatasourceName();
            DataSource dataSource = Objects.requireNonNull(dataSources.get(dataSourceName), "DataSource is null");
            try {
                connection = dataSource.getConnection();
                ConnectionContext.set(connection);
            } catch (SQLException e) {
                throw new LSJdbcException(e);
            }
        }
        return connection;
    }
}
