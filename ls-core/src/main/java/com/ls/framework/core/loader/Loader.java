package com.ls.framework.core.loader;

import java.util.Set;

public interface Loader {

    void doLoad(Set<Class<?>> classSet);

}
