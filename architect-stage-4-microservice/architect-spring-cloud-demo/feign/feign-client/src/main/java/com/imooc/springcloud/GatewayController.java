package com.imooc.springcloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 半仙.
 */
@RestController
@Slf4j
@RequestMapping("gateway")
public class GatewayController {

    public static final Map<Long, Product> items = new ConcurrentHashMap<>();

    @RequestMapping(value = "details", method = RequestMethod.GET)
    @ResponseBody
    public Product get(@RequestParam("pid") Long pid) {
        if (!items.containsKey(pid)) {
            Product prod = Product.builder()
                    .productId(pid)
                    .description("好吃不贵")
                    .stock(100L)
                    .build();
            items.putIfAbsent(pid, prod);
        }
        return items.get(pid);
    }

    @RequestMapping(value = "placeOrder", method = RequestMethod.POST)
    public String buy(@RequestParam("pid") Long pid) {
        Product prod = items.get(pid);

        if (prod == null) {
            return "Product not found";
        } else if (prod.getStock() <= 0L) {
            return "Sold out";
        }

        synchronized (prod) {
            if (prod.getStock() <= 0L) {
                return "Sold out";
            }
            prod.setStock(prod.getStock() - 1);
        }

        return "Order Placed";
    }

}
