//package com.imooc.jvminaction;
//
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
//// 1. Unsafe导致直接内存溢出报错没有小尾巴
//// 2. -XX:MaxDirectMemorySize=100m对Unsafe不起作用
//public class DirectMemoryTest3 {
//    private static final int GB_1 = 1024 * 1024 * 1024;
//
//    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
//        //通过反射获取Unsafe类并通过其分配直接内存
//        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//
//        int i = 0;
//        while (true) {
//            unsafe.allocateMemory(GB_1);
//            System.out.println(++i);
//        }
//    }
//}