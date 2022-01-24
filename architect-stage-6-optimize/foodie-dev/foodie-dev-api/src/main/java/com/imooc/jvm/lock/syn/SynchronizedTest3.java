package com.imooc.jvm.lock.syn;

import java.util.List;
import java.util.Vector;

@SuppressWarnings("Duplicates")
public class SynchronizedTest3 {
    public static void main(String[] args) {
        someMethod();
    }

    private static void someMethod() {
        Object object = new Object();
        synchronized(object) {
            System.out.println(object);
        }
    }
}
