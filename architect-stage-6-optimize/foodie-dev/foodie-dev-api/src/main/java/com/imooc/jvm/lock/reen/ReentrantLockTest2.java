package com.imooc.jvm.lock.reen;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试公平、非公平
 */
public class ReentrantLockTest2 {
    public static void main(String[] args) {
        FairTest test = new FairTest();
        Thread t1 = new Thread(test);
        Thread t2 = new Thread(test);
        Thread t3 = new Thread(test);
        t1.start();
        t2.start();
        t3.start();
    }
}

class FairTest implements Runnable {
    // 默认非公平锁
    // synchronized只能创建非公平锁
    // 性能：非公平锁 > 公平锁
    private ReentrantLock lock = new ReentrantLock(false);

    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + "开始运行");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "拿到锁");
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            System.out.println("SLEEP 发生异常");
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.unlock();
        }
    }
}
