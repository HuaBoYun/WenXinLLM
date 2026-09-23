package com.huabo.log.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseFormat {
	private static Map<Integer, String> messageMap = new HashMap<Integer, String>(0);

	//初始化状态码与文字说明
	static {
		/* 成功状态码 */
		messageMap.put(200, "成功");

		/* 服务器错误 */
		messageMap.put(1000, "服务器错误");

		/* 参数错误：10001-19999 */
		messageMap.put(10001, "参数无效");
		messageMap.put(10002, "参数为空");
		messageMap.put(10003, "参数类型错误");
		messageMap.put(10004, "参数缺失");

		/* 用户错误：20001-29999*/
		messageMap.put(20001, "用户未登录");

		/* 业务错误：30001-39999 */
		messageMap.put(30001, "业务出现问题");


		/* 系统错误：40001-49999 */
		messageMap.put(40001, "系统繁忙，请稍后重试");


		/* 数据错误：50001-599999 */
		messageMap.put(50001, "数据未找到");
		messageMap.put(50002, "数据有误");
		messageMap.put(50004, "查询出错");

		/* 接口错误：60001-69999 */
		messageMap.put(60001, "内部系统接口调用异常");
		messageMap.put(60002, "外部系统接口调用异常");

		/* 权限错误：70001-79999 */
		messageMap.put(70001, "无权限访问");

		/* 权限错误：80001-89999 */

		/*
		 * 自定义编号
		 * */

	}

	public static <T> JsonBean<T> retParam(Integer code, Integer status, T data) {
		return new JsonBean<>(code, messageMap.get(status), data);
	}


	public static <T> JsonBean<T> retParam(Integer code, String msg, T data) {
		return new JsonBean<>(code, msg, data);
	}
}
