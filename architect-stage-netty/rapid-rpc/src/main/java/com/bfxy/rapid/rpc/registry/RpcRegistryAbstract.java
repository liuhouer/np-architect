package com.bfxy.rapid.rpc.registry;

/**
 * 	RpcRegistryAbstract
 * 
 * 	/rapid-rpc   --->  	rapid-rpc-1.0.0
 * 		/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService
 * 			/providers
 * 				/192.168.11.101:5678
 * 				/192.168.11.102:5679
 * 			/consumers
 * 				/192.168.11.103
 * 
 * 
 * 		/com.bfxy.rapid.rpc.invoke.consumer.test.UserService
 * 			/providers
 * 				/192.168.11.101:5678
 * 				/192.168.11.102:5679
 * 			/consumers
 * 				/192.168.11.103
 * 
 */
public abstract class RpcRegistryAbstract {
	
	protected final String ROOT_PATH = "/rapid-rpc";
	
	protected final String ROOT_VALUE = "rapid-rpc-1.0.0";
	
	protected final String PROVIDERS_PATH = "/providers";

	protected final String CONSUMERS_PATH = "/consumers";

}
