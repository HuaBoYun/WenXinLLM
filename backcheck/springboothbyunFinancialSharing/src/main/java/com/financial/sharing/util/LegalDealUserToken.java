package com.financial.sharing.util;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
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
public class LegalDealUserToken {

	public static TblStaffUtil parseUserToken(String token) {
		Jedis jedis = null;
		TblStaffUtil staff = null;
		String jsonStaff = null;
		String staffId = null;
		String redisKey = null;

		try {
			if(token == null || token.trim().isEmpty()) {
				log.debug("Token为空，无法解析用户信息");
				return null;
			}

			// 记录原始token（脱敏处理）
			String maskedToken = token.length() > 10 ? token.substring(0, 10) + "***" : token;
			log.debug("开始解析用户token，原始token: {}", maskedToken);

			// URL解码
			token = UriUtils.decode(token, "utf-8");
			log.debug("URL解码后token: {}", token.length() > 10 ? token.substring(0, 10) + "***" : token);

			// AES解密获取staffId
			staffId = EncryptUtil.getInstance().AESdecode(token, EncryptUtil.DESKEY);
			if (staffId == null || staffId.trim().isEmpty()) {
				log.error("AES解密失败，解密后的staffId为空，原始token: {}", maskedToken);
				return null;
			}

			// 验证staffId是否为有效数字
			try {
				Long.parseLong(staffId);
				log.debug("AES解密成功，staffId: {}", staffId);
			} catch (NumberFormatException e) {
				log.error("解密后的staffId不是有效数字: {}, 原始token: {}", staffId, maskedToken, e);
				return null;
			}

			// 获取Redis连接
			jedis = JedisUtil.getJedis();
			if(jedis == null) {
				log.error("Redis连接失败，无法获取用户token信息，staffId: {}", staffId);
				return null;
			}

			// 构建Redis key
			redisKey = staffId + JedisUtil.USERINFOKEY;
			log.debug("从Redis获取用户信息，key: {}", redisKey);

			// 从Redis获取用户信息
			jsonStaff = jedis.get(redisKey);
			if(jsonStaff == null) {
				log.warn("Redis中未找到用户信息，key: {}, staffId: {}", redisKey, staffId);
				return null;
			}

			// 去除前后空白字符
			jsonStaff = jsonStaff.trim();
			if (jsonStaff.isEmpty()) {
				log.error("Redis中的用户信息为空字符串，key: {}, staffId: {}", redisKey, staffId);
				return null;
			}

			// 基本JSON格式验证 - 兼容双重序列化场景
			// 正常情况：jsonStaff 形如 {"accbook":{...},"staffid":1,...}
			// 双重序列化：jsonStaff 形如 "{\"accbook\":{...},\"staffid\":1,...}"
			// 后者来自上游某处对已经是 String 的数据再次调用 JSONObject.toJSONString，
			// 这里先脱掉外层字符串，恢复真正的对象 JSON 再 parse。
			String trimmedJson = jsonStaff.trim();
			if (trimmedJson.startsWith("\"") && trimmedJson.endsWith("\"")) {
				try {
					String unwrapped = JSONObject.parseObject(trimmedJson, String.class);
					if (unwrapped != null) {
						trimmedJson = unwrapped.trim();
						log.warn("Redis中的用户信息检测到双重序列化，已自动脱外层字符串，key: {}, staffId: {}",
								redisKey, staffId);
					}
				} catch (Exception unwrapEx) {
					log.error("脱去外层字符串失败，key: {}, staffId: {}, 实际内容: {}",
							redisKey, staffId,
							jsonStaff.length() > 100 ? jsonStaff.substring(0, 100) + "..." : jsonStaff,
							unwrapEx);
					return null;
				}
			}

			if (!trimmedJson.startsWith("{") || !trimmedJson.endsWith("}")) {
				log.error("Redis中的用户信息不是有效的JSON对象格式，key: {}, staffId: {}, 实际内容: {}",
						redisKey, staffId, jsonStaff.length() > 100 ? jsonStaff.substring(0, 100) + "..." : jsonStaff);
				return null;
			}

			// 解析JSON
			try {
				staff = JSONObject.parseObject(trimmedJson, TblStaffUtil.class);
				log.debug("成功解析用户信息，staffId: {}, 用户名: {}", staffId,
						staff != null && staff.getRealname() != null ? staff.getRealname() : "未知");
			} catch (Exception e) {
				log.error("JSON解析失败，key: {}, staffId: {}, 数据内容: {}",
						redisKey, staffId, trimmedJson.length() > 200 ? trimmedJson.substring(0, 200) + "..." : trimmedJson, e);
				return null;
			}

			// 验证解析结果
			if (staff == null) {
				log.error("JSON解析结果为null，key: {}, staffId: {}", redisKey, staffId);
				return null;
			}

			// 验证关键字段
			if (staff.getStaffid() == null) {
				log.error("用户信息缺少staffid字段，key: {}, staffId: {}", redisKey, staffId);
				return null;
			}

			log.info("用户token解析成功，staffId: {}, 用户名: {}", staff.getStaffid(), staff.getRealname());

		} catch (Exception e) {
			log.error("解析用户token失败，原始token: {}, 解析后的staffId: {}, Redis key: {}, Redis中的数据: {}",
					token != null ? (token.length() > 10 ? token.substring(0, 10) + "***" : token) : "null",
					staffId, redisKey,
					jsonStaff != null ? (jsonStaff.length() > 100 ? jsonStaff.substring(0, 100) + "..." : jsonStaff) : "null", e);
		} finally {
			if(jedis != null) {
				try {
					jedis.close();
				} catch (Exception e) {
					log.warn("关闭Redis连接时发生异常", e);
				}
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
