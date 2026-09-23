package com.huabo.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblOaFlowMessage;
import com.huabo.system.oracle.service.TblOaFlowMessageService;
import com.huabo.system.utils.HttpClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * OA流程消息集成控制器
 * <p>提供OA流程消息的列表查询、修改等集成接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="Oa流程消息集成",description="Oa流程消息集成")
public class OaMessageController {
	
	@Resource
	private TblOaFlowMessageService tblOaFlowMessageService;
	
	@Resource
    private UserProvider userProvider;
	
	@RequestMapping(value = "/oaInfo/flowInfoList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
	@Operation(summary="OA消息获取待办消息集合分页")
	public JsonBean choosetContractType(HttpServletRequest request,
		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
		@Parameter(description="每页数量",required=false)@RequestParam(value="pageSize",required=false,defaultValue="5") Integer pageSize,
		@Parameter(description="当前页数",required=false)@RequestParam(value="currentPage",required=false,defaultValue="1") Integer currentPage,
		@Parameter(description="状态，已办-col_done",required=false)@RequestParam(value="state",required=false) String state,
		@Parameter(description="流程标题模糊查询",required=false)@RequestParam(value="title",required=false) String title,
		@Parameter(description="流程所属类型模糊查询",required=false)@RequestParam(value="flowType",required=false) String flowType){
		 String url = null;
		 Map<String, Object> resultMap = new HashMap<String, Object>(0);
         try {
        	 TblStaffUtil loginStaff = userProvider.get();
  	       	 if (loginStaff == null) {
  	       		 return ResponseFormat.retParam(0, 20006, null);
  	         }
  	       	 
  	       	 PageInfo<TblOaFlowMessage> pageInfo = new PageInfo<TblOaFlowMessage>();
  	       	 pageInfo.setCurrentPage(currentPage);
  	       	 pageInfo.setPageSize(pageSize);
  	       	 if(flowType != null && !"".equals(flowType)) {
  	       		 TblOaFlowMessage condition = new TblOaFlowMessage();
  	       		 condition.setTitle(title);
  	       		 condition.setFlowType(flowType);
  	       		 if(state != null && "col_done".equals(state)) {
  	       			 condition.setFlowModule("已办");
  	       		 }else {
  	       			 condition.setFlowModule("待办");
  	       		 }
  	       		 
  	       		 pageInfo.setCondition(condition);
  	       		 
  	       		 this.tblOaFlowMessageService.setPageInfoList(pageInfo);
  	       		 
  	       		 
  	       	 }else {
	  	       	CloseableHttpClient httpClient = HttpClients.createDefault();
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(300 * 1000)
	                    .setConnectTimeout(300 * 1000)
	                    .build();
	            JSONObject obj=new JSONObject();
	            obj.put("page", currentPage);
	            obj.put("pageSize",pageSize);
	            obj.put("loginName",loginStaff.getUsername());
	            if(title != null && !"".equals(title)) {
	            	obj.put("subject", title);
	            }
	            if(state != null && !"".equals(state)) {
	            	obj.put("state", state);
	            }
	            
	            url = HttpClient.oaUrl+HttpClient.oaFlowInfoList;
	            HttpPost post = new HttpPost(url);
	            
	            post.setConfig(requestConfig);
	            
	            post.setHeader("Content-Type","application/json;charset=utf-8");
	            String oaToken = this.getOaDocumentToken(loginStaff.getUsername());
	            post.setHeader("token",oaToken);
	            
	            System.out.println(obj.toString());
	            StringEntity postingString = new StringEntity(obj.toString(),
	                    "utf-8");
	            post.setEntity(postingString);
	            HttpResponse response = httpClient.execute(post);
	            String content = EntityUtils.toString(response.getEntity());
	            JSONObject result = JSONObject.parseObject(content);
	            String code = result.getString("code");
	            if(!"0".equals(code)) {
	           	 return ResponseFormat.retParam(0, "OA消息待办接口调用失败", null);
	            }
	            
	            JSONObject datasJson = result.getJSONObject("datas");
	            
	            Integer dataCount = datasJson.getInteger("dataCount");
	            Integer totlaCount = datasJson.getInteger("total");
	            List<TblOaFlowMessage> messageList = new ArrayList<TblOaFlowMessage>(0);
	            if(dataCount > 0 ) {
	           	 messageList = JSONObject.parseArray(datasJson.getString("data"), TblOaFlowMessage.class);
	            }
	            
	            TblOaFlowMessage message = null;
	            if(state != null && !"col_done".equals(state)) {
	           	 for (TblOaFlowMessage flow : messageList) {
	           		message = this.tblOaFlowMessageService.findEntityById(flow.getId());
	   				if(message != null ) {
	   					flow.setFlowType(message.getFlowType());
	   					if(message.getFlowModule() != null && !"已办".equals(message.getFlowModule())) {
	   						message.setFlowModule("已办");
	   						this.tblOaFlowMessageService.modifyFlowMessage(message);
	   					}
	   				}
	                }
	            }else {
	           	 for (TblOaFlowMessage flow : messageList) {
	           		message = this.tblOaFlowMessageService.findEntityById(flow.getId());
	           		if(message != null ) {
	           			flow.setFlowType(message.getFlowType());
	   				}
	               	 
	                }
	            }
	            pageInfo.setTlist(messageList);
	            pageInfo.setTotalRecord(totlaCount);
  	       	 }
             resultMap.put("pageInfo", pageInfo);
         } catch (Exception e) {
        	 ResponseFormat.retParam(0, "系统异常", null);
         }
         return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	
	@RequestMapping(value = "/oaInfo/modifyFlowInfo",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
	@Operation(summary="OA消息获取待办消息集合分页")
	public JsonBean oaInfo_modifyFlowInfo(HttpServletRequest request,TblOaFlowMessage tblOaFlowMessage,
		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception{
         try {
        	 TblStaffUtil loginStaff = userProvider.get();
  	       	 if (loginStaff == null) {
  	       		 return ResponseFormat.retParam(0, 20006, null);
  	         }
  	       	 tblOaFlowMessage.setOrgid(loginStaff.getLinkDetp().getOrgid());
  	       	 tblOaFlowMessage.setOrgname(loginStaff.getLinkDetp().getOrgname());
  	         tblOaFlowMessage.setStaffid(loginStaff.getStaffid());
  	         tblOaFlowMessage.setRealname(loginStaff.getRealname());
  	       	 this.tblOaFlowMessageService.saveOrUpdateEntity(tblOaFlowMessage);
         } catch (Exception e) {
        	 return null;
         }
         return ResponseFormat.retParam(1, 200, null);
	}
	
	
	//获取OAtoken数据
	public String getOaDocumentToken(String loginname) {
		String token=null;
		try { 
			 String url="";
	            CloseableHttpClient httpClient = HttpClients.createDefault();
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(300 * 1000)
	                    .setConnectTimeout(300 * 1000)
	                    .build();
	            url = HttpClient.oaUrl+HttpClient.getToken;
	            HttpPost post = new HttpPost(url);
	            post.setConfig(requestConfig);
	            JSONObject obj=new JSONObject();
	            obj.put("userName", HttpClient.restuname);//第三方待办主键（保证唯一）
	            obj.put("password", HttpClient.restpassword);//为第三方配置的系统注册编码
	            obj.put("loginName", loginname);//为第三方配置的系统注册编码
	            post.setHeader("Content-Type","application/json;charset=utf-8");
	            StringEntity postingString = new StringEntity(obj.toString(),
	                    "utf-8");
	            post.setEntity(postingString);
	            HttpResponse response = httpClient.execute(post);
	            String content = EntityUtils.toString(response.getEntity());
	            JSONObject result=JSONObject.parseObject(content);
	            token= result.get("id").toString();
	            System.out.println(token);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取token失败："+e.getMessage());
			return token;
		}
		return token;
	}
}
