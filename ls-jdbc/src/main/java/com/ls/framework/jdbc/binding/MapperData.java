package com.ls.framework.jdbc.binding;

import com.ls.framework.jdbc.annotation.LSDbParam;
import com.ls.framework.jdbc.exception.LSJdbcException;
import lombok.Getter;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapperData {

    private static final String paramRegex = "\\$\\{(\\w+)\\}";
    private static Pattern paramNamePattern = Pattern.compile(paramRegex);
    private String sql;
    private List<String> sqlParamNames = new ArrayList<>();
    private Map<String, Integer> sqlParamIndexMap = new HashMap<>();
//    private Method method;
    private boolean modifying;

    @Getter
    private final String dataSourceName;

    public MapperData(Method method, String sql, boolean modifying, String dataSourceName) {
        this.sql = sql;
        this.modifying = modifying;
        this.dataSourceName = dataSourceName;
//        this.method = method;
        int pos = 0;
        Matcher matcher = paramNamePattern.matcher(sql);
        while (matcher.find()) {
            String name = matcher.group(1);
            sqlParamNames.add(name);
        }
        for (Parameter parameter : method.getParameters()) {
            LSDbParam lsDbParam = parameter.getAnnotation(LSDbParam.class);
            sqlParamIndexMap.put(lsDbParam.value(), pos++);
        }
    }

    public String buildSql(Object[] params) {
        // 占位符生成，可以考虑遍历填充preparedstatement参数的时候生成，减少一次O(n)循环
        String realSql = sql;
        for (Object obj : params) {
            String placeholder = "?";
            if (obj.getClass().isArray()) {
                placeholder = getPlaceholders(Array.getLength(obj));
            } else if (Collection.class.isAssignableFrom(obj.getClass())) {
                placeholder = getPlaceholders(((Collection)obj).size());
            }
            realSql = realSql.replaceFirst(paramRegex, placeholder);
        }
        return realSql;
    }

    public void sortParams(Object[] params, Parameter[] parameters) {
        if (params.length != sqlParamNames.size()) {
            throw new LSJdbcException("The param length is invalid");
        }

        Map<String, Object> paramMap = buildParamMap(params, parameters);
        for (int i = 0; i < params.length; i++) {
            params[i] = paramMap.get(sqlParamNames.get(i));
        }
    }

    public Object[] sortParams(Object[] params) {
        if (params.length != sqlParamNames.size()) {
            throw new LSJdbcException("The param length is invalid");
        }
        Object[] sortedParams = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            int index = sqlParamIndexMap.get(sqlParamNames.get(i));
            sortedParams[i] = params[index];
        }
        return sortedParams;
    }

    private Map<String, Object> buildParamMap(Object[] params, Parameter[] parameters) {

        Map<String, Object> resultMap = new HashMap<>();
        int pos = 0;
        for (Parameter parameter : parameters) {
            LSDbParam lsDbParam = parameter.getAnnotation(LSDbParam.class);
            String name = lsDbParam.value();
            resultMap.put(name, params[pos++]);
        }
        return resultMap;
    }

    private String getPlaceholders(int count) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("?");
        }
        return stringBuilder.toString();
    }

    public boolean isModifying() {
        return modifying;
    }

}
