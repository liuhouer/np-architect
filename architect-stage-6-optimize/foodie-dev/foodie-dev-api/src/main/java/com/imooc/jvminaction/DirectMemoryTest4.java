package com.imooc.jvminaction;

import java.nio.ByteBuffer;

// 1. ByteBuffer直接内存溢出报错是java.lang.OutOfMemoryError: Direct buffer memory
// 2. -XX:MaxDirectMemorySize对ByteBuffer有效
public class DirectMemoryTest4 {
    private static final int GB_1 = 1024 * 1024 * 1024;

    /**
     * ByteBuffer参考文档：
     * https://blog.csdn.net/z69183787/article/details/77102198/
     *
     * @param args args
     */
    public static void main(String[] args) {
        int i= 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(GB_1);
            System.out.println(++i);
        }
    }
}
