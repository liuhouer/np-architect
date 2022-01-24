package com.imooc.architect.service;

import com.imooc.architect.dao.ZooDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author banxian
 */
@Service("zooServiceImpl")
@Slf4j
public class ZooServiceImpl implements ZooService {

    @Autowired
    private ZooDao zooDao;

    @Override
    public boolean takeAway(Long id) {
        Integer count = zooDao.takeAway(id);
        log.error("Take away - id={}, count={}", id, count);
        return count > 0;
    }
}
