package com.imooc.architect.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author banxian
 */
@Data
@Builder
public class Housework {

    private Long id;

    private Long fridgeId;

    private Long animalId;

    // 1 = started ,  2 = finished,  3 = cancelled (后面留给TCC再来讲)
    private Integer statusId;
}
