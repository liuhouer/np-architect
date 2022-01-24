package com.imooc.jvminaction;


import java.util.HashSet;
import java.util.Set;

/**
 * JDK 6: -XX:PermSize=6m -XX:MaxPermSize=6m
 * 报永久代溢出(java.lang.OutOfMemoryError: PermGen space)
 * ==========
 * JDK 7: -XX:PermSize=6m -XX:MaxPermSize=6m
 * 不报错，原因：JDK 7把字符串常量池放到堆了，设置-Xmx6m会报堆内存溢出
 * ==========
 * JDK 8+：同JDK 7
 */
public class MethodAreaOOMTest1 {
    public static void main(String[] args) {
        // 使用Set保持着常量池引用
        Set<String> set = new HashSet<String>();
        int i = 0;
        while (true) {
            // intern()：native方法
            // 如果字符串常量池里面已经包含了等于字符串x的字符串；那么
            // 就返回常量池中这个字符串的引用
            // 如果常量池中不存在，那么就会把当前字符串添加到常量池，
            // 并返回这个字符串的引用
            set.add(String.valueOf(i++).intern());
        }
    }
}