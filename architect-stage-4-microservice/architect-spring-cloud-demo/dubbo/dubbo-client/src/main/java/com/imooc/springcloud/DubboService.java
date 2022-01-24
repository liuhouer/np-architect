package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * Created by 半仙.
 */
@Service
@Slf4j
public class DubboService implements IDubboService {

    @Override
    public Product publish(Product prod) {
        log.info("Publishing prod {}", prod.getName());
        return prod;
    }
}
