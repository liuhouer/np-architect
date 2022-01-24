package com.bfxy.netty.codec;

import java.util.concurrent.atomic.AtomicInteger;

import com.bfxy.netty.protocol.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class RequestDecoder extends LengthFieldBasedFrameDecoder {

	/* 数据包基本长度  */
	public static int BASE_LENTH = 4 + 2 + 2 + 4;
	
	public AtomicInteger index = new AtomicInteger(0);
	
	public RequestDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
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
		}
		
		//模块号
		short module = in.readShort();
		//命令号
		short cmd = in.readShort();
		
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
		
		Request request = new Request();
		request.setModule(module);
		request.setCmd(cmd);
		request.setData(data);
		
		//继续往下传递 
		return request;
		
    }


}
