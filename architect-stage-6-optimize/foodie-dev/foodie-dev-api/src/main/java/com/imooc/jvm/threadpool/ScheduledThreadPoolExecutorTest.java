package com.imooc.jvm.threadpool;

import java.util.Date;
import java.util.concurrent.*;

public class ScheduledThreadPoolExecutorTest {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    ScheduledThreadPoolExecutor executor
      = new ScheduledThreadPoolExecutor(
      10,
      Executors.defaultThreadFactory(),
      new ThreadPoolExecutor.AbortPolicy()
    );
//    // 延时3秒之后再去执行任务
//    executor.schedule(
//      new Runnable() {
//        @Override
//        public void run() {
//          System.out.println("aaa");
//        }
//      },
//      3,
//      TimeUnit.SECONDS
//    );
//
//    // 延时4秒之后再去执行任务，可以返回执行结果
//    ScheduledFuture<String> future = executor.schedule(new Callable<String>() {
//      @Override
//      public String call() throws Exception {
//        return "bbb";
//      }
//    }, 4, TimeUnit.SECONDS);
//    String s = future.get();
//    System.out.println(s);

    executor.scheduleAtFixedRate(
      new Runnable() {
        @Override
        public void run() {
          System.out.println("scheduleAtFixedRate" + new Date());
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      },
      // 第一次执行任务时，延时多久
      0,
      // 每个多久执行这个任务
      3, TimeUnit.SECONDS
    );

    executor.scheduleWithFixedDelay(
      new Runnable() {
        @Override
        public void run() {
          System.out.println("scheduleWithFixedDelay" + new Date());
          try {
            Thread.sleep(1000L);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      },
      // 第一次执行任务时，延时多久
      0,
      // 每次执行完任务之后，延迟多久再次执行这个任务
      3, TimeUnit.SECONDS
    );
  }
}
