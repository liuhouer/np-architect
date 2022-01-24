package com.imooc.live.seata.tcc;

import java.io.IOException;

import io.seata.core.store.StoreMode;
import io.seata.server.Server;

/**
 * 启动Seata服务器
 */
public class SeataServer {

    public static void main(String[] args) throws IOException {
        // 这里采用最简单的seata默认配置（全部基于本地文件管理），点进去main方法可以看到各个默认项
        // 对seata定制感兴趣的同学，可以参考我2月份的直播源码（更改seata配置直连eureka，并将全局/分支事务状态存到数据库）
        new Server().main(args);
    }

}
