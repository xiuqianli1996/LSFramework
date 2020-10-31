package com.ls.framework.jdbc.config;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Data
public class LSDbConfiguration {

    private Map<String, DataSource> dataSources = new HashMap<>();

    private boolean mapUnderscoreToCamelCase = true;

}
