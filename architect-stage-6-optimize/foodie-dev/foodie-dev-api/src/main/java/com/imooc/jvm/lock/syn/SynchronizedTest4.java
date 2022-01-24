package com.imooc.jvm.lock.syn;

@SuppressWarnings("Duplicates")
public class SynchronizedTest4 {
    private Object object2 = null;

    public void someMethod2() {
        object2 = this.someMethod();
    }

    private Object someMethod() {
        Object object = new Object();
        synchronized (object) {
            return object;
        }
    }
}
