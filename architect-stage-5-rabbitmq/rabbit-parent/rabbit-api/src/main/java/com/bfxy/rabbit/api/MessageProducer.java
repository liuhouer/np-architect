package com.bfxy.rabbit.api;

import java.util.List;

import com.bfxy.rabbit.api.exception.MessageRunTimeException;

/**
 * 	$MessageProducer
 * @author Alienware
 *
 */
public interface MessageProducer {

	/**
	 * 	$send消息的发送 附带SendCallback回调执行响应的业务逻辑处理
	 * @param message
	 * @param sendCallback
	 * @throws MessageRunTimeException
	 */
	void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;
	
	/**
	 * 	
	 * @param message消息的发送
	 * @throws MessageRunTimeException
	 */
	void send(Message message) throws MessageRunTimeException;
	
	/**
	 * 	$send 消息的批量发送
	 * @param messages
	 * @throws MessageRunTimeException
	 */
	void send(List<Message> messages) throws MessageRunTimeException;
	
}
