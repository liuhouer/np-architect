package com.imooc.architect.dao;

import com.imooc.architect.entity.Fridge;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author 姚半仙
 */
@Repository
public interface FridgeDao {

    /**
     * 把冰箱门打开
     * @param id
     * @return
     */
    boolean open(@Param("id") Long id);

    /**
     * 把冰箱门关上
     * @param id
     * @return
     */
    boolean close(@Param("id") Long id);

    /**
     * 把大象放进去
     *
     * @param id
     * @param animalId
     * @return
     */
    boolean put(@Param("id") Long id, @Param("animalId") Long animalId);

    /**
     * 把大象放进去
     *
     * @param id
     * @return
     */
    int clear(@Param("id") Long id);

    Fridge lockFridge(@Param("id") Long id);
}
