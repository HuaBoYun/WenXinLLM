package com.hbfk.config;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.YMParam;
import com.hbfk.util.BaseDao;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.YMDesUtil;
import com.hbfk.util.YMMd5Util;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;

public class YMDifferentVConfig {
	
	public static HashMap<String, Object> setTestFormData(){
		HashMap<String, Object> formMap = new HashMap<String, Object>();
		formMap.put("depSelectField321a07", "363756296102477893");
		formMap.put("inputField176f66", "asdasd");
		formMap.put("inputField67d1a1", "cesda");
		formMap.put("userSelectFielddb8256", "410454893803864133");
		return formMap;
	}
	
	public static String getUserFidByAccountSql(YMParam param)  {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id FROM base_user WHERE f_account = '"+param.getRid()+"'";
				break;
			default:
				sql = "SELECT F_Id FROM base_user WHERE F_Account = '"+param.getAccount()+"'";
				break;
		}
		return sql;
	}
	
	public static String getRoleIdSql(YMParam param) {
		String sql = "";
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id FROM base_role WHERE f_en_code = '"+param.getRid()+"'";
				break;
			default:
				sql = "SELECT role.F_Id FROM base_role role WHERE role.F_EnCode = '"+param.getRid()+"'";
				break;
		}
		return sql;
	}
	
	
	public static String getYmPkOrgIdByOrgNameSql(YMParam param) {
		String sql = "";
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id FROM base_organize WHERE f_full_name =  '"+param.getOrgName()+"' AND f_en_code = '"+param.getOrgNumber()+"'";
				if(StringUtils.isNotBlank(param.getFatherId())) {
					sql += " AND f_parent_id = '"+param.getFatherId()+"'";
				}
				break;
			default:
				sql = "SELECT F_Id FROM base_organize WHERE F_FullName =  '"+param.getOrgName()+"' AND F_EnCode = '"+param.getOrgNumber()+"'";
				if(StringUtils.isNotBlank(param.getFatherId())) {
					sql += " AND F_ParentId = '"+param.getFatherId()+"'";
				}
				break;
		}
		return sql;
	}
	
	
	public static String getYmLoginPwd() {
		String pwd = YMMd5Util.getStringMd5(YMUrlStatic.ymPassword);
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				pwd = YMDesUtil.aesOrDecode(pwd, true, true);
				break;
			default:
				pwd = pwd.toLowerCase();
				break;
		}
		
		return pwd;
	}

	/**
	 * 获取 流程任务 主键信息
	 */
	public static String getYmFlowFormId(String keys, String fformid,JSONObject reJson) throws Exception {
		String fid = null;
		switch (YMUrlStatic.YMVERSION) {
		case "5.0+":
			JSONObject dataObject = reJson.getJSONObject("data");
			fid = dataObject.getString("taskId");
			break;
		default:
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = BaseDao.getInstance().getConnection();
				ps = con.prepareStatement("SELECT F_Id FROM flow_task WHERE F_FlowFormContentJson LIKE '%\""+keys+"\":\""+fformid+"\"%' ORDER BY F_CreatorTime desc LIMIT 0,1");
				rs = ps.executeQuery();
				while (rs.next()) {
					fid = rs.getString("F_Id");
				}
			}finally {
				BaseDao.getInstance().close(con,rs,ps);
			}
			break;
		}
		return fid;
	}
	
	public static String getJobFidSql(YMParam param) {
		String sql = "";
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id FROM base_position WHERE f_organize_id = '"+param.getPkymOrgId()+"' AND f_en_code = '"+param.getJobid()+"'";
				break;
			default:
				sql = "SELECT F_Id FROM base_position WHERE F_OrganizeId = '"+param.getPkymOrgId()+"' AND F_EnCode = '"+param.getJobid()+"'";
				break;
		}
		return sql;
	}
	
	public static JsonBean getCandiateNodeInfo(YMParam param, TblStaffUtil loginStaff) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String,Object>(0);
		String result = null;
		JSONObject reJson = null;
		Integer code = 0;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getCandidateNodeUrl.replace("#{id}", param.getFlowTaskOperatorId()),param.getFormDataMap(),param.getHeaderMap(),loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				reJson = JSONObject.parseObject(result);
				code = reJson.getInteger("code");
				if(code != 200) {
					String msg = reJson.getString("msg");
					return ResponseFormat.retParam(0, msg, null);
				}
				JSONArray jsonNodeArray = reJson.getJSONObject("data").getJSONArray("list");
				resultMap.put("list", jsonNodeArray);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getApprovalUser.replace("#{id}", param.getFlowTaskOperatorId()),param.getFormDataMap(),param.getHeaderMap(),loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				reJson = JSONObject.parseObject(result);
				code = reJson.getInteger("code");
				if(code != 200) {
					String msg = reJson.getString("msg");
					return ResponseFormat.retParam(0, msg, null);
				}
				String  dataStr = reJson.getString("data");
				if(dataStr != null && !"".equals(dataStr) && !"[]".equals(dataStr)) {
					JSONObject dataJson = reJson.getJSONObject("data");
					resultMap.put("candidateType", dataJson.get("type"));//type ==1 流程分支   type ==2 候选人
					resultMap.put("list",dataJson.getJSONArray("list"));
				}else {
					resultMap.put("candidateType",0);
				}
				break;
		}
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	public static HashMap<String, Object> setCandidateNodeInfo(String flowId, String id, HashMap<String, Object> formMap, String flowTaskOperatorId) throws Exception {
		HashMap<String, Object> formDataMap = new HashMap<String,Object>(0);
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		formMap.put("flowId",flowId);
		formMap.put("id",id);
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				formDataMap.put("formData",formMap);
				formDataMap.put("id",id);
				formDataMap.put("flowId",flowId);
				if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
					formDataMap.put("flowUrgent",1);
					formDataMap.put("status",1);
				}
				break;
			default:
				filedMap.put("data",JSONObject.toJSONString(formMap));
				filedMap.put("id",id);
				filedMap.put("flowId",flowId);
				if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
					filedMap.put("flowUrgent",1);
					filedMap.put("status",0);
				}
				formDataMap.put("formData",JSONObject.toJSON(filedMap));
				break;
		}
		
		return formDataMap;
	}
	
	public static HashMap<String, Object> setCandidateNodeInfo(HashMap<String, Object> formMap, String id, String flowId, String flowTaskOperatorId, String nodeCode, String keyword) {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		HashMap<String, Object> formDataMap = new HashMap<String,Object>(0);
		switch (YMUrlStatic.YMVERSION) {
		case "5.0+":
			formDataMap.put("id",id);
			formDataMap.put("flowId",flowId);
			if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
				formDataMap.put("flowUrgent",1);
				formDataMap.put("status",1);
			}
			formDataMap.put("formData",formMap);
			formDataMap.put("currentPage",1);
			formDataMap.put("pageSize",100);
			formDataMap.put("nodeCode",nodeCode);
			formDataMap.put("keyword",keyword);
			break;
		default:
			filedMap.put("data",JSONObject.toJSONString(formMap));
			filedMap.put("id",id);
			filedMap.put("flowId",flowId);
			if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
				filedMap.put("flowUrgent",1);
				filedMap.put("status",0);
			}
			formDataMap.put("formData",formMap);
			formDataMap.put("currentPage",1);
			formDataMap.put("pageSize",100);
			formDataMap.put("nodeCode",nodeCode);
			formDataMap.put("keyword",keyword);
			break;
		}
		
		return formDataMap;
	}

	public static Map<String,Object> getCondidatesUserInfo(YMParam param, TblStaffUtil loginStaff) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String,Object>(0);
		String result = null;
		JSONObject reJson = null;
		Integer code = 0;
		JSONArray pageArray = null;
		JSONObject dataJson = null;
		Integer total = 0;
		Integer currentPage = 0;
		Integer pageSize = 0;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getCandidateUserUrl.replace("#{id}", param.getFlowTaskOperatorId()),param.getFormDataMap(),param.getHeaderMap(),loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				reJson = JSONObject.parseObject(result);
				code = reJson.getInteger("code");
				if(code != 200) {
					String msg = reJson.getString("msg");
					resultMap.put("code", 0);
					resultMap.put("msg", msg);
					return resultMap;
				}
				dataJson = reJson.getJSONObject("data");
				pageArray = dataJson.getJSONArray("list");
				break;
			default:
				//获取流程候选人接口
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getCandidateUser.replace("#{id}", param.getFlowTaskOperatorId()),param.getFormDataMap(),param.getHeaderMap(),loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				reJson = JSONObject.parseObject(result);
				code = reJson.getInteger("code");
				if(code != 200) {
					String msg = reJson.getString("msg");
					resultMap.put("code", 0);
					resultMap.put("msg", msg);
					return resultMap;
				}
				dataJson = reJson.getJSONObject("data");
				pageArray = dataJson.getJSONArray("list");
				/*
				 * JSONObject pageJson = dataJson.getJSONObject("pagination"); 
				 * currentPage = pageJson.getInteger("currentPage"); pageSize =
				 * pageJson.getInteger("pageSize"); total = pageJson.getInteger("total");
				 */
				break;
		}
		resultMap.put("list", pageArray);
		/*Integer totalPage = total/pageSize;
		if(total%pageSize != 0) {
			totalPage++;
		}
		resultMap.put("currentPage", currentPage);
		resultMap.put("pageSize", pageSize);
		dataMap.put("totalCount", total);*/
		resultMap.put("currentPage", 1);
		resultMap.put("pageSize", 100);
		resultMap.put("totalPage", 1);
		resultMap.put("code", 1);
		return resultMap;
	}

	public static HashMap<String, Object> setSubmitFlowData(YMParam param) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		switch (YMUrlStatic.YMVERSION) {
		case "5.0+":
			filedMap = setSubmitFlowDate501(param);
			break;
		default:
			filedMap = setSubmitFlowData342(param);
			break;
		}
		
		return filedMap;
		
	}

	private static HashMap<String, Object> setSubmitFlowDate501(YMParam param) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		if(StringUtils.isNotBlank(param.getTaskId())) {
			param.getFormDataMap().put("id",param.getTaskId());
			param.getFormDataMap().put("flowId",param.getFlowEngienId());
			filedMap.put("id",param.getTaskId());
		}else {
			param.getFormDataMap().put("id","");
			param.getFormDataMap().put("flowId","");
			filedMap.put("id","");
		}
		filedMap.put("flowId",param.getFlowEngienId());
		filedMap.put("status",1);
		filedMap.put("flowUrgent",1);
		filedMap.put("eventType", param.getEventType());
		filedMap.put("isFlow", 1);
		filedMap.put("formData",param.getFormDataMap());
		if(StringUtils.isNotBlank(param.getCandidateList())) {
			filedMap.put("candidateList", JSON.parseObject(URLDecoder.decode(param.getCandidateList(), "UTF-8")));
		}
		return filedMap;
	}

	private static HashMap<String, Object> setSubmitFlowData342(YMParam param) {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		if(StringUtils.isNotBlank(param.getTaskId())) {
			param.getFormDataMap().put("id",param.getTaskId());
			param.getFormDataMap().put("flowId",param.getFlowEngienId());
			filedMap.put("id",param.getTaskId());
		}else {
			param.getFormDataMap().put("id","");
			param.getFormDataMap().put("flowId","");
			filedMap.put("id","");
		}
		filedMap.put("data",JSONObject.toJSONString(param.getFormDataMap()));
		
		//放入其他数据信息
		filedMap.put("flowId",param.getFlowEngienId());
		filedMap.put("status",0);
		filedMap.put("flowUrgent",1);
		HashMap<String,Object> formData = new HashMap<String,Object>();
		formData.put("formData", filedMap);
		
		filedMap.put("candidateType",param.getCandidateType());
		Map<String, Object> userrMap = new HashMap<String, Object>(0);
		List<String> branchList = new ArrayList<String>(0);
		
		if("2".equals(param.getCandidateType())){
			//判断有没有流程分支
			List<String> userList = new ArrayList<String>(0);
			if(StringUtils.isNotBlank(param.getCandidateList())) {
				String[] barchS = param.getCandidateList().split(",");
				for (String barch : barchS) {
					userList.add(barch);
				}
			}
			userrMap.put(param.getNodeCode(), userList);
		}else if("1".equals(param.getCandidateType())){
			//判断流程分支和候选人是否并存
			if(StringUtils.isNotBlank(param.getBranchStrs())) {
				String[] barchS = param.getBranchStrs().split(",");
				if(StringUtils.isNotBlank(param.getCandidateList())) {
					String[] userStrs = param.getCandidateList().split("~");
					String[] userChS = null;
					Integer k = 0;
					List<String> userList = null;
					for (String barch : barchS) {
						branchList.add(barch);
						userChS = userStrs[k].split(",");
						userList = new ArrayList<String>(0);
						for (String user : userChS) {
							userList.add(user);
						}
						userrMap.put(barch, userList);
						k++;
					}
				}else {
					for (String barch : barchS) {
						branchList.add(barch);
					}
				}
			}
		}
			
		filedMap.put("branchList",branchList);
		filedMap.put("candidateList",userrMap);
		param.getFormDataMap().put("huaboformId",param.getFormId() + param.getFlowEngienId());//放入关联主键信息，后续根据此主键绑定表单和流程实例的关系
		filedMap.put("data",JSONObject.toJSONString(param.getFormDataMap()));
		return filedMap;
	}

	public static String getNextTaskOperatorListSql(String ymFromId) throws Exception {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_handle_id,f_handle_status,f_node_code,f_node_name,f_completion,f_node_id,f_task_id,f_status,f_parent_id FROM workflow_operator WHERE f_task_id = ? AND f_completion = 0 AND f_status NOT IN (-1,-2,7)";
				break;
			default:
				sql = "SELECT F_ID,F_HandleType,F_HandleId,F_HandleStatus,F_HandleTime,F_NodeCode,F_NodeName,F_Completion,F_TaskNodeId,F_TaskId,F_Type,F_State,F_ParentId,F_Reject,F_Automation FROM flow_taskoperator WHERE F_TaskId = ? AND F_Completion = 0 ORDER BY F_SortCode ASC";
				break;
		}
		return sql;
	}

	public static String getFlowTaskInfoSql() {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_en_code,f_full_name,f_urgent,f_flow_id,f_flow_code,f_flow_name,f_flow_type,f_flow_category,f_flow_version,f_current_node_code,f_status,f_parent_id,f_creator_user_id,f_is_async,f_reject_data_id,f_instance_id FROM workflow_task WHERE f_id = ?";
				break;
			default:
				sql = "SELECT F_Id,F_ProcessId,F_EnCode,F_FullName,F_FlowUrgent,F_FlowId,F_FlowCode,F_FlowName,F_FlowType,F_FlowCategory,F_FlowVersion,F_ThisStep,F_ThisStepId,F_Status,F_Completion,F_CreatorUserId,F_ParentId,F_IsAsync,F_IsBatch,F_TaskNodeId,F_FormType,F_RejectDataId FROM flow_task WHERE F_Id = ?";
				break;
		}
		return sql;
	}

	public static HashMap<String, Object> setRejectMapInfo(YMParam param) {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				param.getFormDataMap().put("f_flow_task_id", param.getTaskId());
				param.getFormDataMap().put("f_id", param.getTaskId());
				param.getFormDataMap().put("flowTaskId", param.getTaskId());
				param.getFormDataMap().put("id", param.getTaskId());
				param.getFormDataMap().put("flowId", param.getFlowEngienId());
				filedMap.put("formData", param.getFormDataMap());
				filedMap.put("flowId", param.getFlowEngienId());
				filedMap.put("handleOpinion", param.getHandleOpinion());
				filedMap.put("handleStatus", param.getHandleStatus());
				filedMap.put("id", param.getTaskId());
				filedMap.put("signImg", param.getSignImg());
				filedMap.put("useSignNext", "false");
				filedMap.put("approvalField", null);
				filedMap.put("fileList", null);
				filedMap.put("copyIds",param.getCopyYmId());
				break;
			default:
				param.getFormDataMap().put("flowId",param.getFlowEngienId());
				filedMap.put("handleOpinion",param.getHandleOpinion());
				filedMap.put("signImg",param.getSignImg());
				filedMap.put("enCode",param.getEnCode());
				filedMap.put("copyIds",param.getCopyYmId());
				filedMap.put("candidateType",param.getCandidateType());
				filedMap.put("branchList",param.getBranchStrs());
				filedMap.put("formData",JSONObject.toJSON(param.getFormDataMap()));
				filedMap.put("rejectStep",param.getRejectStep());
				filedMap.put("id", param.getTaskId());
				if(StringUtils.isNotBlank(param.getRejectStep())) {
					filedMap.put("rejectType",param.getRejectStep());
				}
				break;
		}
		
		
		return filedMap;
	}

	public static String getFlowOperatorSql() {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_handle_id,f_handle_status,f_handle_time,f_node_name,f_node_code,f_task_id,f_node_id,f_status,f_parent_id,f_completion FROM workflow_operator WHERE f_id = ?";
				break;
			default:
				sql = "SELECT F_Id,F_HandleType,F_HandleId,F_HandleStatus,F_HandleTime,F_NodeCode,F_NodeName,F_Completion,F_CreatorTime,F_TaskNodeId,F_TaskId,F_Type,F_State,F_ParentId"
						+ ",F_Automation,F_SortCode,F_Reject,F_Automation FROM flow_taskoperator WHERE F_ID = ? ";
				break;
		}
		return sql;
	}

	public static HashMap<String, Object> setAuditFlowDateInfoMap(YMParam param) {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				filedMap.put("flowId", param.getFlowEngienId());
				filedMap.put("handleOpinion", param.getHandleOpinion());
				filedMap.put("handleStatus", 1);
				filedMap.put("id", param.getTaskId());
				filedMap.put("signImg", param.getSignImg());
				filedMap.put("useSignNext", "false");
				filedMap.put("fileList", null);
				filedMap.put("approvalField", null);
				filedMap.put("signId", RandomUtil.uuStringId());
				param.getFormDataMap().put("flowId", param.getFlowEngienId());
				param.getFormDataMap().put("f_flow_task_id", param.getTaskId());
				param.getFormDataMap().put("f_id", param.getTaskId());
				param.getFormDataMap().put("flowTaskId", param.getTaskId());
				param.getFormDataMap().put("id", param.getTaskId());
				filedMap.put("copyIds",param.getCopyYmId());
				filedMap.put("formData", param.getFormDataMap());
				if(StringUtils.isNotBlank(param.getCandidateList())) {
					filedMap.put("candidateList", JSON.parseObject(param.getCandidateList()));
				}
				break;
			default:
				filedMap.put("flowId",param.getFlowEngienId());
				filedMap.put("id",param.getTaskId());
				filedMap.put("f_id",param.getTaskId());
				filedMap.put("huoboformId",param.getFormId()+param.getFlowEngienId());
				param.getFormDataMap().put("data",JSONObject.toJSONString(param.getFormDataMap()));
				param.getFormDataMap().put("id",param.getTaskId());
				param.getFormDataMap().put("flowId",param.getFlowEngienId());
				filedMap.put("formData",JSONObject.toJSON(param.getFormDataMap()));
				filedMap.put("handleOpinion",param.getHandleOpinion());
				filedMap.put("signImg",param.getSignImg());
				filedMap.put("enCode",param.getEnCode());
				filedMap.put("copyIds",param.getCopyYmId());
				
				Map<String, Object> userrMap = new HashMap<String, Object>(0);
				List<String> branchList = new ArrayList<String>(0);
				if("2".equals(param.getCandidateType())){
					//流程分支处理
					List<String> userList = new ArrayList<String>(0);
					if(StringUtils.isNotBlank(param.getCandidateList())) {
						String[] barchS = param.getCandidateList().split(",");
						for (String barch : barchS) {
							userList.add(barch);
						}
					}
					userrMap.put(param.getNodeCode(), userList);
				}else if("1".equals(param.getCandidateType())){
					//既有流程分支又有候选人处理
					if (StringUtils.isNotBlank(param.getBranchStrs())) {
					    String[] barchS = param.getBranchStrs().split(",");
					    String[] userStrs = param.getCandidateList().split("~");
					    String[] userChS = null;
					    Integer k = 0;
					    List<String> userList = null;
					    String[] barchInfo = null;
					    for (String barch : barchS) {
					    	barchInfo = barch.split("~");
					    	if ("true".equals(barchInfo[1])) {
					    		branchList.add(barchInfo[0]);
					    		userChS = userStrs[k].split(",");
					    		userList = new ArrayList<String>(0);
					    		for (String user : userChS) {
					    			userList.add(user);
					    		}
					    		userrMap.put(barchInfo[0], userList);
					    		k++;
					    	} else {
					    		branchList.add(barchInfo[0]);
					    	}
					    }
					}
				}
				filedMap.put("candidateType",param.getCandidateType());
				filedMap.put("branchList",branchList);
				filedMap.put("candidateList",userrMap);
				filedMap.put("signImg",param.getSignImg());
				if(StringUtils.isNotBlank(param.getFreeApproverYmUserId())) {
					filedMap.put("freeApproverUserId",param.getFreeApproverYmUserId());
				}
				break;
		}
		
		return filedMap;
	}

	public static String getEndFlowTaskCount() {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT COUNT(0) AS ENDCO FROM workflow_task WHERE f_id = ? AND f_status = 2 ";
				break;
			default:
				sql = "SELECT COUNT(0) AS ENDCO FROM FLOW_TASK WHERE F_ID = ? AND F_Status = 2";
				break;
		}
		return sql;
	}

	public static String getAutoAuditOperaListSql() {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_handle_id,f_handle_status,f_handle_time,f_node_code,f_node_name,f_completion,f_task_id,f_status,f_parent_id FROM workflow_operator WHERE f_task_id = ? AND f_node_code = ? AND f_status NOT IN (-1,-2,7) AND f_completion = 1 AND f_handle_time IS NULL";
				break;
			default:
				sql = "SELECT F_ID,F_HandleType,F_HandleId,F_HandleStatus,F_HandleTime,F_NodeCode,F_NodeName,F_Completion,F_TaskNodeId,F_TaskId,F_Type,F_State,F_ParentId,F_Reject,F_Automation " + 
						" FROM flow_taskoperator WHERE F_TaskId = ? AND F_NodeCode = ? AND F_Automation = 1 ORDER BY F_HandleTime ASC";
				break;
		}
		return sql;
	}

	public static String getSelectBeforeCopyFlowNameSql(String ymWorkFrom) {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_full_name FROM workflow_template WHERE f_id = '"+ymWorkFrom+"'";
				break;
			default:
				sql = "SELECT F_FullName FROM flow_engine WHERE F_Id = '"+ymWorkFrom+"'";
				break;
		}
		return sql;
	}

	public static String getCopyAfterFlowInfo(String flowName) throws Exception {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_full_name FROM workflow_template WHERE f_full_name LIKE '"+flowName+"%' ORDER BY f_creator_time DESC"+DataBaseSqlConfig.getRowLimitSql(0, 1,YMUrlStatic.dbType);
				break;
			default:
				sql = "SELECT F_Id,F_FullName FROM flow_engine WHERE F_FullName LIKE '"+flowName+"%' ORDER BY F_CreatorTime DESC"+DataBaseSqlConfig.getRowLimitSql(0, 1,YMUrlStatic.dbType);
				break;
		}
		return sql;
	}

	public static String getupdateWorkNameByIdSql(String workName, String workId) {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "UPDATE workflow_template SET f_full_name = '"+workName+"' WHERE f_id = '"+workId+"'";
				break;
			default:
				sql = "UPDATE flow_engine SET F_FullName = '"+workName+"' WHERE F_Id = '"+workId+"'";
				break;
		}
		return sql;
	}

	public static HashMap<String, Object> getFlowInfoData(YMParam param) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String, Object>(0);
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				filedMap.put("operatorId",param.getFlowTaskOperatorId());
				filedMap.put("flowId",param.getFlowEngienId());
				filedMap.put("opType",param.getOpType());
				break;
			default:
				filedMap.put("taskNodeId",param.getCurrNodeId());
				filedMap.put("taskOperatorId",param.getFlowTaskOperatorId());
				break;
		}
		return filedMap;
	}

	public static Map<String, Object> dealPendgingFlowData(JSONObject dataJson) throws Exception {
		HashMap<String, Object> dataMap = new HashMap<String, Object>(0);
		JSONObject flowTaskInfo = null;
		JSONArray flowTaskNodeList = null;
		JSONObject flowTaskNode = null;
		String thisStepIdNode = null;
		int arrayLength = 0;
		String nextStepId = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				flowTaskInfo = dataJson.getJSONObject("flowInfo");
				flowTaskNodeList = dataJson.getJSONArray("nodeList");
				
				dataMap.put("flowTaskInfo", flowTaskInfo);
				dataMap.put("flowTaskNodeList", flowTaskNodeList);
				dataMap.put("flowTaskOperatorList", dataJson.getJSONArray("progressList"));
				dataMap.put("flowTaskOperatorRecordList", dataJson.getJSONArray("recordList"));
				dataMap.put("approversProperties", dataJson.getJSONObject("nodeProperties"));
				dataMap.put("taskInfo", dataJson.getJSONObject("taskInfo"));
				dataMap.put("btnInfo", dataJson.getJSONObject("btnInfo"));
				break;
			default:
				flowTaskInfo = dataJson.getJSONObject("flowTaskInfo");
				flowTaskNodeList = dataJson.getJSONArray("flowTaskNodeList");
				thisStepIdNode = flowTaskInfo.getString("thisStepId");
				arrayLength = flowTaskNodeList.size()-1;
				//获取下一步办理节点的 NodeCode 最后一步 end
				thisStepIdNode += ",";
				for (int i = 0; i <= arrayLength; i++) {
					flowTaskNode = flowTaskNodeList.getJSONObject(i);
					if(thisStepIdNode.indexOf(flowTaskNode.getString("nodeCode")+",") != -1) {
						nextStepId = flowTaskNode.getString("nodeNext");
						break;
					}
				}
				dataMap.put("flowTaskInfo", flowTaskInfo);
				dataMap.put("flowTaskNodeList", flowTaskNodeList);
				dataMap.put("flowTaskOperatorList", dataJson.getJSONArray("flowTaskOperatorList"));
				dataMap.put("flowTaskOperatorRecordList", dataJson.getJSONArray("flowTaskOperatorRecordList"));
				dataMap.put("approversProperties", dataJson.getJSONObject("approversProperties"));
				dataMap.put("nextStepId", nextStepId);
				break;
		}
		return dataMap;
	}

	public static String getAlredyOperatorListSql() {
		String sql = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				sql = "SELECT f_id,f_handle_id,f_handle_status,f_node_code,f_node_name,f_completion,f_node_id,f_task_id,f_status,f_parent_id FROM workflow_operator WHERE f_task_id = ? AND f_node_code = ? AND f_completion = 1 AND f_status NOT IN (-1,-2,7)";
				break;
			default:
				sql = "SELECT F_ID,F_HandleType,F_HandleId,F_HandleStatus,F_HandleTime,F_NodeCode,F_NodeName,F_Completion,F_TaskNodeId,F_TaskId,F_Type,F_State,F_ParentId,F_Reject,F_Automation FROM flow_taskoperator WHERE F_TaskId = ? AND F_NodeCode = ? AND F_Completion = 1 ORDER BY F_SortCode ASC";
				break;
		}
		return sql;
	}

	public static HashMap<String, Object> getCopyFlowInfoData(YMParam param) {
		HashMap<String, Object> filedMap = new HashMap<String, Object>(0);
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				filedMap.put("operatorId",param.getFlowTaskOperatorId());
				filedMap.put("flowId",param.getFlowEngienId());
				filedMap.put("opType",5);
				break;
			default:
				filedMap.put("taskNodeId",param.getCurrNodeId());
				break;
		}
		return filedMap;
	}
	

}
