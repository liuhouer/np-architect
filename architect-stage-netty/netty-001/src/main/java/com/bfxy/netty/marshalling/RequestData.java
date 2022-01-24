package com.bfxy.netty.marshalling;

import java.io.Serializable;
/**
 * 	$RequestData 请求对象
 * @author 17475
 *
 */
public class RequestData implements Serializable {

	private static final long serialVersionUID = 7359175860641122157L;

	private String id;
	
	private String name;
	
	private String requestMessage;
	
	private byte[] attachment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
}
