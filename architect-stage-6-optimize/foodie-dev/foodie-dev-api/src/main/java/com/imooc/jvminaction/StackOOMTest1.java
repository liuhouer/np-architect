package com.imooc.jvminaction;

// 默认配置：17868
// Xss144k：498
public class StackOOMTest1 {
    private int stackLength = 1;

    private void stackLeak() {
        stackLength++;
        this.stackLeak();
    }

    public static void main(String[] args) {
        StackOOMTest1 oom = new StackOOMTest1();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}