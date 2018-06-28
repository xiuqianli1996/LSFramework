package com.ls.framework.jdbc;

import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.core.annotation.LSConfiguration;
import com.ls.framework.core.utils.PropKit;
import com.ls.framework.jdbc.base.SimpleDataSource;
import com.ls.framework.jdbc.config.LSDbConfiguration;
import com.ls.framework.jdbc.executor.DefaultJdbcExecutor;
import com.ls.framework.jdbc.executor.JdbcExecutor;
import com.ls.framework.jdbc.session.MySqlSession;
import com.ls.framework.jdbc.session.SqlSession;

import javax.sql.DataSource;

@LSConfiguration
public class JdbcConfig {

    @LSBean("sqlSession")
    public SqlSession getSqlSession() {
        String jdbcUrl = PropKit.get("jdbc.url");
        String jdbcUser = PropKit.get("jdbc.user");
        String jdbcPwd = PropKit.get("jdbc.pwd");
        String jdbcDriver = PropKit.get("jdbc.driver");
        DataSource dataSource = new SimpleDataSource(jdbcUrl, jdbcUser, jdbcPwd, jdbcDriver);
        LSDbConfiguration configuration = new LSDbConfiguration();
        configuration.setDataSource(dataSource);

        JdbcExecutor executor = new DefaultJdbcExecutor(configuration.isMapUnderscoreToCamelCase());
        configuration.setExecutor(executor);

        return new MySqlSession(configuration);
    }

}
