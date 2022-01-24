package com.imooc.architect.controller;


import com.imooc.architect.entity.Fridge;
import com.imooc.architect.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚半仙
 */
@RestController
@RequestMapping("fridge")
public class FridgeController {

    @Autowired
    private FridgeService fridgeService;

    @PostMapping("open")
    public String open(@RequestParam("id") Long id){
        if (fridgeService.openFridge(id)) {
            return "OK";
        } else {
            throw new IllegalArgumentException("Fridge Not Found");
        }
    }

    @PostMapping("close")
    public String close(@RequestParam("id") Long id){
        if (fridgeService.closeFridge(id)) {
            return "OK";
        } else {
            throw new IllegalArgumentException("Fridge Not Found");
        }
    }

    // 如果里面塞了个东西，或者此时冰箱门被关上了，那就报错
    @PostMapping("put")
    public String putAnimal(@RequestParam("fridgeId") Long id, @RequestParam("animalId") Long animalId){
        if (fridgeService.putAnimal(id, animalId)) {
            return "success";
        } else {
            throw new IllegalArgumentException("Fridge is full or perhaps closed");
        }
    }

    // 如果里面塞了个东西，或者此时冰箱门被关上了，那就报错
    // 返回Animal ID
    @PostMapping("clear")
    public Long clear(@RequestParam("id") Long id){
        Fridge fridge = fridgeService.clearFridge(id);
        return fridge.getAnimalId();
    }
}
