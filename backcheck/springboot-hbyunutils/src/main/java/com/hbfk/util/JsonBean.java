package com.hbfk.util;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 统一JSON返回类
 * @author sxd
 * @since 2018/4/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonBean implements Serializable {
	private static final long serialVersionUID = 1596798275673616274L;
	/**
	 * 程序定义状态码    1成功  0失败
     */
    private int code;
    /**
            * 必要的提示信息
     */
    private String msg;
    /**
     	* 业务数据
     */
    private Object data;
    
    private Object result;
    
    public JsonBean(Integer code, String msg, Object data) {
    	this.code = code;
    	this.msg = msg;
    	this.data = data;
	}
    

    /**
     	* 对业务数据单独处理
     * @return
     */
    @Override
    public String toString() {
        if(Objects.isNull(this.data)){
            this.setData(new Object());
        }
        return JSON.toJSONString(this);
    }

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
	
	public JsonBean(Integer code, Object data) {
		this.code = code;
		this.data = data;
	}

	public static String success(Object result,Object data) {
		JsonBean jsonBean = new JsonBean();
		jsonBean.setCode(1);
		jsonBean.setMsg("操作成功");
		jsonBean.setResult(result);
		jsonBean.setData(data);
		return  JSONObject.toJSONString(jsonBean);
	}

	
}
