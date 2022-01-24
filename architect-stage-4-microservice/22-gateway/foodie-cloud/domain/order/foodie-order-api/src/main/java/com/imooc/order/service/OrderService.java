package com.imooc.order.service;

import com.imooc.order.pojo.OrderStatus;
import com.imooc.order.pojo.bo.PlaceOrderBO;
import com.imooc.order.pojo.bo.SubmitOrderBO;
import com.imooc.order.pojo.vo.OrderVO;
import com.imooc.pojo.ShopcartBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("foodie-order-service")
@RequestMapping("order-api")
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param orderBO
     */
    @PostMapping("placeOrder")
    public OrderVO createOrder(@RequestBody PlaceOrderBO orderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    @PostMapping("updateStatus")
    public void updateOrderStatus(@RequestParam("orderId") String orderId,
                                  @RequestParam("orderStatus") Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    @GetMapping("orderStatus")
    public OrderStatus queryOrderStatusInfo(@RequestParam("orderId") String orderId);

    /**
     * 关闭超时未支付订单
     */
    @PostMapping("closePendingOrders")
    public void closeOrder();

}
