package com.bfxy.netty.codec;

import java.util.concurrent.atomic.AtomicInteger;

import com.bfxy.netty.protocol.Request;
import com.bfxy.netty.protocol.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;



/**
 * response解码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * | 包头          | 模块号        | 命令号      | 消息类型  |  长度          |   数据       |
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节short
 * 命令号2字节short
 * 长度4字节(描述数据部分字节长度)
 */
public class ResponseDecoder extends LengthFieldBasedFrameDecoder{
	
	/* 数据包基本长度  */
	public static int BASE_LENTH = 4 + 2 + 2 + 4;
	
	public AtomicInteger index = new AtomicInteger(0);
	
	public ResponseDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		if (in == null || in.readableBytes() < BASE_LENTH) {
		    return null;
		}
		
		//记录包头开始的index
		int beginReader;
		
		while(true){
			beginReader = in.readerIndex();
			in.markReaderIndex();
			//包头
			if(in.readInt() == Request.CrcCode){
				break;
			}
			//未读到包头，略过一个字节
			in.resetReaderIndex();
			in.readByte();
			//长度又变得不满足
			if(in.readableBytes() < BASE_LENTH){
				return null;
			}
		};
		
		//模块号
		short module = in.readShort();
		//命令号
		short cmd = in.readShort();
		
		//消息类型
		byte messageType = in.readByte();
		
		//ReentrantLock reentrantLock = new ReentrantLock();
		//reentrantLock.lock();
		
		//长度
		int length = in.readInt();
		
		//判断请求数据包数据是否到齐
		if(in.readableBytes() < length){
			//还原读指针
			in.readerIndex(beginReader);
			return null;
		}
		
		//System.out.println(index.incrementAndGet());
		//reentrantLock.unlock();
		
		
		//读取data数据
		byte[] data = new byte[length];
		in.readBytes(data);
		
		Response response = new Response();
		response.setModule(module);
		response.setCmd(cmd);
		response.setMessageType(messageType);
		response.setData(data);
		
		//继续往下传递 
		return response;
		
    }

}
