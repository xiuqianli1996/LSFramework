package demo.web.config;

import com.ls.framework.core.annotation.LSAutowired;
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
public class DbConfig {

    @LSBean("dataSource")
    public DataSource getDataSource() {
        String jdbcUrl = PropKit.get("jdbc.url");
        String jdbcUser = PropKit.get("jdbc.user");
        String jdbcPwd = PropKit.get("jdbc.pwd");
        String jdbcDriver = PropKit.get("jdbc.driver");
        return new SimpleDataSource(jdbcUrl, jdbcUser, jdbcPwd, jdbcDriver);
    }

    @LSBean
    public JdbcExecutor getJdbcExecutor() {
        return new DefaultJdbcExecutor(Boolean.valueOf(PropKit.get("jdbc.mapUnderscore", "true"))); //参数设置是否将下划线转为驼峰
    }

    @LSBean
    public LSDbConfiguration getLSDbConfiguration(@LSAutowired("dataSource") DataSource dataSource
            , @LSAutowired JdbcExecutor executor) {
        LSDbConfiguration configuration = new LSDbConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setExecutor(executor);
        return configuration;
    }

    @LSBean("sqlSession") //必须叫这名
    public SqlSession getSqlSession(@LSAutowired LSDbConfiguration configuration) {
        return new MySqlSession(configuration);
    }

}
