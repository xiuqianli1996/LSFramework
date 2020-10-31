package com.ls.framework.ioc.config;

import com.ls.framework.core.annotation.LSProperty;
import com.ls.framework.ioc.annotation.LSBean;
import lombok.Data;

@Data
@LSBean
@LSProperty(prefix = "test")
public class TestProperties {
    private String msg;
    private int count;
}
