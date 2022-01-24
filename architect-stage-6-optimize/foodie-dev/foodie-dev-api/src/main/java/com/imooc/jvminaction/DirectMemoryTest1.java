//package com.imooc.jvminaction;
//
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
//public class DirectMemoryTest1 {
//    private static final int MB_1 = 1024 * 1024;
//
//    public static void main(String[] args) throws IllegalAccessException, NoSuchFieldException {
//        //通过反射获取Unsafe类并通过其分配直接内存
//        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//        unsafeField.setAccessible(true);
//        Unsafe unsafe = (Unsafe) unsafeField.get(null);
//
//        // 分配1M内存，并返回这块内存的起始地址
//        long address = unsafe.allocateMemory(MB_1);
//
//        // 向内存地址中设置对象
//        unsafe.putByte(address, (byte) 1);
//
//        // 从内存中获取对象
//        byte aByte = unsafe.getByte(address);
//        System.out.println(aByte);
//
//        // 释放内存
//        unsafe.freeMemory(address);
//    }
//}