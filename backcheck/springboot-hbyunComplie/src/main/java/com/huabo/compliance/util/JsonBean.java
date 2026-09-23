package com.huabo.compliance.util;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一JSON返回类
 * @author sxd
 * @since 2018/4/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 程序定义状态码    1成功  0失败
	 */
	@Schema(name="程序定义状态码  1成功  0失败 400业务异常")
	private int code;
	/**
	 * 必要的提示信息
	 */
	@Schema(name="必要的提示信息")
	private String msg;
	/**
	 * 业务数据
	 */
	@Schema(name="对象")
	private T data;

	public static String error(int i) {
		JsonBean json = new JsonBean();
		json.setCode(i);
		json.setMsg("操作失败");
		return JSON.toJSONString(json);
	}

	public static String success(String msg) {
		JsonBean json = new JsonBean();
		json.setCode(1);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String success() {
		JsonBean json = new JsonBean();
		json.setCode(1);
		json.setMsg("操作成功");
		return JSON.toJSONString(json);
	}

	public static String error(String msg) {
		JsonBean json = new JsonBean();
		json.setCode(0);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String error() {
		JsonBean json = new JsonBean();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}

	public static String errors() {
		JsonBean json = new JsonBean();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}
}
