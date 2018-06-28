package com.ls.framework.core.bean;

import java.util.Objects;

public class TestBean {

    private int val1;
    private  String val2;

    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public String getVal2() {
        return val2;
    }

    public void setVal2(String val2) {
        this.val2 = val2;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "val1=" + val1 +
                ", val2='" + val2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBean testBean = (TestBean) o;
        return val1 == testBean.val1 &&
                Objects.equals(val2, testBean.val2);
    }

    @Override
    public int hashCode() {

        return Objects.hash(val1, val2);
    }
}
