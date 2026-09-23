package com.regulatory.penetration.util;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
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
public class MyJsonBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 程序定义状态码    1成功  0失败
	 */
	@ApiModelProperty("程序定义状态码  1成功  0失败 400业务异常")
	private int code;
	/**
	 * 必要的提示信息
	 */
	@ApiModelProperty("必要的提示信息")
	private String msg;
	/**
	 * 业务数据
	 */
	@ApiModelProperty("对象")
	private T data;

	public static String error(int i) {
		MyJsonBean json = new MyJsonBean();
		json.setCode(i);
		json.setMsg("操作失败");
		return JSON.toJSONString(json);
	}

	public static String success(String msg) {
		MyJsonBean json = new MyJsonBean();
		json.setCode(1);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String success() {
		MyJsonBean json = new MyJsonBean();
		json.setCode(1);
		json.setMsg("操作成功");
		return JSON.toJSONString(json);
	}

	public static String error(String msg) {
		MyJsonBean json = new MyJsonBean();
		json.setCode(0);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String error() {
		MyJsonBean json = new MyJsonBean();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}

	public static String errors() {
		MyJsonBean json = new MyJsonBean();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}
}
