package com.ls.framework.core.loader;

import com.ls.framework.common.intf.Ordered;
import com.ls.framework.core.ApplicationContext;

import java.util.Set;

public interface Loader extends Ordered {

    void doLoad(ApplicationContext context, Set<Class<?>> classSet);

    default void shutdown() {}

}
