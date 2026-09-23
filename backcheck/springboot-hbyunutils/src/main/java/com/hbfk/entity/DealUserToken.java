package com.hbfk.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.YMDifferentVConfig;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.util.BaseDao;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.YMDesUtil;
import com.hbfk.util.YMMd5Util;
import com.hbfk.util.user.UserProvider;

import redis.clients.jedis.Jedis;

@Component
public class DealUserToken {
	
	@Resource
	private UserProvider userProvider;
	
	private static UserProvider staticuserProvider;
	
	@PostConstruct
	public void init() {
		staticuserProvider = userProvider;  // 再赋值给静态字段
	}
	
	public static TblStaffUtil parseUserToken(String token) throws Exception {
		Jedis jedis = null;
		TblStaffUtil staff = null;
		String jsonStaff = null;
		try {
			if(token == null) {
				return null;
			}
			token = UriUtils.decode(token, "utf-8");
			String staffId = EncryptUtil.getInstance().AESdecode(token,EncryptUtil.DESKEY);
			
			jedis = JedisUtil.getJedis();
			jsonStaff = jedis.get(staffId+JedisUtil.USERINFOKEY);
			if(jsonStaff == null) {
				return null;
			}
			staff = (TblStaffUtil) JSONObject.parseObject(jsonStaff,TblStaffUtil.class);
		} catch (Exception e) {
			return null;
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
			
			String pwd = YMDifferentVConfig.getYmLoginPwd();
			
			filedMap.put("password", pwd);
			filedMap.put("grant_type", "password");
			filedMap.put("origin", "password");
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
			
			String result = HttpClient.httpPostLoginClient(YMUrlStatic.interfaceUrl+YMUrlStatic.singelSignUrl, filedMap,headerMap);
			
			if(result == null) {
				return ResponseFormat.retParam(0, 800002, null);
			}
			String ymToken = null;
			
			JSONObject reJson = JSONObject.parseObject(result);
			if("200".equals(reJson.getString("code"))) {
				JSONObject dataJson = reJson.getJSONObject("data");
				ymToken = dataJson.getString("token");
				loginStaff.setYmToken(ymToken);
				loginStaff.setJnpfOrigin(origin);
				resultMap.put("ymToken", ymToken);
			}
		}finally{
			staticuserProvider.add(loginStaff);
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
			
			String pwd = YMDifferentVConfig.getYmLoginPwd();
			
			filedMap.put("password", pwd);
			filedMap.put("grant_type", "password");
			filedMap.put("client_id","admin");
			filedMap.put("client_secret", "123456");
			filedMap.put("scope", "all");
			
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Content-Type","application/x-www-form-urlencoded");
			
			String result = HttpClient.httpPostLoginClient(YMUrlStatic.interfaceUrl+YMUrlStatic.singelSignUrl, filedMap,headerMap);
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
		
		try {
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
//			System.out.println("业务中台返回结果:-------"+reJson);
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
		}catch (Exception e) {
			e.printStackTrace();
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
