package com.hbfk.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class AutoNoUtil {
	public static final String autourl = "http://127.0.0.1:8763/baseInfo/getAutoNumber";
	
	
	public static String getAutoNo(String token,String configId) throws Exception {
	
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("token",token); 
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("configId",configId);
		
		String result = HttpClient.httpGetClient(autourl, filedMap,headerMap);
		
		JSONObject reJson = JSONObject.parseObject(result);
		System.out.println(reJson);
		return reJson.getString("data");
	}
	
}
