package com.bfxy.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;

public class HelloWorld {

	/**
	 * 	对主流的5种流控策略做了 底层的抽象和资源的封装
	 * 
	 * 	对于规则： FlowRule 、DegradeRule、ParamFlowRule、SystemRule、AuthorityRule
	 * 	对于管理器：FlowRuleManager、DegradeRuleManager、ParamFlowRuleManager、SystemRuleManager、AuthorityRuleManager
	 *  对于异常：FlowException、DegradeException、ParamFlowException、SystemBlockException、AuthorityException
	 * 
	 */
	public static void intFlowRules() {
		List<FlowRule> rules = new ArrayList<FlowRule>();
		FlowRule rule = new FlowRule();
		//	注意： 我们的规则一定要绑定到对应的资源上，通过资源名称进行绑定
		rule.setResource("helloworld");
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule.setCount(20);
		rules.add(rule);
		// 规则管理器
		FlowRuleManager.loadRules(rules);
	}
	
//	public static void main(String[] args) throws InterruptedException {
//		
//		
//		
//		//1. 第一步引入对应的依赖
//		
//		//2. 定义资源
//		
//		// 初始化规则
//		intFlowRules();
//		
//		while(true) {
//			
//			Entry entry = null;
//			try {
//				//	2.1 定义资源名称
//				entry = SphU.entry("helloworld");
//				
//				//	2.2 执行资源逻辑代码
//				System.err.println("helloworld: 访问数据库");
//				System.err.println("helloworld: 访问远程redis");
//				System.err.println("helloworld: 数据库持久化操作");
//				Thread.sleep(20);
//				
//			} catch (BlockException e) {
//				
//				System.err.println("要访问的资源被流控了, 执行流控逻辑！");
//				
//			} finally {
//				if(entry != null) {
//					entry.exit();
//				}
//			}
//		}
//		
//		
//		//3. 定义规则
//		
//		//4. 查看结果
//		
//		//5. 控制台
//		
//		
//		
//	}
}
