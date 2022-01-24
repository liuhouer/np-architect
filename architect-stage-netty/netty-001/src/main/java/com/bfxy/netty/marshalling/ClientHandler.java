package com.bfxy.netty.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try {
			ResponseData rd = (ResponseData)msg;
			System.err.println("输出服务器端相应内容: " + rd.getId());
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
}
