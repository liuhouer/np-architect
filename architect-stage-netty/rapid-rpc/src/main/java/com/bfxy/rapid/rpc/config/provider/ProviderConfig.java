package com.bfxy.rapid.rpc.config.provider;

import com.bfxy.rapid.rpc.config.RpcConfigAbstract;

/**
 * 	$ProviderConfig
 * 	接口名称
 * 	程序对象
 * @author 17475
 *
 */
public class ProviderConfig extends RpcConfigAbstract {
	
	protected Object ref;
	
	protected String address;	// ip:port
	
	protected String version = "1.0.0";
	
	protected int weight = 1;	// 权重
	
	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}

	public String getAddress() {
		return address;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
