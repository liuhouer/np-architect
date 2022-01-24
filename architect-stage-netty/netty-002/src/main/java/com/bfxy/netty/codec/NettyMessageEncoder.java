package com.bfxy.netty.codec;

import java.io.IOException;
import java.util.Map;

import com.bfxy.netty.struct.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


public final class NettyMessageEncoder extends MessageToByteEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
    	this.marshallingEncoder = new MarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, ByteBuf sendBuf) throws Exception {
		if(msg == null || msg.getHeader() == null) {
			 throw new Exception("The encode message is null");
		}
		
		//写入包头，起始位置为4 	==> 占4个字节位置
		sendBuf.writeInt((msg.getHeader().getCrcCode()));
		//数据长度			==> 占4个字节位置
		sendBuf.writeInt((msg.getHeader().getLength()));
		//开始写入数据			==> 占8个字节位置
		sendBuf.writeLong((msg.getHeader().getSessionID()));
		//==> 占1个字节位置
		sendBuf.writeByte((msg.getHeader().getType()));
		//==> 占1个字节位置
		sendBuf.writeByte((msg.getHeader().getPriority()));
		//==> 占4个字节位置
		sendBuf.writeInt((msg.getHeader().getAttachment().size()));
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
		    key = param.getKey();
		    keyArray = key.getBytes("UTF-8");
		    sendBuf.writeInt(keyArray.length);
		    sendBuf.writeBytes(keyArray);
		    value = param.getValue();
		    this.marshallingEncoder.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
		    marshallingEncoder.encode(msg.getBody(), sendBuf);
		} else {
			//如果为空则写四个自己进行补位
			sendBuf.writeInt(0);
		}
		//更新数据长度    ：从第四个字节开始更新（因为第四个字节之后的int类型表示整个数据包长度）
		//总长度 = sendBuf.readableBytes() - 4(包头的起始位置) - 4（body数据长度占位长度）
		// 因为netty认为 frame的长度为所载内容的长度，而不是报文的长度。 报文的长度为 length+lengthOffset+lengthFieldLength
		sendBuf.setInt(4, sendBuf.readableBytes() - 4 - 4);
    }
    
    
    
    
    
    
    
    
}
