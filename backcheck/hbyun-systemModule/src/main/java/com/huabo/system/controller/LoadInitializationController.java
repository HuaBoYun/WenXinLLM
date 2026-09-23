package com.huabo.system.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.oracle.service.TblSystemProjectOracleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import redis.clients.jedis.Jedis;

/**
 * 登录初始化加载控制器
 * <p>提供登录后定时信息获取、系统模块同步等初始化接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/loadinit")
@Tag(name="LoadInitializationController",description="加载登录获取定时信息接口")
public class LoadInitializationController {
	
	@Resource
	private TblSystemProjectOracleService tblSystemProjectOracleService;
	
	@Resource
    private UserProvider userProvider;

	@RequestMapping(value = "/getRectificationPlan/dueStr", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="获取即将到期的整改方案信息集合")
    public JsonBean getRectificationPlan_dueStr(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
		 JsonBean jsonBean = null;
		 Jedis jedis = null;
			try {
				TblStaffUtil loginStaff = userProvider.get();
		        if (loginStaff == null) {
		            return ResponseFormat.retParam(0, 20006, null);
		        }
		        List<TblSystemDistribution> list = null;
		        jedis = JedisUtil.getJedis();
		        
		        String disStrs = jedis.get(loginStaff.getStaffid().toString() + JedisUtil.TIPRECTIFICATIONPLAN);
		        
		        if(StringUtils.isNotBlank(disStrs)) {
		        	list = JSONObject.parseArray(disStrs, TblSystemDistribution.class);
				}
		        
		        //jedis.del(loginStaff.getStaffid().toString() + JedisUtil.TIPRECTIFICATIONPLAN);
				jsonBean =  ResponseFormat.retParam(1, 200, list);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (jedis != null) {
	                jedis.close();
	            }
			}
			return jsonBean;
    }
	
	@RequestMapping(value = "/synchronization/systemModule", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="获取即将到期的整改方案信息 (弃用)")
    public JsonBean getRectificationPlan_dueStr(HttpServletRequest request) throws Exception {
		
		this.tblSystemProjectOracleService.syncSystemModuleInit();
		return ResponseFormat.retParam(1, 200, null);
		
	}
}
