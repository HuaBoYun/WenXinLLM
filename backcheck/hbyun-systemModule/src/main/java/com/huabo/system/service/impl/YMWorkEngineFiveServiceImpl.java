package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.SnowflakeIdWorker;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.controller.MessageToDoZzController;
import com.huabo.system.entity.TblContractTypeActivity;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.flow.FlowTask;
import com.huabo.system.flow.FlowTaskOperator;
import com.huabo.system.mapper.TblAuthorizationRecordMapper;
import com.huabo.system.mapper.TblContractTypeActivityMapper;
import com.huabo.system.mapper.TblFlowApproverInfoMapper;
import com.huabo.system.mapper.TblFlowInformInfoMapper;
import com.huabo.system.mapper.TblFlowMessageMapper;
import com.huabo.system.mapper.TblFlowTaskInfoMapper;
import com.huabo.system.mapper.TblFlowTemplateMapper;
import com.huabo.system.mapper.TblJobDao;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblRoleMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemFormFlowMapper;
import com.huabo.system.mapper.TblSystemSheetTableMapper;
import com.huabo.system.mapper.TblYmFlowRecordAttMapper;
import com.huabo.system.mapper.TblYmprocessInfoMapper;
import com.huabo.system.service.TblFlowTaskInfoService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.YMFormDataService;
import com.huabo.system.service.YMWorkEngineFiveService;
import com.huabo.system.util.OrganizeAdminIsTratorCrForm;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
public class YMWorkEngineFiveServiceImpl implements YMWorkEngineFiveService {
	
	@Resource
	private YMFormDataService ymFormDataService;
	
	@Resource
	private TblStaffMapper tblStaffMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Resource
	private TblRoleMapper tblRoleMapper;
	
	@Resource
	private TblSystemSheetTableMapper tblSystemSheetTableMapper;
	
	@Resource
	private TblJobDao tblJobDao;
	
	@Resource
	private TblStaffService  tblStaffService;
	
	@Resource
	private MessageToDoZzController messageToDoZzController;

	@Resource
	private TblFlowTaskInfoService tblFlowTaskInfoService;

	@Resource
	private TblFlowInformInfoMapper tblFlowInformInfoMapper;
	
	@Resource
	private TblYmFlowRecordAttMapper tblYmFlowRecordAttMapper;
	
	@Resource
	private TblFlowApproverInfoMapper tblFlowApproverInfoMapper;
	
	@Resource
	private TblFlowMessageMapper tblFlowMessageMapper;
	
	@Resource
	private TblFlowTemplateMapper tblFlowTemplateMapper;
	
	@Resource
	private TblYmprocessInfoMapper tblYmprocessInfoMapper;
	
	@Resource 
	private TblSystemFormFlowMapper tblSystemFormFlowMapper;
	
	@Resource
	private TblContractTypeActivityMapper tblContractTypeActivityMapper;
	
	@Resource
	private TblAuthorizationRecordMapper tblAuthorizationRecordMapper;
	
	@Resource
	private TblFlowTaskInfoMapper tblFlowTaskInfoMapper;
	
	@Resource
    private UserProvider userProvider;
	
	private SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(5, 5);
	
	@Override
	public JsonBean saveFlowInfoFive(String description, String enCode, String fullName, String id, BigDecimal tableid, String typeid, String category, Integer flowType) throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}

			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken);
			Integer count = 0;
			// flowType 默认标准流程
			int safeFlowType = (flowType != null) ? flowType : 0;

			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("icon", "icon-ym icon-ym-header-sys-toggle");
			filedMap.put("iconBackground", "#008cff");
			filedMap.put("category", category);
			filedMap.put("description", description);
			filedMap.put("enCode", enCode);
			filedMap.put("fullName", fullName);
			filedMap.put("id", id);
			filedMap.put("type", safeFlowType);  // 传递流程类型：0-标准 1-简单 2-任务

			String url = YMUrlStatic.interfaceUrl+YMUrlStatic.dealFlowBaseInfo;
			String httpType = HttpClient.HTTPPOST;
			boolean ishave = false;
			if(StringUtils.isNotBlank(id)) {
				url = YMUrlStatic.interfaceUrl+YMUrlStatic.dealFlowBaseInfo+"/"+id;
				httpType = HttpClient.HTTPPUT;
				ishave = true;
			}

			String result = DealUserToken.dealYmUniqueMethod(url,filedMap,YMUrlStatic.headerMap,httpType,HttpClient.PARAMBODY);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");

			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			String data = reJson.getString("data");
			if(StringUtils.isBlank(id)) {
				id = data;
			}
			//存入自己的数据库
			//判断创建是合同流程还是其他模块的业务流程
			if(StringUtils.isNotBlank(typeid) && !"NaN".equals(typeid)) {
				//合同类型 流程关联
				BigDecimal ctypeId = new BigDecimal(typeid);
				String activityId = this.tblContractTypeActivityMapper.selectTableYmFlowFiveCount(ctypeId,id,loginStaff.getCurrentOrg().getOrgid(),tableid);
				TblContractTypeActivity act = new TblContractTypeActivity();
				act.setOrgId(loginStaff.getCurrentOrg().getOrgid());
				act.setTypeId(ctypeId);
				act.setFlowTemplateId(id);
				act.setYmWorkName(fullName);
				act.setFlowType(safeFlowType);

				if(StringUtils.isNotBlank(activityId)) {
					//修改
					act.setActivityId(activityId);
					this.tblContractTypeActivityMapper.updateById(act);
				}else{
					//新增
					activityId = RandomUtil.uuStringId();
					act.setActivityId(activityId);
					act.setQyStats(TblContractTypeActivity.NO);
					Integer version = this.tblContractTypeActivityMapper.selectMaxVersion(ctypeId,loginStaff.getCurrentOrg().getOrgid());
					if(version == null) {
						version = 0;
					}
					version++;
					act.setVersion(version);
					act.setTableId(tableid);
					this.tblContractTypeActivityMapper.insert(act);
				}
			}else {
				// 其他模块业务流程 流程关联
				if(ishave){
					//修改
					this.tblSystemSheetTableMapper.UpdateSystemYmWorkTemplateWithType(tableid,id,loginStaff.getCurrentOrg().getOrgid(),fullName,safeFlowType);
				}else {
					//新增
					this.tblSystemSheetTableMapper.InsertSystemYmWorkTemplateWithType(tableid,id,loginStaff.getCurrentOrg().getOrgid(),fullName,count+1,safeFlowType);
				}
			}
			//返回正确的所有信息
			return ResponseFormat.retParam(1, 200, id);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	@Override
	public JsonBean getFLowTemplateInfo(String id) throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.dealFlowBaseInfo+"/"+id,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			String data = reJson.getString("data");
			return ResponseFormat.retParam(1, 200,data );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	@Override
	public JsonBean getDictionaryData() throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getDictionaryData,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			String data = reJson.getString("data");
			return ResponseFormat.retParam(1, 200,data );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public JsonBean saveWorkFlowEngineInfo(String token, String flowId, String tableId, String id, String typeId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(StringUtils.isNotBlank(typeId) && !"NaN".equals(typeId)) {
			TblContractTypeActivity act = this.tblContractTypeActivityMapper.selectContractFlowInfo(typeId,id,loginStaff.getCurrentOrg().getOrgid(),tableId);
			act.setYmWorkFrom(flowId);
			this.tblContractTypeActivityMapper.updateById(act);
		}else {
			this.tblSystemSheetTableMapper.updateSystemYmFlowInfo(tableId,id,flowId,loginStaff.getCurrentOrg().getOrgid());
		}
		return ResponseFormat.retParam(200, 200, null);
	}

	@Override
	public JsonBean transact(String[] ids) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("ids", ids);
		String ymUrl = YMUrlStatic.interfaceUrl+YMUrlStatic.flowTransactUrl;
		log.info("[transact] 请求流程平台URL: {}", ymUrl);
		log.info("[transact] 请求参数 ids: {}", java.util.Arrays.toString(ids));
		log.info("[transact] ymToken: {}", loginStaff.getYmToken());
		String result = DealUserToken.dealYmUniqueMethod(ymUrl,filedMap,headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		log.info("[transact] 流程平台原始响应: {}", result);
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		String msg = reJson.getString("msg");

		if(!"200".equals(code)) {
			log.warn("[transact] 流程平台返回失败 code={} msg={}", code, msg);
			return ResponseFormat.retParam(0, msg, null);
		}
		String data = reJson.getString("data");
		return ResponseFormat.retParam(1, 200,data );
	}

	@Override
	public JsonBean sendBack(String id, String flowid, String backNodeCode, String handleOpinion, String signImg, String copyIds, String candidateList, String backType, String taskid,String backNodeName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
		String copyYmId = "";
		
		//获取流程基础配置表和表单主键
		formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(taskid);
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		sheet = this.resolveSheet(flowid, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}
		
		//判断是否有抄送，如果有 保存到流程抄送记录表里 并获取抄送人员的业务中台主键
		if(copyIds != null && !"".equals(copyIds)) {
			String[] cys = copyIds.split(",");
			for (String cyId : cys) {
				this.tblFlowInformInfoMapper.insertEntity(loginStaff.getStaffid(),new BigDecimal(cyId),flowid,taskid,formId,taskid,RandomUtil.uuBigDecimalId());
				copyYmId += this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(cyId))+",";
			}
			copyYmId = copyYmId.substring(0,copyYmId.length()-1);
		}
		
		//封存需要传入的参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("backNodeCode", backNodeCode);
		filedMap.put("handleOpinion", handleOpinion);
		filedMap.put("signImg", signImg);
		filedMap.put("copyIds", copyIds);
		filedMap.put("backType", backType);
		filedMap.put("candidateList", candidateList);
		filedMap.put("fileList", null);
		
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.sendBackUrl.replace("#{id}", id),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		tblFlowTaskInfoService.insertSendBacknfo(id,loginStaff,sheet,formId,handleOpinion,backNodeCode,backNodeName,taskid,flowid);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean assist(String id, String flowid, String handleIds, String handleOpinion, String signImg,
			String nodeCode, String pause, String taskid) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		List<String> ymStaffIds = this.tblStaffMapper.selectYmPkStaffIdByStaffIds(handleIds);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("handleOpinion", handleOpinion);
		filedMap.put("nodeCode", nodeCode);
		filedMap.put("pause", pause);
		filedMap.put("signImg", signImg);
		filedMap.put("fileList", "[]");
		filedMap.put("handleIds", String.join(",", ymStaffIds));
		
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.assistUrl.replace("#{id}", id),filedMap,headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		String msg = reJson.getString("msg");
		if(!"200".equals(code)) {
			return ResponseFormat.retParam(0, msg, null);
		}
		return ResponseFormat.retParam(1, 200,null );
	}

	@Override
	public JsonBean addSign(String id, String flowid, String taskid, String signImg, String handleOpinion,
			String addSignType, Integer counterSign, Integer auditRatio, String addSignUserIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		List<String> ymStaffIds = this.tblStaffMapper.selectYmPkStaffIdByStaffIds(addSignUserIds);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		BigDecimal formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(taskid);
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		TblSystemSheetTable sheet = this.resolveSheet(flowid, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}

		//flowtask主键 获取提交审批节点，taskNode信息
		FlowTask task = this.tblFlowTaskInfoService.getFlowTaskById(taskid);

		//通过operatorId 获取当前操作信息
		FlowTaskOperator preOper = this.tblFlowTaskInfoService.selectCurrentOperator(id);

		TblStaff startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,taskid,flowid);
		// 5.0 版本撤回重提交后 flowid 为实例 flowId，而 FORMFLOW 存的是定义 flowId，用实例 flowId 查不到发起人，
		// 用 formId 兜底回定义 flowId 再查一次，避免后续 startStaff 空指针
		if (startStaff == null && formId != null) {
			String defFlowId = this.tblSystemSheetTableMapper.selectDefFlowIdByFormId(formId, loginStaff.getCurrentOrg().getOrgid());
			if (defFlowId != null && !"".equals(defFlowId) && !defFlowId.equals(flowid)) {
				startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,taskid,defFlowId);
				if (startStaff != null) {
					log.info("[addSign] flowid={} 查不到发起人，已用 formId={} 查回定义 flowId={} 兜底命中", flowid, formId, defFlowId);
				}
			}
		}
		if (startStaff == null) {
			log.warn("[addSign] 未找到流程发起人 formId={} taskid={} flowid={}", formId, taskid, flowid);
			return ResponseFormat.retParam(0, "未找到流程发起人信息，请联系管理员", null);
		}

		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("handleOpinion", handleOpinion);
		filedMap.put("signImg", signImg);
		filedMap.put("id", taskid);
		filedMap.put("backNodeCode", "");
		filedMap.put("backType", 1);
		filedMap.put("candidateList", null);
		filedMap.put("copyIds", "");
		filedMap.put("fileList", null);
		filedMap.put("flowId", flowid);
		
		HashMap<String, Object> formMap = this.ymFormDataService.setYmFormData(sheet,formId,loginStaff);
		formMap.put("flowId", flowid);
		formMap.put("f_flow_task_id", taskid);
		formMap.put("f_id", taskid);
		formMap.put("flowTaskId", taskid);
		formMap.put("id", taskid);
		filedMap.put("formData", formMap);
		
		HashMap<String, Object> signMap = new HashMap<String,Object>(0);
		signMap.put("addSignType", addSignType);
		signMap.put("addSignUserIdList", ymStaffIds);
		signMap.put("auditRatio", auditRatio);
		signMap.put("counterSign", counterSign);
		filedMap.put("addSignParameter", signMap);
		
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.addSignUrl.replace("#{id}", id),filedMap,headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		String msg = reJson.getString("msg");
		if(!"200".equals(code)) {
			return ResponseFormat.retParam(0, msg, null);
		}
		
		this.tblFlowTaskInfoService.insertAddSignInfo(sheet,formId,task,loginStaff,id,flowid,taskid,signImg,handleOpinion,
				addSignType,counterSign,auditRatio,addSignUserIds,startStaff,preOper);
		return ResponseFormat.retParam(1, 200,null );
	}

	/**
	 * 统一安全获取流程基础配置 sheet（兜底链，零数据污染）。
	 * 5.0 版本撤回重提交会生成新的流程平台“流程实例”flowId，审批接口拿到的就是这个实例 flowId，
	 * 而本地映射表登记的是“定义 flowId”，直接用 selectSheetTableInfoByFlowId(实例flowId) 返回 null，
	 * 后续 sheet.getClassName() 空指针。兜底顺序：① flowId 查 → ② templateId 查 → ③ formId 经 FORMFLOW 查回定义 flowId 再查。
	 * 仅只读查询，不修改任何配置数据。
	 */
	private TblSystemSheetTable resolveSheet(String flowId, String templateId, BigDecimal formId, BigDecimal orgId) throws Exception {
		TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(flowId, orgId);
		if (sheet != null) {
			return sheet;
		}
		// ② templateId 兜底
		if (templateId != null && !"".equals(templateId)) {
			sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByTemplateId(templateId, orgId);
			if (sheet != null) {
				log.info("[resolveSheet] flowId={} 查不到 sheet，已用 templateId={} 兜底命中，flowType={}", flowId, templateId, sheet.getClassName());
				return sheet;
			}
		}
		// ③ formId → 定义 flowId 兜底
		if (formId != null) {
			String defFlowId = this.tblSystemSheetTableMapper.selectDefFlowIdByFormId(formId, orgId);
			if (defFlowId != null && !"".equals(defFlowId) && !defFlowId.equals(flowId)) {
				sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(defFlowId, orgId);
				if (sheet != null) {
					log.info("[resolveSheet] flowId={} 查不到 sheet，已用 formId={} 查回定义 flowId={} 兜底命中，flowType={}", flowId, formId, defFlowId, sheet.getClassName());
					return sheet;
				}
			}
		}
		log.warn("[resolveSheet] flowId={} templateId={} formId={} 三种方式均未找到 sheet", flowId, templateId, formId);
		return null;
	}

	@Override
	public JsonBean transfer(String id, String flowid, String taskid, String signImg, String handleOpinion,
			String handleIds, String nodeCode, Integer pause) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		List<String> ymStaffIds = this.tblStaffMapper.selectYmPkStaffIdByStaffIds(handleIds);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		BigDecimal formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(taskid);
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		TblSystemSheetTable sheet = this.resolveSheet(flowid, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}

		//flowtask主键 获取提交审批节点，taskNode信息
		FlowTask task = this.tblFlowTaskInfoService.getFlowTaskById(taskid);

		//通过operatorId 获取当前操作信息
		FlowTaskOperator preOper = this.tblFlowTaskInfoService.selectCurrentOperator(id);

		TblStaff startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,taskid,flowid);
		// 5.0 版本撤回重提交后 flowid 为实例 flowId，而 FORMFLOW 存的是定义 flowId，用实例 flowId 查不到发起人，
		// 用 formId 兜底回定义 flowId 再查一次，避免后续 startStaff 空指针
		if (startStaff == null && formId != null) {
			String defFlowId = this.tblSystemSheetTableMapper.selectDefFlowIdByFormId(formId, loginStaff.getCurrentOrg().getOrgid());
			if (defFlowId != null && !"".equals(defFlowId) && !defFlowId.equals(flowid)) {
				startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,taskid,defFlowId);
				if (startStaff != null) {
					log.info("[transfer] flowid={} 查不到发起人，已用 formId={} 查回定义 flowId={} 兜底命中", flowid, formId, defFlowId);
				}
			}
		}
		if (startStaff == null) {
			log.warn("[transfer] 未找到流程发起人 formId={} taskid={} flowid={}", formId, taskid, flowid);
			return ResponseFormat.retParam(0, "未找到流程发起人信息，请联系管理员", null);
		}

		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("handleOpinion", handleOpinion);
		filedMap.put("handleIds", String.join(",", ymStaffIds));
		filedMap.put("fileList", null);
		filedMap.put("nodeCode", nodeCode);
		filedMap.put("pause", pause);
		filedMap.put("signImg", signImg);
		
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.transferFiveUrl.replace("#{id}", id),filedMap,headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONObject reJson = JSONObject.parseObject(result);
		String code = reJson.getString("code");
		String msg = reJson.getString("msg");
		if(!"200".equals(code)) {
			return ResponseFormat.retParam(0, msg, null);
		}
		
		this.tblFlowTaskInfoService.insertTransferInfo(sheet,formId,task,loginStaff,id,flowid,taskid,signImg,handleOpinion,
				ymStaffIds,startStaff,preOper);
		return ResponseFormat.retParam(1, 200,null );
	}

	@Override
	public JsonBean getInProgressList(String token, Integer currentPage, Integer pageSize, String flowName)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(pageSize == null) {
			pageSize = 15;
		}
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("currentPage",currentPage);
		filedMap.put("pageSize",pageSize);
		if(flowName != null) {
			filedMap.put("keyword",flowName);
		}
		//pathValue = 1 时 查询我的待办
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"2",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		dataMap.put("list", dataJson.getJSONArray("list"));
		JSONObject pageJson = dataJson.getJSONObject("pagination");
		dataMap.put("currentPage", pageJson.getInteger("currentPage"));
		dataMap.put("pageSize", pageJson.getInteger("pageSize"));
		Integer total = pageJson.getInteger("total");
		
		Integer totalPage = total/pageSize;
		if(total%pageSize != 0) {
			totalPage++;
		}
		dataMap.put("totalCount", total);
		dataMap.put("totalPage", totalPage);
		
		return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public JsonBean getPendingSignList(String token, Integer currentPage, Integer pageSize, String flowName)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(pageSize == null) {
			pageSize = 15;
		}
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("currentPage",currentPage);
		filedMap.put("pageSize",pageSize);
		if(flowName != null) {
			filedMap.put("keyword",flowName);
		}
		//pathValue = 1 时 查询我的待办
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"0",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		dataMap.put("list", dataJson.getJSONArray("list"));
		JSONObject pageJson = dataJson.getJSONObject("pagination");
		dataMap.put("currentPage", pageJson.getInteger("currentPage"));
		dataMap.put("pageSize", pageJson.getInteger("pageSize"));
		Integer total = pageJson.getInteger("total");
		
		Integer totalPage = total/pageSize;
		if(total%pageSize != 0) {
			totalPage++;
		}
		dataMap.put("totalCount", total);
		dataMap.put("totalPage", totalPage);
		
		return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public JsonBean getAdministratoList(String keyword, Integer currentPage, Integer pageSize) throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("currentPage", currentPage);
			filedMap.put("pageSize", pageSize);
			
			if(StringUtils.isNotBlank(keyword)) {
				filedMap.put("keyword", keyword);
			}
			
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.administratorUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			
			JSONObject dataJson = reJson.getJSONObject("data");
			JSONObject pageJson = dataJson.getJSONObject("pagination");
			Integer total = pageJson.getInteger("total");
			Integer totalPage = total/pageSize;
			
			if(total%pageSize > 0) {
				totalPage = totalPage +1;
			}
			
			Map<String, Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("list", dataJson.getJSONArray("list"));
			resultMap.put("totalRecord", total);
			resultMap.put("currentPage", currentPage);
			resultMap.put("pageSize", pageSize);
			resultMap.put("totalPage", totalPage);
			return ResponseFormat.retParam(1, 200,resultMap );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public JsonBean getAdministratoSelector(String staffId) throws Exception {
		
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			Map<String, Object> resultMap = new HashMap<String,Object>(0);
			
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("userId", staffId);
			
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.adminSelectorUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			resultMap.put("selectorJson", reJson.getJSONObject("data"));
			
			filedMap = new HashMap<String,Object>(0);
			List<String> ids = new ArrayList<String>(0);
			ids.add(staffId);
			filedMap.put("ids", ids);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.userInfoUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			reJson = JSONObject.parseObject(result);
			code = reJson.getString("code");
			msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			resultMap.put("userListJson", reJson.getJSONObject("data"));
			
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("userId", staffId);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.selectAsyncListUrl+"0",filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			reJson = JSONObject.parseObject(result);
			code = reJson.getString("code");
			msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			resultMap.put("orgListJson", reJson.getJSONArray("data"));
			
			return ResponseFormat.retParam(1, 200,resultMap );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public JsonBean saveAdministratorInfo(@Valid OrganizeAdminIsTratorCrForm organizeAdminIsTratorCrForm, String id)
			throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("id", id);
			filedMap.put("managerGroup", organizeAdminIsTratorCrForm.getManagerGroup());
			filedMap.put("moduleIds", organizeAdminIsTratorCrForm.getModuleIds());
			filedMap.put("orgAdminModel", organizeAdminIsTratorCrForm.getOrgAdminModel());
			filedMap.put("systemIds", organizeAdminIsTratorCrForm.getSystemIds());
			filedMap.put("userId", organizeAdminIsTratorCrForm.getUserId());
			
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.administratorUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			return ResponseFormat.retParam(1, 200,null );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public JsonBean removeAdministratorInfo(String id) throws Exception {
		Jedis jedis = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.administratorUrl+"/"+id,filedMap,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,null);
			JSONObject reJson = JSONObject.parseObject(result);
			String code = reJson.getString("code");
			String msg = reJson.getString("msg");
			
			if(!"200".equals(code)) {
				return ResponseFormat.retParam(0, msg, null);
			}
			return ResponseFormat.retParam(1, 200,null );
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

}
