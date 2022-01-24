package com.bfxy.netty.codec;

import com.bfxy.netty.protocol.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 请求编码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * | 包头          | 模块号        | 命令号      |信息类型    |  长度          |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节short
 * 命令号2字节short
 * 长度4字节(描述数据部分字节长度)
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf out) throws Exception {
		
		//包头
		out.writeInt(Response.CrcCode);
		//module
		out.writeShort(response.getModule());
		//cmd
		out.writeShort(response.getCmd());
		//状态码
		out.writeByte(response.getMessageType());
		//长度
		out.writeInt(response.getDataLength());
		//data
		if(response.getData() != null){
			out.writeBytes(response.getData());
		}
		
	}



}
