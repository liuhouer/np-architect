package com.bfxy.netty.protocol;

public class Response {
	
	/* 请求包头  */
	public static final int CrcCode = 0xabef0101;
	
	/* 请求模块  */
	private short module;
	
	/* 命令号  */
	private short cmd;
	
	/* 状态码  */
	private byte messageType;

	/* 数据部分  */
	private byte[] data;

	public short getModule() {
		return module;
	}

	public void setModule(short module) {
		this.module = module;
	}

	public short getCmd() {
		return cmd;
	}

	public void setCmd(short cmd) {
		this.cmd = cmd;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	public int getDataLength(){
		if(data == null){
			return 0;
		}
		return data.length;
	}
}
