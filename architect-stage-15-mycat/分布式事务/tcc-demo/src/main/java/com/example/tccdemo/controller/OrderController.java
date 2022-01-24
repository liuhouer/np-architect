package com.example.tccdemo.controller;

import com.example.tccdemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("handleOrder")
    public String handleOrder(int orderId){

        try{
            int result = orderService.handleOrder(orderId);

            if (result == 0) return "success";

            return "fail";
        }catch (Exception e){
            return "fail";
        }

    }

}
