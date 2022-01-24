package com.bfxy.netty.quickstart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//ChannelHandlerAdapter
public class ServerHandler extends ChannelInboundHandlerAdapter {


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server channel active... ");
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "utf-8");
			System.out.println("Server :" + body );
			String response = "进行返回给客户端的响应：" + body ;
			
//			PooledByteBufAllocator pool = new PooledByteBufAllocator();
//			PooledByteBufAllocator.DEFAULT.heapBuffer();
//			ctx.writeAndFlush(Unpooled.wrappedBuffer(response.getBytes()));		
			
			ByteBuf directBuf = Unpooled.directBuffer(1024*10, 1024*32);
			directBuf.writeBytes(response.getBytes());
			ctx.writeAndFlush(directBuf);	

			//.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
			throws Exception {
		System.out.println("读完了");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
			throws Exception {
		System.err.println(t);
		ctx.close();
	}

}
