package com.bfxy.rabbitmq.api.dlx;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Receiver4DLXtExchange {

	public static void main(String[] args) throws Exception {
		
		
        ConnectionFactory connectionFactory = new ConnectionFactory() ;  
        
        connectionFactory.setHost("192.168.11.71");
        connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        
        Channel channel = connection.createChannel();  
		//4 声明正常的 exchange queue 路由规则
		String queueName = "test_dlx_queue";
		String exchangeName = "test_dlx_exchange";
		String exchangeType = "topic";
		String routingKey = "group.*";
		//	声明 exchange
		channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);
		
		
		//	注意在这里要加一个特殊的属性arguments: x-dead-letter-exchange
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("x-dead-letter-exchange", "dlx.exchange");
		//arguments.put("x-dead-letter-routing-key", "dlx.*");
		//arguments.put("x-message-ttl", 6000);
		channel.queueDeclare(queueName, false, false, false, arguments);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		
		//dlx declare:
		channel.exchangeDeclare("dlx.exchange", exchangeType, true, false, false, null);
		channel.queueDeclare("dlx.queue", false, false, false, null);
		channel.queueBind("dlx.queue", "dlx.exchange", "#");
		
		
        //	durable 是否持久化消息
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //	参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(queueName, true, consumer);  
        //	循环获取消息  
        while(true){  
            //	获取消息，如果没有消息，这一步将会一直阻塞  
            Delivery delivery = consumer.nextDelivery();  
            String msg = new String(delivery.getBody());    
            System.out.println("收到消息：" + msg);  
        } 
	}
}
