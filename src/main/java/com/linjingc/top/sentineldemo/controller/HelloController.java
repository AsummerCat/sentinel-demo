package com.linjingc.top.sentineldemo.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class HelloController {

	@RequestMapping("index")
	public String index() {
		Entry entry = null;
		try {
			entry = SphU.entry("HelloWorld");
			System.out.println("HelloController.index 正常访问");
		} catch (BlockException e) {
			System.out.println("HelloController.index 限流中");
			e.printStackTrace();
		} finally {
			if (entry != null) {
				entry.exit();
			}
		}
		return "HelloWorld";
	}
}
