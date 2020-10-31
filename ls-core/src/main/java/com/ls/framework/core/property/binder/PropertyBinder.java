package com.ls.framework.core.property.binder;

import com.ls.framework.common.intf.Ordered;
import com.ls.framework.core.property.PropertySource;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public interface PropertyBinder extends Ordered {
    void bind(Object obj, PropertySource propertySource, String prefix, Predicate<Field> predicate);
}
