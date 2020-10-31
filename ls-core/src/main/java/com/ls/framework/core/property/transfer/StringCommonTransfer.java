package com.ls.framework.core.property.transfer;

import com.ls.framework.common.kit.ConvertKit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class StringCommonTransfer implements DataTransfer {

    private static final Set<Class<?>> matchClasses = new HashSet<>(Arrays.asList(Long.class, long.class, boolean.class, Boolean.class,
            int.class, Integer.class, float.class, Float.class, double.class, Double.class, Date.class, LocalDate.class, LocalDateTime.class));

    @Override
    public boolean match(Class fromClass, Class toClass) {
        return String.class.isAssignableFrom(fromClass) && matchClasses.contains(toClass);
    }

    @Override
    public Object transfer(Object from, Class toClass) {
        return ConvertKit.convert(from, toClass);
    }
}
