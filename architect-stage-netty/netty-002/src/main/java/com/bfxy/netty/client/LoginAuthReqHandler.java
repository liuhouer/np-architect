package com.bfxy.netty.client;

import com.bfxy.netty.protocol.MessageType;
import com.bfxy.netty.struct.Header;
import com.bfxy.netty.struct.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	ctx.writeAndFlush(buildLoginReq());
    }
    
    private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	NettyMessage message = (NettyMessage) msg;
		if(message.getHeader() != null 
				&& message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			
			byte loginResult = (byte) message.getBody();
			if (loginResult != (byte) 0) {
				ctx.close();	
		    } else {
		    	System.out.println("Login is ok : " + message);
		    	ctx.fireChannelRead(msg);
		    }
			
		} else {
			 ctx.fireChannelRead(msg);
		}
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	ctx.fireExceptionCaught(cause);
    }
    
}
