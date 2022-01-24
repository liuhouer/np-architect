package com.imooc.live.seata.tcc.task.impl;

import com.imooc.live.seata.tcc.dao.FridgeDao;
import com.imooc.live.seata.tcc.dao.ZooDao;
import com.imooc.live.seata.tcc.entity.Fridge;
import com.imooc.live.seata.tcc.entity.Zoo;
import io.seata.rm.tcc.api.BusinessActionContext;
import com.imooc.live.seata.tcc.task.TaskTwo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class TaskTwoImpl implements TaskTwo {

    @Autowired
    private FridgeDao fridgeDao;

    @Override
    public boolean prepare(BusinessActionContext actionContext, int wanted) {
        log.info("***** TaskTwo prepare, xid={}", actionContext.getXid());
        Fridge fridge = fridgeDao.get();

        // lock之前
        log.info("***** TaskTwo [try] zoo={}", fridge);
        if (fridge.getAvailable() < wanted) {
            log.error("冰箱不够用");
            throw new RuntimeException();
        }

        fridgeDao.reserve(wanted);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        log.info("***** TaskTwo commit, xid={}", actionContext.getXid());
        Integer wanted = (Integer) actionContext.getActionContext("wanted");

        log.info("***** TaskTwo commit: wanted={}", wanted);
        fridgeDao.occupy(wanted);

        log.info("***** TaskTwo fridge={}", fridgeDao.get());
        return true;
    }

    @Override
    public boolean cancel(BusinessActionContext actionContext) {
        log.info("***** TaskTwo cancel, xid={}", actionContext.getXid());
        Integer wanted = (Integer) actionContext.getActionContext("wanted");

        log.info("***** TaskTwo cancel: wanted={}", wanted);
        fridgeDao.cancel(wanted);

        log.info("***** TaskTwo fridge={}", fridgeDao.get());
        return true;
    }

}
