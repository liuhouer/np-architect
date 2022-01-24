package com.bfxy.rapid.rpc.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 	$RpcDecoder
 * @author 17475
 *
 */
public class RpcDecoder extends ByteToMessageDecoder {

	private Class<?> genericClass;

	public RpcDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}	
	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//	如果请求数据包 不足4个字节 直接返回, 如果大于等于4个字节 那么程序继续执行
		if(in.readableBytes() < 4) {
			return;
		}
		
		//	首先记录一下当前的位置
		in.markReaderIndex();
		//	当前请求数据包的大小读取出来
		int dataLength = in.readInt();
		if(in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		
		//	真正读取需要长度的数据包内容
		byte[] data = new byte[dataLength];
		in.readBytes(data);
		
		// 	解码操作 返回指定的对象
		Object obj = Serialization.deserialize(data, genericClass);
		
		//	填充到buffer中，传播给下游handler做实际的处理
		out.add(obj);
	}

}
