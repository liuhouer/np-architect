package com.imooc.jvm.objectpool.commonspool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class MoneyPooledObjectFactory
  implements PooledObjectFactory<Money> {
  public static final Logger LOGGER =
    LoggerFactory.getLogger(MoneyPooledObjectFactory.class);

  @Override
  public PooledObject<Money> makeObject() throws Exception {
    DefaultPooledObject<Money> object = new DefaultPooledObject<>(
      new Money("USD", new BigDecimal("1"))
    );
    LOGGER.info("makeObject..state = {}", object.getState());
    return object;
  }

  @Override
  public void destroyObject(PooledObject<Money> p) throws Exception {
    LOGGER.info("destroyObject..state = {}", p.getState());
  }

  @Override
  public boolean validateObject(PooledObject<Money> p) {
    LOGGER.info("validateObject..state = {}", p.getState());
    return true;
  }

  @Override
  public void activateObject(PooledObject<Money> p) throws Exception {
    LOGGER.info("activateObject..state = {}", p.getState());
  }

  @Override
  public void passivateObject(PooledObject<Money> p) throws Exception {
    LOGGER.info("passivateObject..state = {}", p.getState());
  }
}
