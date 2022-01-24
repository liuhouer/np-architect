package com.bfxy.rapid.rpc.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.bfxy.rapid.rpc.codec.RpcDecoder;
import com.bfxy.rapid.rpc.codec.RpcEncoder;
import com.bfxy.rapid.rpc.codec.RpcRequest;
import com.bfxy.rapid.rpc.codec.RpcResponse;
import com.bfxy.rapid.rpc.config.provider.ProviderConfig;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class RpcServer {

	private String serverAddress;
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private volatile Map<String /* interface name */, Object> handlerMap = new HashMap<String, Object>();
	
	
	public RpcServer(String serverAddress) throws InterruptedException {
		this.serverAddress = serverAddress;
		this.start();
	}

	/**
	 * 	$start
	 * @throws InterruptedException
	 */
	private void start() throws InterruptedException {
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		//	tpc = sync + accept  = backlog
		.option(ChannelOption.SO_BACKLOG, 1024)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline cp = ch.pipeline();
				cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
				cp.addLast(new RpcDecoder(RpcRequest.class));
				cp.addLast(new RpcEncoder(RpcResponse.class));
				cp.addLast(new RpcSeverHandler(handlerMap));
			}
		});
		
		
		String[] array = serverAddress.split(":");
		String host = array[0];
		int port = Integer.parseInt(array[1]);
		
		ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					log.info("server success bing to " + serverAddress);
				} else {
					log.info("server fail bing to " + serverAddress);
					throw new Exception("server start fail, cause: " + future.cause());
				}
			}
		});
		
		try {
			channelFuture.await(5000, TimeUnit.MILLISECONDS);	
			if(channelFuture.isSuccess()) {
				log.info("start rapid rpc success! ");
			}
		} catch (InterruptedException e) {
			log.error("start rapid rpc occur Interrupted, ex: " + e);
		}
		
	}
	
	/**
	 * 	$registerProcessor 程序注册器
	 */
	public void registerProcessor(ProviderConfig providerConfig) {
		//key ： providerConfig.insterface (userService接口权限命名)
		//value ： providerConfig.ref (userService接口下的具体实现类 userServiceImpl实例对象)
		handlerMap.put(providerConfig.getInterface(), providerConfig.getRef());
	}
	
	/**
	 * 	$close
	 */
	public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
	

}
