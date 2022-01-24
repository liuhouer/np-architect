package com.imooc.live.seata.tcc.task.impl;

import com.imooc.live.seata.tcc.dao.ZooDao;
import com.imooc.live.seata.tcc.entity.Zoo;
import com.imooc.live.seata.tcc.task.TaskOne;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class TaskOneImpl implements TaskOne {

    @Autowired
    private ZooDao zooDao;

    @Override
    @Transactional
    public boolean prepare(BusinessActionContext actionContext, int num) {
        log.info("***** TaskOne prepare, xid={}", actionContext.getXid());
        Zoo zoo = zooDao.getElephant();

        // lock之前
        log.info("***** TaskOne [try] zoo={}", zoo);
        if (zoo.getAvailable() < num) {
            log.error("你要的我不能给, available={}, wanted={}", zoo.getAvailable(), num);
            throw new RuntimeException();
        }
        zooDao.lock(num);

        // lock之后 - 纯属打log用
        log.info("***** TaskOne [try] zoo={}", zooDao.getElephant());
        return true;
    }

    @Override
    @Transactional
    public boolean commit(BusinessActionContext actionContext) {
        log.info("***** TaskOne commit, xid={}", actionContext.getXid());
        Integer wanted = (Integer) actionContext.getActionContext("wanted");

        log.info("***** TaskOne commit: wanted={}", wanted);
        zooDao.freeze(wanted);

        log.info("***** TaskOne zoo={}", zooDao.getElephant());
        return true;
    }

    @Override
    @Transactional
    public boolean cancel(BusinessActionContext actionContext) {
        log.info("***** TaskOne cancel, xid={}", actionContext.getXid());
        Integer wanted = (Integer) actionContext.getActionContext("wanted");

        log.info("***** TaskOne cancel: wanted={}", wanted);
        zooDao.cancel(wanted);

        log.info("***** TaskOne zoo={}", zooDao.getElephant());
        return true;
    }
}
