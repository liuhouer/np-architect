package com.imooc.jvm.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class InlineTest2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(InlineTest2.class);

    public static void main(String[] args) {
        long cost = compute();
        // 方法内联了416ms add1：12byte add2：4byte
        // 方法不内联580ms
        LOGGER.info("执行花费了{}ms", cost);
    }

    private static long compute() {
        long start = System.currentTimeMillis();
        int result = 0;
        Random random = new Random();
        for (int i = 0; i < 10000000; i++) {
            int a = random.nextInt();
            int b = random.nextInt();
            int c = random.nextInt();
            int d = random.nextInt();
            result = add1(a, b, c, d);
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    private static int add1(int x1, int x2, int x3, int x4) {
        return add2(x1, x2) + add2(x3, x4);
    }

    private static int add2(int x1, int x2) {
        return x1 + x2;
    }
}
