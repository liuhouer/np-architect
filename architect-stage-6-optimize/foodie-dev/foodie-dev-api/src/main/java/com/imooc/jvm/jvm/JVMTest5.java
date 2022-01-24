package com.imooc.jvm.jvm;

public class JVMTest5 {
    static {
        System.out.println("JVMTest5静态块");
    }

    {
        System.out.println("JVMTest5构造块");
    }

    public JVMTest5() {
        System.out.println("JVMTest5构造方法");
    }

    public static void main(String[] args) {
        new Sub();
    }
}

class Super {
    static {
        System.out.println("Super静态块");
    }

    public Super() {
        System.out.println("Super构造方法");
    }

    {
        System.out.println("Super构造块");
    }
}

class Sub extends Super {
    static {
        System.out.println("Sub静态块");
    }

    public Sub() {
        System.out.println("Sub构造方法");
    }

    {
        System.out.println("Sub构造块");
    }
}