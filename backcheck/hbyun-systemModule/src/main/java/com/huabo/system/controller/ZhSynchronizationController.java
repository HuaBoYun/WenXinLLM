package com.huabo.system.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblFlowTaskInfo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSynchronizationRecord;
import com.huabo.system.mapper.TblSynchronizationRecordMapper;
import com.huabo.system.service.TblJobGradeService;
import com.huabo.system.service.TblJobService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.TblSystemDistributionService;
import com.huabo.system.service.YMBusinessService;

import cn.hutool.json.JSONArray;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 中核主数据组织结构集成控制器
 * <p>提供中核主数据组织结构同步（索为）和门户系统单点登录等集成接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "中核-主数据组织结构集成接口（索为）and门户系统单点登录接口", description = "中核-主数据组织结构集成接口and门户系统单点登录接口")
public class ZhSynchronizationController {
	private static final Log logger = LogFactory.getLog(ZhSynchronizationController.class);
    public static ResourceBundle mainData = ResourceBundle.getBundle("setting/mainDataResources");

	public String knToken;

    @Resource
    private TblStaffService tblStaffService;

    @Resource
    private TblJobService tblJobService;

    @Resource
    private TblOrganizaService tblOrganizaService;

    @Resource
    private TblJobGradeService tblJobGradeService;

	@Resource
	public YMBusinessService ymBusinessService;

	@Resource
	RestTemplate restTemplate;

	@Resource
	private UserProvider userProvider;

	@Resource
	private TblSystemDistributionService tblSystemDistributionService;

	@Resource
	private TblSynchronizationRecordMapper tblSynchronizationRecordMapper;

//	@Resource
//	private TblBigDataService tblBigDataService;

//	/**
//     * 必要参数
//     *
//     * @return
//     * @throws Exception
//     */
//    private String getRequiredParams() throws Exception {
//        String devToken = getToken(this.dev_account, this.dev_password);
//        userId = getUserId(this.phone, devToken);
//        return String.format("&dev_token=%s&user_id=%s&dev_login_type=%s", devToken, userId, "PHONE");
//    }

//    org: 组织信息
//    dept: 部门信息
//    user: 人员基本信息
//    home:家庭信息--
//    partJob: 兼职记录--
//    abroad: 出国境情况--
//    personPostCode: 岗位信息

	/**
	 * 1.先完成公司、部门信息同步
	 * 2.不做自动同步，通过手动调用接口进行同步
	 * 3.只处理新增、修改、不做删除
	 * 4.人员信息同步注意密级信息
	 */

	private static final String getDepartUrl = mainData.getString("mainDataIP") + mainData.getString("getDepartUrl");


	/**
	 * 更新组织信息(全量)
	 */
    @RequestMapping(value = "/updateOrgInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="更新组织信息(全量)")
    public void updateOrgInfo(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) {
    	TblSynchronizationRecord record = new TblSynchronizationRecord();
    	record.setRecordtype("org");
    	record.setCreatetime(new Date());
    	record.setRecordid(RandomUtil.uuStringId());

        String xmlRequest = "<![CDATA[<requestData>"
        		+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>"
        		+ "<time></time>"
        		+ "<objType>"+mainData.getString("orgType")+"</objType>"
        		+ "</requestData>]]>";
        try {
        	String response = callWebServiceBySOAP(xmlRequest);
        	record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);

            tblOrganizaService.syncZhOrg(response,record);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 更新组织信息(增量)
	 * 注意：增量更新的返回结果中没有batch节点
	 */
	@Scheduled(cron = "0 0 22 25 5 ?")
	@RequestMapping(value = "/updateOrgNewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="更新组织信息(增量)")
    public void updateOrgNewInfo() throws Exception {
		TblSynchronizationRecord record = new TblSynchronizationRecord();
    	record.setRecordtype("org");
    	record.setCreatetime(new Date());
    	record.setRecordid(RandomUtil.uuStringId());

    	//获取上一次同步时间
//    	Date lastDate = this.tblSynchronizationRecordMapper.selectLastSyncDateByType(record.getRecordtype());
    	Date lastDate = null;

        String xmlRequest = "<![CDATA[<requestData>"
        		+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>";

        if(lastDate == null) {
        	xmlRequest += "<time>2025-5-10</time>";
        }else {
        	xmlRequest += "<time>"+DateUtil.parseDate(lastDate, DateUtil.DATE_SMALL_STR)+"</time>";
        }
        xmlRequest	+= "<objType>"+mainData.getString("orgType")+"</objType>"
        		+ "</requestData>]]>";

		System.out.println(xmlRequest);

        try {
        	String response = callWebServiceBySOAP(xmlRequest);

        	record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);

            tblOrganizaService.syncZhNewOrg(response,record);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//	@RequestMapping(value = "/updateOrgNewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
//	@Operation(summary="更新组织信息(增量)")
	//更新组织信息 增量 定时任务 每天晚上十一点触发
	@Scheduled(cron = "0 0 23 * * ?")
	public void scheduleUpdateOrgNewInfo() {
		TblSynchronizationRecord record = new TblSynchronizationRecord();
    	record.setRecordtype("org");
    	record.setCreatetime(new Date());
    	record.setRecordid(RandomUtil.uuStringId());
    	//获取上一次同步时间
    	Date lastDate = this.tblSynchronizationRecordMapper.selectLastSyncDateByType(record.getRecordtype());

		String xmlRequest = "<![CDATA[<requestData>"
				+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>";

		if(lastDate == null) {
        	xmlRequest += "<time>2024-11-20</time>";
        }else {
        	xmlRequest += "<time>"+DateUtil.parseDate(lastDate, DateUtil.DATE_SMALL_STR)+"</time>";
        }
		xmlRequest += "<objType>"+mainData.getString("orgType")+"</objType>"
				+ "</requestData>]]>";
		try {
			String response = callWebServiceBySOAP(xmlRequest);

			record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);

			tblOrganizaService.syncZhNewOrg(response,record);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用纯 SOAP HTTP 请求调用 WebService 接口
	 */
	private String callWebServiceBySOAP(String xmlRequest) throws IOException {
		String soapAction = "urn:getOrgHrInfo";

		String soapEnvelope = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://server.adapter.sysware.com\">\n" +
				"   <soapenv:Header/>\n" +
				"   <soapenv:Body>\n" +
				"     <ser:getOrgHrInfo>\n" +
				"       <ser:xmlStr>\n" +
				"         " + xmlRequest + "\n" +
				"       </ser:xmlStr>\n" +
				"     </ser:getOrgHrInfo>\n" +
				"   </soapenv:Body>\n" +
				"</soapenv:Envelope>";

		try {
			URL url = new URL(getDepartUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(500000);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			connection.setRequestProperty("SOAPAction", soapAction);

			// 发送请求
			try (OutputStream out = connection.getOutputStream()) {
				out.write(soapEnvelope.getBytes(StandardCharsets.UTF_8));
			}

			// 判断响应状态
			int responseCode = connection.getResponseCode();

			// 获取响应
			StringBuilder response = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
			}

			return response.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

//	@RequestMapping(value = "/updateDeptInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
//    @Operation(summary="更新部门信息--已废除，由更新组织接口统一更新公司和部门")
//    public void updateDeptInfo(HttpServletRequest request) {
//		String xmlRequest = "<![CDATA[<requestData><sysCode>"+mainData.getString("sysCode")+"</sysCode><time></time><objType>"+mainData.getString("orgType")+"</objType></requestData>]]>";
//        try {
//			String response = callWebServiceBySOAP(xmlRequest);
//			System.out.println("更新部门信息--响应:");
//			System.out.println(response);
//
//			// 同步本地数据
//			TblOrganizaService.syncZhDept(response);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//    }

    @RequestMapping(value = "/updateUserInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="更新人员信息(全量)")
    public void updateUserInfo(HttpServletRequest request) {
    	String xmlRequest = "<![CDATA[<requestData>"
    			+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>"
    			+ "<time></time>"
//    			+ "<time>2025-03-20</time>"
    			+ "<objType>"+mainData.getString("userType")+"</objType>"
    			+ "</requestData>]]>";
		try {

			TblSynchronizationRecord record = new TblSynchronizationRecord();
	    	record.setRecordtype("staff");
	    	record.setCreatetime(new Date());
	    	record.setRecordid(RandomUtil.uuStringId());

			// 调用统一的 callWebServiceBySOAP 方法
			String response = callWebServiceBySOAP(xmlRequest);
//			String response = "";
			record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);
			// 处理返回结果
			tblStaffService.syncZhUser(response,record);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@RequestMapping(value = "/updateUserInfo2", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="更新人员信息(全量)")
	public void updateUserInfo2(HttpServletRequest request) {
		String xmlRequest = "<![CDATA[<requestData>"
				+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>"
//    			+ "<time>2024-11-20</time>"
//				+ "<time>2025-03-20</time>"
				+ "<objType>"+mainData.getString("userType")+"</objType>"
				+ "</requestData>]]>";
		try {

			TblSynchronizationRecord record = new TblSynchronizationRecord();
			record.setRecordtype("staff");
			record.setCreatetime(new Date());
			record.setRecordid(RandomUtil.uuStringId());

			// 调用统一的 callWebServiceBySOAP 方法
			String response = callWebServiceBySOAP(xmlRequest);
			record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);
			// 处理返回结果
			tblStaffService.syncZhUser(response,record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @RequestMapping(value = "/updateUserNewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="更新人员信息(增量)")
    public void updateUserNewInfo(HttpServletRequest request) {
    	TblSynchronizationRecord record = new TblSynchronizationRecord();
    	record.setRecordtype("staff");
    	record.setCreatetime(new Date());
    	record.setRecordid(RandomUtil.uuStringId());

    	Date lastDate = this.tblSynchronizationRecordMapper.selectLastSyncDateByType(record.getRecordtype());

    	String xmlRequest = "<![CDATA[<requestData>"
    			+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>";
    	if(lastDate == null) {
    		xmlRequest += "<time>2024-11-20</time>";
    	}else {
    		xmlRequest += "<time>"+DateUtil.parseDate(lastDate, DateUtil.DATE_SMALL_STR)+"</time>";
    	}
    	xmlRequest += "<objType>"+mainData.getString("userType")+"</objType>"
    			+ "</requestData>]]>";
		try {
			// 调用统一的 callWebServiceBySOAP 方法
			String response = callWebServiceBySOAP(xmlRequest);

			record.setResponsetext("请求地址："+getDepartUrl+"；请求参数："+xmlRequest+"；响应结果："+response);

			// 处理返回结果
			tblStaffService.syncZhNewUser(response,record);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

//	@RequestMapping(value = "/updateUserNewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
//	@Operation(summary="更新人员信息(增量)")
	//更新人员信息 增量 定时任务 每天晚上十一点触发
	@Scheduled(cron = "0 0 23 * * ?")
	public void scheduleUpdateUserNewInfo() {
		TblSynchronizationRecord record = new TblSynchronizationRecord();
    	record.setRecordtype("staff");
    	record.setCreatetime(new Date());
    	record.setRecordid(RandomUtil.uuStringId());

    	Date lastDate = this.tblSynchronizationRecordMapper.selectLastSyncDateByType(record.getRecordtype());

		String xmlRequest = "<![CDATA[<requestData>"
				+ "<sysCode>"+mainData.getString("sysCode")+"</sysCode>";

		if(lastDate == null) {
		    xmlRequest += "<time>2024-11-20</time>";
		}else {
		    xmlRequest += "<time>"+DateUtil.parseDate(lastDate, DateUtil.DATE_SMALL_STR)+"</time>";
		}

		xmlRequest += "<objType>"+mainData.getString("userType")+"</objType>"
				+ "</requestData>]]>";
		try {
			// 调用统一的 callWebServiceBySOAP 方法
			String response = callWebServiceBySOAP(xmlRequest);
			System.out.println("更新人员信息--响应（注意：增量更新的返回结果中没有batch节点）:");
			System.out.println(response);

			// 处理返回结果
			tblStaffService.syncZhNewUser(response,record);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//    @RequestMapping(value = "/updatePersonPostCodeInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
//    @Operation(summary="更新岗位信息")
//    public void updatePersonPostCodeInfo(HttpServletRequest request) {
//		try {
//            String url = mainData.getString("mainDataIP") + mainData.getString("inUrl")
//            		+ "?sysCode="+mainData.getString("sysCode") + "&time=2024-11-06 17:38:03" + "&objType=personPostCode";
//            String result = HttpUtil.post(url, "");
//            TblOrganizaService.syncZhPersonPostCode(result);
//		} catch (Exception e) {
//			ResponseFormat.retParam(1,1000,e.getMessage());
//		}
//    }

    //提供给门户系统 请求一体化系统的接口，获取门户系统token和重定向地址
    @RequestMapping(value ="/gwLoginCheck", method = {RequestMethod.GET})
	@Operation(summary="提供给门户系统 请求一体化系统的接口，获取门户系统token和重定向地址")
	public ModelAndView gwLoginCheck(HttpServletRequest request,
			@Parameter(name="portalToken",description="",required=false)@RequestParam(value = "portalToken", required = false) String portalToken,
			@Parameter(name="redirectionUrl",description="",required=false)@RequestParam(value = "redirectionUrl", required = false) String redirectionUrl,
			@Parameter(name="key",description="",required=false)@RequestParam(value = "key", required = false) String key) {
		JsonBean jsonBean = null;
		try {
			//接受门户系统数据
			System.out.println("1111接受门户系统portalToken================="+portalToken);
			System.out.println("redirectionUrl================="+redirectionUrl);
			System.out.println("key================="+key);
			String ip = IpUtil.getIpAddr(request);
        	System.out.println("===========用户访问IP地址："+ip);

			//调用门户系统授权认证接口
			String url="";
            CloseableHttpClient chttpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            url = mainData.getString("gwIp") + mainData.getString("gwAuthUrl");
			HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONObject obj=new JSONObject();
            post.setHeader("Content-Type","application/json;charset=utf-8");
            portalToken = "Bearer "+portalToken;
            post.setHeader("Authorization",portalToken);
            StringEntity postingString = new StringEntity(obj.toString(),
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = chttpClient.execute(post);
            String content = EntityUtils.toString(response.getEntity());
            System.out.println("门户系统返回content===="+content);
            JSONObject result=JSONObject.parseObject(content);
            String code = code= result.get("code").toString();
            if(null!=code && "200".equals(code)) {
            	String data = code= result.get("data").toString();
            	JSONObject dataObj=JSONObject.parseObject(data);
            	String userCode = code= dataObj.get("userCode").toString();

            	//通过userCode获取用户信息并实现登录
//            	String fkythToken = this.tblStaffService.dealGWLoginSystem(userCode,ip);
//            	String fkythUrl = "redirect:http://192.0.2.200/#/?token="+fkythToken;
            	String fkythUrl = "redirect:http://192.0.2.200/#/sso-auth?ssoname="+userCode;
                return new ModelAndView(fkythUrl);

            	//
            }else{
				log.error("获取用户信息失败！");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户信息失败："+e.getMessage());
			return null;
		}
	}


    /**
     * 统一跳转，根据用户名获取token
     */
    @GetMapping(value = "/getzhToken")
    @Operation(summary="统一跳转，根据用户名获取token")
    public JsonBean getzhToken(HttpServletRequest request,
    		@Parameter(name = "ssoname", description = "登录账号", required = true) @RequestParam("ssoname") String ssoname) {
    	JsonBean jsonBean = null;
		try {
			String ip = IpUtil.getIpAddr(request);
			System.out.println("===========用户访问IP地址："+ip);
			String fkythToken = this.tblStaffService.dealGWLoginSystem(ssoname,ip);
			Map<String,Object> resultMap = new HashMap<>();
		    resultMap.put("token", fkythToken);
		    jsonBean= ResponseFormat.retParam(1,200,resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			 jsonBean= ResponseFormat.retParam(0,"获取用户失败！",null);
		}


        return jsonBean;
    }

    /**
     * 根据用户修改登出时间
     */
    @PostMapping(value = "/getUsersx")
    @Operation(summary="根据用户修改登出时间")
    public void getUsersx(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
    		@Parameter(name = "username", description = "账号", required = true) @RequestParam("username") String username) {
		try {
			String ip = IpUtil.getIpAddr(request);
			System.out.println("===========用户访问IP地址："+ip);
			this.tblStaffService.userSx(ip, username, request);
		} catch (Exception e) {
			e.printStackTrace();
		}


    }





    @RequestMapping(value = "/adm/importOrg",method = {RequestMethod.POST})
	@Operation(summary="导入公司信息(EXCEL)")
	public String importOrg(HttpServletRequest request,
								@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token,
								@Parameter(name="excelPath",description="",required=false)@RequestParam(value = "excelPath", required = false) String excelPath){
		String result = null;
		try {
			System.out.println(excelPath);
			Map<String,Object>  resultMap = this.tblOrganizaService.importOrg(token,excelPath);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

    @RequestMapping(value = "/adm/importDept",method = {RequestMethod.POST})
	@Operation(summary="导入部门信息(EXCEL)")
	public String importDept(HttpServletRequest request,
								@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token,
								@Parameter(name="excelPath",description="",required=false)@RequestParam(value = "excelPath", required = false) String excelPath){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblOrganizaService.importDept(token,excelPath);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

    //导入人员信息(EXCEL)
    @RequestMapping(value = "/adm/importStaff",method = {RequestMethod.POST})
	@Operation(summary="导入人员信息(EXCEL)")
	public String importStaff(HttpServletRequest request,
								@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token,
								@Parameter(name="excelPath",description="",required=false)@RequestParam(value = "excelPath", required = false) String excelPath){
		String result = null;
		try {
			Map<String,Object>  resultMap = this.tblStaffService.importStaff(token,excelPath);
			JSONObject jsonObj = new JSONObject(resultMap);
			result = jsonObj.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

    //导入角色信息，科长、处长、公司领导(EXCEL)
    @RequestMapping(value = "/adm/importZHRole",method = {RequestMethod.POST})
   	@Operation(summary="导入角色信息，科长、处长、公司领导(EXCEL)")
   	public String importZHRole(HttpServletRequest request,
   								@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token,
   								@Parameter(name="excelPath",description="",required=false)@RequestParam(value = "excelPath", required = false) String excelPath){
   		String result = null;
   		try {
   			Map<String,Object>  resultMap = this.tblStaffService.importZHRole(token,excelPath);
   			JSONObject jsonObj = new JSONObject(resultMap);
   			result = jsonObj.toString();
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   		return result;
   	}
    //增量接口 sendSubmitInfo
	public String saveTask(TblFlowTaskInfo taskInfo, TblStaff submitStaff, TblStaff nextStaff) throws Exception {
		String url = null;
		JSONObject jb=new JSONObject();
		jb.put("code",0);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(300 * 1000)
					.setConnectTimeout(300 * 1000)
					.build();
			JSONObject obj=new JSONObject();
			obj.put("taskSource", "FK");//待办来源
			obj.put("secrecy","");//密级
			obj.put("parentProcessId","");//父流程ID
			obj.put("processId",taskInfo.getProcessId());//流程ID
			obj.put("processName",taskInfo.getTaskTitle());//流程名称
			obj.put("nodeId",taskInfo.getThisStepId());//节点ID
			obj.put("nodeName",taskInfo.getCurrenRole());//节点名称
			obj.put("taskId",taskInfo.getFlowTaskId());//任务ID
			obj.put("taskName",taskInfo.getTaskTitle());//任务名称
			obj.put("taskType",taskInfo.getModuleType());//任务类型
			obj.put("dataId",taskInfo.getThisStepId());//数据ID
			obj.put("topic",taskInfo.getTaskTitle());//主题
			obj.put("redirectionUrl", mainData.getString("loginUrl")+"/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());//跳转链接
			obj.put("status",1);//状态：1:未办理；2:已办理
			obj.put("releaseDate",sdf.format(taskInfo.getCreateTime()));//发布时间
			obj.put("releaseId",submitStaff.getStaffid());//发布人ID
			obj.put("releaseName",submitStaff.getUsername());//发布人姓名
			obj.put("releaseDeptId",submitStaff.getFgorgs());//发布人部门ID
			obj.put("releaseDeptName",submitStaff.getFgorgnames());//发布人部门名称
			obj.put("createdDate",sdf.format(taskInfo.getCreateTime()));//创建时间
			obj.put("createdId",submitStaff.getStaffid());//创建人ID
			obj.put("createdName",submitStaff.getUsername());//创建人姓名
			obj.put("createdDeptID",submitStaff.getFgorgs());//创建人部门ID
			obj.put("createdDeptName",submitStaff.getFgorgnames());//创建人部门名称
			obj.put("processingDate",sdf.format(taskInfo.getCreateTime()));//处理时间
			obj.put("personId",nextStaff.getStaffid());//待处理人ID
			obj.put("personName",nextStaff.getUsername());//待处理人姓名
			obj.put("processingId","");//处理人ID
			obj.put("processingName","");//处理人姓名
			obj.put("processingDeptId","");//处理人部门ID
			obj.put("processingDeptName","");//处理人部门名称
			obj.put("priority","");//优先级
			System.out.print("obj内容为processingId:"+obj);
			url = mainData.getString("gwIp")+mainData.getString("gwsaveTask");
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			com.alibaba.fastjson.JSONArray arr=new com.alibaba.fastjson.JSONArray();
			arr.add(obj);
			JSONObject object=new JSONObject();
			object.put("pendingList", arr);
			System.out.println("arr:"+arr);
			post.setHeader("Content-Type","application/json;charset=utf-8");
			post.setHeader("identity",mainData.getString("gwidentity"));
			StringEntity postingString = new StringEntity(object.toString(),
					"utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			String content = EntityUtils.toString(response.getEntity());
			System.out.println(content);
			return content;
		} catch (SocketTimeoutException e) {
			return jb.toString();
		} catch (Exception e) {
			return jb.toString();
		}
	}


	//待办任务状态改变接口
	public String changeTaskStatus(TblFlowTaskInfo preTaskInfo, int type, TblStaffUtil loginStaff) {
		String url = null;
		JSONObject jb=new JSONObject();
		jb.put("code",0);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(300 * 1000)
					.setConnectTimeout(300 * 1000)
					.build();
			JSONObject obj=new JSONObject();
			obj.put("taskSource", "FK");//待办来源
			obj.put("taskId",preTaskInfo.getFlowTaskId());//任务ID
			obj.put("processingId",loginStaff.getStaffid());//处理人ID
			obj.put("processingName",loginStaff.getUsername());//处理人姓名
			obj.put("status",type);//状态：0:未办理；1:已办理
			url = mainData.getString("gwIp")+mainData.getString("gwchangeTaskStatus");
			System.out.println(url);
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			JSONArray arr=new JSONArray();
			arr.add(obj);
			JSONObject object=new JSONObject();
			object.put("messages", arr);
			post.setHeader("Content-Type","application/json;charset=utf-8");
			post.setHeader("identity",mainData.getString("gwidentity"));
			System.out.println(mainData.getString("gwidentity"));
			System.out.println("Obj:"+obj.toString());
			StringEntity postingString = new StringEntity(obj.toString(),
					"utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			String content = EntityUtils.toString(response.getEntity());
			System.out.println("content:"+content);
			return content;
		} catch (SocketTimeoutException e) {
			return jb.toString();
		} catch (Exception e) {
			return jb.toString();
		}
	}
	//任务删除接口
	public String deleteTask(TblFlowTaskInfo taskInfo) {
		String url = null;
		JSONObject jb=new JSONObject();
		jb.put("code",0);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(300 * 1000)
					.setConnectTimeout(300 * 1000)
					.build();
			JSONObject obj=new JSONObject();
			obj.put("taskSource", "FK");//待办来源
			obj.put("processId",taskInfo.getProcessId());//流程Id

			url = mainData.getString("gwIp")+mainData.getString("gwdeleteTask");
			System.out.println(url);
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			JSONArray arr=new JSONArray();
			arr.add(obj);
			JSONObject object=new JSONObject();
			object.put("messages", arr);
			post.setHeader("Content-Type","application/json;charset=utf-8");
			post.setHeader("identity",mainData.getString("gwidentity"));
			System.out.println(mainData.getString("gwidentity"));
			System.out.println("Obj:"+obj.toString());
			StringEntity postingString = new StringEntity(obj.toString(),
					"utf-8");
			post.setEntity(postingString);
			HttpResponse response = httpClient.execute(post);
			String content = EntityUtils.toString(response.getEntity());
			System.out.println("content:"+content);
			return content;
		} catch (SocketTimeoutException e) {
			return jb.toString();
		} catch (Exception e) {
			return jb.toString();
		}
	}
	// 统计待办数量接口
	@RequestMapping(value = "/counttask", method = {RequestMethod.GET})
	@Operation(summary="统计待办数量接口")
	public String counttask(@RequestParam(value = "userCode", required = false) String userCode) {
		JSONObject jb = new JSONObject();
		jb.put("code", 0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			//门户系统入参userCode为风控系统的登陆账号username，通过username获取token信息
			String fkythToken = this.tblStaffService.dealGWLoginSystem(userCode,null);
			if(null==fkythToken) {
				return null;
			}
			//获取审批待办数量
			JsonBean dealt = ymBusinessService.getDealtMH(fkythToken, 1, 10, "");
			Integer totalCount = null;
			if (dealt.getData() instanceof Map) {
				Map<String, Object> dataMap = (Map<String, Object>) dealt.getData();
				if (dataMap.containsKey("totalCount")) {
					totalCount = (Integer) dataMap.get("totalCount");
					System.out.println("totalCount: " + totalCount);
				} else {
					System.out.println("数据中不包含 totalCount 字段");
				}
			} else {
				System.out.println("data 不是 Map 类型，无法直接获取 totalCount");
			}
			//获取下发通知待办数量
			Integer issuedCount = this.tblSystemDistributionService.getDistributionCount(fkythToken);
			Integer counttask = totalCount+issuedCount;
			System.out.println("用户："+userCode+"-待办数量为:"+counttask+"---------------------------------------------------------");
			return counttask+"";
		} catch (SocketTimeoutException e) {
			return jb.toString();
		} catch (Exception e) {
			return jb.toString();
		}
	}

		@RequestMapping(value = "/issuedCountTask", method = {RequestMethod.GET})
		@Operation(summary=" 风险、内控、审计下发消息待办数量接口 ")
		public String issuedCountTask(@RequestParam(value = "userCode", required = false) String userCode) {
			JSONObject jb = new JSONObject();
			jb.put("code", 0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				//门户系统入参userCode为风控系统的登陆账号username，通过username获取token信息
				String fkythToken = this.tblStaffService.dealGWLoginSystem(userCode,null);
				if(null==fkythToken) {
					return null;
				}
				Integer counttask = this.tblSystemDistributionService.getDistributionCount(fkythToken);
				System.out.println("用户："+userCode+"-下发消息待办数量为:"+counttask+"---------------------------------------------------------");
				return counttask+"";
			} catch (SocketTimeoutException e) {
				return jb.toString();
			} catch (Exception e) {
				return jb.toString();
			}
		}

	//知识管理平台对接  192.0.2.200
	@RequestMapping(value = "/knowledge", method = {RequestMethod.POST})
	@Operation(summary="知识管理平台对接")
	public JsonBean knowledge( @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
							 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
							 @Parameter(name="Token",description="登录用户Token",required=false) @RequestHeader("Token")String Token
	) throws Exception {
		System.out.println(knToken);
		TblStaffUtil staff = userProvider.get();
		if (staff == null){
			return ResponseFormat.retParam(0, 20006, null);
		}
		if (knToken == null){
			knToken = this.getToken(staff.getUsername());
		}
		Boolean validToken = this.isValidToken(knToken);
		if(!validToken){
			knToken = this.getToken(staff.getUsername());
		}

		String url = mainData.getString("Fileurl");
		// 使用 LinkedMultiValueMap 正确构建表单参数
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("module", "FolderOperationManager");
		params.add("fun", "SearchFolderList");
		params.add("pageNum", pageNumber);
		params.add("pageSize", pageSize);
		params.add("sortField", "folderCreateTime");
		params.add("sortDesc", "true");
		params.add("folderId", "1");
		params.add("startDate", "2022-04-06 00:00:00");
		params.add("token", knToken);
		// 获取当前时间
		LocalDateTime now = LocalDateTime.now();// 定义时间格式
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");// 格式化当前时间
		String currentDateTime = now.format(formatter);// 添加到参数中
		params.add("endDate", currentDateTime);
//		params.add("personalFolderHidden", "true");

		// 设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// 创建正确的请求实体
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
		try {
			/*String result = "{\n" +
					"\t\"folderOptCountList\": [{\n" +
					"\t\t\"folderId\": 464,\n" +
					"\t\t\"folderName\": \"bb\",\n" +
					"\t\t\"folderPath\": \"企业内容库\\\\test1\\\\bb\",\n" +
					"\t\t\"curFolderCount\": 0,\n" +
					"\t\t\"curFileCount\": 0,\n" +
					"\t\t\"usedSize\": 0,\n" +
					"\t\t\"maxSize\": 0,\n" +
					"\t\t\"folderCreateTime\": \"2025-04-15 12:19:31\",\n" +
					"\t\t\"departName\": \" /某某公司\"\n" +
					"\t},  {\n" +
					"\t\t\"folderId\": 446,\n" +
					"'folderName': \"test1\",\n" +
					"'folderPath':  \"企业内容库\\\\test1\",\n" +
					"'curFolderCount': 1,\n" +
					"'curFileCount': 1,\n" +
					"'usedSize': 14756,\n" +
					"'maxSize': 0,\n" +
					"\t\t\"folderCreateTime\":  \"2025-01-15 15:36:36\",\n" +
					"\t\t\"departName\": \" /某某公司/默默机构 / 某某部门\"\n" +
					"\t}],\n" +
					"\t\"totalCount\":2,\n" +
					"\t\"result\": 0\n" +
					"}";
			JSONObject jsonObject = JSONObject.parseObject(result);
			System.out.println(result);
			return ResponseFormat.retParam(200, 200, jsonObject);*/
			// 发送 POST 请求并获取响应
			ResponseEntity<String> response = restTemplate.exchange(
					url,
					HttpMethod.POST,
					requestEntity,
					String.class
			);
			System.out.println("响应内容: "+response);
			log.info("响应内容: " + response);
			// 检查响应状态
			if (response.getStatusCode().is2xxSuccessful()) {
				//解析响应体为JsonNode
				String result = response.getBody();

				Integer start = result.indexOf("{");
				Integer end = result.indexOf("result");
				Integer index = result.indexOf("}", end);
				result = result.substring(start,index+1);
				JSONObject jsonObject = JSONObject.parseObject(result);
				System.out.println(result);
				return ResponseFormat.retParam(200, 200, jsonObject);

//				return response.toString(); // 返回实际响应内容
			} else {
				// 处理非成功状态码
				log.error("API 请求失败: {}", response.getStatusCode());
				return ResponseFormat.retParam(500, 1000, null);
			}
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// 处理 HTTP 错误
			log.error("HTTP 错误: {}", e.getStatusCode());
			return ResponseFormat.retParam(500, 1000, null);
		} catch (Exception e) {
			// 处理其他异常
			log.error("API 调用异常: {}", e.getMessage());
			return ResponseFormat.retParam(500, 1000, null);
		}
	}

		@PostMapping("/knowledgeSearch")
		@Operation(summary="搜索文件列表")
		public JsonBean knowledgeSearch(
				@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
				@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
				@Parameter(name = "folderId", description = "文件夹ID", required = false)@RequestParam(value = "folderId", required = true) Integer folderId,
				@Parameter(name = "fileVerCode", description = "文件版本编码", required = false)@RequestParam(value = "fileVerCode", required = false) String fileVerCode,
				@Parameter(name = "fileSecurityLevel", description = "文件名", required = false)@RequestParam(value = "fileSecurityLevel", required = false) String fileSecurityLevel,
				@Parameter(name = "fileExtName", description = "文件扩展名（如：pdf、docx）", required = false)@RequestParam(value = "fileExtName", required = false) String fileExtName,
				@Parameter(name = "fileRemark", description = "文件备注", required = false)@RequestParam(value = "fileRemark", required = false) String fileRemark,
				@Parameter(name="Token",description="登录用户Token",required=false) @RequestHeader("Token")String Token
				) throws Exception {

			TblStaffUtil staff = userProvider.get();
			if (staff == null){
				return ResponseFormat.retParam(0, 20006, null);
			}

			Boolean validToken = this.isValidToken(knToken);
			if(!validToken){
				knToken = this.getToken(staff.getUsername());
			}

			String url = mainData.getString("Fileurl");
			// 构建请求参数
			MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
			params.add("module", "FileOperationManager");
			params.add("fun", "GetAllFileList");
			params.add("pageNum", pageNum);
			params.add("pageSize", pageSize);
			params.add("sortField", "fileName");
			params.add("sortDesc", true);
			params.add("token", knToken);
			params.add("folderId", folderId);
			if (fileVerCode != null) {
				params.add("fileVerCode", fileVerCode);
			}
			params.add("fileModifyTimeStart", "2022-04-06");

			if (fileSecurityLevel != null) {
				params.add("fileName", fileSecurityLevel);
			}
			if (fileExtName != null) {
				params.add("fileExtName", fileExtName);
			}
			if (fileRemark != null) {
				params.add("fileRemark", fileRemark);
			}
			// 设置当前时间作为默认结束时间（如果未提供）
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String currentDate = now.format(formatter);
			params.add("fileModifyTimeEnd", currentDate);
//			params.add("personalFolderHidden", true);

			// 设置请求头
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			// 创建请求实体
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

			try {
				// 发送POST请求并获取响应
				ResponseEntity<String> response = restTemplate.exchange(
						url,
						HttpMethod.POST,
						requestEntity,
						String.class
				);

				log.info("文件搜索响应: {}", response);
				System.out.println("文件搜索响应: " + response);
				// 检查响应状态
				if (response.getStatusCode().is2xxSuccessful()) {
//					return response.getBody(); // 返回实际响应内容
					String result = response.getBody();// 返回实际响应内容

					JSONObject jsonObject = JSONObject.parseObject(result);
					return ResponseFormat.retParam(200, 200, jsonObject);
				} else {
					log.error("文件搜索API请求失败: {}", response.getStatusCode());
					return ResponseFormat.retParam(500, 1000, null);
				}
				/*String result = "{\n" +
						"\t\"folderOptCountList\": [{\n" +
						"\t\t\"folderId\": 464,\n" +
						"\t\t\"folderName\": \"bb\",\n" +
						"\t\t\"folderPath\": \"企业内容库\\test1\\bb\",\n" +
						"\t\t\"curFolderCount\": 0,\n" +
						"\t\t\"curFileCount\": 0,\n" +
						"\t\t\"usedSize\": 0,\n" +
						"\t\t\"maxSize\": 0,\n" +
						"'folderCreateTime': '2025-04-15 12:19:31',\n" +
						"'departName': '某某公司'" +
						"\t},  {\n" +
						"'folderId': 446,\n" +
						"'folderName': \"test1\",\n" +
						"'folderPath': \"企业内容库\\test1\",\n" +
						"'curFolderCount': 1,\n" +
						"'curFileCount': 1,\n" +
						"'usedSize': 14756,\n" +
						"'maxSize': 0,\n" +
						"'folderCreateTime': '2025-01-1515:36:36',\n" +
						"'departName':'某某公司/默默机构/某某部门'" +
						"\t}],\n" +
						"\t\"totalCount\": 2,\n" +
						"\t\"result\": 0\n" +
						"}";
				JSONObject jsonObject = JSONObject.parseObject(result);
				return ResponseFormat.retParam(200, 200, jsonObject);*/
			} catch (Exception e) {
				log.error("文件搜索API调用异常: {}", e.getMessage());
				return ResponseFormat.retParam(500, 1000, null);
			}
		}

        @RequestMapping("/getFileUrl")
		@Operation(summary="获取链接")
		public JsonBean getFileUrl(
				@Parameter(name = "fileId", description = "文件ID", required = true)@RequestParam(value = "fileId", required = true) String fileId,
				@Parameter(name="Token",description="登录用户Token",required=false) @RequestHeader("Token")String Token
		) throws Exception {
			TblStaffUtil staff = userProvider.get();
			if (staff == null){
				return ResponseFormat.retParam(0, 20006, null);
			}

			Boolean validToken = this.isValidToken(knToken);
			if(!validToken){
				knToken = this.getToken(staff.getUsername());
			}
			String URL = "http://192.0.2.200/jump.html?token="+knToken+"&returnUrl=http://192.0.2.200/preview.html?fileid="+fileId;
			String result = "{"+"data"+":'"+URL.toString()+"'}";
			System.out.println(result);
			JSONObject jsonObject = JSONObject.parseObject(result);
			return ResponseFormat.retParam(200, 200, jsonObject);
		}


/*	@PostMapping("/getToken")
	@Operation(summary="获取Token")*/
	public String getToken(@Parameter(name="username",description="登录用户",required=false) @RequestParam("username")String username) throws Exception {
		System.out.println("getToken 开始执行");
		System.out.println("getToken 当前用户为："+username);
		String clientIp =  mainData.getString("getToknenIP");

		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = mainData.getString("getToknenUrl");
		//构建请求体
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("LoginName", username);//登录账号
		requestBody.put("IPAddress", clientIp);//登陆方IP
//		requestBody.put("apiUrl", apiUrl);//http:<ip>:<prot>/api/services/Org/UserLoginIntegrationByUserLoginName
		requestBody.put("IntegrationKey", mainData.getString("IntegrationKey"));
		//设置请求头
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		//发送请求
		ResponseEntity<Map> response =restTemplate.exchange(
				apiUrl,
				HttpMethod.POST,
				new HttpEntity<>(requestBody, headers),
				Map.class
		);
		System.out.println("getToken 响应结果："+ response);
		//处理响应
		if(response.getStatusCode() == HttpStatus.OK){
			Map<String, Object> body = response.getBody();
			if (body!=null && body.get("result").equals(0)){
				String knowToken = (String) body.get("data");//返回token
				knToken = knowToken;
				return knowToken;
			}
		}
		return null;
//		return "123Token";
	}



	public Boolean isValidToken(@Parameter(name = "token", description = "知识管理平台token", required = false)@RequestParam(value = "token", required = false) String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		String apiUrl = mainData.getString("isValidTokenToken");
		RestTemplate restTemplate = new RestTemplate();
		// 构建URL参数
		String url = apiUrl + "?token=" + token;
		// 发送请求
		ResponseEntity<Map> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				HttpEntity.EMPTY,
				Map.class
		);
		// 处理响应
		if (response.getStatusCode().is2xxSuccessful()) {
			Map<String, Object> body = response.getBody();
			log.info(body.toString());
			Boolean result =( body != null
					&& body.get("result").equals(0)
					&& Boolean.TRUE.equals(body.get("data")));
			return result;
		}
		return false;
//		return true;

	}
//	public void sendDealResult(TblFlowTaskInfo preTaskInfo, int i, int i1) {
//	}
//	@Scheduled(fixedDelay = 1000*60*60)
//	public void syncBigData() {
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String currentTime = sdf.format(new Date());
//			System.out.println("定时任务执行时间: " + currentTime);
//			// 在这里编写你想要执行的逻辑
//                String url = mainData.getString("bigdataUrl1");
//                String result = HttpRequest.get(url).header(Header.ACCEPT,"*/*")
//						.header("Content-Type","application/json;utf-8")
//						.header("token",mainData.getString("bigdataToken"))
//						.execute().body();
//				/*String result ="{\n" +
//				"\t\"codea\": 200,\n" +
//				"\t\"data\": {\n" +
//				"\t\t\"columnNames\": [\n" +
//				"\t\t\t\"CODE_BM_MEATYPE\",\n" +
//				"\t\t\t\"CODE_ENTITY\",\n" +
//				"\t\t\t\"CODE_VERSION\",\n" +
//				"\t\t\t\"CODE_MEASURE\",\n" +
//				"\t\t\t\"CODE_MVTYPE\",\n" +
//				"\t\t\t\"CODE_BM_TRAIL\",\n" +
//				"\t\t\t\"PK_ACCP\",\n" +
//				"\t\t\t\"PK_ACCM\",\n" +
//				"\t\t\t\"VALUE\",\n" +
//				"\t\t\t\"FZHJ\",\n" +
//				"\t\t\t\"ZCHJ\"\n" +
//				"\t\t],\n" +
//				"\t\t\"value\": [{\n" +
//				"\t\t\t\t\"CODE_BM_MEATYPE\": \"测试数据1\",\n" +
//				"\t\t\t\t\"CODE_ENTITY\": \"测试数据11\",\n" +
//				"\t\t\t\t\"CODE_VERSION\": \"测试数据111\",\n" +
//				"\t\t\t\t\"CODE_MEASURE\": \"测试数据1111\",\n" +
//				"\t\t\t\t\"CODE_MVTYPE\": \"测试数据11111\",\n" +
//				"\t\t\t\t\"CODE_BM_TRAIL\": \"测试数据111111\",\n" +
//				"\t\t\t\t\"PK_ACCP\": \"10\",\n" +
//				"\t\t\t\t\"PK_ACCM\": \"20\",\n" +
//				"\t\t\t\t\"VALUE\": \"30.3\",\n" +
//				"\t\t\t\t\"FZHJ\": \"40.4\",\n" +
//				"\t\t\t\t\"ZCHJ\": \"50.5\"\n" +
//				"\t\t\t},\n" +
//				"\t\t\t{\n" +
//				"\t\t\t\t\"CODE_BM_MEATYPE\": \"gai测试数据2\",\n" +
//				"\t\t\t\t\"CODE_ENTITY\": \"gay测试数据22\",\n" +
//				"\t\t\t\t\"CODE_MEASURE\": \"ga测试数据2222\",\n" +
//				"\t\t\t\t\"CODE_MVTYPE\": \"ga测试数据22222\",\n" +
//				"\t\t\t\t\"CODE_BM_TRAIL\": \"测试数ga据222222\",\n" +
//				"\t\t\t\t\"PK_ACCM\": \"220\",\n" +
//				"\t\t\t\t\"VALUE\": \"230.3\",\n" +
//				"\t\t\t\t\"FZHJ\": \"240.4\",\n" +
//				"\t\t\t}\n" +
//				"\n" +
//				"\t\t],\n" +
//				"\t\t\"valueSize\": 1384\n" +
//				"\t},\n" +
//				"\t\"message\": \"success\",\n" +
//				"\t\"success\": true\n" +
//				"}";*/
//			System.out.println(result);
//			tblBigDataService.syncBigData( result);
//		} catch (Exception e) {
//			System.out.println("定时任务执行异常");
//		}
//	}
//
//	@Scheduled(fixedDelay = 1000*60*60)
//	public void syncBigDataTCY() {
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String currentTime = sdf.format(new Date());
//			System.out.println("定时任务执行时间: " + currentTime);
//			// 在这里编写你想要执行的逻辑
//			String url = mainData.getString("bigdataUrl2");
//			String result = HttpRequest.get(url).header(Header.ACCEPT,"*/*")
//					.header("Content-Type","application/json;utf-8")
//					.header("token",mainData.getString("bigdataToken"))
//					.execute().body();
//
//			/*String result ="{\n" +
//					"\t\"codea\": 200,\n" +
//					"\t\"data\": {\n" +
//					"\t\t\"columnNames\": [\n" +
//					"\t\t\t\"CODE_BM_MEATYPE\",\n" +
//					"\t\t\t\"CODE_ENTITY\",\n" +
//					"\t\t\t\"CODE_VERSION\",\n" +
//					"\t\t\t\"CODE_MEASURE\",\n" +
//					"\t\t\t\"CODE_MVTYPE\",\n" +
//					"\t\t\t\"CODE_BM_TRAIL\",\n" +
//					"\t\t\t\"PK_ACCP\",\n" +
//					"\t\t\t\"PK_ACCM\",\n" +
//					"\t\t\t\"VALUE\",\n" +
//					"\t\t\t\"FZHJ\",\n" +
//					"\t\t\t\"ZCHJ\"\n" +
//					"\t\t],\n" +
//					"\t\t\"value\": [{\n" +
//					"\t\t\t\t\"CODE_BM_MEATYPE\": \"测试数据1\",\n" +
//					"\t\t\t\t\"VALUE\": \"测试数据11\",\n" +
//					"\t\t\t\t\"CODE_MEASURE\": \"测试数据111\",\n" +
//					"\t\t\t\t\"CODE_ENTITY\": \"测试数据1111\",\n" +
//					"\t\t\t\t\"CODE_VERSION\": \"测试数据11111\",\n" +
//					"\t\t\t\t\"CODE_MVTYPE\": \"测试数据111111\",\n" +
//					"\t\t\t},\n" +
//					"\t\t\t{\n" +
//					"\t\t\t\t\"CODE_BM_MEATYPE\": \"测试数据2\",\n" +
//					"\t\t\t\t\"VALUE\": \"测试数据22\",\n" +
//					"\t\t\t\t\"CODE_MEASURE\": \"ga测试数据2222\",\n" +
//					"\t\t\t\t\"CODE_ENTITY\": \"ga测试数据22222\",\n" +
//					"\t\t\t\t\"CODE_VERSION\": \"测试数ga据222222\",\n" +
//					"\t\t\t\t\"CODE_MVTYPE\": \"220\",\n" +
//					"\t\t\t}\n" +
//					"\n" +
//					"\t\t],\n" +
//					"\t\t\"valueSize\": 1384\n" +
//					"\t},\n" +
//					"\t\"message\": \"success\",\n" +
//					"\t\"success\": true\n" +
//					"}";*/
//			System.out.println(result);
//			tblBigDataService.syncBigDataTCY(result);
//		} catch (Exception e) {
//			System.out.println("定时任务执行异常");
//		}
//	}
}
