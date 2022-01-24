package com.imooc.jvm.async;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncDemoTheadPool {
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

  private void subBiz1() throws InterruptedException {
    Thread.sleep(1000L);
    System.out.println(new Date() + "subBiz1");
  }

  private void subBiz2() throws InterruptedException {
    Thread.sleep(1000L);
    System.out.println(new Date() + "biz2");
  }


  private void saveOpLog() throws InterruptedException {
    executor.submit(new SaveOpLogThread());
  }

  private void biz() throws InterruptedException {
    this.subBiz1();
    this.saveOpLog();
    this.subBiz2();

    System.out.println(new Date() + "执行结束");
  }

  public static void main(String[] args) throws InterruptedException {
    new AsyncDemoTheadPool().biz();
  }
}

class SaveOpLogThread2 implements Runnable {

  @Override
  public void run() {
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(new Date() + "插入操作日志");
  }
}