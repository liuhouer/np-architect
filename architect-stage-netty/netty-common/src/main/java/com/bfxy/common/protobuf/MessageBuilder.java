package com.bfxy.common.protobuf;

import com.bfxy.common.protobuf.MessageModule.Message;
import com.bfxy.common.protobuf.MessageModule.MessageType;
import com.bfxy.common.protobuf.MessageModule.ResultType;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;

/**
 * 	$MessageBuilder
 * 	创建请求 和 回送响应的一个数据封装类
 * @author 17475
 *
 */
public class MessageBuilder {

	private static final int CRCCODE = 0xabef0101;
	
	/**
	 * 	$getRequestMessage 请求封装
	 * @param module
	 * @param cmd
	 * @param data
	 * @return
	 */
	public static Message getRequestMessage(String module,
			String cmd,
			GeneratedMessageV3 data) {
		
		return MessageModule.Message.newBuilder()
		.setCrcCode(CRCCODE)
		.setMessageType(MessageType.REQUEST)
		.setModule(module)
		.setCmd(cmd)
		.setBody(ByteString.copyFrom(data.toByteArray()))
		.build();
	}
	
	/**
	 * 	$getResponseMessage 响应封装
	 * @param module
	 * @param cmd
	 * @param resultType
	 * @param data
	 * @return
	 */
	public static Message getResponseMessage(String module,
			String cmd,
			ResultType resultType,
			GeneratedMessageV3 data) {
		
		return MessageModule.Message.newBuilder()
		.setCrcCode(CRCCODE)
		.setMessageType(MessageType.RESPONSE)
		.setModule(module)
		.setCmd(cmd)
		.setResultType(resultType)
		.setBody(ByteString.copyFrom(data.toByteArray()))
		.build();
	}
	
}
