package com.global.treasurer.util;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
// import lombok.AllArgsConstructor;
// import lombok.Data; // 已移除
// import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一JSON返回类
 * @author sxd
 * @since 2018/4/1
 */
// @Data // 已移除,使用手动编写的getter/setter
// @NoArgsConstructor // 已移除
// @AllArgsConstructor // 已移除
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

	// 构造器
	public MyJsonBean() {
	}

	public MyJsonBean(int code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

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

	/**
	 * 成功返回对象
	 */
	public static <T> MyJsonBean<T> success(T data) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.setCode(200);
		json.setMsg("操作成功");
		json.setData(data);
		return json;
	}

	/**
	 * 成功返回对象（带消息）
	 */
	public static <T> MyJsonBean<T> success(T data, String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.setCode(200);
		json.setMsg(msg);
		json.setData(data);
		return json;
	}

	/**
	 * 失败返回对象
	 */
	public static <T> MyJsonBean<T> errorBean(String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.setCode(0);
		json.setMsg(msg);
		return json;
	}

	/**
	 * 失败返回对象（带错误码）
	 */
	public static <T> MyJsonBean<T> errorBean(int code, String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.setCode(code);
		json.setMsg(msg);
		return json;
	}

	/**
	 * 成功返回对象（successBean别名）
	 */
	public static <T> MyJsonBean<T> successBean(T data) {
		return success(data);
	}

	/**
	 * 成功返回对象（仅消息）
	 */
	public static <T> MyJsonBean<T> successBean(String msg) {
		MyJsonBean<T> json = new MyJsonBean<>();
		json.setCode(200);
		json.setMsg(msg);
		return json;
	}

	/**
	 * 转换为JSON字符串
	 */
	public String toJson() {
		return JSON.toJSONString(this);
	}

    // 以下方法由Lombok生成,手动添加以解决编译问题


    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

}
