package com.ls.framework.web.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RequestParamKit {

    public static Object convertBean(Class<?> clazz) {
        //尝试转换为bean
        Object result = null;
        try {
            result = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {

                if (Modifier.isFinal(field.getModifiers())) {
                    continue;
                }

                String name = field.getName();
                Object val = ConvertUtil.convert("da", field.getType());

            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

}
