package com.financial.sharing.exception;


import lombok.Data;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务操作异常
 * wuqian
 */
@Data
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 8758932446375202371L;
	private Integer code;
	private String msg;

	private static Map<Integer, String> messageMap = new HashMap<Integer, String>(0);

	//初始化状态码与文字说明
	static {
		/* 成功状态码 */
		messageMap.put(200, "成功");
		messageMap.put(201, "状态不符，无法删除");
		messageMap.put(202, "编号重复");
		messageMap.put(203, "上传失败");

		/* 服务器错误 */
		messageMap.put(1000, "服务器错误");

		/* 参数错误：10001-19999 */
		messageMap.put(10001, "参数无效");
		messageMap.put(10002, "参数为空");
		messageMap.put(10003, "参数类型错误");
		messageMap.put(10004, "参数缺失");
		messageMap.put(10005, "验证码已失效");
		messageMap.put(10006, "验证码错误");

		/* 用户错误：20001-29999*/
		messageMap.put(20001, "用户未登录");
		messageMap.put(20002, "账号或密码错误");
		messageMap.put(20003, "账号已被禁用");
		messageMap.put(20004, "用户不存在");
		messageMap.put(20005, "用户已存在");
		messageMap.put(20006, "用户已失效");
		messageMap.put(20007, "有下级数据，无法删除");
		messageMap.put(20007, "两次密码输入不一致");
		messageMap.put(20007, "不能包含中文");

		/* 业务错误：30001-39999 */
		messageMap.put(30001, "业务出现问题");
		messageMap.put(30002, "审批失败");
		messageMap.put(30003, "请先借阅");
		messageMap.put(30004, "借阅已过期");


		/* 系统错误：40001-49999 */
		messageMap.put(40001, "系统繁忙，请稍后重试");


		/* 数据错误：50001-599999 */
		messageMap.put(50001, "数据未找到");
		messageMap.put(50002, "数据有误");
		messageMap.put(50003, "数据已存在");
		messageMap.put(50004, "查询出错");
		messageMap.put(50005, "旧密码不正确");
		messageMap.put(50006, "文件不存在");

		/* 接口错误：60001-69999 */
		messageMap.put(60001, "内部系统接口调用异常");
		messageMap.put(60002, "外部系统接口调用异常");
		messageMap.put(60003, "该接口禁止访问");
		messageMap.put(60004, "接口地址无效");
		messageMap.put(60005, "接口请求超时");
		messageMap.put(60006, "接口负载过高");

		/* 权限错误：70001-79999 */
		messageMap.put(70001, "无权限访问");
		messageMap.put(70002, "授权成功");
		messageMap.put(70003, "删除成功");
	}

	public ServiceException() {

	}

	public ServiceException(Integer code, String msg) {
		this(msg);
		this.code = code;
		this.msg = msg;
	}

	public ServiceException(Integer code, Integer msg) {
		this(messageMap.getOrDefault(msg, "未知异常"));
		this.code = code;
		this.msg = messageMap.getOrDefault(msg, "未知异常");
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Object... args) {
		super(MessageFormat.format(message, args));
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}


}
