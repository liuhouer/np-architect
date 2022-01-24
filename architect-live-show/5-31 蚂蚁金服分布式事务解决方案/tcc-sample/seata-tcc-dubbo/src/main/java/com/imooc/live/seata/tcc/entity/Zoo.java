package com.imooc.live.seata.tcc.entity;

import lombok.Data;

@Data
public class Zoo {

    // 动物种类
    private String animalType;

    // 可用数量
    private Integer available;

    // 被锁定
    private Integer locked;

    // 被送进冰箱
    private Integer frozen;
}
