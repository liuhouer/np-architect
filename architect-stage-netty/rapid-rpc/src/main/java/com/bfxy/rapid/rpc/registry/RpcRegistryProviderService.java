package com.bfxy.rapid.rpc.registry;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.bfxy.rapid.rpc.config.provider.ProviderConfig;
import com.bfxy.rapid.rpc.utils.FastJsonConvertUtil;
import com.bfxy.rapid.rpc.zookeeper.ZookeeperClient;

/**
 * 
 * RpcRegistryProviderService 服务提供者的注册到zookeeper的核心实现类
 */
public class RpcRegistryProviderService extends RpcRegistryAbstract {

	private ZookeeperClient zookeeperClient;
	
	public RpcRegistryProviderService(ZookeeperClient zookeeperClient) throws Exception {
		this.zookeeperClient = zookeeperClient;
		//	初始化根节点
		if(!zookeeperClient.checkExists(ROOT_PATH)) {
			zookeeperClient.addPersistentNode(ROOT_PATH, ROOT_VALUE);
		}
	}
	
	/**
	 * 	/rapid-rpc   --->  	rapid-rpc-1.0.0
	 * 		/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService:1.0.0
	 * 			/providers
	 * 				/192.168.11.101:5678
	 * 				/192.168.11.102:5678
	 * 		/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService:1.0.1
	 * 			/providers
	 * 				/192.168.11.201:1234
	 * 				/192.168.11.202:1234
	 * 
	 */
	public void registry(ProviderConfig providerConfig) throws Exception {
		//	接口命名： com.bfxy.rapid.rpc.invoke.consumer.test.HelloService		
		String interfaceClass = providerConfig.getInterface();
		//	实例对象：HelloServiceImpl
		Object ref = providerConfig.getRef();
		//	接口对应的版本号：1.0.0
		String version = providerConfig.getVersion();
		
		//	/rapid-rpc/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService:1.0.0
		String registeryKey = ROOT_PATH + "/" + interfaceClass + ":" + version ;
		
		//	如果当前的path不存在 则进行注册到zookeeper
		if(!zookeeperClient.checkExists(registeryKey)) {
			
			/**
			 * 	@Override
				public String hello(String name) {
					return "hello! " + name;
				}
			
				@Override
				public String hello(User user) {
					return "hello! " + user.getName();
				}

			 */
			Method[] methods = ref.getClass().getDeclaredMethods();
			Map<String, String> methodMap = new HashMap<String, String>();
			
			for(Method method : methods) {
				// 	方法名字
				String methodName = method.getName();
				//	入参类型
				Class<?>[] parameterTypes = method.getParameterTypes();
				String methodParameterTypes = "";
				for(Class<?> clazz : parameterTypes) {
					String parameterTypeName = clazz.getName();
					methodParameterTypes += parameterTypeName + ",";
				}
				
				//	hello@java.lang.String
				//	hello@com.bfxy.rapid.rpc.invoke.consumer.test.User
				
				//	自己和大家演示的：hello@com.bfxy.rapid.rpc.invoke.consumer.test.User,java.lang.String
				String key = methodName + "@" + methodParameterTypes.substring(0, methodParameterTypes.length()-1);
				methodMap.put(key, key);
			}
			
			//	持久化操作
			
			//	key: ==>	/rapid-rpc/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService:1.0.0
			//	value: ==> methodMap to json
			zookeeperClient.addPersistentNode(registeryKey, 
					FastJsonConvertUtil.convertObjectToJSON(methodMap));
			
			zookeeperClient.addPersistentNode(registeryKey + PROVIDERS_PATH, "");
		} 
		
		String address = providerConfig.getAddress();
		String registerInstanceKey = registeryKey + PROVIDERS_PATH + "/" + address;
		
		Map<String, String> instanceMap = new HashMap<String, String>(); 
		instanceMap.put("weight", providerConfig.getWeight() + "");
		
		//	key: /rapid-rpc/com.bfxy.rapid.rpc.invoke.consumer.test.HelloService:1.0.0/providers/127.0.0.1:5678
		//	value: instanceMap to json
		this.zookeeperClient.addEphemeralNode(registerInstanceKey, 
				FastJsonConvertUtil.convertObjectToJSON(instanceMap));
		
	}
	
}
