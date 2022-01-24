package com.bfxy.netty.client;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.bfxy.netty.codec.RequestEncoder;
import com.bfxy.netty.codec.ResponseDecoder;
import com.bfxy.netty.protocol.Request;
import com.bfxy.netty.transfer.User;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;



public class Client {

	public static void main(String[] args) throws InterruptedException {
		
	    EventLoopGroup group = new NioEventLoopGroup();
	    
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
			    	ch.pipeline().addLast("responseDecoder", new ResponseDecoder(1024 * 1024, 4, 4));
			    	ch.pipeline().addLast("requestEncoder", new RequestEncoder());
			    	ch.pipeline().addLast("clientHandler", new ClientHandler());
				}
		    });
	    // 发起异步连接操作
	    ChannelFuture future = b.connect(new InetSocketAddress("127.0.0.1", 8765)).sync();

		Channel channel = future.channel();
		
		System.out.println(" Client start.. ");
		
		for (int i = 0; i < 1000; i++) {
			Request request = new Request();
			request.setModule((short) 1);
			request.setCmd((short) 1);
			
			User requestUser = new User();
			requestUser.setUserId("1001");
			requestUser.setUserName("张三");
			requestUser.setAge(20);
			List<String> favorite = new ArrayList<String>();
			favorite.add("足球");
			favorite.add("篮球");
			requestUser.setFavorite(favorite);
			
			request.setData(requestUser.getBytes());
			//发送请求
			channel.writeAndFlush(request);
			//TimeUnit.NANOSECONDS.sleep(1);
		}
			
			
	    future.channel().closeFuture().sync();
	    

		
	}

}
