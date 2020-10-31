package com.ls.framework.jdbc;

import com.ls.framework.ioc.annotation.LSAutowired;
import com.ls.framework.ioc.annotation.LSBean;
import com.ls.framework.jdbc.annotation.LSTransacted;

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

    @LSTransacted
    public void update() {
        String day = "2017-5-23";
        ResultBean resultBean = testMapper2.selectOneByDay(day);
        System.out.println(resultBean);
        String val;
        if ("胜".equals(resultBean.result)) {
            val = "负";
        } else {
            val = "胜";
        }
        testMapper2.updateWithoutReturn(val, day);
        System.out.println(get(day));        throw new RuntimeException();
    }

}
