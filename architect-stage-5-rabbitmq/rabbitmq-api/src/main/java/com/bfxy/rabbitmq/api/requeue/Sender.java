package com.bfxy.rabbitmq.api.requeue;

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	
	public static void main(String[] args) throws Exception {
		
		//1 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.11.71");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		//2 创建Connection
		Connection connection = connectionFactory.newConnection();
		//3 创建Channel
		Channel channel = connection.createChannel();  
		//4 声明
		String queueName = "test001";  
        //参数: queue名字,是否持久化,独占的queue（仅供此连接）,不使用时是否自动删除, 其他参数
		channel.queueDeclare(queueName, true, false, false, null);
		
		for(int i = 0; i < 5;i++) {
			String msg = "Hello World RabbitMQ " + i;
			Map<String, Object> headers = new HashMap<String, Object>();
			headers.put("flag", i);
			AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
			.deliveryMode(2)
			.contentEncoding("UTF-8")
			.headers(headers).build();
			channel.basicPublish("", queueName , props , msg.getBytes()); 			
		}
	}
	
}
