package com.imooc.live.seata.tcc.entity;

import lombok.Data;

@Data
public class Fridge {

    private Long id;

    // 可用数量
    private Integer available;

    // 被预定
    private Integer reserved;

    // 被占用
    private Integer occupied;
}
