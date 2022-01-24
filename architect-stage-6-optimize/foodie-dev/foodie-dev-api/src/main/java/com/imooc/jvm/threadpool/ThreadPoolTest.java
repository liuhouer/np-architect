package com.imooc.jvm.threadpool;

import java.util.concurrent.*;

public class ThreadPoolTest {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ThreadPoolExecutor executor =
      new ThreadPoolExecutor(
        5,
        10,
        // 默认情况下指的是非核心线程的空闲时间
        // 如果allowCoreThreadTimeOut=true：核心线程/非核心线程允许的空闲时间
        10L,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(100),
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy()
      );
    executor.allowCoreThreadTimeOut(true);
    // 核心线程 -> 正式员工
    // 非核心线程 -> 临时工
    executor.execute(new Runnable() {
      @Override
      public void run() {
        System.out.println("线程池测试");
      }
    });

    executor.execute(new Runnable() {
      @Override
      public void run() {
        System.out.println("线程池测试2");
      }
    });

    Future<String> future = executor.submit(new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "测试submit";
      }
    });
    String s = future.get();
    System.out.println(s);


  }
}
