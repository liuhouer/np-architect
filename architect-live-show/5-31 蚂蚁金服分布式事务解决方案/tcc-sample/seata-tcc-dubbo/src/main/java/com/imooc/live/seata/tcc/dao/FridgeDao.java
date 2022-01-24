package com.imooc.live.seata.tcc.dao;

import com.imooc.live.seata.tcc.entity.Fridge;
import com.imooc.live.seata.tcc.entity.Zoo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author banxian
 */
@Repository
public interface FridgeDao {

    Fridge get();

    int reserve(@Param("num") Integer num);
    int occupy(@Param("num") Integer num);
    int cancel(@Param("num") Integer num);

}
