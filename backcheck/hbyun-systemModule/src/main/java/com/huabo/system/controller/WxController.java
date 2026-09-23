package com.huabo.system.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.SNSUserInfo;
import com.hbfk.entity.WeiXinUtil;
import com.hbfk.entity.WeixinOauth2Token;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblWxUserInfo;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.TblWxUserInfoService;

import cn.hutool.http.HttpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 微信小程序控制器
 * <p>提供微信小程序的登录授权、用户信息获取、消息推送等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/wxindex")
@Tag(name="wxindex",description="微信相关方法的接口口")
public class WxController {

	@Resource
	private TblStaffService tblStaffService;

	@Resource
	public TblWxUserInfoService tblWxUserInfoService;

	@Resource
	private TblOrganizaService tblOrganizaService;

	@GetMapping("/qrcode")
	@Operation(summary="获取扫码登录的二维码")
    public JsonBean  generateQRCode(HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //微信规定需要对redirect_url进行URLEncoder编码
        String redirectUrl = WeiXinUtil.redirectUrl;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl, "utf-8");
        }catch(Exception e) {
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                WeiXinUtil.pcappid,
                redirectUrl,
                "atguigu"
        );

        //重定向到请求微信地址里面
        return ResponseFormat.retParam(1, 200, "redirect:"+url);
    }


   //扫码成功所走方法
    @PostMapping(value = "wxLogin")
    @Operation(summary="云端扫一扫二维码登录")
    public JsonBean WeiXinTest(ModelMap map, HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="code",description="微信回调Code",required=true) @RequestParam(value = "code", required = true) String code) throws Exception{
    	response.addHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        //获取微信用户是否授权
        WeixinOauth2Token oauth2Token =WeiXinUtil.getOauth2AccessToken(WeiXinUtil.pcappid, WeiXinUtil.pcappSecret, code);
        String accessToken=oauth2Token.getAccessToken();
        String openId=oauth2Token.getOpenId();
        System.out.println("openid:============="+openId);
        System.out.println("acces_TOKEN:=========="+accessToken);

       SNSUserInfo  snsUserInfo=new  SNSUserInfo();
       //根据授权码和openid获取用户信息
       if (openId!=null) {
        	snsUserInfo = WeiXinUtil.getSNSUserInfo(accessToken, openId);
		}
        //根据用户的唯一键unionid比对用户表中的用户，是否有此用户
        if(snsUserInfo.getUnionid()!=null){
            return tblStaffService.findStaffByUnionId(snsUserInfo.getUnionid());
        }else{
        	return ResponseFormat.retParam(0,20011,null);
        }
    }

    @GetMapping(value = "/getWxuserPhone", produces = "application/json; charset=utf-8")
    @Operation(summary="微信获取用户手机号")
	public JsonBean getUserPhone(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name="aesInfo",description="微信用户信息",required=true) @RequestParam(value = "code", required = true)String code) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String,String> resultMap = new HashMap<String,String>(0);
		Object res = null;
		try {
			 //通过appid和secret来获取token
            //WXContent.APPID是自定义的全局变量
            String tokenUrl = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",WeiXinUtil.appid, WeiXinUtil.appSecret);
            JSONObject token = JSON.parseObject(HttpUtil.get(tokenUrl));

            //通过token和code来获取用户手机号
            String url = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=" + token.getString("access_token");

            //封装请求体
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("code", code);

            //封装请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(paramMap,headers);

            //通过RestTemplate发送请求，获取到用户手机号码
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> responseResult = restTemplate.postForEntity(url, httpEntity, Object.class);

            //返回到前端展示
            res = responseResult.getBody();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(200,200,res);
	}

    /**
	 * 获取微信用户unionId等敏感信息
	 * userInfoMap存储用户信息
	 */
    @GetMapping("/getSensitiveInfo")
	@Operation(summary="获取微信用户unionId等敏感信息")
	public JsonBean getSensitiveInfo(HttpServletRequest request,HttpServletResponse response, String code,String avatarurl,String appName,String iv ,String encryptedData){
		response.addHeader("Access-Control-Allow-Origin", "*");
		//logger.info("访问路径 /wxLogin/getSensitiveInfo, 获取微信用户unioiId等信息，传入参数：code="+code);
		Map<String,String> userInfoMap = null;
		try {
			userInfoMap = new HashMap<String, String>();
			if (code == null || code.length() == 0) {
				userInfoMap.put("status", "0");
			}else{
				String wxspAppid = WeiXinUtil.appid;
				//小程序的 app secret (在微信小程序管理后台获取)
				String wxspSecret = WeiXinUtil.appSecret;
				//授权（必填）
				String grant_type = WeiXinUtil.GRANT_TYPE;


				//////////////// 1、向微信服务器 使用登录凭证 code 获取 session_key 和 openid ////////////////
				//请求参数
				String params = "appid=" + wxspAppid + "&secret="REDACTED"&js_code=" + code + "&grant_type=" + grant_type;
				//发送请求 开始获取微信用户信息，访问地址https://api.weixin.qq.com/sns/jscode2session，传入参数：
				String sr = HttpClient.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
				//logger.info("获取微信用户加密信息成功，返回结果"+sr);
				JSONObject json = JSONObject.parseObject(sr);
				//获取会话密钥（session_key）
				String sessionkey = json.get("session_key").toString();
				userInfoMap.put("sessionkey", sessionkey);
		        //String result = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
		        //logger.info("微信用户信息解密成功，返回结果："+result);
		        //if (null != result && result.length() > 0) {
		        userInfoMap.put("openId", json.get("openid").toString());
		        userInfoMap.put("unionId", json.get("unionid").toString());
		            //{"unionid":"oKihF0t5QN0sIgGaK3OMGaxS9luI","openid":"ojiL50KM8aExL7eQYHdpjNhPGzm0","session_key":"rXwmNA+WKFwmr4RuJxM13w=="}

		            	TblWxUserInfo userInfo = tblWxUserInfoService.findWxUserInfoByOpenId(json.get("unionid").toString());
		            	//TblWxUserInfo userInfo = tblWxUserInfoService.findWxUserInfoByOpenId("oKihF0uLlvVwybYUsXIdOh0H88bU");
			            if(userInfo == null){
			        	   userInfoMap.put("status", "1"); //没有用户信息前往注册页面
			            }else{
			        	   /*Integer orgFatherId = tblOrganizationService.findRegisterOrganizationCount(userInfo.getOrgId(),TblOrganization.REGISTERORGFATHERID);
			        	   logger.info("判断该用户的根级公司注册是否到期，1：未到期，0：已到期 返回结果："+orgFatherId);
			        	   if(orgFatherId == 1||SystemStaticValue.WBSJSTAFFID.indexOf(userInfo.getStaffId()) == -1 ) {*/
			        		   if(avatarurl != null && !avatarurl.equals("undefined")){
			        			   if(!avatarurl.equals(userInfo.getAvatarUrl())) {
			        				   userInfo.setAvatarUrl(avatarurl);
				        			   tblWxUserInfoService.updateAvatarUrl(userInfo);
			        			   }
			        		   }
			        		  /* Integer companyId = this.tblOrganizaService.findCompanyIdByDeptId(userInfo.getOrgId().toString());
			        		   TblStaff staff = this.tblStaffService.selectWxappAdmin(new BigDecimal(companyId));
			        		   if(userInfo.getStaffId().compareTo(staff.getStaffid()) == 0) {
			        			   userInfo.setIsAdmin(0);//是管理员
			        		   }else {
			        			   userInfo.setIsAdmin(1);//不是管理员
			        		   }*/
			        		   userInfoMap.put("status","2");//状态等于二 可以正常使用
			        		   userInfoMap.put("userInfo",JSON.toJSONString(userInfo));

			        		   this.tblStaffService.dealWxUserInfo(userInfoMap,userInfo);
			            }
		       //}
			}
			//logger.info("微信用户信息获取成功：返回参数对象："+userInfoMap);
	    } catch (Exception e) {
	    	userInfoMap.put("status", "-1");//系统异常
	    	e.printStackTrace();
	    }
		return ResponseFormat.retParam(200,200,userInfoMap);
	}


    /**
	 * 注册
	 *
	 * @param orgid
	 * @return
	 */
    @PostMapping(value = "/register", produces = "application/json; charset=utf-8")
	@Operation(summary="微信小程序用户注册")
	public JsonBean register(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name="user",description="微信用户信息",required=true) @RequestParam(value = "user", required = true)String user) throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.tblStaffService.wxXcxRegisterUser(user);
	}

    /**
   	 * 注册
   	 *
   	 * @param orgid
   	 * @return
   	 */
    @PostMapping(value = "/modifyUserInfo", produces = "application/json; charset=utf-8")
   	@Operation(summary="微信小程序用户修改用户昵称 、头像等数据")
   	public JsonBean modifyUserInfo(HttpServletRequest request,HttpServletResponse response,TblWxUserInfo userInfo) throws Exception {
   		response.addHeader("Access-Control-Allow-Origin", "*");
   		return this.tblStaffService.wxXcxModifyUser(userInfo);
   	}



	/**
	 * 小程序邀请注册
	 * @param request
	 * @param response
	 * @param user
	 * @return
	 * @throws Exception
	 */
    @GetMapping(value = "/inviteregister", produces = "application/json; charset=utf-8")
	@Operation(summary="微信小程序用户邀请注册")
	public JsonBean inviteregister(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name="user",description="微信用户信息",required=true) @RequestParam(value = "user", required = true)String user) throws Exception {
		response.addHeader("Access-Control-Allow-Origin", "*");
		return this.tblStaffService.wxXcxInviteRegisterUser(user);
	}

    /**
     * 校验公司名称是否重复
     * @param request
     * @param response
     * @param user
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/checkCompanyName", produces = "application/json; charset=utf-8")
   	@Operation(summary="校验公司名称是否重复")
   	public JsonBean checkCompanyName(HttpServletRequest request,HttpServletResponse response,
   			@Parameter(name="companyName",description="公司名称",required=true) @RequestParam(value = "companyName", required = true)String companyName) throws Exception {
   		response.addHeader("Access-Control-Allow-Origin", "*");
   		return this.tblOrganizaService.checkCompanyName(companyName);
   	}

	@GetMapping(value="/getQrCode" ,produces = "application/json; charset=utf-8")
	@Operation(summary="获取邀请二维码")
	public byte[] getQrCode(HttpServletRequest request,String orgId,HttpServletResponse response,String appName){
		response.addHeader("Access-Control-Allow-Origin", "*");
        byte[] result = null;
		try {
			String params = "appid=" + WeiXinUtil.appid + "&secret="REDACTED"&grant_type=client_credential";

			//logger.info("开始获取access_token，访问地址https://api.weixin.qq.com/cgi-bin/token，传入参数："+params);
			String sr = HttpClient.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
			//logger.info("获取access_token，返回结果"+sr);
			JSONObject json = JSONObject.parseObject(sr);
			String accessToken = json.get("access_token").toString();

			Map<String,Object> map = new HashMap<String,Object>(0);
			map.put("width", 160);
			map.put("path", "pages/mine/inviteregister/inviteregister");
			map.put("scene",orgId);//邀请码
			params = JSON.toJSONString(map);
			//logger.info("开始获取二维码信息，访问地址https://api.weixin.qq.com/wxa/getwxacodeunlimit，传入参数："+params);
			String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
			RestTemplate rest = new RestTemplate();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity requestEntity = new HttpEntity(map, headers);
			ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
			//logger.info("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
			result = entity.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

        }
		return result;
	}

	/**
	 *
	 * @param request
	 * @return
	 */
	@GetMapping(value="/getInviteQrCode" ,produces = "application/json; charset=utf-8")
	@Operation(summary="获取推广二维码")
	public byte[] getSessionId(HttpServletRequest request,String staffId,String appName){
        byte[] result = null;
		try {
			String params = "appid=" + WeiXinUtil.appid + "&secret="REDACTED"&grant_type=client_credential";
			//logger.info("开始获取access_token，访问地址https://api.weixin.qq.com/cgi-bin/token，传入参数："+params);
			String sr = HttpClient.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
			//logger.info("获取access_token，返回结果"+sr);
			JSONObject json = JSONObject.parseObject(sr);
			String accessToken = json.get("access_token").toString();

			Map<String,Object> map = new HashMap<String,Object>(0);
			map.put("width", 160);
			map.put("path", "pages/work/work");
			map.put("scene",staffId+",1");//推广码
			params = JSON.toJSONString(map);
			//logger.info("开始获取二维码信息，访问地址https://api.weixin.qq.com/wxa/getwxacodeunlimit，传入参数："+params);
			String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken;
			RestTemplate rest = new RestTemplate();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity requestEntity = new HttpEntity(map, headers);
			ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
			//logger.info("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
			result = entity.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

        }
		return result;
	}


	@GetMapping(value = "/findDeptByCompanyName", produces = "application/json; charset=utf-8")
	@Operation(summary="注册时 判断公司名称是否包含，返还管理员姓名")
	public Map<String, Object> findDeptByCompanyName(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name="companyName",description="公司名称",required=true) @RequestParam(value = "companyName", required = true)String companyName){
		response.addHeader("Access-Control-Allow-Origin", "*");
		//logger.info("访问路径 /wxLogin/findDeptByCompanyName,注册用户根据公司名称获取部门信息，传入参数：companyName："+companyName);
		Map<String, Object> resultMap = new HashMap<String, Object>(0);

		try {
			Integer count = 0;
			String adminName = null;
				count = this.tblOrganizaService.selectCountByName(companyName);
				//查找注册公司第一个注册的人 管理员
				if(count > 0) {
					BigDecimal orgId = this.tblOrganizaService.findDeptInfoByorgName(companyName);
					TblStaff staff = this.tblStaffService.selectWxappAdmin(orgId);
					adminName = staff.getRealname();
				}
			resultMap.put("adminName",adminName);
			resultMap.put("result", count);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}


	/**
   	 * 文档生成接口
   	 *
   	 * @param orgid
   	 * @return
   	 */
    @PostMapping(value = "/generateDoc", produces = "application/json; charset=utf-8")
   	@Operation(summary="微信小程序用户修改用户昵称 、头像等数据")
   	public JsonBean generateDoc(HttpServletRequest request,HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="prompt",description="提问的问题",required=false) @RequestParam(value = "prompt", required = true) String prompt,
    		@Parameter(name="think",description="是否使用think模型",required=false) @RequestParam(value = "think", required = false) boolean think,
    		@Parameter(name="history",description="jsonArray数组",required=false) @RequestParam(value = "history", required = false) String history) throws Exception {
   		response.addHeader("Access-Control-Allow-Origin", "*");
   		return this.tblWxUserInfoService.generateDoc(token,prompt,history,think);
   	}

}
