package com.imooc.jvminaction;

/**
 * 该测试简单占用cpu，4个用户线程，一个占用大量cpu资源，3个线程处于空闲状态
 */
public class HoldCPUMain {
    //大量占用cpu
    public static class HoldCPUTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                double a = Math.random() * Math.random();
                System.out.println(a);
            }
        }
    }

    //空闲线程
    public static class LazyTask implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //开启线程，占用cpu
        new Thread(new HoldCPUTask()).start();
        //3个空闲线程
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
        new Thread(new LazyTask()).start();
    }
}