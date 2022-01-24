package com.bfxy.netty.codec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bfxy.netty.struct.Header;
import com.bfxy.netty.struct.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
    	super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
		marshallingDecoder = new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
		    return null;
		}

		NettyMessage message = new NettyMessage();
		Header header = new Header();
		//获取crcCode			==> 占4个字节位置
		header.setCrcCode(frame.readInt());
		//获取数据包总长度			==> 占4个字节位置
		header.setLength(frame.readInt());
		//获取sessionId			==> 占8个字节位置
		header.setSessionID(frame.readLong());
		//获取消息类型				==> 占1个字节位置
		header.setType(frame.readByte());
		//获取消息优先级			==> 占1个字节位置
		header.setPriority(frame.readByte());
		//获取附件个数				==> 占4个字节位置
		int size = frame.readInt();
		//如果附件个数>0 证明存在附件
		if (size > 0) {
		    Map<String, Object> attch = new HashMap<String, Object>(size);
		    int keySize = 0;
		    byte[] keyArray = null;
		    String key = null;
		    //循环附件个数，取值
		    for (int i = 0; i < size; i++) {
				keySize = frame.readInt();
				keyArray = new byte[keySize];
				frame.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				attch.put(key, marshallingDecoder.decode(frame));
		    }
		    keyArray = null;
		    key = null;
		    header.setAttachment(attch);
		}
		//如果最终数据包>4 证明是有数据的，则开始解码
		if (frame.readableBytes() > 4) {
		    message.setBody(marshallingDecoder.decode(frame));
		}
		message.setHeader(header);
		return message;
    }
}
