package com.bfxy.netty.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;


public class MarshallingDecoder {

    private final Unmarshaller unmarshaller;

    /**
     * Creates a new decoder whose maximum object size is {@code 1048576} bytes.
     * If the size of the received object is greater than {@code 1048576} bytes,
     * a {@link StreamCorruptedException} will be raised.
     * 
     * @throws IOException
     * 
     */
    public MarshallingDecoder() throws IOException {
    	this.unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
    }

    protected Object decode(ByteBuf in) throws Exception {
    	//先读取数据包的长度
		int objectSize = in.readInt();
		System.out.println("进行解码操作，首先获取数据总长度为： " + objectSize);
		//进行slice截取从读取位置开始为：in.readerIndex() 读取后objectSize的数据长度
		System.out.println("从in.readerIndex()= "+ in.readerIndex() +" 位置开始, 读取数据长度objectSize=" + objectSize + "的数据！");
		ByteBuf buf = in.slice(in.readerIndex(), objectSize);
		ByteInput input = new ChannelBufferByteInput(buf);
		try {
			//进行解码操作
			this.unmarshaller.start(input);
		    Object obj = this.unmarshaller.readObject();
		    this.unmarshaller.finish();
		    //读取完毕后，更新当前读取起始位置为：in.readerIndex() + objectSize
		    in.readerIndex(in.readerIndex() + objectSize);
		    return obj;
		} finally {
			this.unmarshaller.close();
		}
    }
}
