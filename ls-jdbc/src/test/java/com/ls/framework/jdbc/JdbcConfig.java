package com.ls.framework.jdbc;

import com.ls.framework.common.kit.PropKit;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.jdbc.base.SimpleDataSource;
import com.ls.framework.jdbc.config.Constants;
import com.ls.framework.jdbc.config.LSDbConfiguration;
import com.ls.framework.jdbc.executor.DefaultJdbcExecutor;
import com.ls.framework.jdbc.executor.JdbcExecutor;
import com.ls.framework.jdbc.session.DefaultSqlSession;
import com.ls.framework.jdbc.session.SqlSession;

import javax.sql.DataSource;

@LSBean
public class JdbcConfig {

    @LSAutowired
    private ApplicationContext applicationContext;

    @LSBean("sqlSession")
    public SqlSession getSqlSession() {
        String jdbcUrl = applicationContext.getProperty("jdbc.url");
        String jdbcUser = applicationContext.getProperty("jdbc.user");
        String jdbcPwd = applicationContext.getProperty("jdbc.pwd");
        String jdbcDriver = applicationContext.getProperty("jdbc.driver");
        DataSource dataSource = new SimpleDataSource(jdbcUrl, jdbcUser, jdbcPwd, jdbcDriver);
        LSDbConfiguration configuration = new LSDbConfiguration();
        configuration.getDataSources().put(Constants.DEFAULT_DATASOURCE_NAME, dataSource);

        JdbcExecutor executor = new DefaultJdbcExecutor(configuration.isMapUnderscoreToCamelCase());

        return new DefaultSqlSession(configuration, executor);
    }

}
