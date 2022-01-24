package com.imooc.jvm.lock.syn;

public class SynchronizedTest1 implements Runnable {
    // 1. 存在共享数据
    // 2. 多个线程同时操作共享数据
    private static int i = 0;

    // 可重入锁
    private synchronized void increase() {
        i = i + 1;
        this.someMethod();
    }

    private synchronized void someMethod() {
        synchronized (this) {
            System.out.println(1);
        }
    }

    @Override
    public void run() {
        for (int j = 0; j < 10000; j++) {
            increase();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest1 t = new SynchronizedTest1();
        Thread t1 = new Thread(t);
        t1.start();
        Thread t2 = new Thread(t);
        t2.start();
        // 等待两个线程停止
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
