package com.ls.framework.core.listener;

import com.ls.framework.common.intf.Ordered;
import com.ls.framework.core.ApplicationContext;
import com.ls.framework.core.property.PropertySourceFactory;

public interface ApplicationContextListener extends Ordered {

    default void setApplicationContext(ApplicationContext applicationContext) {}

    default void setPropertySourceFactory(PropertySourceFactory propertySourceFactory) {}

    default void beforeStartLoaders() {}

    default void afterStartLoaders() {}

}
