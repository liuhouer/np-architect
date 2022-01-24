package com.bfxy.rapid.rpc.client;

import com.bfxy.rapid.rpc.codec.RpcDecoder;
import com.bfxy.rapid.rpc.codec.RpcEncoder;
import com.bfxy.rapid.rpc.codec.RpcRequest;
import com.bfxy.rapid.rpc.codec.RpcResponse;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 	$RpcClientInitializer
 * @author 17475
 *
 */
public class RpcClientInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		ChannelPipeline cp = ch.pipeline();
		//	编解码的handler
		cp.addLast(new RpcEncoder(RpcRequest.class));
//	     * @param lengthFieldOffset
//	     *        the offset of the length field
//	     * @param lengthFieldLength
//	     *        the length of the length field
		cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
		cp.addLast(new RpcDecoder(RpcResponse.class));
		//	实际的业务处理器RpcClientHandler
		cp.addLast(new RpcClientHandler());
		
		
	}

}
