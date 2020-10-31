package com.ls.framework.core.property.binder;

import com.ls.framework.common.kit.ObjectKit;
import com.ls.framework.core.property.PropertySource;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class PropertyBinderService implements PropertyBinder {

    private static PropertyBinderService INSTANCE = new PropertyBinderService();

    private PropertyBinder propertyBinder;

    public static PropertyBinderService sharedBinder() {
        return INSTANCE;
    }

    public void setBinder(Class<? extends PropertyBinder> binderClass) {
        propertyBinder = ObjectKit.getInstance(binderClass);
    }

    @Override
    public void bind(Object obj, PropertySource propertySource, String prefix, Predicate<Field> predicate) {
        propertyBinder.bind(obj, propertySource, prefix, predicate);
    }
}
