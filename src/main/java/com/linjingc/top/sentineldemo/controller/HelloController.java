package com.linjingc.top.sentineldemo.controller;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.linjingc.top.sentineldemo.mock.HelloControllerMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
@Slf4j
public class HelloController {

	/**
	 * 异常限流处理  entry=SphU.entry("HelloWorld");
	 * entry.exit();
	 * 必须成对出现
	 *
	 * @return
	 */
	@RequestMapping("index")
	public String index() {
		Entry entry = null;
		try {
			entry = SphU.entry("HelloWorld");
			return "HelloController.index 正常访问";
		} catch (BlockException e) {
			e.printStackTrace();
			return "HelloController.index 限流中";
		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
	}

	/**
	 * if限流处理  SphO.entry("HelloWorld");
	 *
	 * @return
	 */
	@RequestMapping("index1")
	public String index1() {
		if (SphO.entry("HelloWorld")) {

			return "HelloController.index1 正常访问";
		} else {
			return "HelloController.index1 限流中";
		}
	}


	/**
	 * 注解限流 @SentinelResource
	 * blockHandler:blockHandlerClass中对应的异常处理方法名。参数类型和返回值必须和原方法一致
	 * blockHandlerClass：自定义限流逻辑处理类
	 * fallback: 对应降级方法
	 * fallbackClass: 相应降级策略
	 * exceptionsToIgnore：用于指定哪些异常被排除掉，不会计入异常统计中，也不会进入 fallback 逻辑中，而是会原样抛出。
	 *
	 * @return
	 */
	@RequestMapping("index2")
	@SentinelResource(value = "HelloWorld",
			blockHandler = "index2",
			blockHandlerClass = HelloControllerMock.class,
			fallback = "index2",
			fallbackClass = HelloControllerMock.class
	)
	public String index2() {
		return "HelloController.index2 正常访问";
	}


	/**
	 * **********************************************************************
	 * **********************************************************************
	 * **********************************************************************
	 */
	/**
	 * 注解限流 @SentinelResource
	 * 直接写在一个类中 对应限流 降级方法
	 *
	 * @return
	 */
	@RequestMapping("index3")
	@SentinelResource(value = "HelloWorld",
			blockHandler = "block",
			fallback = "fallback"
	)
	public String index3() {
		int a=1/0;
		return "HelloController.index3 正常访问";
	}

	/**
	 * 处理限流或者降级
	 */
	public String block(BlockException e) {
		return "限流，或者降级了 block";
	}

	public String fallback(BlockException e) {
		return "限流，或者降级了 fallback";
	}

	/**
	 * **********************************************************************
	 * **********************************************************************
	 * **********************************************************************
	 */

	@RequestMapping("index4")
	public String index4() {

		try {
			AsyncEntry entry = SphU.asyncEntry("HelloWorld1");
			new Thread(() ->
					ContextUtil.runOnContext(entry.getAsyncContext(), () -> {
						System.out.println("异步执行ing....");
						index3();
						entry.exit();
					})
			).start();
		} catch (BlockException e) {
			e.printStackTrace();
		} finally {

		}
		return "HelloController.index4 正常访问";
	}


	/**
	 * 根据调用者做限流
	 */
	@RequestMapping("index5")
//	@SentinelResource(value = "HelloWorld",
//			blockHandler = "index2",
//			blockHandlerClass = HelloControllerMock.class,
//			fallback = "index2",
//			fallbackClass = HelloControllerMock.class
//	)
	public String index5() {
		ContextUtil.enter("HelloWorld","caller");
		if (SphO.entry("HelloWorld")) {
			return "HelloController.index5 正常访问";
		} else {
			return "HelloController.index5 限流中";
		}
	}

}
