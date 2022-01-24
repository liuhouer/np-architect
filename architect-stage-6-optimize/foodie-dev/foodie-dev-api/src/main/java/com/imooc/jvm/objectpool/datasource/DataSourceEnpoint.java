package com.imooc.jvm.objectpool.datasource;

import com.google.common.collect.Maps;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

// 可以用/actuator/datasource
@Endpoint(id = "datasource")
public class DataSourceEnpoint {
  private DMDataSource dataSource;

  public DataSourceEnpoint(DMDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @ReadOperation
  public Map<String, Object> pool() {
    GenericObjectPool<MyConnection> pool = dataSource.getPool();
    HashMap<String, Object> map = Maps.newHashMap();
    map.put("numActive", pool.getNumActive());
    map.put("numIdle", pool.getNumIdle());
    map.put("createdCount", pool.getCreatedCount());
    return map;
  }
}
