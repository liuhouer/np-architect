package com.bfxy.rabbitmq.api.delay;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

/**
 * <B>中文类名：</B>Consumer4Delay<BR>
 * <B>概要说明：</B><BR>
 * @author bhz（Alienware）
 * @since 2016年7月6日
 */
public class Consumer4Delay {  
  
    public static void main(String[] args) throws Exception {  
          
        ConnectionFactory connectionFactory = new ConnectionFactory() ;  
          
        connectionFactory.setHost("192.168.11.76");
        connectionFactory.setPort(5672);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        Connection connection = connectionFactory.newConnection();
        
        Channel channel = connection.createChannel() ;  
        
        //延迟队列
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("x-delayed-type", "topic");
        channel.exchangeDeclare("delay.exchange", "x-delayed-message", true, false, map);
        channel.queueDeclare("delay.queue", true, false, false, null);
        channel.queueBind("delay.queue", "delay.exchange", "delay.#");
        QueueingConsumer consumer = new QueueingConsumer(channel) ;
        channel.basicConsume("delay.queue", false, consumer) ; 
        System.out.println("---------consume queue---------"); 
        while(true){  
            Delivery delivery = consumer.nextDelivery() ; 
            String msg = new String(delivery.getBody()) ;  
        	try {
        		System.out.println("received: " + msg); 
        		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        }  
          
    }  
      
}  
