package com.ls.framework.core.property.transfer;

import java.util.Arrays;
import java.util.List;

public class StringListTransfer implements DataTransfer<List<String>> {
    @Override
    public boolean match(Class<?> fromClass, Class<?> toClass) {
        return String.class.isAssignableFrom(fromClass) && List.class.isAssignableFrom(toClass);
    }

    @Override
    public List<String> transfer(Object from, Class<?> toClass) {
        if (from == null) {
            return null;
        }
        String data = (String) from;
        return Arrays.asList(data.split(","));
    }
}
