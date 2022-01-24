package com.imooc.jvminaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注：
 * 本示例可能会造成机器假死，请慎重测试！！！
 * 本示例可能会造成机器假死，请慎重测试！！！
 * 本示例可能会造成机器假死，请慎重测试！！！
 * =======
 * -Xms=10m -Xmx=10m -Xss=144k -XX:MetaspaceSize=10m
 * =======
 * cat /proc/sys/kernel/threads-max
 * - 作用：系统支持的最大线程数，表示物理内存决定的理论系统进程数上限，一般会很大
 * - 修改：sysctl -w kernel.threads-max=7726
 * =======
 * cat /proc/sys/kernel/pid_max
 * - 作用：查看系统限制某用户下最多可以运行多少进程或线程
 * - 修改：sysctl -w kernel.pid_max=65535
 * =======
 * cat /proc/sys/vm/max_map_count
 * - 作用：限制一个进程可以拥有的VMA(虚拟内存区域)的数量，虚拟内存区域是一个连续的虚拟地址空间区域。
 * 在进程的生命周期中，每当程序尝试在内存中映射文件，链接到共享内存段，或者分配堆空间的时候，这些区域将被创建。
 * - 修改：sysctl -w vm.max_map_count=262144
 * =======
 * * ulimit –u
 * * - 作用：查看用户最多可启动的进程数目
 * * - 修改：ulimit -u 65535
 *
 * @author 大目
 */
public class StackOOMTest3 {
    public static final Logger LOGGER = LoggerFactory.getLogger(StackOOMTest3.class);
    private AtomicInteger integer = new AtomicInteger();

    private void dontStop() {
        while (true) {
        }
    }

    public void newThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
            LOGGER.info("线程创建成功，threadName = {}", thread.getName());

            integer.incrementAndGet();
        }
    }

    public static void main(String[] args) {
        StackOOMTest3 oom = new StackOOMTest3();
        try {
            oom.newThread();
        } catch (Throwable throwable) {
            LOGGER.info("创建的线程数：{}", oom.integer);
            LOGGER.error("异常发生", throwable);
            System.exit(1);
        }
    }
}