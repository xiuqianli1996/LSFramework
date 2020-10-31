package com.ls.framework.core.property.binder;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.common.kit.ReflectKit;
import com.ls.framework.common.kit.StrKit;
import com.ls.framework.core.annotation.LSProperty;
import com.ls.framework.core.annotation.LSValue;
import com.ls.framework.core.property.PropertySource;
import com.ls.framework.core.property.transfer.DataTransferService;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultPropertyBinder implements PropertyBinder {

    private static final String CONFIG_SPLIT_CHAR = ".";

    @Override
    public void bind(Object obj, PropertySource propertySource, String prefix, Predicate<Field> predicate) {
        prefix = StrKit.orDefault(prefix, () -> "");
        // 前缀不为空的话拼上连接的点
        String realPrefix = StrKit.isBlank(prefix) ? prefix : prefix + CONFIG_SPLIT_CHAR;
        Class<?> clazz = obj.getClass();
        ReflectKit.doWithFiled(clazz, field -> {
            String paramName = resolvePropertyName(realPrefix, field);
            Object value = resolveValue(field, propertySource.get(paramName));
            if (value == null) {
                return;
            }
            ReflectKit.setFieldValue(obj, field, value);
        }, predicate);
    }

    private String resolvePropertyName(String prefix, Field field) {
        if (!field.isAnnotationPresent(LSValue.class)) {
            return prefix + field.getName();
        }
        LSValue value = field.getAnnotation(LSValue.class);

        return prefix + StrKit.requireNotBlank(value.value());
    }

    private Object resolveValue(Field field, Object source) {
        if (source == null) {
            return null;
        }
        Class<?> fieldType = field.getType();
        Object result = DataTransferService.transfer(source, fieldType);
        if (result != null && List.class.isAssignableFrom(fieldType)) {
            Type listType = field.getGenericType();
            if (listType instanceof ParameterizedType) {
                listType = ((ParameterizedType) listType).getActualTypeArguments()[0];
            }
            Class<?> toClass = (Class)listType;
            return ((List)result).stream().map(item -> DataTransferService.transfer(item, toClass)).collect(Collectors.toList());
        }

        return result;
    }
}
