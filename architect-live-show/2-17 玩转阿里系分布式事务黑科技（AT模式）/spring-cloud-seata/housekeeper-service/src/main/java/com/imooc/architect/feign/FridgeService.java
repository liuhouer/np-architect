package com.imooc.architect.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 姚半仙
 */
@FeignClient(value = "FRIDGE-SERVICE")
@RequestMapping("fridge")
public interface FridgeService {

    @PostMapping("open")
    public String open(@RequestParam("id") Long id);

    @PostMapping("close")
    public String close(@RequestParam("id") Long id);

    @PostMapping("put")
    public String putAnimal(@RequestParam("fridgeId") Long id, @RequestParam("animalId") Long animalId);

}
