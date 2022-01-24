package com.imooc.jvm.jvm.gc;

@SuppressWarnings("Duplicates")
public class GCTest1 {
    private static GCTest1 obj;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize被调用了");
        obj = this;
    }

    public static void main(String[] args) throws InterruptedException {
        obj = new GCTest1();
        obj = null;
        System.gc();

        Thread.sleep(1000L);
        if (obj == null) {
            System.out.println("obj == null");
        } else {
            System.out.println("obj可用");
        }

        Thread.sleep(1000L);
        obj = null;
        System.gc();
        if (obj == null) {
            System.out.println("obj == null");
        } else {
            System.out.println("obj可用");
        }
    }
}
