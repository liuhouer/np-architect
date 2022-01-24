package com.bfxy.test.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;

@Service
public class DegradeService {

	private AtomicInteger count = new AtomicInteger(0);
	
	/**
	 * 	blockHandler: 流控降级的时候进入的兜底函数
	 *  fallback: 抛出异常的时候进入的兜底函数
	 *  (1.6.0 之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理)
	 * @return
	 */
	@SentinelResource(
			value = "com.bfxy.test.service.DegradeService:degrade",
			entryType = EntryType.OUT,
			blockHandler = "degradeBlockHandler",
			fallback = "degradeFallback")
	public String degrade() {
		System.err.println("----> 正常执行degrade方法");
		
		if(count.incrementAndGet() % 3 == 0) {
			throw new RuntimeException("抛出业务异常");
		}
		
		return "degrade";
	}
	
	public String degradeBlockHandler(BlockException ex) {
		System.err.println("----> 触发 降级流控策略:" + ex);
		return "执行 降级流控方法";
	}
	
	public String degradeFallback(Throwable t) {
		System.err.println("----> 触发 异常时的降级策略:" + t);
		return "执行 异常降级方法";
	}
	
	
	
}
