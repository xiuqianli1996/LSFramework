package com.ls.framework.jdbc.config;

import com.ls.framework.jdbc.binding.MapperContainer;
import com.ls.framework.jdbc.executor.JdbcExecutor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class LSDbConfiguration {

    public Map<String, DataSource> dataSourceMap = new HashMap<>(); //为多数据源做准备

    private DataSource dataSource;
    private JdbcExecutor executor;

    private boolean mapUnderscoreToCamelCase = true;


    public boolean isMapUnderscoreToCamelCase() {
        return mapUnderscoreToCamelCase;
    }

    public void setMapUnderscoreToCamelCase(boolean mapUnderscoreToCamelCase) {
        this.mapUnderscoreToCamelCase = mapUnderscoreToCamelCase;
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(JdbcExecutor executor) {
        this.executor = executor;
    }
}
