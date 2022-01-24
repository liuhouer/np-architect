package com.imooc.jvm.jhsdb;

public class JHSDBTest {
    static class Test {
        static SomeObject staticObj = new SomeObject();
        SomeObject instanceObj = new SomeObject();

        void foo() {
            SomeObject localObj = new SomeObject();
            System.out.println("done"); // 这里设一个断点
        }
    }

    private static class SomeObject {
        private Short age;
        private String name;
    }

    public static void main(String[] args) {
        Test test = new JHSDBTest.Test();
        test.foo();
    }
}