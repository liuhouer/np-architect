package com.bfxy.rapid.rpc.server;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bfxy.rapid.rpc.codec.RpcRequest;
import com.bfxy.rapid.rpc.codec.RpcResponse;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

/**
 * 	$RpcSeverHandler
 * @author 17475
 *
 */
@Slf4j
public class RpcSeverHandler extends SimpleChannelInboundHandler<RpcRequest> {
	
	private Map<String, Object> handlerMap;
	
	private ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(65536));
	

	public RpcSeverHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				RpcResponse response = new RpcResponse();
				response.setRequestId(rpcRequest.getRequestId());
				try {
					Object result = handle(rpcRequest);
					response.setResult(result);
				} catch (Throwable t) {
					response.setThrowable(t);
					log.error("rpc service handle request Throwable: " + t);
				}
				
				ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						if(future.isSuccess()) {
							// afterRpcHook
						}
					}
				});
				
			}
		});
	}

	/**
	 * 	$handle 解析request请求并且去通过反射获取具体的本地服务实例后执行具体的方法
	 * @param request
	 * @return
	 * @throws InvocationTargetException
	 */
	private Object handle(RpcRequest request) throws InvocationTargetException {
		String className = request.getClassName();
		Object serviceRef = handlerMap.get(className);
		Class<?> serviceClass = serviceRef.getClass();
		String methodName = request.getMethodName();
		Class<?>[] paramterTypes = request.getParamterTypes();
		Object[] paramters = request.getParamters();
		
		// JDK relect
		
		// Cglib
		FastClass serviceFastClass = FastClass.create(serviceClass);
		FastMethod servicFastMethod = serviceFastClass.getMethod(methodName, paramterTypes);
		return servicFastMethod.invoke(serviceRef, paramters);
	}

	/**
	 * 	$exceptionCaught 异常处理关闭连接
	 */
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("server caught Throwable: " + cause);
		ctx.close();
	}
	
}
