package com.management.accountant.util;

import com.alibaba.fastjson.JSON;
import com.management.accountant.common.ResultCode;
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

	public static <T> MyJsonBean<T> error(int i) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = i;
		json.msg = "操作失败";
		return json;
	}

	public static <T> MyJsonBean<T> success(String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 1;
		json.msg = msg;
		return json;
	}

	public static <T> MyJsonBean<T> success() {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 1;
		json.msg = "操作成功";
		return json;
	}

	public static <T> MyJsonBean<T> success(String msg, T data) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 1;
		json.msg = msg;
		json.data = data;
		return json;
	}

	public static <T> MyJsonBean<T> success(T data) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 1;
		json.data = data;
		return json;
	}

	public static <T> MyJsonBean<T> error(String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 0;
		json.msg = msg;
		return json;
	}

	public static <T> MyJsonBean<T> error() {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 0;
		json.msg = "操作失败！";
		return json;
	}



	public static <T> MyJsonBean<T> successData(T data, String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 1;
		json.msg = msg;
		json.data = data;
		return json;
	}

	public static <T> MyJsonBean<T> error(Integer code, String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = code;
		json.msg = msg;
		return json;
	}

	public static <T> MyJsonBean<T> result(ResultCode resultCode) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = resultCode.getCode();
		json.msg = resultCode.getMessage();
		return json;
	}

	public static <T> MyJsonBean<T> result(ResultCode resultCode, T data) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = resultCode.getCode();
		json.msg = resultCode.getMessage();
		json.data = data;
		return json;
	}

	public boolean isSuccess() {
		return ResultCode.SUCCESS.getCode().equals(this.code);
	}

	public static <T> MyJsonBean<T> errors() {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.code = 0;
		json.msg = "操作失败！";
		return json;
	}

	// 显式添加setter方法以确保编译通过
	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setData(T data) {
		this.data = data;
	}
}
