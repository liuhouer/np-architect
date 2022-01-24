package com.bfxy.netty.client;

import com.bfxy.netty.protocol.Response;
import com.bfxy.netty.transfer.User;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * 消息接受处理类
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	try {
	    	Response message = (Response)msg;
			if (message.getModule() == 1) {
				if(message.getCmd() == 1){
					User responseUser = new User();
					responseUser.readFromBytes(message.getData());
					System.out.println("=====客户端=====" + "userId: " + responseUser.getUserId() + ", userName: " + responseUser.getUserName());
				} else if(message.getCmd() == 2){
					//TODO
				}
			} else if (message.getModule() == 1){
				//TODO
			}
		} finally {
			ReferenceCountUtil.release(msg);
		}
    }
}
