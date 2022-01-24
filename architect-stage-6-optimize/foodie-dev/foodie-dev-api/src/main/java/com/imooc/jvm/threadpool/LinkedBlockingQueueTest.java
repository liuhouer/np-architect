package com.imooc.jvm.threadpool;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueTest {
  public static void main(String[] args) {
    LinkedBlockingQueue<Object> queue =
      new LinkedBlockingQueue<>(1);
    queue.add("abc");
    boolean def = queue.offer("def");
    System.out.println(def);
    queue.add("g");
  }
}
