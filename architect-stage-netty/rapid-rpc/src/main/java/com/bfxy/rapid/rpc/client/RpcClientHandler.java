package com.bfxy.rapid.rpc.client;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bfxy.rapid.rpc.codec.RpcRequest;
import com.bfxy.rapid.rpc.codec.RpcResponse;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 	$RpcClientHandler 实际的业务处理器(Handler)
 * @author 17475
 *
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

	private Channel channel;
	
	private SocketAddress remotePeer;
	
	private Map<String /*requestId*/, RpcFuture> pendingRpcTable = new ConcurrentHashMap<String, RpcFuture>();
	
    public Channel getChannel() {
        return channel;
    }
    
	/**
	 * 通道激活的时候触发此方法
	 */
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		this.channel = ctx.channel();
	}
	
	/**
	 * 通道激活的时候触发此方法
	 */
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.remotePeer = this.channel.remoteAddress();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
		String requestId = rpcResponse.getRequestId();
		RpcFuture rpcFuture = pendingRpcTable.get(requestId);
		if(rpcFuture != null) {
			pendingRpcTable.remove(requestId);
			rpcFuture.done(rpcResponse);
		}
	}

	public SocketAddress getRemotePeer() {
		return this.remotePeer;
	}

	/**
	 * Netty提供了一种主动关闭连接的方式.发送一个Unpooled.EMPTY_BUFFER 这样我们的ChannelFutureListener的close事件就会监听到并关闭通道
	 */
	public void close() {
		channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	
	/**
	 * 	异步发送请求方法
	 * @param request
	 * @return
	 */
	public RpcFuture sendRequest(RpcRequest request) {
		RpcFuture rpcFuture = new RpcFuture(request);
		pendingRpcTable.put(request.getRequestId(), rpcFuture);
		channel.writeAndFlush(request);
		return rpcFuture;
	}

}
