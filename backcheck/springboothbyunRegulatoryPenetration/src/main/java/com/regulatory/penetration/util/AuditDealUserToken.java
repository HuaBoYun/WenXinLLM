package com.regulatory.penetration.util;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriUtils;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuditDealUserToken {


	public static TblStaffUtil parseUserToken(String token) {
		Jedis jedis = null;
		TblStaffUtil staff = null;
		String jsonStaff = null;
		try {
			if(token == null) {
				return null;
			}
			token = UriUtils.decode(token, "utf-8");
			String staffId = EncryptUtil.getInstance().AESdecode(token, EncryptUtil.DESKEY);
			jedis = JedisUtil.getJedis();
			jsonStaff = jedis.get(staffId+ JedisUtil.USERINFOKEY);
			if(jsonStaff == null) {
				return null;
			}
			staff = (TblStaffUtil) JSONObject.parseObject(jsonStaff, TblStaffUtil.class);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null) {
				jedis.close();
			}
		}
		return staff;
	}
	
	/**
	 * 流程平台用户登录 并放入当前登录用户token中
	 * @param loginStaff
	 * @param origin 
	 * @return
	 * @throws Exception
	 */
	public static JsonBean getYmTokenMothod(TblStaffUtil loginStaff, String origin) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Jedis jedis = null;
		HashMap<String,Object> resultMap = new HashMap<String,Object>(0);
		
		try {
			if(loginStaff.getPkYmStaffId() == null || "".equals(loginStaff.getPkYmStaffId())) {
				return ResponseFormat.retParam(0, 20008, null);
			}
			
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_SECRETKEY FROM base_user WHERE F_ID = '"+loginStaff.getPkYmStaffId()+"'");
			rs = ps.executeQuery();
			String secretKey = null;
			while (rs.next()) {
				secretKey = rs.getString("F_SECRETKEY");
			}

			if(secretKey == null) {
				return ResponseFormat.retParam(0, 80002, null);
			}
			HashMap<String,String> filedMap = new HashMap<String,String>(0);
			
			filedMap.put("account", loginStaff.getUsername());
			
			//filedMap.put("password", YMMd5Util.getStringMd5(YMMd5Util.getStringMd5("hbyun@123456".toLowerCase()) + secretKey.toLowerCase()));
			filedMap.put("password", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
			filedMap.put("grant_type", "password");
			filedMap.put("client_id","admin");
			filedMap.put("client_secret", "123456");
			filedMap.put("scope", "all");
			
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Content-Type","application/x-www-form-urlencoded");
			
			if("app".equals(origin)) {
				headerMap.put("Jnpf-Origin","app");
			}else {
				headerMap.put("Jnpf-Origin","pc");
			}
			
			String result = HttpClient.httpPostLoginClient(YMUrlStatic.interfaceUrl+ YMUrlStatic.singelSignUrl, filedMap,headerMap);
			
			if(result == null) {
				return ResponseFormat.retParam(0, 800002, null);
			}
			String ymToken = null;
			
			JSONObject reJson = JSONObject.parseObject(result);
			System.out.println(reJson);
			if("200".equals(reJson.getString("code"))) {
				JSONObject dataJson = reJson.getJSONObject("data");
				ymToken = dataJson.getString("token");
				jedis = JedisUtil.getJedis();
				loginStaff.setYmToken(ymToken);
				loginStaff.setJnpfOrigin(origin);
			    jedis.set(loginStaff.getStaffid() + JedisUtil.USERINFOKEY, JSONObject.toJSONString(loginStaff));
				resultMap.put("ymToken", ymToken);
			}else {
				return ResponseFormat.retParam(0, 40001, null);
			}
		}finally{
			if(jedis != null) {
				jedis.close();
			}
			BaseDao.getInstance().close(con,rs,ps);
		}
		 return ResponseFormat.retParam(1, 200, resultMap);
	}

	/**
	 * 根据用户账号 登录流程平台中，并返回登录token
	 * @return
	 * @throws Exception
	 */
	public static String getYmTokenMothod(String account) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String ymToken = null;
		Jedis jedis = null;
		try {
			
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_SECRETKEY FROM base_user WHERE F_Account = '"+account+"'");
			rs = ps.executeQuery();
			String secretKey = null;
			while (rs.next()) {
				secretKey = rs.getString("F_SECRETKEY");
			}

			if(secretKey == null) {
				return "-1";
			}
			HashMap<String,String> filedMap = new HashMap<String,String>(0);
			
			filedMap.put("account", account);
			
			filedMap.put("password", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
			filedMap.put("grant_type", "password");
			filedMap.put("client_id","admin");
			filedMap.put("client_secret", "123456");
			filedMap.put("scope", "all");
			
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Content-Type","application/x-www-form-urlencoded");
			
			String result = HttpClient.httpPostLoginClient(YMUrlStatic.interfaceUrl+ YMUrlStatic.singelSignUrl, filedMap,headerMap);
			if(result == null) {
				return "-1";
			}
			
			JSONObject reJson = JSONObject.parseObject(result);
			if("200".equals(reJson.getString("code"))) {
				JSONObject dataJson = reJson.getJSONObject("data");
				ymToken = dataJson.getString("token");
				jedis = JedisUtil.getJedis();
				jedis.set(YMUrlStatic.YMSPACKEY, ymToken);
			}else {
				return "-1";
			}
		}finally{
			jedis.close();
			BaseDao.getInstance().close(con,rs,ps);
		}
		 return ymToken;
	}
	
	
	
	public static String dealYmUrlMethod(String url, HashMap<String, Object> filedMap,
			Map<String, String> headerMap, TblStaffUtil loginStaff, String type, Integer parambody) throws Exception {
		String result = null;
		
		switch (type) {
		case "put":
			result = HttpClient.httpPutClient(url, filedMap,headerMap,parambody);
			break;
		case "post":
			result = HttpClient.httpPostClient(url, filedMap,headerMap,parambody);
			break;
		case "get":
			result = HttpClient.httpGetClient(url, filedMap,headerMap);
			break;
		case "delete":
			result = HttpClient.httpDeleteClient(url, filedMap,headerMap);
			break;
		default:
			break;
		}
		
		if(result == null) {
			return null;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		if("600".equals(reJson.getString("code")) || "601".equals(reJson.getString("code"))) {
			JsonBean jsonBean = getYmTokenMothod(loginStaff,loginStaff.getJnpfOrigin());
			JSONObject json = JSONObject.parseObject(jsonBean.toString());
			String ymToken = json.getJSONObject("data").getString("ymToken");
			headerMap.put("Authorization", ymToken);
			result = dealYmUrlMethod(url,filedMap,headerMap,loginStaff,type,parambody);
			return result;
		}else if(!"200".equals(reJson.getString("code"))){
			return result;
		}
		
		return result;
	}

	
	public static String dealYmUniqueMethod(String url, HashMap<String, Object> filedMap,
			Map<String, String> headerMap, String type, Integer parambody) throws Exception {
		String result = null;
		switch (type) {
		case "put":
			result = HttpClient.httpPutClient(url, filedMap,headerMap,parambody);
			break;
		case "post":
			result = HttpClient.httpPostClient(url, filedMap,headerMap,parambody);
			break;
		case "get":
			result = HttpClient.httpGetClient(url, filedMap,headerMap);
			break;
		case "delete":
			result = HttpClient.httpDeleteClient(url, filedMap,headerMap);
			break;
		default:
			break;
		}
		if(result == null) {
			return null;
		}
		JSONObject reJson = JSONObject.parseObject(result);
		System.out.println(reJson);
		if("600".equals(reJson.getString("code")) || "601".equals(reJson.getString("code"))) {
			String ymToken = getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			headerMap.put("Authorization", ymToken);
			result = dealYmUniqueMethod(url,filedMap,headerMap,type,parambody);
			return result;
		}else if(!"200".equals(reJson.getString("code"))){
			return result;
		}
		return result;
	}

}
