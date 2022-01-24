package com.bfxy.netty.server;

import java.io.IOException;

import com.bfxy.netty.codec.NettyMessageDecoder;
import com.bfxy.netty.codec.NettyMessageEncoder;
import com.bfxy.netty.protocol.NettyConstant;

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
import io.netty.handler.timeout.ReadTimeoutHandler;


public class NettyServer {

    public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {
			    @Override
			    public void initChannel(SocketChannel ch)
				    throws IOException {
				ch.pipeline().addLast(
					new NettyMessageDecoder(1024 * 1024, 4, 4));
				ch.pipeline().addLast(new NettyMessageEncoder());
				ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
				ch.pipeline().addLast(new LoginAuthRespHandler());
				ch.pipeline().addLast("HeartBeatHandler", new HeartBeatRespHandler());
			    }
			});
		ChannelFuture cf = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
		System.out.println("Netty com.bfxy.netty.server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
		cf.channel().closeFuture().sync();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();  
    }

    public static void main(String[] args) throws Exception {
    	new NettyServer().bind();
    }
}
