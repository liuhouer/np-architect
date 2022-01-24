package com.imooc.jvm.objectpool.commonspool;

import java.math.BigDecimal;

public class Money {
  public static Money init() {
    // 假设对象new非常耗时
    try {
      Thread.sleep(10L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return new Money("USD", new BigDecimal("1"));
  }

  private String type;
  private BigDecimal amount;

  public Money(String type, BigDecimal amount) {
    try {
      Thread.sleep(100L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    this.type = type;
    this.amount = amount;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
}