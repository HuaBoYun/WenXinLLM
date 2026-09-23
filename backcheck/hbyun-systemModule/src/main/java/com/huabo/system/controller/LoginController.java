package com.huabo.system.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.IpUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.ValidateCode;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.utils.HttpClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 登录控制器
 * <p>提供用户登录验证、验证码获取、退出登录等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/login")
@Slf4j
@Tag(name="LoginController",description="登录接口")
public class LoginController {

	@Resource
	private TblStaffService tblStaffService;

	/**
	 * 生成验证码
	 * @throws IOException
	 */
	@Operation(summary="生成验证码")
	@RequestMapping(value ="/getVerificationCode", method = {RequestMethod.GET})
	public void getVerificationCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ServletOutputStream out = null;
		Jedis jedis = null;
		log.info("登录页面访问生成验证码");
		try {
			// 设置response，输出图片客户端不缓存
			response.setDateHeader("Expires", 0);
			response.addHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.setContentType("image/jpeg");
			out = response.getOutputStream();
			ValidateCode vservice = new ValidateCode();
			String code = vservice.getCode();
			String ip = IpUtil.getIpAddr(request);
			log.info("ip: "+ip+",rightCode: "+code);
			jedis = JedisUtil.getJedis();
			jedis.set(ip, code);
			vservice.write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			jedis.close();
		}finally {
			if(out != null) {
				out.close();
			}
			if(jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * 登录验证用户名验证码 和密码
	 */
	@RequestMapping(value ="/loginCheck", method = {RequestMethod.POST})
	@Operation(summary="设置登录方法")
	public JsonBean loginCheck(HttpServletRequest request,HttpServletResponse response, @Parameter(name="userName",description="用户名",required=true)String userName, @Parameter(name="password",description="密码",required=true)String password,
			 @Parameter(name="loginType",description="登录页面类型",required=false)String loginType ) {
		JsonBean jsonBean = null;
		System.out.println("登录");
		try {
			//获取登录ip地址
			String ip = IpUtil.getIpAddr(request);
			jsonBean = this.tblStaffService.dealLoginSystem(userName,password,loginType,ip,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@RequestMapping(value ="/logout", method = {RequestMethod.POST})
	@Operation(summary= "用户退出登录")
	public JsonBean logout(HttpServletRequest request,HttpServletResponse response ) {
		JsonBean jsonBean = null;
		try {
			//获取登录ip地址
			String ip = IpUtil.getIpAddr(request);
			jsonBean = this.tblStaffService.logout(ip,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	/*给OA提供的单点登录接口*/
	@RequestMapping(value ="/oaLoginCheck", method = {RequestMethod.GET})
	@Operation(summary="给OA提供的单点登录接口")
	public JsonBean oaLoginCheck(HttpServletRequest request,HttpServletResponse response,@Parameter(name="ticket",description="",required=true)@RequestParam(value = "ticket", required = true)String ticket) {
		JsonBean jsonBean = null;
		try {
			String ip = IpUtil.getIpAddr(request);
			String userName=sendGetRequest(HttpClient.OaLoginUrl,ticket);
			if(userName!=null&&StringUtils.isNotBlank(userName)){
			 jsonBean = this.tblStaffService.dealOaLoginSystem(userName,ip,request);
			}else{
				log.error("获取用户信息失败！");
				return ResponseFormat.retParam(0,60001,"获取单点登录用户信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
		return jsonBean;
	}


	/*给OA待办Url提供的接口*/
	@RequestMapping(value ="/oaSendLoginCheck", method = {RequestMethod.GET})
	@Operation(summary="给OA待办跳转审批Url提供的接口")
	public JsonBean oaSendLoginCheck(HttpServletRequest request,HttpServletResponse response,@Parameter(name="v5ticket",description="",required=true)@RequestParam(value = "v5ticket", required = true)String v5ticket) {
		JsonBean jsonBean = null;
		try {
			log.info("给OA待办跳转审批Url提供的接口！");
			String ip = IpUtil.getIpAddr(request);
			//传递ticket 获取OA用户登录名  目前系统用户名与OA用户名一致
			String userName=sendGetRequest(HttpClient.OaLoginUrl,v5ticket);
			if(userName!=null&&StringUtils.isNotBlank(userName)){
			 jsonBean = this.tblStaffService.dealOaLoginSystem(userName,ip,request);
			}else{
				log.error("获取用户信息失败！");
				return ResponseFormat.retParam(0,60001,"获取单点登录用户信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
		return jsonBean;
	}
	public String sendGetRequest(String url, String datas) {
		HttpURLConnection conn = null;
		BufferedReader rd = null;
		StringBuilder sb = new StringBuilder();
		String line = null;
		String response = null;
		try {
			conn = (HttpURLConnection) new java.net.URL(url + datas).openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setUseCaches(false);
			conn.connect();
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			response = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rd != null) {
					rd.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * 给浪潮提供的认证接口
	 * @param request
	 * @param response
	 * @param userName
	 * @return
	 */
	@RequestMapping(value ="/lcLoginCheck", method = {RequestMethod.GET})
	@Operation(summary="给浪潮提供的认证接口")
	public JsonBean lcLoginCheck(HttpServletRequest request,HttpServletResponse response,@Parameter(name="userName",description="",required=true)@RequestParam(value = "userName", required = true)String userName) {
		JsonBean jsonBean = null;
		try {
			String ip = IpUtil.getIpAddr(request);
			//String userName=sendGetRequest(HttpClient.OaLoginUrl,ticket);
			if(userName!=null&&StringUtils.isNotBlank(userName)){
				jsonBean = this.tblStaffService.dealOaLoginSystem(userName,ip,request);
			}else{
				log.error("获取用户信息失败！");
				return ResponseFormat.retParam(0,60001,"获取单点登录用户信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息失败："+e.getMessage());
			return ResponseFormat.retParam(0,60001,e.getMessage());
		}
		return jsonBean;
	}
}
