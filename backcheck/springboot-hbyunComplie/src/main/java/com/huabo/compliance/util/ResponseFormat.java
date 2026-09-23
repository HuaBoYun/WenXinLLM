package com.huabo.compliance.util;

import java.util.HashMap;
import java.util.Map;

public class ResponseFormat {
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
		messageMap.put(20010, "用户未绑定角色");

		/* 业务错误：30001-39999 */
		messageMap.put(30001, "业务出现问题");
		messageMap.put(30002, "审批失败");
		messageMap.put(30004, "整改未完成，无法提交");
		messageMap.put(30003, "当前不存在实施项目");
		messageMap.put(30005, "请先借阅");
		messageMap.put(30006, "借阅已过期");
		messageMap.put(30007, "流程审批中！");
		messageMap.put(30008, "流程调整中，请去我的待办提交！");
		messageMap.put(30009, "流程已通过！");
		messageMap.put(30010, "流程已完成！");
		messageMap.put(30011, "部门负责人需配置！");
		messageMap.put(30012, "分管领导需配置！");


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


		messageMap.put(80001, "该项目未启动，不能实施");
		messageMap.put(80002, "项目已完成，不能再次实施");
		messageMap.put(80003, "项目不完整，请完善项目！");

		/* 权限错误：80001-89999 */
		//        messageMap.put(80003, "服务器异常");
		messageMap.put(80004, "该人员未在审批流程信息中！");
		messageMap.put(80005, "撤回失败！");

		/*
		 * 自定义编号
		 * */
		messageMap.put(90001, "模板无内容，禁止使用,请重新选择");
		messageMap.put(90002, "日期格式不正确,保存失败");
		messageMap.put(90003, "该方案已完成");
		messageMap.put(90004, "该方案已启动");
		messageMap.put(90005, "该方案已启动");
		messageMap.put(90006, "请选择");
		messageMap.put(90007, "该编号正在被模板使用,不能删除");
		messageMap.put(90008, "该方案未完成分配");
		messageMap.put(90009, "要素编号不能为空");
		messageMap.put(90010, "正在被引用,不能修改");


	}

	public static <T> JsonBean<T> retParam(Integer code, Integer status, T data) {
		return new JsonBean<>(code, messageMap.get(status), data);
	}


	public static <T> JsonBean<T> retParam(Integer code, String msg, T data) {
		return new JsonBean<>(code, msg, data);
	}
}
