package com.bfxy.netty.server;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bfxy.netty.protocol.MessageType;
import com.bfxy.netty.struct.Header;
import com.bfxy.netty.struct.NettyMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
    private String[] whitekList = { "127.0.0.1", "192.168.1.200" };

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	NettyMessage message = (NettyMessage) msg;

		if (message.getHeader() != null
			&& message.getHeader().getType() == MessageType.LOGIN_REQ
				.value()) {
		    String nodeIndex = ctx.channel().remoteAddress().toString();
		    NettyMessage loginResp = null;
		    if (nodeCheck.containsKey(nodeIndex)) {
		    	loginResp = buildResponse((byte) -1);
		    } else {
			InetSocketAddress address = (InetSocketAddress) ctx.channel()
				.remoteAddress();
			String ip = address.getAddress().getHostAddress();
			boolean isOK = false;
			for (String WIP : whitekList) {
			    if (WIP.equals(ip)) {
				isOK = true;
				break;
			    }
			}
			loginResp = isOK ? buildResponse((byte) 0)
				: buildResponse((byte) -1);
			if (isOK)
			    nodeCheck.put(nodeIndex, true);
		    }
		    System.out.println("The login response is : " + loginResp
			    + " body [" + loginResp.getBody() + "]");
		    ctx.writeAndFlush(loginResp);
		} else {
		    ctx.fireChannelRead(msg);
		}
    }

    private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
		cause.printStackTrace();
		nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除缓存
		ctx.close();
		ctx.fireExceptionCaught(cause);
    }
}
