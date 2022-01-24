package com.bfxy.netty.server;

import java.util.ArrayList;
import java.util.List;

import com.bfxy.netty.protocol.MessageType;
import com.bfxy.netty.protocol.Request;
import com.bfxy.netty.protocol.Response;
import com.bfxy.netty.transfer.User;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	
		Request message = (Request)msg;
		
		if(message.getModule() == 1) {
			if(message.getCmd() == 1){
				
				User requestUser = new User();
				requestUser.readFromBytes(message.getData());
				System.out.println("====服务器端====:" + "userId: " + requestUser.getUserId() + "   " + "userName: " + requestUser.getUserName());
				
				//回写数据
				User responseUser = new User();
				responseUser.setUserId("1002");
				responseUser.setUserName("李四");
				responseUser.setAge(29);
				List<String> favorite = new ArrayList<String>();
				favorite.add("足球");
				favorite.add("篮球");
				requestUser.setFavorite(favorite);
				
				Response response = new Response();
				response.setModule((short) 1);
				response.setCmd((short) 1);
				response.setMessageType(MessageType.SUCCESS.value());
				response.setData(responseUser.getBytes());
				ctx.writeAndFlush(response);
				
			} else if(message.getCmd() == 2){
				
			}
			
		} else if (message.getModule() == 2){
			
			
		}    	
    }



}
