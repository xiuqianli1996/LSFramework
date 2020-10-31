package com.ls.framework.ioc.component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TestComponent2 {
    private String msg;

    public String test() {
        return msg;
    }

}
