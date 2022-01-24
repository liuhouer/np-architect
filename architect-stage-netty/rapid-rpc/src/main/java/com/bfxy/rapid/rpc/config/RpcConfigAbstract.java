package com.bfxy.rapid.rpc.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

public abstract class RpcConfigAbstract {
	
	private AtomicInteger generator = new AtomicInteger(0);
	
	protected String id;
	
	protected String interfaceClass = null;

	//	服务的调用方(consumer端特有的属性)
	protected Class<?> proxyClass = null;
	
	public String getId() {
		if(StringUtils.isBlank(id)) {
			id = "rapid-cfg-gen-" + generator.getAndIncrement();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setInterface(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	
	public String getInterface() {
		return this.interfaceClass;
	}
	 
}
