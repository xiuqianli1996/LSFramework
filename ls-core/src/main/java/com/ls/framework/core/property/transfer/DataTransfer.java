package com.ls.framework.core.property.transfer;

import com.ls.framework.common.intf.Ordered;

public interface DataTransfer<T> extends Ordered {
    boolean match(Class<?> fromClass, Class<?> toClass);
    T transfer(Object from, Class<?> toClass);
}
