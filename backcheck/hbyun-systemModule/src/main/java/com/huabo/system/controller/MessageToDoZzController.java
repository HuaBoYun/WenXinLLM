package com.huabo.system.controller;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblFlowTaskInfo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.service.MessageToDoZzService;
import com.huabo.system.utils.HttpClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * OA集成待办控制器
 * <p>提供OA系统待办事项的发送、完成、查询等集成接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "OA集成待办Controller", description = "OA集成待办")
@RequestMapping("/message")
public class MessageToDoZzController {

	@Resource
	public MessageToDoZzService messageToDoZzService;
	
	@RequestMapping(value = "/getOaToken", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="获取OA验证token")
	public String getOaToken() {
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
	
	
	//待办方法  
	/*
	 * param type: 0 待办  1 取消待办
	 * 
	 * 
	 * */
	public  String sendToDo(TblSystemSheetTable sheet,BigDecimal fromId,String ymflowid,TblStaffUtil loginStaff,String type) {
        String url = null;
        JSONObject jb=new JSONObject();
        jb.put("code",0);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            com.alibaba.fastjson.JSONObject obj=messageToDoZzService.returnObject(sheet,fromId,loginStaff,type,ymflowid);
            url = HttpClient.oaUrl+HttpClient.sendToDo;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONArray arr=new JSONArray();
            arr.add(obj);
            JSONObject object=new JSONObject();
            object.put("pendingList", arr);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
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
	
	
	
	//已办方法
	public  String sendToDoDone(Integer taskId,Integer state) {
        String url = null;
        JSONObject jb=new JSONObject();
        jb.put("code",0);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            url = HttpClient.oaUrl+HttpClient.sendToDoDone;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
            JSONObject obj=new JSONObject();
            obj.put("registerCode",HttpClient.oaCode);//第三方待办主键（保证唯一）
            obj.put("taskId",taskId);//为第三方配置的系统注册编码
            obj.put("state",state);//状态：0:未办理；1:已办理
            StringEntity postingString = new StringEntity(obj.toString(),
                    "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post); //处理后状态：0/1/2/3 同意已办/不同意已办/取消/驳回
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(content);
            return content;
        } catch (SocketTimeoutException e) {
            return jb.toString();
        } catch (Exception e) {
            return jb.toString();
        }
    }
	 
	//发送消息 ;消息完结的时候推送给起草人消息：已经办结
		public  String sendMessage(TblSystemSheetTable sheet,BigDecimal fromId,TblStaffUtil loginStaff) {
	        String url = null;
	        JSONObject jb=new JSONObject();
	        jb.put("code",0);
	        try {
	            CloseableHttpClient httpClient = HttpClients.createDefault();
	            RequestConfig requestConfig = RequestConfig.custom()
	                    .setSocketTimeout(300 * 1000)
	                    .setConnectTimeout(300 * 1000)
	                    .build();
	            com.alibaba.fastjson.JSONObject obj=messageToDoZzService.returnObjectMessage(sheet,fromId,loginStaff);
	            url = HttpClient.oaUrl+HttpClient.singleMessage;
	            HttpPost post = new HttpPost(url);
	            post.setConfig(requestConfig);
	            JSONArray arr=new JSONArray();
	            arr.add(obj);
	            JSONObject object=new JSONObject();
	            object.put("messages", arr);
	            post.setHeader("Content-Type","application/json;charset=utf-8");
	            post.setHeader("token",this.getOaToken());
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
		
	
	//========================================================测试方法
     @RequestMapping(value = "/TestSendToDo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
 	@Operation(summary="测试待办发送到OA")
     public String TestSendToDo(HttpServletRequest reques) throws Exception {
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
             obj.put("taskId", "123");//第三方待办主键（保证唯一） 必填
             obj.put("registerCode",HttpClient.oaCode);//为第三方配置的系统注册编码 必填
             obj.put("title", "测试待办标题");//待办标题 必填  dataMap.get("title")
             obj.put("thirdSenderId", "1108");////第三方待办发起人主键（保证唯一） 非必填  createStaff.getHistorycode()
             obj.put("senderName","徐亮");////第三方待办发起人姓名 必填  createStaff.getRealname()
             obj.put("thirdReceiverId","1108");//第三方待办接收人主键（保证唯一）必填
             obj.put("creationDate", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
             obj.put("state", 0);//state 状态：0:未办理；1:已办理 必填
             obj.put("url", "");//oa点击地址
             obj.put("noneBindingSender","szfk");//对应的发起人员oa登录名
             obj.put("noneBindingReceiver","szfk");//对应的处理人员oa登录名
             url = HttpClient.oaUrl+HttpClient.sendToDo;
             HttpPost post = new HttpPost(url);
             post.setConfig(requestConfig);
             JSONArray arr=new JSONArray();
             arr.add(obj);
             JSONObject object=new JSONObject();
             object.put("pendingList", arr);
             post.setHeader("Content-Type","application/json;charset=utf-8");
             post.setHeader("token",this.getOaToken());
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
     
	
 	
     @RequestMapping(value = "/TestSendToDoDone", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
 	@Operation(summary="测试已办发送到OA")
     public String TestSendToDoDone(HttpServletRequest reques) throws Exception {
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
             obj.put("taskId", "123");//第三方待办主键（保证唯一） 必填
             obj.put("registerCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
             obj.put("state", 1);// 
             obj.put("subState",0);// 
             url = HttpClient.oaUrl+HttpClient.sendToDoDone;
             HttpPost post = new HttpPost(url);
             post.setConfig(requestConfig);
             post.setHeader("Content-Type","application/json;charset=utf-8");
             post.setHeader("token",this.getOaToken());
             StringEntity postingString = new StringEntity(obj.toString(),
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

     /**
      * 推送待办消息接口
      * @param taskInfo
      * @param submitStaff
      * @param nextStaff
      * @return
      * @throws Exception
      */
	public String sendSubmitInfo(TblFlowTaskInfo taskInfo, TblStaff submitStaff, TblStaff nextStaff) throws Exception {
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
            obj.put("taskId", taskInfo.getTaksId());//第三方待办主键（保证唯一） 必填
            obj.put("registerCode",HttpClient.oaCode);//为第三方配置的系统注册编码 必填
            obj.put("title", taskInfo.getTaskTitle());//待办标题 必填  dataMap.get("title")
            obj.put("thirdSenderId", submitStaff.getStaffid());////第三方待办发起人主键（保证唯一） 非必填  createStaff.getHistorycode()
            obj.put("senderName",submitStaff.getRealname());////第三方待办发起人姓名 必填  createStaff.getRealname()
            obj.put("thirdReceiverId",nextStaff.getStaffid());//第三方待办接收人主键（保证唯一）必填
            obj.put("creationDate", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
            obj.put("state", 0);//state 状态：0:未办理；1:已办理 必填
            obj.put("url", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());//oa点击地址
            obj.put("h5url", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId()+"&type=shenpi&username="+nextStaff.getUsername());//oa点击地址

//            obj.put("url", "http://192.0.2.200/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());//oa点击地址
//            obj.put("h5url", "http://192.0.2.200/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());//oa点击地址
         // obj.put("h5url", "http://192.0.2.200/#/view?flowName="+taskInfo.getModuleType()+"model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId()+"&type=shenpi&username="+nextStaff.getUsername());//oa点击地址
            obj.put("noneBindingSender",submitStaff.getUsername());//对应的发起人员oa登录名
            obj.put("noneBindingReceiver",nextStaff.getUsername());//对应的处理人员oa登录名
            url = HttpClient.oaUrl+HttpClient.sendToDo;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONArray arr=new JSONArray();
            arr.add(obj);
            JSONObject object=new JSONObject();
            object.put("pendingList", arr);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
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

	/**
	 *  处理待办 接口
	 * @param preTaskInfo
	 * @param state
	 * @param subState
	 * @return
	 */
	public String sendDealResult(TblFlowTaskInfo preTaskInfo, int state, int subState) {
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
             obj.put("taskId", preTaskInfo.getTaksId());//第三方待办主键（保证唯一） 必填
             obj.put("registerCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
             obj.put("state", state);// 
             obj.put("subState", subState);// 
             url = HttpClient.oaUrl+HttpClient.sendToDoDone;
             HttpPost post = new HttpPost(url);
             post.setConfig(requestConfig);
             post.setHeader("Content-Type","application/json;charset=utf-8");
             post.setHeader("token",this.getOaToken());
             StringEntity postingString = new StringEntity(obj.toString(),
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


	/**
	 * 单条推送消息接口
	 * @param taskInfo
	 * @param loginStaff
	 * @param nextStaff
	 * @return
	 */
	public String sendEndMessage(TblFlowTaskInfo taskInfo, TblStaff startStaff, TblStaff nextStaff,Integer type,TblStaffUtil loginStaff) {
		String url = null;
        JSONObject jb=new JSONObject();
        jb.put("code",0);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        	
        	String title=taskInfo.getTaskTitle();
        	switch (type) {
    		case 2:
    			title = title+" "+loginStaff.getRealname()+" 已办理 【拒绝】";
    			break;
    		case 3:
    			title = title+" "+loginStaff.getRealname()+" 已办理 【同意】";
    			break;
    		case 4:
    			title = title+" "+loginStaff.getRealname()+" 已办理 【转审】";
    			break;
    		case 5:
    			title = title+" "+loginStaff.getRealname()+" 已办理 【撤回】";
    			break;
    		case 6:
    			title = title+" "+loginStaff.getRealname()+" 消息通知";
    			break;
    		default:
    			title = title+" 待处理";
    			break;
    		}
        	System.out.println("消息类型：----------"+type);
        	
            CloseableHttpClient httpClient = HttpClients.createDefault();
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(300 * 1000)
                    .setConnectTimeout(300 * 1000)
                    .build();
            JSONObject obj=new JSONObject();
            obj.put("thirdpartyMessageId", taskInfo.getTaksId()+""+nextStaff.getStaffid());//第三方待办主键（保证唯一） 必填
            obj.put("thirdpartyRegisterCode",HttpClient.oaCode);//为第三方配置的系统注册编码 必填
            obj.put("messageContent", title);//待办标题 必填  dataMap.get("title")
            obj.put("thirdpartySenderId", startStaff.getStaffid());////第三方待办发起人主键（保证唯一） 非必填  createStaff.getHistorycode()
            obj.put("thirdpartyReceiverId",nextStaff.getStaffid());//第三方待办接收人主键（保证唯一）必填
            obj.put("creation_date", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
            obj.put("messageType",4);//state 状态：0:未办理；1:已办理 必填
            obj.put("downloadurl","");
            obj.put("noneBindingSender",startStaff.getUsername());//对应的发起人员oa登录名
            obj.put("noneBindingReceiver",nextStaff.getUsername());//对应的处理人员oa登录名
            if(type!=null && type==0) {
            	obj.put("messageURL", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());//oa点击地址
                obj.put("messageH5URL", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&id="+taskInfo.getOperatorId()+"&thisStepId="+taskInfo.getTaskNodeId());
	        }else {
	            obj.put("messageURL", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&thisStepId="+taskInfo.getTaskNodeId());//oa点击地址
	            obj.put("messageH5URL", "https://szfk.zjzsco.com/#/msg/wddb?model="+taskInfo.getModuleType()+"&processId="+taskInfo.getProcessId()+"&flowId="+taskInfo.getFlowId()+"&thisStepId="+taskInfo.getTaskNodeId());
	                 
	        }
           obj.put("appParam","");
            url = HttpClient.oaUrl+HttpClient.singleMessage;
            System.out.println(url);
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONArray arr=new JSONArray();
            arr.add(obj);
            JSONObject object=new JSONObject();
            object.put("messages", arr);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
            System.out.println(this.getOaToken());
            System.out.println(obj.toString());
            StringEntity postingString = new StringEntity(obj.toString(),
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


	public String sendActionsWithdraw(TblFlowTaskInfo taskInfo, TblStaffUtil loginStaff) {
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
            obj.put("taskId", taskInfo.getTaksId());//第三方待办主键（保证唯一） 必填
            obj.put("registerCode",HttpClient.oaCode);//为第三方配置的系统注册编码 必填
            obj.put("title", taskInfo.getTaskTitle());//待办标题 必填  dataMap.get("title")
            obj.put("thirdSenderId", loginStaff.getHistorycode());////第三方待办发起人主键（保证唯一） 非必填  createStaff.getHistorycode()
            obj.put("senderName",loginStaff.getRealname());////第三方待办发起人姓名 必填  createStaff.getRealname()
            obj.put("creationDate", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
            obj.put("state", 1);//state 状态：0:未办理；1:已办理 必填
            obj.put("noneBindingSender",loginStaff.getUsername());//对应的发起人员oa登录名
            url = HttpClient.oaUrl+HttpClient.sendToDo;
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);
            JSONArray arr=new JSONArray();
            arr.add(obj);
            JSONObject object=new JSONObject();
            object.put("pendingList", arr);
            post.setHeader("Content-Type","application/json;charset=utf-8");
            post.setHeader("token",this.getOaToken());
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
     
	public static void main(String[] args) {
	}
}
