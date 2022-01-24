package com.bfxy.rapid.rpc.invoke.consumer.test;

import java.util.concurrent.ExecutionException;

import com.bfxy.rapid.rpc.client.RpcClient;
import com.bfxy.rapid.rpc.client.RpcFuture;
import com.bfxy.rapid.rpc.client.proxy.RpcAsyncProxy;

public class ConsumerStarter {
	
	public static void sync() {
		//	rpcClient
		RpcClient rpcClient = new RpcClient();
		rpcClient.initClient("127.0.0.1:8765", 3000);
		HelloService helloService = rpcClient.invokeSync(HelloService.class);
		String result = helloService.hello("zhang3");
		System.err.println(result);		
	}
	
	public static void async() throws InterruptedException, ExecutionException {
		RpcClient rpcClient = new RpcClient();
		rpcClient.initClient("127.0.0.1:8765", 3000);
		RpcAsyncProxy proxy = rpcClient.invokeAsync(HelloService.class);
		RpcFuture future = proxy.call("hello", "li4");
		RpcFuture future2 = proxy.call("hello", new User("001", "wang5"));

		Object result = future.get();
		Object result2 = future2.get();
		System.err.println("result: " + result);
		System.err.println("result2: " + result2);

	}
	
	public static void main(String[] args) throws Exception {
		async();
	}
}
