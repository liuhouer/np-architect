package com.imooc.architect.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author banxian
 */
@Repository
public interface ZooDao {

    /**
     * 把大象拿走
     * @param id - 大象ID
     */
    int takeAway(@Param("id") Long id);
}
