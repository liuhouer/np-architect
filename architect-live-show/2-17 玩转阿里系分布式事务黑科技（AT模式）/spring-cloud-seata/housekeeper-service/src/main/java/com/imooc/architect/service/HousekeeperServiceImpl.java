package com.imooc.architect.service;

import com.imooc.architect.dao.HouseworkDao;
import com.imooc.architect.entity.Housework;
import com.imooc.architect.feign.FridgeService;
import com.imooc.architect.feign.ZooService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 姚半仙.
 */
@Service("housekeeperService")
@Slf4j
public class HousekeeperServiceImpl implements HousekeeperService {

    @Autowired
    private FridgeService fridgeService;

    @Autowired
    private ZooService zooService;

    @Autowired
    private HouseworkDao houseworkDao;

    @Override
    @GlobalTransactional(name = "housekeeper-homework",rollbackFor = Exception.class)
    public void putElephantIntoFridge(Long fridgeId, Long animalId) {
        Housework housework = Housework.builder()
                .animalId(animalId)
                .fridgeId(fridgeId)
                .statusId(1).build();

        log.info("start housework - fridgeId={}, animalId={}", fridgeId, animalId);
        houseworkDao.start(housework);

        fridgeService.open(fridgeId);
        log.info("fridge is opened - fridgeId={}", fridgeId);

        zooService.takeAway(animalId);
        log.info("elephant is ready - animalId={}", animalId);

        fridgeService.putAnimal(fridgeId, animalId);
        log.info("elephant is in place in the fridage - fridgeId={}, animalId={}", animalId, fridgeId);

        fridgeService.close(fridgeId);
        log.info("fridge closed - fridgeId={}", fridgeId);

        houseworkDao.finish(housework.getId());
        log.info("work done - id={}", housework.getId());
    }










//        log.info("start housework - fridgeId={}, animalId={}", fridgeId, animalId);
//        houseworkDao.start(housework);
//
//        fridgeService.open(fridgeId);
//        log.info("fridge is opened - fridgeId={}", fridgeId);
//
//        zooService.takeAway(animalId);
//        log.info("elephant is ready - animalId={}", animalId);
//
//        fridgeService.putAnimal(fridgeId, animalId);
//        log.info("elephant is in place in the fridage - fridgeId={}, animalId={}", animalId, fridgeId);
//
//        fridgeService.close(fridgeId);
//        log.info("fridge closed - fridgeId={}", fridgeId);
//
//        houseworkDao.finish(housework.getId());
//        log.info("finished cleaning the zoo - id={}", housework.getId());
}
