package com.bfxy.rabbit.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 	$Message
 * @author Alienware
 *
 */
@Data
public class Message implements Serializable {

	private static final long serialVersionUID = 841277940410721237L;

	/* 	消息的唯一ID	*/
	private String messageId;
	
	/*	消息的主题		*/
	private String topic;
	
	/*	消息的路由规则	*/
	private String routingKey = "";
	
	/*	消息的附加属性	*/
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	/*	延迟消息的参数配置	*/
	private int delayMills;
	
	/*	消息类型：默认为confirm消息类型	*/
	private String messageType = MessageType.CONFIRM;

	public Message() {
	}
	
	public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills) {
		this.messageId = messageId;
		this.topic = topic;
		this.routingKey = routingKey;
		this.attributes = attributes;
		this.delayMills = delayMills;
	}
	
	public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills,
			String messageType) {
		this.messageId = messageId;
		this.topic = topic;
		this.routingKey = routingKey;
		this.attributes = attributes;
		this.delayMills = delayMills;
		this.messageType = messageType;
	}
	
}
