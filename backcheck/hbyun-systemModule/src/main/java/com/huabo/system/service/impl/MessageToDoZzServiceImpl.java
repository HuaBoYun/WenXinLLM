package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.service.MessageToDoZzService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.YMFormDataService;
import com.huabo.system.utils.HttpClient;

@Service
public class MessageToDoZzServiceImpl implements MessageToDoZzService {
	
	@Resource
	private YMFormDataService ymFormDataService;
	
	@Resource
	private TblStaffService tblStaffService;

	
	//发送OA待办
	/*
	 * type 当角色改变需要撤销待办的时候  状态赋值为1  其他默认为0
	 * */
	@Override
	public  JSONObject returnObject(TblSystemSheetTable sheet, BigDecimal fromId,TblStaffUtil loginStaff,String type,String processId) throws Exception {
		// TODO Auto-generated method stub
		 HashMap<String, Object> dataMap = ymFormDataService.setYmFormDataMessage(sheet,fromId); 
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 JSONObject obj=new JSONObject();
         obj.put("taskId",fromId);//第三方待办主键（保证唯一） 必填
         obj.put("registerCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
         obj.put("title", dataMap.get("title").toString());//待办标题 必填  
         obj.put("thirdSenderId", loginStaff.getStaffid());////第三方待办发起人主键（保证唯一） 非必填  createStaff.getHistorycode()
         obj.put("senderName",loginStaff.getRealname());////第三方待办发起人姓名 必填  createStaff.getRealname()
         obj.put("thirdReceiverId","1108");//第三方待办接收人主键（保证唯一）必填
         obj.put("creationDate", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
         obj.put("state", 0);//state 状态：0:未办理；1:已办理 必填
         if(type!=null&&type.equals("1")){  
        	 obj.put("state", 1); 
        	 obj.put("subState", 2);//：0/1/2/3 同意已 办/不同意已办/取消/驳回
         }
         obj.put("url", "http://192.0.2.200:88/#/msg/wddb?model="+dataMap.get("model").toString()+"&processId="+processId);//oa点击待办地址
         obj.put("h5url", "http://192.0.2.200:88/#/msg/wddb?model="+dataMap.get("model").toString()+"&processId="+processId);
         obj.put("noneBindingSender",loginStaff.getUsername());//OA 对应的发起人员==当前登录人员
         obj.put("noneBindingReceiver","szfk");//OA 对应的处理人员
		 return  obj;
	}
	 
	

	//发送消息
	@Override
	public  JSONObject returnObjectMessage(TblSystemSheetTable sheet, BigDecimal fromId,TblStaffUtil loginStaff) throws Exception {
		// TODO Auto-generated method stub
		 HashMap<String, Object> dataMap = ymFormDataService.setYmFormDataMessage(sheet,fromId); 
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 JSONObject obj=new JSONObject();
         obj.put("thirdpartyMessageId",fromId);//第三方待办主键（保证唯一） 必填
         obj.put("thirdpartyRegisterCode", HttpClient.oaCode);//为第三方配置的系统注册编码 必填
         obj.put("messageContent", dataMap.get("title"));//待办标题 必填  
         obj.put("thirdpartySenderId", loginStaff.getStaffid());////第三方待办发起人主键（保证唯一） 非必填 
         obj.put("thirdpartyReceiverId","1108");////第三方待办发起人姓名 必填
         obj.put("creation_date", sdf.format(new Date()));//待办创建时间（格式：yyyyMM-dd HH:mm:ss）
         obj.put("messageType", 0);//0：PC；
         obj.put("messageURL", "");//PC 端穿透链接
         obj.put("noneBindingSender",loginStaff.getUsername());//OA 对应的发起人员==当前登录人员
         obj.put("noneBindingReceiver","szfk");//OA 对应的处理人员
		 return  obj;
	}
}
