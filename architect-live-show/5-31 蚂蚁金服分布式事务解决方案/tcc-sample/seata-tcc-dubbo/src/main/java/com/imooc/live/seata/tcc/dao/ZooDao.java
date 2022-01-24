package com.imooc.live.seata.tcc.dao;

import com.imooc.live.seata.tcc.entity.Zoo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author banxian
 */
@Repository
public interface ZooDao {

    Zoo getElephant();

    int lock(@Param("num") Integer num);
    int freeze(@Param("num") Integer num);
    int cancel(@Param("num") Integer num);

}
