package com.linjingc.top.sentineldemo;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
@SpringBootTest
public class test {
	/**
	 * 测试 调用方限流
	 * @param args
	 */
	public static void main(String[] args) {
//		initFlowRuleForCaller();
		testFlowRuleForCaller();

	}
	/*定义根据调用者的流控规则*/
	public static void initFlowRuleForCaller(){
		List<FlowRule> rules = new ArrayList<>();
		FlowRule rule = new FlowRule();
		//定义资源名
		rule.setResource("echo");
		//定义阈值类型
		rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//定义阈值
		rule.setCount(2);
		//定义限制调用者
		rule.setLimitApp("caller");
		rules.add(rule);


		FlowRule rule1 = new FlowRule();
		rule1.setResource("echo");
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule1.setLimitApp("other");
		rule1.setCount(3);
		rules.add(rule1);
		FlowRuleManager.loadRules(rules);
	}


	public static void testFlowRuleForCaller(){
		initFlowRuleForCaller();
		for (int i = 0; i < 5; i++) {
//			ContextUtil.enter("c1");
			ContextUtil.enter("c1","caller");
			Entry entry = null;
			try {
				entry = SphU.entry("echo");
				System.out.println("访问成功");
			} catch (BlockException e) {
				System.out.println("网络异常，请刷新！");
			}finally {
				if (entry != null){
					entry.exit();
				}
			}
		}
	}
}
