package com.ls.framework.common.intf;

public interface Ordered {

    default int order() {
        return 0;
    }
}
