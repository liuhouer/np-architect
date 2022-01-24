package com.imooc.jvm.lock.reen;

import java.util.concurrent.locks.StampedLock;

public class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    void move(double deltaX, double deltaY) { // an exclusively locked method
        // 添加写锁
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            // 释放写锁
            sl.unlockWrite(stamp);
        }
    }

    double distanceFromOrigin() { // A read-only method
        // 获得一个乐观读锁
        long stamp = sl.tryOptimisticRead();

        // (x,y) = (10,10)
        // 可能会被其他线程修改为(20,20)
        double currentX = x, currentY = y;
        // 验证获得乐观锁之后，有没有发生过写操作
        if (!sl.validate(stamp)) {
            // 其他线程执行过写操作，用悲观读锁重读数据
            stamp = sl.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                // 读完数据后，释放悲观读锁
                sl.unlockRead(stamp);
            }
        }
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    void moveIfAtOrigin(double newX, double newY) { // upgrade
        // Could instead start with optimistic, not read mode
        // 创建一个悲观读锁
        long stamp = sl.readLock();
        try {
            while (x == 0.0 && y == 0.0) {
                // 准备写数据了，所以转换成写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                // 写锁获得成功
                if (ws != 0L) {
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                }
                // 写锁获得失败，就释放悲观读锁，直接用写锁
                else {
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
            sl.unlock(stamp);
        }
    }
}