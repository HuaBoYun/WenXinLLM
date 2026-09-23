package com.management.accountant.common;

import java.io.Serializable;

/**
 * 统一响应结果
 *
 * @param <T> 业务数据类型
 */
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer code;

	private String msg;

	private T data;

	public Result() {
	}

	public Result(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public static <T> Result<T> success(T data, String msg) {
		return new Result<T>(ResultCode.SUCCESS.getCode(), msg, data);
	}

	public static <T> Result<T> success(T data) {
		return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
	}

	public static <T> Result<T> error(String msg) {
		return new Result<T>(ResultCode.ERROR.getCode(), msg, null);
	}

	public static <T> Result<T> error(ResultCode resultCode) {
		return new Result<T>(resultCode.getCode(), resultCode.getMessage(), null);
	}

	public Integer getCode() { return code; }
	public void setCode(Integer code) { this.code = code; }

	public String getMsg() { return msg; }
	public void setMsg(String msg) { this.msg = msg; }

	public T getData() { return data; }
	public void setData(T data) { this.data = data; }
}
