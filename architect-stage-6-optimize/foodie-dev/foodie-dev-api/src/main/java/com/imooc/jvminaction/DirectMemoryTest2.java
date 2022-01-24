package com.imooc.jvminaction;

import java.nio.ByteBuffer;

public class DirectMemoryTest2 {
    private static final int ONE_MB = 1024 * 1024;

    /**
     * ByteBuffer参考文档：
     * https://blog.csdn.net/z69183787/article/details/77102198/
     *
     * @param args args
     */
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(ONE_MB);
        // 相对写，向position的位置写入一个byte，并将postion+1，为下次读写作准备
        buffer.put("abcde".getBytes());
        buffer.put("fghij".getBytes());

        // 转换为读取模式
        buffer.flip();

        // 相对读，从position位置读取一个byte，并将position+1，为下次读写作准备
        // 读取第1个字节(a)
        System.out.println((char) buffer.get());

        // 读取第2个字节
        System.out.println((char) buffer.get());

        // 绝对读，读取byteBuffer底层的bytes中下标为index的byte，不改变position
        // 读取第3个字节
        System.out.println((char) buffer.get(2));
    }
}
