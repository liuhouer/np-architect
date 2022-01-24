package com.imooc.jvm.jvm;

public class JVMTest2 {
    private static final String CONST_FIELD = "AAA";
    private static String staticField;
    private String field;

    public String add() {
        return staticField + field + CONST_FIELD;
    }

    public static void main(String[] args) {
        new JVMTest2().add();
    }
}
