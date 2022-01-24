package com.bfxy.netty.server;

import java.io.IOException;

import com.bfxy.netty.codec.RequestDecoder;
import com.bfxy.netty.codec.ResponseEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public class Server {


	public static void main(String[] args) throws Exception {
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 1024)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
			    @Override
			    public void initChannel(SocketChannel ch) throws IOException {
			    	ch.pipeline().addLast("requestDecoder", new RequestDecoder(1024 * 1024, 4, 4));
			    	ch.pipeline().addLast("responseEncoder", new ResponseEncoder());
			    	ch.pipeline().addLast("serverHandler", new ServerHandler());
			    }
			});
		// 绑定端口，同步等待成功
		ChannelFuture cf = b.bind(8765).sync();
		System.out.println(" Server start.. ");
		cf.channel().closeFuture().sync();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		
	}

}
