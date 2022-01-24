package com.bfxy.apollo.service;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Service
public class FlowService {

	@SentinelResource(value = "com.bfxy.apollo.service.FlowService:test",
			blockHandler = "testblockHandler")
	public String test() {
		System.err.println("正常执行");
		return "test";
	}
	
	public String testblockHandler(BlockException ex) {
		System.err.println("流控执行, " + ex);
		return "流控执行";
	}
	
	
}
