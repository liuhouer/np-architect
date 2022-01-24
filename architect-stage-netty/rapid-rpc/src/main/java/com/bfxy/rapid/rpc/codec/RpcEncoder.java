package com.bfxy.rapid.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 	$RpcEncoder 编码器
 * @author 17475
 *
 */
public class RpcEncoder extends MessageToByteEncoder<Object> {
	
	private Class<?> genericClass;

	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}	
	
	/**
	 * 	编码器要做的事情
	 * 	1. 把对应的java对象进行编码
	 * 	2. 之后把内容填充到buffer中去
	 * 	3. 写出到Server端
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		if(genericClass.isInstance(msg)) {
			byte[] data = Serialization.serialize(msg);
			// 消息分为：1.包头 (数据包长度)  2.包体(数据包内容)
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}

}
