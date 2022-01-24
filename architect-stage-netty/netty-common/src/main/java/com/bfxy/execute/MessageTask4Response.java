package com.bfxy.execute;

import org.springframework.boot.autoconfigure.security.servlet.RequestMatcherProvider;

import com.bfxy.common.protobuf.MessageModule;
import com.bfxy.common.protobuf.MessageModule.ResultType;
import com.bfxy.scanner.Invoker;
import com.bfxy.scanner.InvokerTable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class MessageTask4Response implements Runnable {

	private MessageModule.Message message;
	private ChannelHandlerContext ctx;
	
	public MessageTask4Response(MessageModule.Message message, ChannelHandlerContext ctx) {
		this.message = message;
		this.ctx = ctx;
	}
	
	@Override
	public void run() {
		try {
			//	user-return
			String module = this.message.getModule();
			//	save-return
			String cmd = this.message.getCmd();
			//	响应的结果
			ResultType resultType = this.message.getResultType();
			//	响应的内容
			byte[] data = this.message.getBody().toByteArray();
			
			Invoker invoker = InvokerTable.getInvoker(module, cmd);
			invoker.invoke(resultType, data);	
			
		} finally {
			ReferenceCountUtil.release(message);
		}
	}

}
