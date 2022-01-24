package com.bfxy.rapid.rpc.client.proxy;

import com.bfxy.rapid.rpc.client.RpcFuture;

/**
 * 	$RpcAsyncProxy 异步代理接口
 * @author 17475
 *
 */
public interface RpcAsyncProxy {

	RpcFuture call(String funcName, Object... args);
	
}
