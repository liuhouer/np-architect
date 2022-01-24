package com.imooc.architect.entity;

import lombok.Data;

/**
 * @author banxian
 */
@Data
public class Fridge {

    // 冰箱ID
    private Long id;

    // 门有没有关上
    private boolean doorOpened;

    // 动物ID
    private Long animalId;

}
