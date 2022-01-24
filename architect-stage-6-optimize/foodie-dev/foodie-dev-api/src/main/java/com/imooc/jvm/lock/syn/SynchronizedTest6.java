package com.imooc.jvm.lock.syn;

@SuppressWarnings("Duplicates")
public class SynchronizedTest6 {
    private final Object lock = new Object();

    // 粗化前
    public void doSomethingMethod1() {
        synchronized (lock) {
            // do some thing
        }
        // 能够很快执行完毕，且无需同步的代码
        synchronized (lock) {
            // do other thing
        }
    }

    // 粗化后：
    public void doSomethingMethod2() {
        //进行锁粗化：整合成一次锁请求、同步、释放
        synchronized (lock) {
            // do some thing
            // 能够很快执行完毕，且无需同步的代码
            // do other thing
        }
    }
}
