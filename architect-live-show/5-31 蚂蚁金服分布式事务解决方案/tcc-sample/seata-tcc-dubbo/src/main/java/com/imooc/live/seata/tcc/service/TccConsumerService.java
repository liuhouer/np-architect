package com.imooc.live.seata.tcc.service;

import com.imooc.live.seata.tcc.task.TaskOne;
import io.seata.core.context.RootContext;
import com.imooc.live.seata.tcc.task.TaskTwo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TccConsumerService {

    // 这里虽然用了注入，但是真正发起事务调用的时候，请求会被代理发送到dubbo的service provider，
    // 如果IDE这里报红字'Cannot autowire'，不用理会，IDE没文化不识别Dubbo的配置
    @Autowired
    private TaskOne taskOne;

    @Autowired
    private TaskTwo taskTwo;

    @GlobalTransactional
    public String success() {
        taskOne.prepare(null, 1);
        taskTwo.prepare(null, 1);
        return RootContext.getXID();
    }

    @GlobalTransactional
    public String fail() {
        taskOne.prepare(null, 1);
        taskTwo.prepare(null, 1);
        throw new RuntimeException("error");
    }

}
