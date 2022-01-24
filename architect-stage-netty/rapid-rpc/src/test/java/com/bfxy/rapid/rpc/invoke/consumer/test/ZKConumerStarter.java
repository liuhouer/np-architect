package com.bfxy.rapid.rpc.invoke.consumer.test;

import com.bfxy.rapid.rpc.config.consumer.ConsumerConfig;
import com.bfxy.rapid.rpc.config.consumer.RpcClientConfig;
import com.bfxy.rapid.rpc.registry.RpcRegistryConsumerService;
import com.bfxy.rapid.rpc.zookeeper.CuratorImpl;
import com.bfxy.rapid.rpc.zookeeper.ZookeeperClient;

public class ZKConumerStarter {


	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		ZookeeperClient zookeeperClient = new CuratorImpl("192.168.11.221:2181,192.168.11.222:2181,192.168.11.223:2181", 10000);
		RpcRegistryConsumerService rpcRegistryConsumerService = new RpcRegistryConsumerService(zookeeperClient);
		RpcClientConfig rpcClientConfig = new RpcClientConfig(rpcRegistryConsumerService);
		
		Thread.sleep(1000);

		ConsumerConfig<HelloService> consumerConfig = (ConsumerConfig<HelloService>) rpcClientConfig.getConsumer(HelloService.class, "1.0.0");
		HelloService helloService = consumerConfig.getProxyInstance();
		String result1 = helloService.hello("baihezhuo1");
		System.err.println(result1);	
		
		String result2 = helloService.hello("baihezhuo2");
		System.err.println(result2);	
		
		String result3 = helloService.hello("baihezhuo3");
		System.err.println(result3);
	}
}
