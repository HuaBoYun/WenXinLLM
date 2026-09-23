package com.huabo.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import redis.clients.jedis.Jedis;

/**
 * 大庆审计指引API对接控制器
 * <p>提供审计指引系统的SSO单点登录、指引分类树、程序分类树等接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/guide")
@Tag(name="AuditGuidelinesApiController",description="大庆审计指引API对接")
public class AuditGuidelinesApiController {
	
	@Value("${auditguidelines.ssoApiUrl}")
	private String ssoApiUrl;
	
	@Value("${auditguidelines.ssoLoginUrl}")
	private String ssoLoginUrl;
	
	@Value("${auditguidelines.ssoLoginUser}")
	private String ssoLoginUser;
	
	@Value("${auditguidelines.ssoLoginPwd}")
	private String ssoLoginPwd;
	
	@Value("${auditguidelines.ssoJedisKey}")
	private String jedisKey;
	
	@Value("${auditguidelines.guidanceClassUrl}")
	private String guidanceClassUrl;
	
	@Value("${auditguidelines.guidancePageUrl}")
	private String guidancePageUrl;
	
	@Value("${auditguidelines.procedureClassUrl}")
	private String procedureClassUrl;
	
	@Value("${auditguidelines.regulationClassUrl}")
	private String regulationClassUrl;
	
	@Value("${auditguidelines.qualitativePageUrl}")
	private String qualitativePageUrl;
	
	@Value("${auditguidelines.qualitativeGetUrl}")
	private String qualitativeGetUrl;
	
	@Value("${auditguidelines.searchUrl}")
	private String searchUrl;
	
	@Value("${auditguidelines.excelPreviewUrl}")
	private String excelPreviewUrl;
	
	@Value("${auditguidelines.pdfPreviewUrl}")
	private String pdfPreviewUrl;
	
	@Value("${auditguidelines.wordPreviewUrl}")
	private String wordPreviewUrl;
	
	@Value("${auditguidelines.searchContentUrl}")
	private String searchContentUrl;
	
	@Resource
    private UserProvider userProvider;
	
	@RequestMapping(value = "/getSsoToken", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计指引单点登录获取token")
    public JsonBean getAllOrgInfoTree(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Jedis jedis = null;
		JSONObject tokenJson = null;
		try {
			jedis = JedisUtil.getJedis();
			tokenJson = this.getSsoTokenApi(jedis);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null) {
				jedis.close();
			}
		}
		return ResponseFormat.retParam(1, 200, tokenJson);
    }
	
	@RequestMapping(value = "/getGuidanceClassTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计对象指引分类树接口")
    public JsonBean getGuidanceClassTree(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "companyName", description = "被审计单位名称", required = true) @RequestParam(value="companyName",required = true)String companyName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+guidanceClassUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("deptName", companyName);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    	    result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    	    reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
		return ResponseFormat.retParam(1, 200, reJson.getJSONArray("data"));
    }
	
	@RequestMapping(value = "/getProcedureClassTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计程序方法指引分类树查询接口")
    public JsonBean getProcedureClassTree(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+procedureClassUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    		result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    		reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
		return ResponseFormat.retParam(1, 200, reJson.getJSONArray("data"));
    }
	
	@RequestMapping(value = "/getRegulationClassTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "用于查询法规制度指引分类树")
    public JsonBean getRegulationClassTree(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+regulationClassUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    		result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    		reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
		return ResponseFormat.retParam(1, 200, reJson.getJSONArray("data"));
    }
	
	
	@RequestMapping(value = "/getFilePageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "文件数据列表通用分页查询接口")
    public JsonBean getFilePageInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
        @Parameter(name = "agcId",description="指引分类id", required = false)@RequestParam(value = "agcId", required = true) Integer agcId,
        @Parameter(name = "fileName",description="筛选条件-文档名称", required = false)@RequestParam(value = "fileName", required = false) String fileName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        
        String url = ssoApiUrl+guidancePageUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("pageNo", pageNumber);
		filedMap.put("pageSize", pageSize);
		filedMap.put("agcId", agcId);
		if(StringUtils.isNotBlank(fileName)) {
			filedMap.put("name", fileName);
		}
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    		result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    		reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
	    JSONObject dataJson = reJson.getJSONObject("data");
	    
	    Map<String, Object> resultMap = new HashMap<String, Object>(0);
	    resultMap.put("pageNumber", pageNumber);
	    resultMap.put("pageSize", pageSize);
	    resultMap.put("tlist", dataJson.getJSONArray("list"));
	    Integer total = dataJson.getInteger("total");
	    resultMap.put("totalRecord", total);
	    Integer totalPage = total/pageSize;
	    if(total%pageSize!=0){
	    	totalPage++;
		}
	    resultMap.put("totalPage", totalPage);
		return ResponseFormat.retParam(1, 200, resultMap);
    }
	
	@RequestMapping(value = "/getQualitativePageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计发现问题定性词典数据列表接口")
    public JsonBean getQualitativePageInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
        @Parameter(name = "qualitativeCode",description="筛选条件-定性编码", required = false)@RequestParam(value = "qualitativeCode", required = false) String qualitativeCode,
        @Parameter(name = "qualitativeName",description="筛选条件-定性名称", required = false)@RequestParam(value = "qualitativeName", required = false) String qualitativeName,
        @Parameter(name = "scopeProblem",description="筛选条件-问题适用范围", required = false)@RequestParam(value = "scopeProblem", required = false) String scopeProblem,
        @Parameter(name = "regulatoryNames",description="筛选条件-法律法规名称", required = false)@RequestParam(value = "regulatoryNames", required = false) String regulatoryNames,
        @Parameter(name = "createTime",description="筛选条件-创建时间createTime[0]: 2024-03-05 00:00:00 createTime[1]: 2024-10-28 23:59:59", required = false)@RequestParam(value = "createTime", required = false) String[] createTime) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        
        String url = ssoApiUrl+qualitativePageUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("pageNo", pageNumber);
		filedMap.put("pageSize", pageSize);
		if(StringUtils.isNotBlank(qualitativeCode)) {
			filedMap.put("qualitativeCode", qualitativeCode);
		}
		if(StringUtils.isNotBlank(qualitativeName)) {
			filedMap.put("qualitativeName", qualitativeName);
		}
		if(StringUtils.isNotBlank(scopeProblem)) {
			filedMap.put("scopeProblem", scopeProblem);
		}
		if(StringUtils.isNotBlank(qualitativeCode)) {
			filedMap.put("qualitativeCode", qualitativeCode);
		}
		if(StringUtils.isNotBlank(regulatoryNames)) {
			filedMap.put("regulatoryNames", regulatoryNames);
		}
		
		if(createTime != null && createTime.length > 1) {
			filedMap.put("createTime", createTime);
		}
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    		result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    		reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
	    JSONObject dataJson = reJson.getJSONObject("data");
	    
	    Map<String, Object> resultMap = new HashMap<String, Object>(0);
	    resultMap.put("pageNumber", pageNumber);
	    resultMap.put("pageSize", pageSize);
	    resultMap.put("tlist", dataJson.getJSONArray("list"));
	    Integer total = dataJson.getInteger("total");
	    resultMap.put("totalRecord", total);
	    Integer totalPage = total/pageSize;
	    if(total%pageSize!=0){
	    	totalPage++;
		}
	    resultMap.put("totalPage", totalPage);
		return ResponseFormat.retParam(1, 200, resultMap);
    }
	
	
	@RequestMapping(value = "/getQualitativeDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计发现问题定性词典查询数据详情接口")
    public JsonBean getQualitativeDetail(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "id",description="指引文件id", required = true)@RequestParam(value = "id", required = true) Integer id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+qualitativeGetUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("id", id);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    		result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    		reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
		return ResponseFormat.retParam(1, 200, reJson.getJSONObject("data"));
    }
	
	
	@RequestMapping(value = "/advancedSearch", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "高级搜索接口")
    public JsonBean advancedSearch(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        @Parameter(name = "keyword",description="筛选条件-搜索关键词", required = true)@RequestParam(value = "keyword", required = true) String keyword,
        @Parameter(name = "idxstr",description="索引（sjglzy：审计管理指引，sjcxffzy：审计程序方法指引，flzdzy：法律制度指引，sjjyzy：审计经验指引）", required = true)@RequestParam(value = "idxstr", required = true) String idxstr,
        @Parameter(name = "level",description="筛选条件-文件级别（10、集团：20、国家：30）", required = false)@RequestParam(value = "level", required = false,defaultValue = "10") String level,
        @Parameter(name = "searchPattern",description="筛选条件-搜索模式（0:精准搜索  1:模糊搜索）", required = false)@RequestParam(value = "searchPattern", required = false,defaultValue = "0") String searchPattern) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        
        String url = ssoApiUrl+searchUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("pageNo", pageNumber);
		filedMap.put("pageSize", pageSize);
		filedMap.put("keyword", keyword);
		filedMap.put("idxstr", idxstr);
		filedMap.put("level", level);
		filedMap.put("searchPattern", searchPattern);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    	    result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    	    reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
	    JSONObject dataJson = reJson.getJSONObject("data");
	    
	    Map<String, Object> resultMap = new HashMap<String, Object>(0);
	    resultMap.put("pageNumber", pageNumber);
	    resultMap.put("pageSize", pageSize);
	    resultMap.put("tlist", dataJson.getJSONArray("list"));
	    Integer total = dataJson.getInteger("hit");
	    resultMap.put("totalRecord", total);
	    Integer totalPage = total/pageSize;
	    if(total%pageSize!=0){
	    	totalPage++;
		}
	    resultMap.put("totalPage", totalPage);
		return ResponseFormat.retParam(1, 200, resultMap);
    }
	
	
	@RequestMapping(value = "/getSearchContentUrl", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "高级搜索在线查阅接口")
    public JsonBean getSearchContentUrl(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "did",description="指引文件did", required = true)@RequestParam(value = "did", required = true) Integer did) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+searchContentUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("did", did);
	    String result = HttpClient.httpGetClient(url, filedMap, headerMap);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    if(reJson.getIntValue("code") != 0) {
	    	if("账号未登录".equals(reJson.getString("msg"))) {
	    		tokenJson = this.getSsoTokenApi();
	    		headerMap.put("Authorization",tokenJson.getString("accessToken"));
	    	    result = HttpClient.httpGetClient(url, filedMap, headerMap);
	    	    reJson = JSONObject.parseObject(result);
	    	}else {
	    		return ResponseFormat.retParam(0, reJson.getString("msg"));
	    	}
	    }
	    
		return ResponseFormat.retParam(1, 200, reJson.getJSONObject("data"));
    }
	
	
	@RequestMapping(value = "/excelPreview", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "在线预览excel文件，该接口返回的数据为字节流")
    public byte[] excelPreview(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "id",description="指引文件id", required = true)@RequestParam(value = "id", required = true) Integer id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+excelPreviewUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("id", id);
		byte[] result = HttpClient.httpGetByteClient(url, filedMap, headerMap);
		
	    return result;
    }
	
	@RequestMapping(value = "/pdfPreview", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "在线预览pdf文件，该接口返回的数据为字节流")
    public byte[] pdfPreview(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "id",description="指引文件id", required = true)@RequestParam(value = "id", required = true) Integer id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+pdfPreviewUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("id", id);
		byte[] result = HttpClient.httpGetByteClient(url, filedMap, headerMap);
	    
	    return result;
    }
	
	@RequestMapping(value = "/wordPreview", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "在线预览word文件，该接口返回的数据为字节流")
    public byte[] wordPreview(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "id",description="指引文件id", required = true)@RequestParam(value = "id", required = true) Integer id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return null;
        }
        JSONObject tokenJson = this.getSsoTokenFunc();
        String url = ssoApiUrl+wordPreviewUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    headerMap.put("Authorization",tokenJson.getString("accessToken"));
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("did", id);
	    byte[] result = HttpClient.httpGetByteClient(url, filedMap, headerMap);
		
		return result;
    }
	
	private JSONObject getSsoTokenFunc() throws Exception {
		Jedis jedis = null;
		String ssoTokenRedis = null;
		JSONObject redisJson = null;
		try {
			jedis = JedisUtil.getJedis();
			ssoTokenRedis = jedis.get(jedisKey);
			
			if(StringUtils.isBlank(ssoTokenRedis)) {
				//登录获取token并存入redis
				redisJson = this.getSsoTokenApi(jedis);
			}else {
				//获取redis中的token信息 ，并依据当前系统时间判断token是否过期
				redisJson = JSONObject.parseObject(ssoTokenRedis);
				
				long redisTime = redisJson.getLongValue("expiresTime");
				long currentTime = System.currentTimeMillis(); 
				if(currentTime < redisTime) {
					//token未过期直接返回redisJson
					return redisJson;
				}else {
					//token已过期，重新登录获取token并存入redis信息
					redisJson = this.getSsoTokenApi(jedis);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(jedis != null) {
				jedis.close();
			}
		}
		return redisJson;
	}

	private JSONObject getSsoTokenApi(Jedis jedis) throws Exception {
		String url = ssoApiUrl+ssoLoginUrl;
	    Map<String, String> headerMap = new HashMap<>();
	    
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("username", ssoLoginUser);
		filedMap.put("password", EncryptUtil.getInstance().AESdecode(ssoLoginPwd,EncryptUtil.DESKEY));
	    String result = HttpClient.httpPostClient(url, filedMap,headerMap,HttpClient.PARAMJSON);
		
	    JSONObject reJson = JSONObject.parseObject(result);
	    
	    JSONObject dataJson = reJson.getJSONObject("data");
	    
	    String refToken = dataJson.getString("accessToken");
	    
	    //token按照审计指引要求增加 'Bearer '
	    dataJson.put("accessToken", "Bearer "+refToken);
	    jedis.set(jedisKey, JSONObject.toJSONString(dataJson));
	    return dataJson;
	}
	
	private JSONObject getSsoTokenApi() {
		String url = ssoApiUrl+ssoLoginUrl;
		Jedis jedis = null;
	    Map<String, String> headerMap = new HashMap<>();
	    
	    JSONObject dataJson = null;
		try {
			jedis = JedisUtil.getJedis();
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("username", ssoLoginUser);
			filedMap.put("password", EncryptUtil.getInstance().AESdecode(ssoLoginPwd,EncryptUtil.DESKEY));
			String result = HttpClient.httpPostClient(url, filedMap,headerMap,HttpClient.PARAMJSON);
			
			JSONObject reJson = JSONObject.parseObject(result);
			
			dataJson = reJson.getJSONObject("data");
			
			String refToken = dataJson.getString("accessToken");
			
			//token按照审计指引要求增加 'Bearer '
			dataJson.put("accessToken", "Bearer "+refToken);
			jedis.set(jedisKey, JSONObject.toJSONString(dataJson));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(jedis != null) {
				jedis.close();
			}
		}
	    return dataJson;
	}
	
}
