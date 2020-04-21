package com.linjingc.top.sentineldemo.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 配置
 * @date 2020年4月21日17:24:54
 */
@Configuration
public class SentinelConfig {


	private static void initFlowQpsRule() {
		//定义规则 可配置多个规则
		List<FlowRule> rules = new ArrayList<FlowRule>();

		//1.配置资源名称
		FlowRule rule1 = new FlowRule("HelloWorld");
		// set limit qps to 20
		//2.配置限流数量
		rule1.setCount(20);
		//3.限流策略     QPS OR THREAD
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//4. 受来源限制的应用程序名称 默认 default
		rule1.setLimitApp("default");
		//5.添加到list
 		rules.add(rule1);


 		//6.加载规则
		FlowRuleManager.loadRules(rules);
	}

}
