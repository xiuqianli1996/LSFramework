package com.ls.framework.core.property;

import com.ls.framework.common.intf.Ordered;

public interface PropertySource<T> extends Ordered {

    T get(String key);

    T getOrDefault(String key, T defaultVal);

}
