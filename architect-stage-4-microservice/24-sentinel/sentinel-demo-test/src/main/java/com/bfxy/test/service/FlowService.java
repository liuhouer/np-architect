package com.bfxy.test.service;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Service
public class FlowService {
	
	/**
	 * 	blockHandler: 流控降级的时候进入的兜底函数
	 *  fallback: 抛出异常的时候进入的兜底函数
	 *  (1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理)
	 * @return
	 */
	@SentinelResource(
			value = "com.bfxy.test.service.FlowService:flow",
			entryType = EntryType.OUT,
			blockHandler = "flowBlockHandler",
			fallback = "")
	public String flow() {
		System.err.println("----> 正常执行flow方法");
		return "flow";
	}
	
	public String flowBlockHandler(BlockException ex) {
		System.err.println("----> 触发 流控策略:" + ex);
		return "执行 流控方法";
	}

	
	
}
