package demo.controller;

public class TestBean {

    private int val1;
    private static String val2;

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
}
