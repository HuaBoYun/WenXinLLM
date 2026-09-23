package com.huabo.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.util.EncryptUtil;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.service.TblStaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 派可报表集成控制器
 * <p>提供派可数据报表系统的单点登录验证接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="PaikeInterController",description="派可报表相关验证")
public class PaikeInterController extends BaseController{

	@Resource
	private TblStaffService tblStaffService;
	
	/**
	 * 派可数据集成接口1
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "sso/integrationConfig/login", produces = "application/json; charset=utf-8")
	@Operation(summary="派可单点登录验证")
	public @ResponseBody String integrationConfig_login(HttpServletRequest request, HttpServletResponse response,
			String token) throws Exception {
		// 加密
		// String username="admin";
		// String str = EncryptUtil.getInstance().DESencode(username, "hbyun");
		// return str;
		// 解密
		String username = EncryptUtil.getInstance().DESdecode(token, SystemStaticValue.PKPWD);
		TblStaff staff = this.tblStaffService.selectUniqueStaffInfo(username);
		Map<String, Object> hashMap = new HashMap<>();
/*		if (staff == null) {
			hashMap.put("status", 500);
			hashMap.put("message", "系统无此用户");
			hashMap.put("data", "");
		} else {*/
			hashMap.put("status", 200);
			hashMap.put("message", "验证用户正确");
			hashMap.put("data", username);
		/* } */
		return JSON.toJSONString(hashMap);
	}
	
}
