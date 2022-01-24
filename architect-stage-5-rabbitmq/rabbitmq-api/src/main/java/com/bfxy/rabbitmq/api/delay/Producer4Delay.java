package com.bfxy.rabbitmq.api.delay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

public class Producer4Delay {  
      
    public static void main(String[] args) throws Exception {  
          
    	ConnectionFactory connectionFactory = new ConnectionFactory() ;  
    	
        //RabbitMQ-Server安装在本机，所以直接用127.0.0.1
        connectionFactory.setHost("192.168.11.76");
        connectionFactory.setPort(5672);
        Connection connection = connectionFactory.newConnection();
        //创建一个渠道  
        Channel channel = connection.createChannel() ;  
        
		//5 监听
    	channel.addReturnListener(new ReturnListener() {
			public void handleReturn(int replyCode,
						            String replyText,
						            String exchange,
						            String routingKey,
						            AMQP.BasicProperties properties,
						            byte[] body)
					throws IOException {
				System.out.println("**************handleReturn**********");
				System.out.println("replyCode: " + replyCode);
				System.out.println("replyText: " + replyText);
				System.out.println("exchange: " + exchange);
				System.out.println("routingKey: " + routingKey);
				System.err.println("properties:x-delay: " + properties.getHeaders().get("x-delay"));
				System.out.println("body: " + new String(body));
			}
    	});
        Map<String, Object> headers2 = new HashMap<String, Object>();
        headers2.put("x-delay", 5000L);
        AMQP.BasicProperties.Builder props2 = new AMQP.BasicProperties.Builder().headers(headers2);
        channel.basicPublish("delay.exchange", "delay.1111", true, props2.build(), "hello two".getBytes());
//        channel.close();   
//        connection.close();   
           
    }  
  
}  
