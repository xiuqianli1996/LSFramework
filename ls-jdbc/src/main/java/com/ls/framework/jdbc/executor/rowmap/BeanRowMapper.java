package com.ls.framework.jdbc.executor.rowmap;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.jdbc.annotation.LSColumn;
import com.ls.framework.jdbc.util.JdbcConvertKit;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class BeanRowMapper<T> implements RowMapper<T> {

    private Class<?> mapClass;

    public BeanRowMapper(Class<?> mapClass) {
        if (mapClass != null) {
            this.mapClass = mapClass;
        } else {
            this.mapClass = ReflectKit.resolveParameterizedType(this.getClass())[0];
        }
    }

    @Override
    public T map(ResultSet rs, int index) {
        T result = (T) ObjectKit.getInstance(this.mapClass);
        ReflectKit.doWithFiled(this.mapClass, field -> processField(result, field, rs));
        return result;
    }

    private void processField(Object result, Field field, ResultSet resultSet) {
        String columnName = field.getName();
        LSColumn column = field.getAnnotation(LSColumn.class);
        if (column != null) {
            if (column.ignore()) {
                return;
            }
            columnName = StrKit.orDefault(column.value(), columnName);
        }
        ReflectKit.setFieldValue(result, field, JdbcConvertKit.convert(resultSet, columnName, field.getType()));
    }
}
