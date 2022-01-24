package com.imooc.springcloud;

import lombok.Builder;
import lombok.Data;

/**
 * Created by 半仙.
 */
@Data
@Builder
public class Product {

    private Long productId;

    private String description;

    private Long stock;

}
