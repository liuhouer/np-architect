package com.bfxy.netty.client;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.bfxy.netty.protocol.MessageType;
import com.bfxy.netty.struct.Header;
import com.bfxy.netty.struct.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	NettyMessage message = (NettyMessage) msg;
    	
		if (message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			
		    this.heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
		
		} else if (message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
			
		    System.out.println("Client receive com.bfxy.netty.server heart beat message : ---> " + message);
		
		} else {
			 ctx.fireChannelRead(msg);
		}
	}

    private class HeartBeatTask implements Runnable {
		private final ChannelHandlerContext ctx;
	
		public HeartBeatTask(final ChannelHandlerContext ctx) {
		    this.ctx = ctx;
		}
	
		@Override
		public void run() {
		    NettyMessage heatBeat = buildHeatBeat();
		    System.out.println("Client send heart beat messsage to com.bfxy.netty.server : ---> " + heatBeat);
		    ctx.writeAndFlush(heatBeat);
		}
	
		private NettyMessage buildHeatBeat() {
		    NettyMessage message = new NettyMessage();
		    Header header = new Header();
		    header.setType(MessageType.HEARTBEAT_REQ.value());
		    message.setHeader(header);
		    return message;
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	
    	System.err.println("感知到Server端关闭。取消心跳任务");
    	
		cause.printStackTrace();
		if (heartBeat != null) {
		    heartBeat.cancel(true);
		    heartBeat = null;
		}
		ctx.fireExceptionCaught(cause);
    }
    
}
