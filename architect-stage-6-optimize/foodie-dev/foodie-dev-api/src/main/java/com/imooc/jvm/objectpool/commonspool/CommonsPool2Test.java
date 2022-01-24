package com.imooc.jvm.objectpool.commonspool;

import org.apache.commons.pool2.impl.GenericObjectPool;

public class CommonsPool2Test {
  public static void main(String[] args) throws Exception {
    GenericObjectPool<Money> pool = new GenericObjectPool<>(new MoneyPooledObjectFactory());
    Money money = pool.borrowObject();
    money.setType("RMB");
    pool.returnObject(money);
  }
}
