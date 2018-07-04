package com.ls.framework.jdbc;

import com.ls.framework.core.annotation.LSAutowired;
import com.ls.framework.core.annotation.LSBean;
import com.ls.framework.jdbc.annotation.LSTranscated;

import java.util.List;

@LSBean
public class ResultService {

    @LSAutowired
    TestMapper2 testMapper2;

    public ResultBean get() {
        return testMapper2.selectOne();
    }

    public ResultBean get(String day) {
        return testMapper2.selectOneByDay(day);
    }
    public List<ResultBean> list(String[] days) {
        return testMapper2.selectList(days);
    }

    @LSTranscated
    public void update() {
        String day = "2017-5-23";
        ResultBean resultBean = testMapper2.selectOneByDay(day);
        String val;
        if ("胜".equals(resultBean.result)) {
            val = "负";
        } else {
            val = "胜";
        }
        testMapper2.updateWithoutReturn(val, day);
        throw new RuntimeException();
    }

}
