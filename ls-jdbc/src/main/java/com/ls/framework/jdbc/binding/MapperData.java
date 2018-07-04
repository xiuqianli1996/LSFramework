package com.ls.framework.jdbc.binding;

import com.ls.framework.jdbc.annotation.LSDbParam;
import com.ls.framework.jdbc.exception.LSJdbcException;

import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapperData {

    private static final String paramRegex = "\\$\\{(\\w+)\\}";

    private String sql;
    private List<String> sqlParamNames = new ArrayList<>();
//    private Method method;
    private boolean modifying = false;

    public MapperData(Method method, String sql, boolean modifying) {
        this.sql = sql;
        this.modifying = modifying;
//        this.method = method;

        Pattern paramNamePattern = Pattern.compile(paramRegex);
        Matcher matcher = paramNamePattern.matcher(sql);
        while (matcher.find()) {
            sqlParamNames.add(matcher.group(1));
        }
    }

    public String buildSql(Object[] params) {
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
