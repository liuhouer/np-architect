package com.imooc.jvm.lock.syn;

import java.util.List;
import java.util.Vector;

public class SynchronizedTest2 {
    private static List<Integer> list = new Vector<>();

    // 关闭偏向锁：3996
    // 开启偏向锁：3912
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            list.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
