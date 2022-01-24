package com.imooc.jvm.lock.reen;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockTest1 {
    private Object data;
    //缓存是否有效
    private volatile boolean cacheValid;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public void processCachedData() {
        rwl.readLock().lock();
        //如果缓存无效，更新cache;否则直接使用data
        if (!cacheValid) {
            //获取写锁前须释放读锁
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            // Recheck state because another thread might have acquired
            //   write lock and changed state before we did.
            if (!cacheValid) {
                // 更新数据
                data = new Object();
                cacheValid = true;
            }
            // 锁降级，在释放写锁前获取读锁
            rwl.readLock().lock();
            // 释放写锁，依然持有读锁
            rwl.writeLock().unlock();
        }
        // 使用缓存
        // ...

        // 释放读锁
        rwl.readLock().unlock();
    }
}