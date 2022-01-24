package com.bfxy.netty.quickstart;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

	public static void main(String[] args) throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		.channel(NioSocketChannel.class)
//		.option(ChannelOption.TCP_NODELAY, true)	//不延迟
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
//		.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) //内存池
//		.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)	//自动调整下一次缓冲区建立时分配的空间大小，避免内存的浪费
        
        .option(ChannelOption.SO_SNDBUF, 1024 * 1024)
        .option(ChannelOption.SO_RCVBUF, 1024 * 1024)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		ChannelFuture cf1 = b.connect("127.0.0.1", 8765).syncUninterruptibly();
		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
		Thread.sleep(1000);
		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
//		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
//		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
		
		cf1.channel().closeFuture().sync();
		group.shutdownGracefully();
		
		
	}
}
