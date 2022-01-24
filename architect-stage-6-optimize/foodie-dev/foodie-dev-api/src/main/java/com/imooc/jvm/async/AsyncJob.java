package com.imooc.jvm.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

@Component
public class AsyncJob {
  @Async
  public Future<String> saveOpLog() throws InterruptedException {
    Thread.sleep(1000L);
    String result = new Date() + "插入操作日志";
    System.out.println(result);
    return new AsyncResult<>(result);
  }

  @Async
  public void test() throws InterruptedException {
    Thread.sleep(1000L);
    System.out.println("异步任务执行完成");
  }
}
