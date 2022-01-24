package com.bfxy.netty.client;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.bfxy.common.protobuf.MessageModule;
import com.bfxy.execute.MessageTask4Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	ThreadPoolExecutor workerPool = new ThreadPoolExecutor(5,
			10,
			60L,
			TimeUnit.SECONDS, 
			new ArrayBlockingQueue<>(4000),
			new ThreadPoolExecutor.DiscardPolicy());
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	MessageModule.Message response = (MessageModule.Message)msg;
    	workerPool.submit(new MessageTask4Response(response, ctx));
    }
    
}
