package com.bfxy.apollo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfigBean {

	@Value("${timeout:20}")
	private int timeout;
	
	@Value("${newKey:'hello'}")
	private String newKey;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getNewKey() {
		return newKey;
	}

	public void setNewKey(String newKey) {
		this.newKey = newKey;
	}
	
}
