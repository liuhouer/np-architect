package com.example.shardingjdbcdemo;

import com.example.shardingjdbcdemo.dao.AreaMapper;
import com.example.shardingjdbcdemo.dao.OrderItemMapper;
import com.example.shardingjdbcdemo.dao.OrderMapper;
import com.example.shardingjdbcdemo.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingJdbcDemoApplicationTests {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private AreaMapper areaMapper;
    @Resource
    private OrderItemMapper orderItemMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    @Transactional
    public void testOrder() {
        Order order = new Order();
        order.setOrderId(1l);
        order.setUserId(15);
        order.setOrderAmount(BigDecimal.TEN);
        order.setOrderStatus(1);
        orderMapper.insertSelective(order);

        Order order2 = new Order();
        order2.setOrderId(2l);
        order2.setUserId(16);
        order2.setOrderAmount(BigDecimal.TEN);
        order2.setOrderStatus(1);
        orderMapper.insertSelective(order2);

        throw new RuntimeException("test XA");
    }

    @Test
    public void testSelectOrder(){
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderIdEqualTo(4l)
                .andUserIdEqualTo(20);
        List<Order> orders = orderMapper.selectByExample(orderExample);
        orders.forEach(o-> System.out.println(o.getOrderId()+"----"+o.getUserId()));
    }

    @Test
    public void testGlobal(){
        Area area = new Area();
        area.setId(2);
        area.setName("上海");
        areaMapper.insert(area);
    }

    @Test
    public void testSelectGlobal() {
        AreaExample areaExample = new AreaExample();
        areaExample.createCriteria().andIdEqualTo(2);
        List<Area> areas = areaMapper.selectByExample(areaExample);
        System.out.println("area.size() = "+areas.size());

    }

    @Test
    public void testOrderItem(){
        OrderItem orderItem = new OrderItem();
        orderItem.setId(2);
        orderItem.setOrderId(1);
        orderItem.setPruductName("测试商品");
        orderItem.setNum(1);
        orderItem.setUserId(19);
        orderItemMapper.insert(orderItem);
    }

    @Test
    public void testMsOrder() {
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUserIdEqualTo(20)
                .andOrderIdEqualTo(4l);
        for (int i =0 ;i<10;i++){
            List<Order> orders = orderMapper.selectByExample(orderExample);
            orders.forEach(o->{
                System.out.println("orderId:"+o.getOrderId());
                System.out.println("userId:"+o.getUserId());
                System.out.println("orderAmount:"+o.getOrderAmount());
            });
        }
    }

}
