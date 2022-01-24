package com.imooc.architect.service;

import com.imooc.architect.dao.FridgeDao;
import com.imooc.architect.entity.Fridge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 姚半仙.
 */
@Service("fridgeService")
@Slf4j
public class FridgeServiceImpl implements FridgeService {

    @Autowired
    private FridgeDao zooDao;

    @Override
    public boolean openFridge(Long id) {
        return zooDao.open(id);
    }

    @Override
    public boolean closeFridge(Long id) {
        return zooDao.close(id);
    }

    @Override
    public boolean putAnimal(Long fridgeId, Long animalId) {
        return zooDao.put(fridgeId, animalId);
    }

//    @Override
//    public Fridge lockFridge(Long id) {
//        return zooDao.lockFridge(id);
//    }

    @Override
    @Transactional
    public Fridge clearFridge(Long fridgeId) {
        // 加行锁，在这个transaction内别人没法修改
        Fridge fridge = zooDao.lockFridge(fridgeId);
        if (fridge == null) {
            log.error("Fridge not found with ID={}", fridgeId);
            throw new IllegalArgumentException("Fridge not found");
        }

        if (fridge.getAnimalId() == null) {
            log.error("Nothing to clear", fridgeId);
            throw new IllegalStateException("Nothing found");
        }

        if (!fridge.isDoorOpened()) {
            throw new IllegalStateException("Door closed");
        }

        zooDao.clear(fridgeId);

        return fridge;
    }
}
