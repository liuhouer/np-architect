package com.imooc.jvm.lock.syn;

import java.util.List;
import java.util.Vector;

@SuppressWarnings("Duplicates")
public class SynchronizedTest5 {
    public static void main(String[] args) {
        // -server -XX:+DoEscapeAnalysis -XX:+EliminateLocks 2598
        // -server -XX:-DoEscapeAnalysis -XX:-EliminateLocks 2690
        List<Integer> list = new Vector<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            synchronized (list) {
                list.add(i);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
