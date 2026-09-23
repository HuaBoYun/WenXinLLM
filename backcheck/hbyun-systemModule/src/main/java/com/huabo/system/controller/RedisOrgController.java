package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.huabo.system.utils.HttpClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Redis组织缓存控制器
 * <p>提供组织机构Redis缓存的单个更新和批量更新接口</p>
 *
 * @author hbyun
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked", "deprecation" })
@Controller
@RequestMapping(value = "/redisorg")
@Tag(name="RedisController",description="redis组织缓存Controller")
public class RedisOrgController {

	@Value("${insertOrgOneUrl}")
	private String insertOrgOneUrl;

	/**
	 * 更新单个公司所有信息(组织启用、禁用时使用)
	 * @param nodeId
	 * @return
	 */
	@RequestMapping(value = "/insertOrgOne", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="更新单个公司的缓存")
	public @ResponseBody String insertOrgOne(BigDecimal nodeId) {
		String str;
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("nodeId",nodeId);
		try {
			str = HttpClient.request(HttpClient.insertOrgOneUrl, fields, null);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 更新所有公司所有信息redis(组织启用、禁用时使用)
	 * @param nodeId
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/insertOrgAll", produces = "application/json; charset=utf-8")
	@Operation(summary="更新所有公司的缓存")
	public @ResponseBody String insertOrgAll() {
		String str;
		HashMap<String, Object> fields = new HashMap<String, Object>();
		try {
			str = HttpClient.request(HttpClient.insertOrgAllUrl, fields, null);
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	public String insertOrg(BigDecimal orgid) {
		String str;
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("nodeId",orgid);
		try {
			return str = HttpClient.request(HttpClient.insertOrgOneUrl, fields, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
