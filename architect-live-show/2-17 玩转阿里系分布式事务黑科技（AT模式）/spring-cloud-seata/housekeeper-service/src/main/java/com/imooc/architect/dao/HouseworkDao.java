package com.imooc.architect.dao;

import com.imooc.architect.entity.Housework;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author banxian
 */
@Repository
public interface HouseworkDao {

    void start(Housework housework);

    void finish(@Param("id") Long id);
}
