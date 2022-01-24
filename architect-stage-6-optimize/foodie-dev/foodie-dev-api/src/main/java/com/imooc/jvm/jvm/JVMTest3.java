package com.imooc.jvm.jvm;

public class JVMTest3 {
    static {
        i = 1;
    }

    static int i = 0;

    public static void main(String[] args) {
        System.out.println(i);
    }
}
