package com.ls.framework.core.bean;

import java.util.Objects;

public class TestBean2 {

    private int val1;
    private String val2;
    private TestBean testBean;

    public TestBean2(int val1, TestBean testBean) {
        this.val1 = val1;
        this.testBean = testBean;
    }

    public TestBean2() {
    }

    public void test() {
        System.out.println(this);
    }

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

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

    @Override
    public String toString() {
        return "TestBean2{" +
                "val1=" + val1 +
                ", val2='" + val2 + '\'' +
                ", testBean=" + testBean +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestBean2 testBean2 = (TestBean2) o;
        return val1 == testBean2.val1 &&
                Objects.equals(val2, testBean2.val2) &&
                Objects.equals(testBean, testBean2.testBean);
    }

    @Override
    public int hashCode() {

        return Objects.hash(val1, val2, testBean);
    }
}
