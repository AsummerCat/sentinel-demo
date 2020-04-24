package com.linjingc.top.sentineldemo.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sentinel 配置
 *
 * @date 2020年4月21日17:24:54
 */
@Configuration
public class SentinelConfig {

	/**
	 * 若您的应用使用了 Spring AOP（无论是 Spring Boot 还是传统 Spring 应用），您需要通过配置的方式将 SentinelResourceAspect 注册为一个 Spring Bean：
	 * @return
	 */
	@Bean
	public SentinelResourceAspect sentinelResourceAspect() {
		return new SentinelResourceAspect();
	}

	/**
	 * 需要配置执行日志位置 不然会在当前用户下 的logs/csp/文件夹下生成
	 * spring.cloud.sentinel.log.dir:
	 */
	@PostConstruct
	private static void initSentinel() {
		initSystemRule();
		initFlowQpsRule();
		initDegradeRule();
		initAuthorityRule();

	}

	/**
	 * 限流规则策略
	 */
	private static void initFlowQpsRule() {
		//定义规则 可配置多个规则
		List<FlowRule> rules = new ArrayList<>();
		//1.配置资源名称
		FlowRule rule1 = new FlowRule("HelloWorld");
		// set limit qps to 20
		//2.配置限流数量
		rule1.setCount(100);
		//3.限流策略     QPS OR THREAD
		rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
		//4.流量控制的效果  直接拒绝、Warm Up、匀速排队
		rule1.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT);
		//5.流控模式
		//STRATEGY_DIRECT = 0;  //direct 直接模式
		//STRATEGY_RELATE = 1;  //relate 关联
		//STRATEGY_CHAIN = 2;   //chain  链路
		rule1.setStrategy(RuleConstant.STRATEGY_DIRECT);
		//6. 受来源限制的应用程序名称 默认 default
//		rule1.setLimitApp("other");
		//7.添加到list
		rules.add(rule1);


		//11111.配置资源名称 根据调用者做限流
		FlowRule rule2 = new FlowRule("HelloWorld");
		rule2.setLimitApp("caller");
		rule2.setStrategy(RuleConstant.STRATEGY_DIRECT);
		rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
		rule2.setCount(1);
		rules.add(rule2);


		//22222.配置资源名称   调用链路限流
		FlowRule rule3 = new FlowRule("HelloWorld");
		rule3.setCount(10);
		rule3.setStrategy(RuleConstant.STRATEGY_CHAIN);
		//定义入口资源
		rule3.setRefResource("Entrance1");
		rules.add(rule3);


		//8.加载规则
		FlowRuleManager.loadRules(rules);


		System.out.println("初始化限流策略成功");
	}


	/**
	 * 降级规则策略
	 */
	private static void initDegradeRule() {
		List<DegradeRule> rules = new ArrayList<>();
		DegradeRule rule = new DegradeRule();
		//1.定义资源名
		rule.setResource("HelloWorld");
		// set threshold RT, 10 ms
		//2.阀值
		rule.setCount(5);
		//3.熔断策略，支持秒级 RT/秒级异常比例/分钟级异常数	秒级平均 RT 默认:秒级平均RT
		rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
		//4. 降级的时间 ,单位为s
		rule.setTimeWindow(10);
		//5. 滑动窗口 RT 模式下 1 秒内连续多少个请求的平均 RT 超出阈值方可触发熔断  默认5
		rule.setRtSlowRequestAmount(5);
		//6.异常熔断的触发最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断 默认5
		rule.setMinRequestAmount(5);
		rules.add(rule);
		DegradeRuleManager.loadRules(rules);
		System.out.println("初始化降级策略成功");
	}


	/**
	 * 系统保护规则策略
	 * 注意系统规则只针对入口资源（EntryType=IN）生效
	 */
	private static void initSystemRule() {
		List<SystemRule> rules = new ArrayList<>();
		SystemRule rule = new SystemRule();
		// max load is 3
		//1.load1 触发值，用于触发自适应控制阶段
		rule.setHighestSystemLoad(3.0);
		// max cpu usage is 60%
		//2.当前系统的 CPU 使用率（0.0-1.0）
		rule.setHighestCpuUsage(0.6);
		//33所有入口流量的平均响应时间
		rule.setAvgRt(10);
		//4.所有入口资源的 QPS
		rule.setQps(20);
		//5.入口流量的最大并发数
		rule.setMaxThread(10);

		rules.add(rule);
		SystemRuleManager.loadRules(Collections.singletonList(rule));
	}

	/**
	 * 黑白名单控制策略
	 */
	private static void initAuthorityRule() {
		AuthorityRule rule = new AuthorityRule();
		//1.资源名，即限流规则的作用对象
		rule.setResource("HelloWorld1");
		//2.限制模式，AUTHORITY_WHITE 为白名单模式，AUTHORITY_BLACK 为黑名单模式，默认为白名单模式。
		rule.setStrategy(RuleConstant.AUTHORITY_WHITE);
		//3.对应的黑名单/白名单，不同 origin 用 , 分隔，如 appA,appB。
		rule.setLimitApp("appA,appB,caller");
		AuthorityRuleManager.loadRules(Collections.singletonList(rule));
	}
}
