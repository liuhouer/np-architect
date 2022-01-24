package com.imooc.jvm.jvm;

public class JVMTest4 {
    static {
        System.out.println("JVMTest4静态块");
    }

    {
        System.out.println("JVMTest4构造块");
    }

    public JVMTest4() {
        System.out.println("JVMTest4构造方法");
    }

    public static void main(String[] args) {
        System.out.println("main");
        new JVMTest4();
    }
}

