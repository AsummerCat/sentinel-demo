package com.linjingc.top.sentineldemo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphO;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
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
}
