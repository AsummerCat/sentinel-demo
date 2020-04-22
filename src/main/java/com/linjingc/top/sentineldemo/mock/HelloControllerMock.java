package com.linjingc.top.sentineldemo.mock;

import com.alibaba.csp.sentinel.slots.block.BlockException;

/**
 * 限流业务处理
 */
public class HelloControllerMock {
	//注意对应的函数必需为 static 函数，否则无法解析
	public static String index2(BlockException e) {
		return "HelloController.index2 限流中";
	}
}
