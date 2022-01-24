package com.bfxy.rabbitmq.api.consumer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	
	public static void main(String[] args) throws Exception {
		
		//1 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setVirtualHost("/");
		//2 创建线程池 并行执行任务
    	ExecutorService es = Executors.newFixedThreadPool(1);
        Address[] addrs = new Address[]{ new Address("192.168.11.81", 5672)};//, new Address("192.168.11.72", 5672), new Address("192.168.11.73", 5672)};
		
        //	bug once more &
		
		for(int i = 0; i < 5000000;i++) {	//0 1 2 3 4

			Connection connection = connectionFactory.newConnection(es, addrs) ;  

			//3 创建Channel
			Channel channel = connection.createChannel();  
			//4 声明
			String queueName = "test001";  
	        //参数: queue名字,是否持久化,独占的queue（仅供此连接）,不使用时是否自动删除, 其他参数
			channel.queueDeclare(queueName, true, false, false, null);
			String msg = "Hello World RabbitMQ " + i;
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("flag", i);
			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
			.deliveryMode(2)
			.contentEncoding("UTF-8")
			.headers(headers).build();
			channel.basicPublish("", queueName , props , msg.getBytes()); 	
			channel.close();
			Thread.sleep(5);
		}
		
			
	}
	
}
