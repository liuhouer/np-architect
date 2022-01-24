package com.bfxy.rabbitmq.api.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Receiver {

	public static void main(String[] args) throws Exception {
		
		
        ConnectionFactory connectionFactory = new ConnectionFactory() ;  
		Map<String, Object> clientProperties = new HashMap<String, Object>();
		clientProperties.put("description", "test001队列的消费者");
		connectionFactory.setClientProperties(clientProperties);
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
		connectionFactory.setVirtualHost("/");
		
		//2 创建线程池 并行执行任务
    	ExecutorService es = Executors.newFixedThreadPool(5);
        //Address[] addrs = new Address[]{ new Address("192.168.11.71", 5672), new Address("192.168.11.72", 5672), new Address("192.168.11.73", 5672)};
    	Address[] addrs = new Address[]{ new Address("192.168.11.81", 5672)};
    	
    	
    	for(int i =0; i < 5000000; i++) {
    		create(connectionFactory, es, addrs);
    	}

        
	}
	
	
	public static void create(ConnectionFactory connectionFactory, ExecutorService es, Address[] addrs) {
		try {
    		Thread.sleep(50);
        	Connection connection = connectionFactory.newConnection(es, addrs) ;  
            
            Channel channel = connection.createChannel();  
            String queueName = "test001";  
            //durable 是否持久化消息
            channel.queueDeclare(queueName, true, false, false, null);  
            //参数：队列名称、是否自动ACK、Consumer
            channel.basicQos(1);
            channel.basicConsume(queueName, false, new MyConsumer(channel)); 
            System.err.println("connected . and closed ");
            channel.close();
            connection.close();				
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}
