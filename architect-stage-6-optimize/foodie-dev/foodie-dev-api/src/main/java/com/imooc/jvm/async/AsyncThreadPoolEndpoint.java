package com.imooc.jvm.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "async-thread-pool")
public class AsyncThreadPoolEndpoint {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @ReadOperation
    public Map<String, Integer> asyncThreadPool() {
        int corePoolSize = this.threadPoolTaskExecutor.getCorePoolSize();
        int poolSize = this.threadPoolTaskExecutor.getPoolSize();
        int maxPoolSize = this.threadPoolTaskExecutor.getMaxPoolSize();
        int keepAliveSeconds = this.threadPoolTaskExecutor.getKeepAliveSeconds();
        int activeCount = this.threadPoolTaskExecutor.getActiveCount();
        int queueSize = this.threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size();

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("corePoolSize", corePoolSize);
        map.put("poolSize", poolSize);
        map.put("maxPoolSize", maxPoolSize);
        map.put("keepAliveSeconds", keepAliveSeconds);
        map.put("activeCount", activeCount);
        map.put("queueSize", queueSize);
        return map;
    }
}
