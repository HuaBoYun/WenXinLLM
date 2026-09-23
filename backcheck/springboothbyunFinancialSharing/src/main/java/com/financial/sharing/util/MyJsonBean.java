package com.financial.sharing.util;

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
public class MyJsonBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	// 全参数构造函数
	public MyJsonBean(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
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

	// 手动添加setter方法，确保兼容性
	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setData(T data) {
		this.data = data;
	}

	// 手动添加getter方法，确保兼容性
	public int getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}

	public T getData() {
		return this.data;
	}

	public static String error(int i) {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(i);
		json.setMsg("操作失败");
		return JSON.toJSONString(json);
	}

	public static String success(String msg) {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(1);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String success() {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(1);
		json.setMsg("操作成功");
		return JSON.toJSONString(json);
	}

	public static String error(String msg) {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(0);
		json.setMsg(msg);
		return JSON.toJSONString(json);
	}

	public static String error() {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}

	public static String errors() {
		MyJsonBean<Object> json = new MyJsonBean<>();
		json.setCode(0);
		json.setMsg("操作失败！");
		return JSON.toJSONString(json);
	}

	// 新增返回对象的静态方法，用于新的Controller
	public static <T> MyJsonBean<T> successData(T data) {
		return new MyJsonBean<T>(1, "操作成功", data);
	}

	public static <T> MyJsonBean<T> successData(String msg, T data) {
		return new MyJsonBean<T>(1, msg, data);
	}

	public static <T> MyJsonBean<T> successMsg(String msg) {
		return new MyJsonBean<T>(1, msg, null);
	}

	public static <T> MyJsonBean<T> errorData(String msg) {
		return new MyJsonBean<T>(0, msg, null);
	}

	public static <T> MyJsonBean<T> errorData(String msg, T data) {
		return new MyJsonBean<T>(0, msg, data);
	}

	public static <T> MyJsonBean<T> errorData() {
		return new MyJsonBean<T>(0, "操作失败", null);
	}

	public static <T> MyJsonBean<T> errorData(T data) {
		return new MyJsonBean<T>(0, "操作失败", data);
	}

	public static <T> MyJsonBean<T> businessError(String msg) {
		return new MyJsonBean<T>(400, msg, null);
	}

	public static <T> MyJsonBean<T> successData() {
		return new MyJsonBean<T>(1, "操作成功", null);
	}

	// 添加 ok 方法，用于兼容旧代码
	public static <T> MyJsonBean<T> ok(T data) {
		return new MyJsonBean<T>(1, "操作成功", data);
	}

	public static <T> MyJsonBean<T> ok(String msg) {
		return new MyJsonBean<T>(1, msg, null);
	}

	public static <T> MyJsonBean<T> ok(String msg, T data) {
		return new MyJsonBean<T>(1, msg, data);
	}
}
