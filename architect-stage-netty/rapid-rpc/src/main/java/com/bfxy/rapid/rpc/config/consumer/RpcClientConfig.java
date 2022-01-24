package com.bfxy.rapid.rpc.config.consumer;

import com.bfxy.rapid.rpc.registry.RpcRegistryConsumerService;

/**
 * 
 * RpcClientConfig 客户端的核心配置类
 *
 */
public class RpcClientConfig {

	private RpcRegistryConsumerService rpcRegistryConsumerService;
	
	public RpcClientConfig(RpcRegistryConsumerService rpcRegistryConsumerService) {
		this.rpcRegistryConsumerService = rpcRegistryConsumerService;
	}
	
	/**
	 * getConsumer 通过具体的接口类权限命名 + 版本号 获取一个实际的ConsumerConfig 也就是获取了代理对象
	 * @param <T>
	 * @param clazz
	 * @param version
	 * @return ConsumerConfig
	 */
	public <T> ConsumerConfig<?> getConsumer(Class<T> clazz, String version){
		return rpcRegistryConsumerService.getConsumer(clazz.getName(), version);
	}
	
}
