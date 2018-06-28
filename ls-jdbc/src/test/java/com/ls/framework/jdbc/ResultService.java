package com.ls.framework.jdbc;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;

@LSBean
public class ResultService {

    @LSAutowired
    TestMapper2 testMapper2;

    public ResultBean get() {
        return testMapper2.selectOne();
    }

}
