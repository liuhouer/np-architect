package com.imooc.springcloud;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 半仙.
 */
@Data
public class Product implements Serializable {

    private String name;
    private BigDecimal price;

}
