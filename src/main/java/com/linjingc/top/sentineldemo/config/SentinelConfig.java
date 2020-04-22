package com.linjingc.top.sentineldemo.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentinel 配置
 *
 * @date 2020年4月21日17:24:54
 */
@Configuration
public class SentinelConfig {
	/**
	 * 需要配置执行日志位置 不然会在当前用户下 的logs/csp/文件夹下生成
	 * spring.cloud.sentinel.log.dir:
	 */
	@PostConstruct
	private static void initFlowQpsRule() {
		//定义规则 可配置多个规则
		List<FlowRule> rules = new ArrayList<FlowRule>();

		//1.配置资源名称
		FlowRule rule1 = new FlowRule("HelloWorld");
		// set limit qps to 20
		//2.配置限流数量
		rule1.setCount(1);
		//3.限流策略     QPS OR THREAD
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//4. 受来源限制的应用程序名称 默认 default
		rule1.setLimitApp("default");
		//5.添加到list
		rules.add(rule1);



		//1.配置资源名称
		FlowRule rule2 = new FlowRule("HelloWorld1");
		// set limit qps to 20
		//2.配置限流数量
		rule2.setCount(1);
		//3.限流策略     QPS OR THREAD
		rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//4. 受来源限制的应用程序名称 默认 default
		rule2.setLimitApp("default");
		//5.添加到list
		rules.add(rule2);



		//6.加载规则
		FlowRuleManager.loadRules(rules);
		System.out.println("初始化限流策略成功");
	}

}
