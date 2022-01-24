package com.imooc.architect.controller;


import com.imooc.architect.service.HousekeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 姚半仙
 */
@RestController
@RequestMapping("housework")
public class HousekeeperController {

    @Autowired
    private HousekeeperService housekeeperService;

    @PostMapping("putInfoFridge")
    public String open(@RequestParam("fridgeId") Long fridgeId, @RequestParam("animalId") Long animalId){
        housekeeperService.putElephantIntoFridge(fridgeId, animalId);
        return "OK";
    }

}
