package com.imooc.architect.entity;

import lombok.Data;

/**
 * @author banxian
 */
@Data
public class Zoo {

    private Long id;

    private String name;

    // 类型 - 1=大象
    private Long type;

    // 是否当前可用 - 1=可用
    private boolean available;
}
