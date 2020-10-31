package com.ls.framework.jdbc;

import com.ls.framework.jdbc.annotation.LSColumn;

public class ResultBean {

    public long id;
    public String day;
    @LSColumn("col_result")
    public String result;

    @Override
    public String toString() {
        return "ResultBean{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
