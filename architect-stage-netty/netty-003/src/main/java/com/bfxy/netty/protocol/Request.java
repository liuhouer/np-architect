package com.bfxy.netty.protocol;

public class Request {

	/* 请求包头  */
	public static final int CrcCode = 0xabef0101;
	/* 请求模块  */
	private short module;
	/* 请求命令  */
	private short cmd;
	/* 数据内容  */
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
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	/**
	 * <B>方法名称：</B>获取数据长度<BR>
	 * <B>概要说明：</B>获取数据长度<BR>
	 * @return data length 数据长度
	 */
	public int getDataLength(){
		if(this.data == null) {
			return 0;
		}
		return this.data.length;
	}
	

}
