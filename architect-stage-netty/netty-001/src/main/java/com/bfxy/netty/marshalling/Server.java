package com.bfxy.netty.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

	
	public static void main(String[] args) throws InterruptedException {
		
		EventLoopGroup bGroup = new NioEventLoopGroup(1);
		EventLoopGroup wGroup = new NioEventLoopGroup();
		
		ServerBootstrap sb = new ServerBootstrap();
		sb.group(bGroup, wGroup)
		.channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 1024)
		.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
				sc.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
				sc.pipeline().addLast(new ServerHandler());
			}
		});
		
		ChannelFuture cf = sb.bind(8765).sync();
		
		cf.channel().closeFuture().sync();
		bGroup.shutdownGracefully();
		wGroup.shutdownGracefully();
		
		
	}
}
