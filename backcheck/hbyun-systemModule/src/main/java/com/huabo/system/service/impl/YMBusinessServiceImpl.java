package com.huabo.system.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.JsonObject;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.config.YMDifferentVConfig;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.YMParam;
import com.hbfk.util.BaseDao;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.Md5util;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.SnowflakeIdWorker;
import com.hbfk.util.YMMd5Util;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.controller.MessageToDoZzController;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblContractTypeActivity;
import com.huabo.system.entity.TblFlowApproverInfo;
import com.huabo.system.entity.TblFlowInformInfo;
import com.huabo.system.entity.TblFlowMessage;
import com.huabo.system.entity.TblFlowTemplate;
import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemFormFlow;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.entity.TblYmFlowRecordAtt;
import com.huabo.system.entity.TblYmprocessInfo;
import com.huabo.system.entity.flow.FlowTask;
import com.huabo.system.flow.FlowModel;
import com.huabo.system.flow.FlowTaskNode;
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
import com.huabo.system.oracle.vo.FileUploadRes;
import com.huabo.system.oracle.vo.FlowMessageVo;
import com.huabo.system.service.TblFlowTaskInfoService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.YMBusinessService;
import com.huabo.system.service.YMFormDataService;
import com.huabo.system.utils.AESUtil;
import com.huabo.system.utils.ErrorCodeEnum;
import com.huabo.system.utils.FileException;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
@Service
public class YMBusinessServiceImpl implements YMBusinessService {
	
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
	public JsonBean getProcessInfoList() throws Exception {
		List<TblYmprocessInfo> infoList = this.tblYmprocessInfoMapper.selectList(null);
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		resultMap.put("infoList", infoList);
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean copyFlowInfo(String token, String ymWorkFrom, BigDecimal tableId, String orgIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		String result = null;
		JSONObject reJson = null;
		Integer code = 0;
		String msg = null;
		String flowName = null;
		Object[] resObjs = null;
		String workId = null;
		String workName = null;
		TblOrganization org = null;
		
		String[] orgIdStrs = orgIds.split(",");
		
		for (String orgId : orgIdStrs) {
			org = this.tblOrganizationMapper.selectByOrgId(new BigDecimal(orgId));
			//调用流程复制接口进行复制
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.copyFiveVersionFlow.replace("#{id}", ymWorkFrom),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					break;
				default:
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.copyFlow.replace("#{id}", ymWorkFrom),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					break;
			}
			
			reJson = JSONObject.parseObject(result);
			code = reJson.getInteger("code");
			if(code != 200) {
				code = -1;
				msg = reJson.getString("msg");
				break;
				
			}
			//查找复制前的流程名称
			flowName = this.selectBeforeCopyFLowName(ymWorkFrom);
			//查找复制后流程名称和流程Id
			resObjs = this.selectCopyAfterFlowInfo(flowName);
			if(resObjs[0]!=null) {
				workId = resObjs[0].toString();
			}else{
				code = -2;
				break;
			}
			workName = resObjs[1]!=null?resObjs[1].toString():null;
			workName = org.getOrgname()+"-"+flowName;
			this.updateWorkNameById(workName,workId);
			//将复制的流程  授权给所选择的公司
			this.tblSystemSheetTableMapper.InsertSystemYmWork(tableId,workId,new BigDecimal(orgId),workName,1);
		}
		if(code == -1) {
			return ResponseFormat.retParam(0, msg, null);
		}else if(code == -2) {
			return ResponseFormat.retParam(0, "数据异常", null);		
		}
		return ResponseFormat.retParam(1, "复制成功", null);
	}
	
	@Override
	public JsonBean copyContractTypeFlow(String token, String activityId, String orgIds) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblContractTypeActivity act = this.tblContractTypeActivityMapper.selectById(activityId);
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		String[] orgIdStrs = orgIds.split(",");
		String workName = null;
		String workId = null;
		String result = null;
		JSONObject reJson = null;
		Integer code = null;
		String msg = null;
		String flowName = null;
		Object[] resObjs = null;
		TblOrganization org = null;
		Integer version = null;
		//将复制的流程  授权给所选择的公司
		for (String orgId : orgIdStrs) {
			org = this.tblOrganizationMapper.selectByOrgId(new BigDecimal(orgId));
			//调用流程复制接口进行复制
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.copyFiveVersionFlow.replace("#{id}", act.getYmWorkFrom()),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					break;
				default:
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.copyFlow.replace("#{id}", act.getYmWorkFrom()),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					break;
			}
			reJson = JSONObject.parseObject(result);
			code = reJson.getInteger("code");
			if(code != 200) {
				code = -1;
				msg = reJson.getString("msg");
				break;
			}
			//查找复制前的流程名称
			flowName = this.selectBeforeCopyFLowName(act.getYmWorkFrom());
			
			//查找复制后流程名称和流程Id
			resObjs = this.selectCopyAfterFlowInfo(flowName);
			if(resObjs[0]!=null) {
				workId = resObjs[0].toString();
			}else{
				code = -2;
				break;
			};
			
			workName = resObjs[1]!=null?resObjs[1].toString():null;
			workName = org.getOrgname()+"-"+flowName;
			this.updateWorkNameById(workName,workId);
			
			version = this.tblContractTypeActivityMapper.selectMaxVersion(act.getTypeId(),act.getOrgId());
			if(version == null) {version = 1;}else{version++;};
			act.setActivityId(RandomUtil.uuStringId());
			act.setYmWorkFrom(workId);
			act.setOrgId(new BigDecimal(orgId));
			act.setYmWorkName(workName);
			act.setVersion(version);
			this.tblContractTypeActivityMapper.insert(act);
		}
		
		if(code == -1) {
			return ResponseFormat.retParam(0, msg, null);
		}else if(code == -2) {
			return ResponseFormat.retParam(0, "数据异常", null);		
		}
		return ResponseFormat.retParam(1, "复制成功", null);
	}
	
	
	private Object[] selectCopyAfterFlowInfo(String flowName) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object[] resultObj = new Object[2];
		try {
			String sql = YMDifferentVConfig.getCopyAfterFlowInfo(flowName);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				resultObj[0] = rs.getObject("F_Id");
				resultObj[1] = rs.getObject("F_FullName");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return resultObj;
	}
	
	private void updateWorkNameById(String workName, String workId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			String sql = YMDifferentVConfig.getupdateWorkNameByIdSql(workName,workId);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.execute();
		}finally {
			BaseDao.getInstance().close(con,null,ps);
		}
	}


	private String selectBeforeCopyFLowName(String ymWorkFrom) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			String sql = YMDifferentVConfig.getSelectBeforeCopyFlowNameSql(ymWorkFrom);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				fid = rs.getString("F_FullName");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}


	@Override
	public JsonBean startYmWorkFlow(String token, BigDecimal tableId, String ymWorkForm, Integer qystatus) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			Map<String,Object> dataMap = new HashMap<String,Object>(0);
			dataMap.put("tableId", tableId);
			dataMap.put("ymWorkForm", ymWorkForm);
			dataMap.put("orgid", loginStaff.getCurrentOrg().getOrgid());
			dataMap.put("qystatus", qystatus);
			
			int optype = 0;
			String optext ;
			if(qystatus == 0) {
				optype = TblAuthorizationRecord.OPERATIONDEPRECATED;
				optext = "弃用";
			}else {
				optype = TblAuthorizationRecord.OPERATIONENABLE;
				optext = "启用";
			}
			TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectEntityById(tableId);
			
			String flowName = this.tblSystemSheetTableMapper.selectYmWorkFlowName(tableId,ymWorkForm,loginStaff.getCurrentOrg().getOrgid());
			
			String rtext = loginStaff.getCurrentOrg().getOrgname()+sheet.getYmWorkName()+optext+flowName;
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(dataMap));
			confirm.setOperationMemo(rtext);
			confirm.setOperationType(optype);
			confirm.setStatus(0);
			confirm.setTargetId(loginStaff.getCurrentOrg().getOrgid()+"-"+tableId);
			confirm.setRecordText(rtext);
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEFLOW);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblSystemSheetTableMapper.removeYmWorkFormInfo(tableId,loginStaff.getCurrentOrg().getOrgid());
			this.tblSystemSheetTableMapper.startYmWorkFormInfo(tableId,ymWorkForm,loginStaff.getCurrentOrg().getOrgid(),qystatus);
			return ResponseFormat.retParam(1, 200, null);
		}
		
	}
	
	@Override
	public JsonBean startContractTypeFlow(String token, String activityId, Integer qystatus) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblContractTypeActivity act = this.tblContractTypeActivityMapper.selectById(activityId);
		act.setQyStats(qystatus);
		
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			int optype = 0;
			String optext ;
			if(qystatus == 0) {
				optype = TblAuthorizationRecord.OPERATIONDEPRECATED;
				optext = "弃用";
			}else {
				optype = TblAuthorizationRecord.OPERATIONENABLE;
				optext = "启用";
			}
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(act));
			confirm.setOperationMemo(act.getYmWorkName()+optext);
			confirm.setOperationType(optype);
			confirm.setStatus(0);
			confirm.setTargetId(activityId);
			confirm.setRecordText(act.getYmWorkName()+optext);
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPECONTRACTFLOW);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			this.tblContractTypeActivityMapper.removeYmWorkFormInfo(act);
			this.tblContractTypeActivityMapper.updateById(act);
			return ResponseFormat.retParam(1, 200, null);
		}
		
		
	}
	
	@Override
	public JsonBean getWorkCount(String token) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("currentPage",1);
		filedMap.put("pageSize",5);
		// 查询我的待办的数量
		//pathValue = 1 时 查询我的待办
		
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		JSONObject pageJson = dataJson.getJSONObject("pagination");
		Integer total = pageJson.getInteger("total");
		
		// 查询我发起的数量
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowLaunch,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		reJson = JSONObject.parseObject(result);
		code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		dataJson = reJson.getJSONObject("data");
		Integer totalFq = pageJson.getInteger("total");
		// 相加 得到消息的总数量
		dataMap.put("count",total+totalFq);
		 return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean copyInfoList(String token, Integer currentPage, Integer pageSize, String flowName)
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
		//pathValue = 1 时 查询已处理的
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"4",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"3",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}
		
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		
		
		//获取当前人 抄送事宜的 已阅未阅状态；
		JSONArray flowListJson = dataJson.getJSONArray("list");
		if(flowListJson != null && flowListJson.size() != 0) {
			JSONObject flowJson = null;
			StringBuffer taskIds = new StringBuffer();
			//1.获取当前查询的所有taskid 作为查询条件
			for (int i = 0 ; i < flowListJson.size() ; i++) {
				flowJson = flowListJson.getJSONObject(i);
				taskIds.append(flowJson.getString("id")).append(",");
			}
			taskIds.deleteCharAt(taskIds.length()-1);
			
			//2.查询获取符合条件的抄送记录
			String tasks = taskIds.toString().replace(",", "','");
			List<TblFlowInformInfo> infoList = this.tblFlowInformInfoMapper.selectListByLoginUser(tasks,loginStaff.getStaffid());
			JSONArray readArray = new JSONArray();
			for (int i = 0 ; i < flowListJson.size() ; i++) {
				flowJson = flowListJson.getJSONObject(i);
				flowJson.put("isRead", 0);
				for (TblFlowInformInfo info : infoList) {
					if(flowJson.getString("id").equals(info.getId()) && flowJson.getString("flowId").equals(info.getFlowId()) && flowJson.getString("thisStepId").equals(info.getThisStepId())) {
						flowJson.put("isRead", info.getIsRead());
						break;
					}
				}
				readArray.add(flowJson);
			}
			flowListJson = readArray;
		}
		
		dataMap.put("list", flowListJson);
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
	public JsonBean copyInfoDetail(String token, String id, String thisStepId, String flowId, String operatorId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		headerMap.put("Authorization",loginStaff.getYmToken());
		
		YMParam param = new YMParam();
		param.setTaskId(id);
		param.setFlowTaskOperatorId(operatorId);
		param.setFlowEngienId(flowId);
		param.setCurrNodeId(thisStepId);
		
		HashMap<String, Object> filedMap = YMDifferentVConfig.getCopyFlowInfoData(param);
		
		//获取流程信息
		String result = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.taskList+"/"+id,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workInfo+id,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		
		//获取流程基础配置信息表
		dataMap = YMDifferentVConfig.dealPendgingFlowData(reJson.getJSONObject("data"));
		
		
		BigDecimal formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		TblSystemSheetTable sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}
		BigDecimal userId = this.tblSystemSheetTableMapper.selectFormIdByYmUserId(id);
		String  userName= this.tblSystemSheetTableMapper.selectFormIdByYmUserName(userId);
		dataMap.put("flowType", sheet.getClassName());
		String ymStaffId = this.tblStaffMapper.selectYmPkStaffIdByStaffId(loginStaff.getStaffid());
		this.tblFlowInformInfoMapper.updateEntity(loginStaff.getStaffid(),flowId,id,formId);
		dataMap.put("formId", formId);
		dataMap.put("ymStaffId", ymStaffId);
		dataMap.put("userId", userId);
		dataMap.put("userName", userName);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean alreadyList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception {
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
		//pathValue = 1 时 查询已处理的
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"3",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"2",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		JSONObject pageJson = dataJson.getJSONObject("pagination");
		dataMap.put("currentPage", pageJson.getInteger("currentPage"));
		dataMap.put("pageSize", pageJson.getInteger("pageSize"));
		Integer total = pageJson.getInteger("total");
		JSONArray dataArray = dataJson.getJSONArray("list");
		
		if(YMUrlStatic.YMVERSION.equals("5.0+")) {
			dataMap.put("list", dataArray);
		}else {
			JSONObject flowJson = null;
			JSONArray resultArray = new JSONArray();
			Integer taskStatus = null;
			for (int i = 0; i < dataArray.size(); i++) {
				flowJson = dataArray.getJSONObject(i);
				taskStatus = this.getTaskStatusByProcessId(flowJson.getString("processId"));
				flowJson.put("taskStatus", taskStatus);
				resultArray.add(flowJson);
			}
			dataMap.put("list", resultArray);
		}
				
		
		Integer totalPage = total/pageSize;
		if(total%pageSize != 0) {
			totalPage++;
		}
		dataMap.put("totalCount", total);
		dataMap.put("totalPage", totalPage);
		
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getYmFormData(BigDecimal tableId) throws Exception {
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectById(tableId);
		dataMap.put("formData", sheet.getYmFormCode());
		return ResponseFormat.retParam(200, 200, dataMap);
	}
	
	@Override
	public JsonBean actionsWithdraw(String token, String id ,String flowId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("freeApproverUserId","");
		filedMap.put("handleOpinion","");
		filedMap.put("signImg","");
		filedMap.put("nodeCode","");
		filedMap.put("pause","0");
		
		//调用业务中台流程撤回接口
		
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowEngineRecall.replace("#{id}", id),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				flowId = this.tblSystemFormFlowMapper.selectFlowIdByTaskId(id);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.actionsWithdraw.replace("#{id}", id),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
			//获取表单主键
			formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
			//获取流程信息基础配置表
			// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
			sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
			if (sheet == null) {
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
			//修改当前表单为已撤销的状态
			String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_YCX+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
			this.tblSystemSheetTableMapper.executeSql(sql);
		
		//处理待办办理信息
		tblFlowTaskInfoService.insertActionsWithdrawInfo(id,loginStaff,sheet,formId,flowId);
		return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean getEditInfo(String token, String id, String flowId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		//通过流程实例主键获取流程信息
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("#{id}", id),null,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workInfo+"/"+id,null,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		BigDecimal formId = BigDecimal.valueOf(-1);
		
			formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);//表单主键
			// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
			TblSystemSheetTable sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
			if (sheet == null) {
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
			dataMap.put("flowType", sheet.getClassName());//所属模块类型
		dataMap.put("dataJson",dataJson);//流程编辑数据
		dataMap.put("id",id);
		if(formId == null) {
			formId = BigDecimal.valueOf(-1);
		}
		dataMap.put("formId", formId);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getInfo(String token, String id, String thisStepId, String processId, String flowId, String opType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		headerMap.put("Authorization",loginStaff.getYmToken());
		
		YMParam param = new YMParam();
		param.setTaskId(id);
		param.setFlowTaskOperatorId(processId);
		param.setFlowEngienId(flowId);
		param.setCurrNodeId(thisStepId);
		param.setOpType(opType);
		
		HashMap<String, Object> filedMap = YMDifferentVConfig.getFlowInfoData(param);
		
		//获取流程信息
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.taskList+"/"+id,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				//获取流程基础配置信息表
                JSONObject reJson = JSONObject.parseObject(result);
				Integer code = reJson.getInteger("code");
				if(code != 200) {
					String msg = reJson.getString("msg");
					return ResponseFormat.retParam(0, msg, null);
				}
				dataMap = YMDifferentVConfig.dealPendgingFlowData(reJson.getJSONObject("data"));
                BigDecimal formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
                BigDecimal userId = this.tblSystemSheetTableMapper.selectFormIdByYmUserId(id);
                String userName= this.tblSystemSheetTableMapper.selectFormIdByYmUserName(userId);
                String ymStaffId = this.tblStaffMapper.selectYmPkStaffIdByStaffId(loginStaff.getStaffid());
                dataMap.put("formId", formId);
                dataMap.put("userId", userId);
                dataMap.put("userName", userName);
                dataMap.put("ymStaffId", ymStaffId);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workInfo+processId,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				//获取流程基础配置信息表
				JSONObject reJson2 = JSONObject.parseObject(result);
				Integer code2 = reJson2.getInteger("code");
				if(code2 != 200) {
					String msg = reJson2.getString("msg");
					return ResponseFormat.retParam(0, msg, null);
				}
				dataMap = YMDifferentVConfig.dealPendgingFlowData(reJson2.getJSONObject("data"));
				BigDecimal formId2 = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(processId);
				BigDecimal userId2 = this.tblSystemSheetTableMapper.selectFormIdByYmUserId(processId);
				String userName2 = this.tblSystemSheetTableMapper.selectFormIdByYmUserName(userId2);
				String ymStaffId2 = this.tblStaffMapper.selectYmPkStaffIdByStaffId(loginStaff.getStaffid());
				dataMap.put("formId", formId2);
				dataMap.put("userId", userId2);
				dataMap.put("userName", userName2);
				dataMap.put("ymStaffId", ymStaffId2);
				break;
		}
		


		
		TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(flowId,loginStaff.getCurrentOrg().getOrgid());
		// 旧版本流程重新发布后 flowId 已变更，TBL_SYSTEM_YMWORK 里可能查不到对应记录，做 null 容错
		if (sheet == null) {
			// 5.0 版本：撤回后重新提交会生成新的流程实例 flowId，而映射表中 YMWORKFROM 仍是旧 flowId（或未回填），
			// 导致用 flowId 查不到 sheet → flowType 为空 → 前端审批页无法识别业务类型而空白渲染。
			// 此处用稳定不变的流程模板主键 templateId（来自 taskInfo）二次查询兜底补全。
			Object taskInfoObj = dataMap.get("taskInfo");
			if (taskInfoObj instanceof JSONObject) {
				String templateId = ((JSONObject) taskInfoObj).getString("templateId");
				if (templateId != null && !"".equals(templateId)) {
					sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByTemplateId(templateId, loginStaff.getCurrentOrg().getOrgid());
					if (sheet != null) {
						log.info("[getInfo] flowId={} 用 flowId 查不到 sheet，已用 templateId={} 兜底命中，flowType={}", flowId, templateId, sheet.getClassName());
					}
				}
			}
		}
		if (sheet != null) {
			dataMap.put("flowType", sheet.getClassName());
			dataMap.put("sheet", sheet);
		} else {
			log.warn("[getInfo] flowId={} 在 TBL_SYSTEM_YMWORK/TBL_CONTRACTTYPE_ACTIVITY 中未找到对应 sheet，可能是旧版本流程", flowId);
			dataMap.put("flowType", "");
			dataMap.put("sheet", null);
		}

		return ResponseFormat.retParam(1, 200, dataMap);
	}

	/**
	 * 统一安全获取流程基础配置 sheet（兜底链，零数据污染）。
	 * 背景：5.0 版本撤回后重新提交会生成新的流程平台“流程实例”flowId，审批接口拿到的就是这个实例 flowId，
	 * 而本地映射表（TBL_SYSTEM_YMWORK.YMWORKFROM）登记的是“定义 flowId”，二者对不上，
	 * 直接用 selectSheetTableInfoByFlowId(实例flowId) 会返回 null，后续 sheet.getClassName() 空指针。
	 * 兜底顺序：① 原 flowId 查（正常流程命中）→ ② templateId 查（流程模板主键，最稳定）
	 * → ③ formId 经 FORMFLOW 查回“定义 flowId”再查。三者都查不到才返回 null，由调用方决定如何处理。
	 * 注意：仅做只读查询，不修改 YMWORKFROM 等任何配置数据。
	 *
	 * @param flowId     审批接口传入的 flowId（可能是实例 flowId）
	 * @param templateId 流程模板主键（可为 null）
	 * @param formId     业务表单主键（合同 id，可为 null）
	 * @param orgId      当前公司主键
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
	public JsonBean alreadyRecall(String token, String flowTaskOperatorRecordListId, String freeApproverUserId,
			String handleOpinion, String signImg, String flowId, String processId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		//判断是否有抄送人员
		String copyYmId = "";
		if(freeApproverUserId != null && !"".equals(freeApproverUserId)) {
			String[] cys = freeApproverUserId.split(",");
			for (String cyId : cys) {
				copyYmId += this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(cyId))+",";
			}
			copyYmId = copyYmId.substring(0,copyYmId.length()-1);
		}
		
		//获取流程基础配置信息，表单主键，并修改表单状态为已撤销
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
			formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(processId);
			// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
			sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
			if (sheet == null) {
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
			String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_YCX+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
			this.tblSystemSheetTableMapper.executeSql(sql);
		
		//封装调用参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		//调用流程撤销接口
		String result ;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				filedMap.put("fileList",null);
				filedMap.put("handleIds","");
				filedMap.put("handleOpinion",handleOpinion);
				filedMap.put("nodeCode","");
				filedMap.put("pause",0);
				filedMap.put("signImg",signImg);
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowSendBackUrl+flowTaskOperatorRecordListId,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				break;
			default:
				filedMap.put("handleOpinion",handleOpinion);
				filedMap.put("freeApproverUserId",copyYmId);
				filedMap.put("signImg",signImg);
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.recallUrl+flowTaskOperatorRecordListId,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		
		if(code == 200) {
			tblFlowTaskInfoService.insertAlreadyRecall(processId,loginStaff,sheet,formId,flowId,handleOpinion,flowTaskOperatorRecordListId);
			return ResponseFormat.retParam(1, 200, null);
		}else {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
	}
	
	@Override
	public JsonBean rejectList(String token, String operatorId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		//获取审批拒绝节点集合
		String result ;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.sendBackNodeUrl+operatorId,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,HttpClient.PARAMBODY);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.rejectList+operatorId,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,HttpClient.PARAMBODY);
				break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				dataMap.put("rejectList", reJson.getJSONObject("data").getJSONArray("list"));
				break;
			default:
				dataMap.put("rejectList", reJson.getJSONArray("data"));
				break;
		}
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean reject(String token, String flowId, String handleOpinion, String signImg, String copyIds,
			String candidateType, String enCode, String branchList, String id, String operatorId, String rejectStep, String rejectType, String thisStepId, String handleStatus) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
		String copyYmId = "";
		
		//获取流程基础配置表和表单主键
		formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}

		//判断是否有抄送，如果有 保存到流程抄送记录表里 并获取抄送人员的业务中台主键
		if(copyIds != null && !"".equals(copyIds)) {
			String[] cys = copyIds.split(",");
			for (String cyId : cys) {
				this.tblFlowInformInfoMapper.insertEntity(loginStaff.getStaffid(),new BigDecimal(cyId),flowId,id,formId,thisStepId,RandomUtil.uuBigDecimalId());
				copyYmId += this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(cyId))+",";
			}
			copyYmId = copyYmId.substring(0,copyYmId.length()-1);
		}
		
		if(candidateType == null || "".equals(candidateType)) {
			candidateType = "1";
		}
		
		//封存需要传入的参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> formMap = this.ymFormDataService.setYmFormData(sheet,formId,loginStaff);
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		
		YMParam param = new YMParam();
		param.setFlowEngienId(flowId);
		param.setTaskId(id);
		param.setFlowTaskOperatorId(operatorId);
		param.setFormId(formId.toString());
		param.setCandidateType(candidateType);
		param.setHandleOpinion(handleOpinion);
		param.setSignImg(signImg);
		param.setEnCode(enCode);
		param.setCopyYmId(copyYmId);
		param.setBranchStrs(branchList);
		param.setRejectStep(rejectStep);
		param.setRejectType(rejectType);
		param.setHandleStatus(handleStatus);
		param.setFormDataMap(formMap);
		
		filedMap = YMDifferentVConfig.setRejectMapInfo(param);
		
		//调用业务中台流程拒绝接口
		String result;
		if(YMUrlStatic.YMVERSION.equals("5.0+")) {
			result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowAuditUrl.replace("#{id}",operatorId),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		}else {
//			result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.rejectUrl+operatorId,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
//			if(copyIds != null && !"".equals(copyIds)) {
//				String[] cys = copyIds.split(",");
//				for (String cyId : cys) {
//					this.tblFlowInformInfoMapper.insertEntity(loginStaff.getStaffid(),new BigDecimal(cyId),flowId,id,formId,thisStepId);
//					copyYmId += this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(cyId))+",";
//				}
//				copyYmId = copyYmId.substring(0,copyYmId.length()-1);
//			}
			
			if(candidateType == null || "".equals(candidateType)) {
				candidateType = "1";
			}
			
			HashMap<String, Object> filedMap1 = new HashMap<String,Object>(0);
			HashMap<String, String> formMap1 = new HashMap<String,String>(0);
			formMap1.put("flowId",flowId);
			filedMap1.put("handleOpinion",handleOpinion);
			filedMap1.put("signImg",signImg);
			filedMap1.put("enCode",enCode);
			filedMap1.put("copyIds",copyYmId);
			filedMap1.put("candidateType",candidateType);
			filedMap1.put("branchList",branchList);
			filedMap1.put("signImg",signImg);
			filedMap1.put("formData",JSONObject.toJSON(formMap1));
			filedMap1.put("rejectStep",rejectStep);
			if(rejectType != null && !"".equals(rejectType)) {
				filedMap.put("rejectType",rejectType);
			}
			result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.rejectUrl+operatorId,filedMap1,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			
		}
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		tblFlowTaskInfoService.insertRejectInfo(id,loginStaff,sheet,formId,handleOpinion,thisStepId,operatorId);
		
		return ResponseFormat.retParam(1, 200, null);
	}
	

	@Override
	public JsonBean candidates(String token, String flowTaskOperatorId, String id, String flowId,BigDecimal fromId,String tableId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblSystemSheetTable sheet = null;
		
		//获取流程基础配置表 和 流程主键
			if(tableId != null && !"".equals(tableId) && StringUtils.isBlank(flowId)) {
				if("5".equals(tableId) || "7".equals(tableId)) {
					//通过当前表单获取合同类型主键
					BigDecimal typeId = this.ymFormDataService.selectContractTypeIdByContracrtId(fromId);
					flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId, typeId, loginStaff.getCurrentOrg().getOrgid());
					if(StringUtils.isBlank(flowId)) {
						typeId = this.tblContractTypeActivityMapper.selectFatherTypeIdByTypeId(typeId);
						flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
					}
				}
				if(StringUtils.isBlank(flowId)) {
					flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
				}
			}
			if(flowId == null || "".equals(flowId)) {
				 return ResponseFormat.retParam(0, "该模块没有配置流程，请前往流程设计进行配置！", null);
			}
			sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(flowId,loginStaff.getCurrentOrg().getOrgid());
			// 5.0 版本：撤回后重新提交会生成新的流程实例 flowId，而映射表 TBL_SYSTEM_YMWORK.YMWORKFROM 仍是旧 flowId，
			// 导致用 flowId 查不到 sheet → 后续 setYmFormData 调用 sheet.getClassName() 时空指针（candidates 审批报 500）。
			// 此处用稳定不变的流程模板主键 id（即 templateId）二次查询兜底补全，与 getInfo 的兜底逻辑保持一致。
			if (sheet == null && id != null && !"".equals(id)) {
				sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByTemplateId(id, loginStaff.getCurrentOrg().getOrgid());
				if (sheet != null) {
					log.info("[candidates] flowId={} 用 flowId 查不到 sheet，已用 templateId(id)={} 兜底命中，flowType={}", flowId, id, sheet.getClassName());
				}
			}
			if (sheet == null) {
				log.warn("[candidates] flowId={} templateId(id)={} 均未找到对应 sheet，无法封装表单数据", flowId, id);
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
		//封装参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> formMap = this.ymFormDataService.setYmFormData(sheet,fromId,loginStaff);
		
		HashMap<String, Object> filedMap = YMDifferentVConfig.setCandidateNodeInfo(flowId,id,formMap,flowTaskOperatorId);
		
		if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
			flowTaskOperatorId = "0";
		}
		YMParam param = new YMParam();
		param.setFlowTaskOperatorId(flowTaskOperatorId);
		param.setFormDataMap(filedMap);
		param.setHeaderMap(headerMap);
		JsonBean jsonBean = YMDifferentVConfig.getCandiateNodeInfo(param,loginStaff);
		return jsonBean;
	}

	@Override
	public JsonBean candidateUser(String token, String flowTaskOperatorId, String id, String flowId, BigDecimal fromId,
			Integer currentPage, Integer pageSize, String keyword,String nodeCode, String tableId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		if(currentPage == null) {
			currentPage = 1;
		}
		if(pageSize == null) {
			pageSize = 20;
		}
		TblSystemSheetTable sheet = null;
		
			if(tableId != null && !"".equals(tableId) && StringUtils.isBlank(flowId)) {
				if("5".equals(tableId) || "7".equals(tableId)) {
					//通过当前表单获取合同类型主键
					BigDecimal typeId = this.ymFormDataService.selectContractTypeIdByContracrtId(fromId);
					flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId, typeId, loginStaff.getCurrentOrg().getOrgid());
					if(StringUtils.isBlank(flowId)) {
						typeId = this.tblContractTypeActivityMapper.selectFatherTypeIdByTypeId(typeId);
						flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
					}
				}
				if(StringUtils.isBlank(flowId)) {
					flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
				}
			}
			if(flowId == null || "".equals(flowId)) {
				 return ResponseFormat.retParam(0, "该模块没有配置流程，请前往流程设计进行配置！", null);
			}
		sheet = this.resolveSheet(flowId, id, fromId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}

		//封装参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");

		HashMap<String, Object> formMap = this.ymFormDataService.setYmFormData(sheet,fromId,loginStaff);
		formMap.put("flowId",flowId);
		formMap.put("id",id);
		
		
		HashMap<String, Object> formDataMap = YMDifferentVConfig.setCandidateNodeInfo(formMap,id,flowId,flowTaskOperatorId,nodeCode,keyword);
		
		
		if(flowTaskOperatorId == null || "".equals(flowTaskOperatorId)) {
			flowTaskOperatorId = "0";
		}
		YMParam param = new YMParam();
		param.setHeaderMap(headerMap);
		param.setFlowTaskOperatorId(flowTaskOperatorId);
		param.setFormDataMap(formDataMap);
		
		Map<String,Object> dataMap = YMDifferentVConfig.getCondidatesUserInfo(param,loginStaff);
		if(dataMap.get("code").toString().equals("0")) {
			return ResponseFormat.retParam(0, dataMap.get("msg").toString(), null);
		}
		
		if(dataMap.get("list") == null) {
			return ResponseFormat.retParam(1, 200, null);
		}
		//获取流程候选人接口
		/*
		 * if(loginStaff.getCurrentOrg().getUseSecrect() != null &&
		 * loginStaff.getCurrentOrg().getUseSecrect() == 1) { JSONArray pageArray =
		 * JSONArray.parseArray(dataMap.get("list").toString()); if(pageArray == null ||
		 * pageArray.size() == 0) { dataMap.put("list", pageArray); }else { //获取审批单据信息
		 * if(StringUtils.isNotBlank(id)) { sheet =
		 * this.tblSystemSheetTableMapper.selectEntityByProcessId(id); }else {
		 * sheet.setFormId(fromId.toString()); }
		 * if(StringUtils.isBlank(sheet.getSecrectColumn())) { dataMap.put("list",
		 * pageArray); dataMap.put("currentPage", 1); dataMap.put("pageSize", 100);
		 * dataMap.put("totalPage", 1); return ResponseFormat.retParam(1, 200, dataMap);
		 * }
		 * 
		 * //获取审批单据的密级范围与知悉范围人员 String secSql =
		 * "SELECT SECRECYSTAFFSCOPE FROM TBL_SECRECT_LEVEL WHERE LEVELID = (SELECT "
		 * +sheet.getSecrectColumn()+" FROM " +sheet.getTableName()+
		 * " WHERE "+sheet.getPrimaryColumn()+" = '"+sheet.getFormId()+"') "; String
		 * secrectId =
		 * this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(secSql);
		 * 
		 * if(StringUtils.isBlank(secrectId)) { dataMap.put("list", pageArray);
		 * dataMap.put("currentPage", 1); dataMap.put("pageSize", 100);
		 * dataMap.put("totalPage", 1); return ResponseFormat.retParam(1, 200, dataMap);
		 * }
		 * 
		 * //获取所有用户的pkStaffId; List<String> pkYmStaffIdList = IntStream.range(0,
		 * pageArray.size()).mapToObj(pageArray::getJSONObject).map(obj ->
		 * obj.getString("id")).collect(Collectors.toList()); String scoSql =
		 * "SELECT "+sheet.getStaffScopeColumn()+" FROM " +sheet.getTableName()+
		 * " WHERE "+sheet.getPrimaryColumn()+" = '"+sheet.getFormId()+"' "; String
		 * scopeStaffIds =
		 * this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(scoSql);
		 * //通过以上三个条件查询出符合条件的候选人 List<String> rePkStaffIdList =
		 * this.tblStaffMapper.selectFlowHxrBySecrect(pkYmStaffIdList,secrectId,
		 * scopeStaffIds); JSONObject sob = null; JSONArray midArray = new JSONArray();
		 * for (int i = 0; i < pageArray.size(); i++) { sob =
		 * pageArray.getJSONObject(i); for (String pkymId : rePkStaffIdList) {
		 * if(pkymId.equals(sob.getString("id"))) { midArray.add(sob); break; } } }
		 * pageArray = midArray; } dataMap.put("list", pageArray); }
		 */
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	
	
	@Override
	public JsonBean audit(String token, String flowId, String handleOpinion, String signImg, String copyIds,
			String enCode, String branchStrs, String id, String operatorId, String freeApproverUserId, String candidateType, String nodeCode, String candidateList, String nextStepId, String thisStepId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
		String freeApproverYmUserId = "";
		String copyYmId = "";
		
		List<TblFlowApproverInfo> appList =  null;
		TblFlowApproverInfo currentApp = null;
		TblFlowApproverInfo nextApp = null;
		String[] approverUsers = null;
		TblFlowInformInfo info = null;
		if(freeApproverUserId != null && !"".equals(freeApproverUserId)) {
			approverUsers = freeApproverUserId.split(",");
		}
		
		//获取流程基础配置，表单主键，
			formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
			// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
			sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
			if (sheet == null) {
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
			appList = this.tblFlowApproverInfoMapper.selectNextApprovalStaffList(flowId, id, thisStepId);//多人加签集合
			if(appList != null && appList.size() > 0) {
				currentApp = appList.get(0);
			}
			if(approverUsers != null) {
				freeApproverYmUserId = this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(approverUsers[0]));
			}else {
				//size == 2  意味着有下一步办理审理人 将下一步办理人作为加签人放入流程数据中
				if(appList.size() > 1) {
					nextApp = appList.get(1);
					freeApproverYmUserId = this.tblStaffMapper.selectYmPkStaffIdByStaffId(nextApp.getStaffId());
				}
			}
		
			//flowtask主键 获取提交审批节点，taskNode信息
			FlowTask task = this.tblFlowTaskInfoService.getFlowTaskById(id);
			
			//通过operatorId 获取当前操作信息
			FlowTaskOperator preOper = this.tblFlowTaskInfoService.selectCurrentOperator(operatorId);
			
			
			//查找当前审批节点tasknode ,通过 flowTaskId 和 nodeCode
			FlowTaskNode taskNode = null;
			//taskNode = this.tblFlowTaskInfoService.getFlowTaskNodeByTaskIdNodeCode(task.getId(),preOper.getNodeCode());
			thisStepId = preOper.getNodeCode();
			
			
			//查找流程发起人，通过表单主键和taskId 和 flowId
			TblStaff startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,id,flowId);
			
			
		//判断是否自动抄送设置 或者 自动抄送给表单中人的设置
		if(((sheet.getIsAutoCopy() != null && sheet.getIsAutoCopy() == 1) || (sheet.getIsCopyForm() != null && sheet.getIsCopyForm() == 1) ) && "end".equals(nextStepId) ) {
			
			//判断是否有自动抄送至流程审批人的设置
			if(sheet.getIsAutoCopy() != null && sheet.getIsAutoCopy() == 1) {
				List<TblStaff> staffList = this.tblStaffMapper.selectCopyStaffList(id);
				for (TblStaff tblStaff : staffList) {
					info = new TblFlowInformInfo();
					info.setInfoId(RandomUtil.uuBigDecimalId());
					info.setCreateStaff(loginStaff.getStaffid());
					info.setCreateTime(new Date());
					info.setInformStaffId(tblStaff.getStaffid());
					info.setId(id);
					info.setFlowId(flowId);
					info.setFormId(formId.toString());
					info.setThisStepId(preOper.getTaskNodeId());
					info.setIsRead(0);
					this.tblFlowInformInfoMapper.insert(info);
					copyYmId += tblStaff.getPkYmStaffId()+",";
				}
			}
			
			//经营事项审核 审批结束时需要抄送给 事项知会人员
			if(sheet.getIsCopyForm() != null && sheet.getIsCopyForm() == 1) {
				String sql = "SELECT MATTERSINFORMEDPERSONNELID FROM tbl_fwgl_institution_audit WHERE INSTITUTIONAUDITID = "+formId;
				String formStaffId = this.tblSystemSheetTableMapper.executeSqlReturnString(sql);
				if(formStaffId != null && !"".equals(formStaffId)) {
					List<TblStaff> staffList = this.tblStaffMapper.selectListInfoByIds(formStaffId);
					for (TblStaff tblStaff : staffList) {
						info = new TblFlowInformInfo();
						info.setInfoId(RandomUtil.uuBigDecimalId());
						info.setCreateStaff(loginStaff.getStaffid());
						info.setCreateTime(new Date());
						info.setInformStaffId(tblStaff.getStaffid());
						info.setId(id);
						info.setFlowId(flowId);
						info.setFormId(formId.toString());
						info.setThisStepId(preOper.getTaskNodeId());
						info.setIsRead(0);
						this.tblFlowInformInfoMapper.insert(info);
						copyYmId += tblStaff.getPkYmStaffId()+",";
					}
				}
			}
			
		}
		
		if(copyIds != null && !"".equals(copyIds)) {
			String[] cys = copyIds.split(",");
			for (String cyId : cys) {
				//添加知会信息记录
				info = new TblFlowInformInfo();
				info.setInfoId(RandomUtil.uuBigDecimalId());
				info.setCreateStaff(loginStaff.getStaffid());
				info.setCreateTime(new Date());
				info.setInformStaffId(new BigDecimal(cyId));
				info.setId(id);
				info.setFlowId(flowId);
				info.setFormId(formId.toString());
				info.setThisStepId(preOper.getTaskNodeId());
				info.setIsRead(0);
				this.tblFlowInformInfoMapper.insert(info);
				copyYmId += this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(cyId))+",";
			}
			copyYmId = copyYmId.substring(0,copyYmId.length()-1);
		}
		//封装参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> formMap = this.ymFormDataService.setYmFormData(sheet,formId,loginStaff);
		HashMap<String, Object> formDataMap = new HashMap<String,Object>(0);
		YMParam param = new YMParam();
		param.setFlowEngienId(flowId);
		param.setTaskId(id);
		param.setFlowTaskOperatorId(operatorId);
		param.setFormId(formId.toString());
		param.setFormDataMap(formMap);
		param.setHandleOpinion(handleOpinion);
		param.setSignImg(signImg);
		param.setEnCode(enCode);
		param.setCopyYmId(copyYmId);
		param.setCandidateType(candidateType);
		param.setCandidateList(candidateList);
		param.setNodeCode(nodeCode);
		param.setBranchStrs(branchStrs);
		param.setFreeApproverYmUserId(freeApproverYmUserId);
		
		formDataMap = YMDifferentVConfig.setAuditFlowDateInfoMap(param);

		System.out.println(JSON.toJSON(formDataMap));
		//调用流程审批通过接口
		String result = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				// 5.0+：audit 前先调 Transact（开始办理），确保 startHandleTime 已设置
				// Transact 是幂等操作，已开始办理的不会重复更新
				try {
					HashMap<String, Object> transactMap = new HashMap<>(1);
					List<String> transactIds = new ArrayList<>();
					transactIds.add(operatorId);
					transactMap.put("ids", transactIds);
					String transactResult = DealUserToken.dealYmUrlMethod(
							YMUrlStatic.interfaceUrl + YMUrlStatic.flowTransactUrl,
							transactMap, headerMap, loginStaff, HttpClient.HTTPPOST, HttpClient.PARAMBODY);
					JSONObject transactJson = JSONObject.parseObject(transactResult);
					Integer transactCode = transactJson.getInteger("code");
					if (transactCode == null || transactCode != 200) {
						log.warn("[audit] 5.0 Transact 调用失败，operatorId={}, msg={}", operatorId, transactJson.getString("msg"));
					}
				} catch (Exception e) {
					log.warn("[audit] 5.0 Transact 调用异常，继续尝试 Audit，operatorId={}", operatorId, e);
				}
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowAuditUrl.replace("#{id}", operatorId),formDataMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.auditUrl+operatorId,formDataMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				break;
		}

		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		//处理流程待办、办理信息
		tblFlowTaskInfoService.insertAuditInfo(id,loginStaff,sheet,formId,freeApproverUserId,handleOpinion,approverUsers,currentApp,nextApp,task,preOper,taskNode,startStaff);
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean transfer(String token, String flowTaskInfoOperatorId, String handleOpinion, String transferStaffId, String signImg, String id, String flowId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		BigDecimal formId = null;
		TblSystemSheetTable sheet = null;
		String ymStaffId = null;
		
		//获取流程基础配置信息、表单主键和转审人员业务中台的主键
			formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(id);
			// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
			sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
			if (sheet == null) {
				return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
			}
			ymStaffId = this.tblStaffMapper.selectYmPkStaffIdByStaffId(new BigDecimal(transferStaffId));
		
		if(ymStaffId == null) {
			return ResponseFormat.retParam(0, 80004, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("freeApproverUserId",ymStaffId);
		filedMap.put("handleOpinion",handleOpinion);
		filedMap.put("signImg",signImg);
		//调用业务中台转审接口
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.transferUrl+flowTaskInfoOperatorId,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		tblFlowTaskInfoService.insertTransferInfo(id,loginStaff,sheet,formId,transferStaffId,handleOpinion,flowTaskInfoOperatorId);
		
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean getFlowLaunch(String token, Integer currentPage, Integer pageSize, String flowName, String status) throws Exception {
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
		
		if(status != null) {
			filedMap.put("status",status);
		}
		String result = null;
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				if(status != null && !status.equals("")){
					filedMap.put("status", Integer.parseInt(status));
				}
				log.info("请求5.0接口参数： {}",filedMap);
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.taskList,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				log.info("5.0原始返回：{}",result);

				JSONObject reJson5 = JSONObject.parseObject(result);
				if(reJson5.getInteger("code") != 200) {
					return ResponseFormat.retParam(0, reJson5.getString("msg"), null);
				}
				JSONObject dataJson5 = reJson5.getJSONObject("data");
				JSONArray rawList5 = dataJson5.getJSONArray("list");

				// 5.0 服务端不支持按 status 过滤，在此处做本地过滤
				// 前端 status 语义：1=等待审核 2=审核通过(PASSED) 3=审核驳回(REJECTED) 4=流程撤回(RECALL=9)
				JSONArray filteredList5 = new JSONArray();
				if (status != null && !status.isEmpty()) {
					int frontendStatus = Integer.parseInt(status);
					// 前端 status=4(流程撤回) 对应 5.0 的 status=9(RECALL)，其余数值相同
					int ymStatus = (frontendStatus == 4) ? 9 : frontendStatus;
					for (int i = 0; i < rawList5.size(); i++) {
						JSONObject item = rawList5.getJSONObject(i);
						Integer itemStatus = item.getInteger("status");
						if (itemStatus != null && itemStatus == ymStatus) {
							filteredList5.add(item);
						}
					}
				} else {
					filteredList5 = rawList5;
				}

				JSONObject pageJson5 = dataJson5.getJSONObject("pagination");
				int filteredTotal5 = filteredList5.size();
				Integer total5 = (status != null && !status.isEmpty()) ? filteredTotal5 : pageJson5.getInteger("total");
				Integer totalPage5 = total5 / pageSize;
				if (total5 % pageSize != 0) {
					totalPage5++;
				}
				Map<String,Object> dataMap5 = new HashMap<String,Object>(0);
				dataMap5.put("list", filteredList5);
				dataMap5.put("currentPage", pageJson5.getInteger("currentPage"));
				dataMap5.put("pageSize", pageJson5.getInteger("pageSize"));
				dataMap5.put("totalCount", total5);
				dataMap5.put("totalPage", totalPage5);
				return ResponseFormat.retParam(1, 200, dataMap5);

			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowLaunch,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}

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
		Integer totalPage = total / pageSize;
		if (total % pageSize != 0) {
			totalPage++;
		}
		dataMap.put("totalCount", total);
		dataMap.put("totalPage", totalPage);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getDealt(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception {
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
		String result = null;
		
		switch (YMUrlStatic.YMVERSION) {
			case "5.0+":
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.operatorListUrl+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
			default:
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
				break;
		}
		
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
	public JsonBean getDealtMH(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get(token);
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
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
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
	public JsonBean submit(String token, BigDecimal fromId, String tableId, String ymFromId, String candidateType, String branchStrs, String nodeCode,String candidateList, String flowId, Integer status, String typeName, String eventType) throws Exception {
		Date inDate = new Date();
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		headerMap.put("content-type","application/json;charset=utf-8");
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		TblSystemSheetTable sheet = null;
		BigDecimal typeId = null;
		boolean flowFlag = false; //判断流程来自合同类型还是其他模块
		
		if(StringUtils.isBlank(flowId)) {
			if(StringUtils.isNotBlank(typeName)) {
				//获取流程主键信息  判断是否是合同发合同发起时传入 typeName
				typeId = this.tblContractTypeActivityMapper.selectTypeIdByTypeName(typeName);
				flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
				flowFlag = true;
				if(StringUtils.isBlank(flowId)) {
					//查找当前合同类型上级有没有绑定流程
					typeId = this.tblContractTypeActivityMapper.selectFatherTypeIdByTypeId(typeId);
					flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
					flowFlag = true;
				}
				if(StringUtils.isBlank(flowId)) {
					//合同类型 没有绑定流程，则执行流程设计里面的流程
					flowFlag = false;
					flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
				}
			}else {
				//通过模块主键 和公司获取到 当前需要调用的流程主键
				if(StringUtils.isNotBlank(tableId)) {
					flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
				}
				flowFlag = false;
			}
		}
		
		if(StringUtils.isBlank(flowId)) {
			return ResponseFormat.retParam(0, "该模块没有配置流程，请前往流程设计进行配置！", null);
		}
		if(flowFlag) {
			//根据合同类型 、流程主键、公司主键获取tableId获取流程基础配置信息
			sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByContractTypeFlowId(typeId,flowId,loginStaff.getCurrentOrg().getOrgid());
		}else {
			//根据流程主键和公司主键 获取到需要执行流程基础配置信息
			sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(flowId,loginStaff.getCurrentOrg().getOrgid());
		}
		
		//放入表单中需要提交到流程中的表单数据
		HashMap<String, Object> dataMap = this.ymFormDataService.setYmFormData(sheet,fromId,loginStaff);
		//{"F_Description":"asd","F_Account":"sadsdsd","id":"","flowId":""}
		
		
		//根据不同版本准备不同提交流程数据
		YMParam param = new YMParam();
		param.setTaskId(ymFromId);
		param.setFlowEngienId(flowId);
		param.setFormDataMap(dataMap);
		param.setCandidateType(candidateType);
		param.setCandidateList(candidateList);
		param.setNodeCode(nodeCode);
		param.setBranchStrs(branchStrs);
		param.setFormId(fromId.toString());
		param.setEventType(eventType);
		filedMap = YMDifferentVConfig.setSubmitFlowData(param);
		
		String result =  null;
		Integer count = 1;
		if((status != null && status == 4 && YMUrlStatic.YMVERSION.equals("3.4.2"))|| (status != null && status == 3 && YMUrlStatic.YMVERSION.equals("5.0+"))) {
			//如果是撤销状态则删除掉之前的流程数据信息，再次进行提交
			count = 0;
			HashMap<String, Object> deleteMap = new HashMap<String,Object>(0);
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("#{id}", ymFromId),deleteMap,headerMap,loginStaff,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
					break;
				default:
					filedMap.put("freeApproverUserId","");
					filedMap.put("handleOpinion","");
					filedMap.put("signImg","");
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowLaunch+"/"+ymFromId,deleteMap,headerMap,loginStaff,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
					this.tblSystemSheetTableMapper.deleteYmFormInfo(ymFromId);
					break;
			}
			//兼容5.0版本接口
			if(YMUrlStatic.YMVERSION.equals("5.0+")) {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("/#{id}",""),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			}else {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlow.replace("/#{id}",""),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			}
			
		}else if(ymFromId != null && !"".equals(ymFromId)) {
			//ymFromId不为空 ，判断为拒绝后 二次提交，直接发起提交
			//将待办信息存入数据库
			//兼容5.0版本接口
			if(YMUrlStatic.YMVERSION.equals("5.0+")) {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("#{id}",ymFromId),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
			}else {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlow.replace("#{id}",ymFromId),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
			}
		}else {
			//新发起的流程信息
			count = this.tblSystemSheetTableMapper.selectCountFormFlow(fromId,flowId);
			if(count != 0) {
				return ResponseFormat.retParam(1, 200, null);
			}
			//兼容5.0版本接口
			if(YMUrlStatic.YMVERSION.equals("5.0+")) {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("/#{id}",""),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			}else {
				result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlow.replace("/#{id}",""),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			}
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		//获取到流程信息主键
//		String ymflowid = getYmFlowFormId("huaboformId",fromId+flowId);
		
		//兼容5.0版本接口
		String ymflowid = YMDifferentVConfig.getYmFlowFormId("huaboformId",fromId+flowId,reJson);
		
		//修改当前流程提交表单为审批中
		String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_SPZ+"' WHERE "+sheet.getPrimaryColumn()+" = '"+fromId+"'";
		String deptId = "";
			
			//获取流程发起所属部门主键
			if(StringUtils.isNotBlank(sheet.getDeptIdColumn())) {
				String orgSql = "SELECT "+sheet.getDeptIdColumn()+" FROM "+sheet.getTableName()+" WHERE "+sheet.getPrimaryColumn()+" = '"+fromId+"'";
				deptId = this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(orgSql);
			}else {
				deptId = loginStaff.getLinkDetp().getOrgid().toString();
			}
			if(count == 0){
				//保存新提交的流程新系至数据库中
				this.tblSystemSheetTableMapper.insertFormFlowTable(fromId,flowId,ymflowid,loginStaff.getStaffid(),deptId,sheet.getTableId(),loginStaff.getCurrentOrg().getOrgid());
			}
			this.tblSystemSheetTableMapper.executeSql(sql);
		//将待办信息存入数据库
		this.tblFlowTaskInfoService.insertSubmitInfo(ymflowid,loginStaff,sheet,fromId);
		return ResponseFormat.retParam(1, 200, null);
	}
	
	
	@Override
	public JsonBean delete(String token, String ymFromId, String flowId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		headerMap.put("content-type","application/json;charset=utf-8");
		
		String result = null;
		
		HashMap<String, Object> deleteMap = new HashMap<String,Object>(0);
		
		switch (YMUrlStatic.YMVERSION) {
		case "5.0+":
			result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.submitFlowNew.replace("#{id}", ymFromId),deleteMap,headerMap,loginStaff,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
			flowId = this.tblSystemFormFlowMapper.selectFlowIdByTaskId(ymFromId);
			break;
		default:
			result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.flowLaunch+"/"+ymFromId,deleteMap,headerMap,loginStaff,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
			break;
		}
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		BigDecimal formId = this.tblSystemSheetTableMapper.selectFormIdByYmFormId(ymFromId);//表单主键
		// 5.0 版本撤回重提交后 flowId 为实例 flowId，直接查可能为 null，用 formId 兜底回定义 flowId，避免空指针
		TblSystemSheetTable sheet = this.resolveSheet(flowId, null, formId, loginStaff.getCurrentOrg().getOrgid());
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}
		
		//退回表单审批状态
		String sql = "";
		if("XTXGQR".equals(sheet.getClassName())) {
			sql = "DELETE FROM "+sheet.getTableName()+" WHERE "+sheet.getPrimaryColumn() + " = '"+formId+"'";
		}else {
			sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro() + " = "+ YMUrlStatic.STATE_WSP+" WHERE "+sheet.getPrimaryColumn() + " = '"+formId+"'";
		}
		this.tblSystemSheetTableMapper.executeSql(sql);
		
		//删除工作流单据相关信息
		this.tblSystemSheetTableMapper.deleteYmFormInfo(ymFromId);
		this.tblFlowTaskInfoMapper.deleteInfosByProcessId(ymFromId);
		this.tblFlowInformInfoMapper.deleteInfoByProcessId(ymFromId);
		this.tblFlowApproverInfoMapper.deleteInfoByProcessId(ymFromId);
		this.tblFlowMessageMapper.deleteInfoByProcessId(ymFromId);
		this.tblSystemSheetTableMapper.deleteYmFormInfo(ymFromId);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getSystemFlowType(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		//查找当前公司拥有的权限模块
		List<String> tableTypeNameList = this.tblSystemSheetTableMapper.selectTableTypeNameList(loginStaff.getCurrentOrg().getOrgid());
		dataMap.put("tableTypeNameList", tableTypeNameList);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getSystemFlowList(String token, TblSystemSheetTable sheet) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		List<TblSystemSheetTable> list = this.tblSystemSheetTableMapper.selectSystemFlowList(sheet,loginStaff.getCurrentOrg().getOrgid());
		dataMap.put("list", list);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	
	@Override
	public JsonBean getWorkFlowList(String token, String workName, BigDecimal tableId, Integer currentPage, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
			PageInfo<TblSystemSheetTable> pageInfo = new PageInfo<TblSystemSheetTable>();
			pageInfo.setPageSize(pageSize);
			pageInfo.setCurrentPage(currentPage);

			IPage<TblSystemSheetTable> page = new Page<TblSystemSheetTable>(currentPage,pageSize,false);
			IPage<TblSystemSheetTable> pageList = this.tblSystemSheetTableMapper.selectWorkFlowList(tableId,loginStaff.getCurrentOrg().getOrgid(),workName,page);
			
			pageInfo.setTlist(pageList.getRecords());
			pageInfo.setTotalRecord((int)pageList.getTotal());
			dataMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getContractTypeFlowList(String token, String workName, BigDecimal tableId, BigDecimal typeId,
			Integer currentPage, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Page<TblContractTypeActivity> page = new Page<TblContractTypeActivity>(currentPage,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		QueryWrapper<TblContractTypeActivity> query = new QueryWrapper<TblContractTypeActivity>();
		query.eq("TABLEID", tableId);
		query.eq("TYPEID", typeId);
		query.eq("ORGID", loginStaff.getCurrentOrg().getOrgid());
		if(StringUtils.isNotBlank(workName)) {
			query.like("YMWORKNAME", workName);
		}
		
		IPage<TblContractTypeActivity> pageList = this.tblContractTypeActivityMapper.selectPage(page, query);
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		dataMap.put("pageInfo", pageList);
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	
	@Override
	public JsonBean saveWorkFlowFormInfo(String token, String workName, BigDecimal tableId, String ymWorkId, String typeId, Integer flowType) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		// flowType 默认标准流程，兼容旧版 流程平台 3.4.2（无类型概念）
		int safeFlowType = (flowType != null) ? flowType : 0;

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());

		String workId = null;
		//通过工作流名称获取流程平台的工作流信息， 然后比对名称得到Id;
		if(ymWorkId != null && !"".equals(ymWorkId)) {
			workId = ymWorkId;
		}else {
			//根据流程名称 获取所有符合的工作流程
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			filedMap.put("keyword",workName);
			String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getWorkFlowInfo,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);

			JSONObject dateJson = reJson.getJSONObject("data");

			JSONObject pagination = dateJson.getJSONObject("pagination");

			Integer totalCount = pagination.getInteger("total");
			if(totalCount == 0) {
				return ResponseFormat.retParam(0, 80001, null);
			}

			Integer pageSize = pagination.getInteger("total");
			Integer totalPage = totalCount/pageSize;
			if(totalCount%pageSize != 0) {
				totalPage++;
			}
			Integer currentPage = 1;
			JSONArray listJson = null;
			JSONObject workJson = null;
			boolean flag = false;
			//递归分页获取所有符合条件的流程，筛选出新建流程的主键
			do {
				if(currentPage != 1) {
					filedMap.put("currentPage",currentPage);
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getWorkFlowInfo,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
					reJson = JSONObject.parseObject(result);
					dateJson = reJson.getJSONObject("data");
				}
				listJson = dateJson.getJSONArray("list");
				for (int i = 0; i < listJson.size(); i++) {
					workJson = listJson.getJSONObject(i);
					if(workName.equals(workJson.getString("fullName"))) {
						flag = true;
						workId = workJson.getString("id");
						break;
					}
				}
				if(flag) {
					break;
				}
				currentPage++;
			} while (currentPage<totalPage);
		}
			//判断此新建流程是否已经存在当前公司的数据库中，如果存在则修改，不存在则新增；
		Integer	count = 0;
		if(StringUtils.isNotBlank(typeId) && !"NaN".equals(typeId)) {
			//合同类型 流程关联
			BigDecimal ctypeId = new BigDecimal(typeId);
			String activityId = this.tblContractTypeActivityMapper.selectTableYmFlowCount(ctypeId,workId,loginStaff.getCurrentOrg().getOrgid());
			TblContractTypeActivity act = new TblContractTypeActivity();
			act.setOrgId(loginStaff.getCurrentOrg().getOrgid());
			act.setTypeId(ctypeId);
			act.setYmWorkFrom(workId);
			act.setYmWorkName(workName);
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
				act.setTableId(tableId);
				this.tblContractTypeActivityMapper.insert(act);
			}
		}else {
			// tbl_system_sheettable 流程关联
			count = this.tblSystemSheetTableMapper.selectTableYmFlowCount(tableId,workId,loginStaff.getCurrentOrg().getOrgid());
			if(count > 0){
				//修改
				this.tblSystemSheetTableMapper.UpdateSystemYmWorkWithType(tableId,workId,loginStaff.getCurrentOrg().getOrgid(),workName,safeFlowType);
			}else {
				//新增
				this.tblSystemSheetTableMapper.InsertSystemYmWorkWithType(tableId,workId,loginStaff.getCurrentOrg().getOrgid(),workName,count++,safeFlowType);
			}
		}
		return ResponseFormat.retParam(200, 200, null);
	}
	
	
	@Override
	public JsonBean removeWorkFlowFormInfo(String token, BigDecimal tableId, String ymWorkId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}

		Integer	count = 0;

		// 5.0版本：ymWorkId 传入的是 flowtemplateId，selectWorkFlowFormCount 用 YMWORKFROM 查，需特殊处理
		// 3.x版本：ymWorkId 就是 ymWorkFrom，正常查
		if (!"5.0+".equals(YMUrlStatic.YMVERSION)) {
			count = this.tblSystemSheetTableMapper.selectWorkFlowFormCount(ymWorkId);
			if(count > 0){
				return ResponseFormat.retParam(0, "流程正在使用无法删除", null);
			}
		}

		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			Map<String, Object> dataMap = new HashMap<String, Object>(0);
			dataMap.put("tableId", tableId);
			dataMap.put("ymWorkId", ymWorkId);
			dataMap.put("orgId", loginStaff.getCurrentOrg().getOrgid());

			String flowName = this.tblSystemSheetTableMapper.selectYmWorkFlowName(tableId,ymWorkId,loginStaff.getCurrentOrg().getOrgid());
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(dataMap));
			confirm.setOperationMemo(flowName+"删除");
			confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
			confirm.setStatus(0);
			confirm.setTargetId(loginStaff.getCurrentOrg().getOrgid()+"-"+ymWorkId);
			confirm.setRecordText(flowName+"删除");
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEFLOW);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			//调用接口删除流程平台流程信息
			this.removeFlowInfo(ymWorkId);
			// 数据库中删除本地维护信息：5.0版本按 flowtemplateId 删，3.x按 ymWorkFrom 删
			if ("5.0+".equals(YMUrlStatic.YMVERSION)) {
				this.tblSystemSheetTableMapper.deleteTableYmFlowInfoByTemplateId(tableId, ymWorkId, loginStaff.getCurrentOrg().getOrgid());
			} else {
				this.tblSystemSheetTableMapper.deleteTableYmFlowInfo(tableId, ymWorkId, loginStaff.getCurrentOrg().getOrgid());
			}
			return ResponseFormat.retParam(1, 200, null);
		}
	}
	
	@Override
	public JsonBean removeWorkFlowContractType(String token, BigDecimal activityId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		
		TblContractTypeActivity act = this.tblContractTypeActivityMapper.selectById(activityId);
		
		Integer	count = this.tblSystemSheetTableMapper.selectWorkFlowFormCount(act.getYmWorkFrom());
		if(count > 0){
			return ResponseFormat.retParam(0, "流程正在使用无法删除", null);
		}
		
		if(SystemStaticValue.REQUIREMENTVALIDATE) {
			TblAuthorizationRecord confirm = new TblAuthorizationRecord();
			confirm.setRecordId(RandomUtil.uuStringId());
			confirm.setCreationTime(new Date());
			confirm.setCreator(loginStaff.getStaffid());
			confirm.setCreatorName(loginStaff.getRealname());
			confirm.setOperationData(JSONObject.toJSONString(act));
			confirm.setOperationMemo(act.getYmWorkName()+"删除");
			confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
			confirm.setStatus(0);
			confirm.setTargetId(act.getActivityId());
			confirm.setRecordText(act.getYmWorkName()+"删除");
			confirm.setTargetType(TblAuthorizationRecord.TARGETTYPECONTRACTFLOW);
			this.tblAuthorizationRecordMapper.insert(confirm);
			return ResponseFormat.retParam(1, 200, confirm);
		}else {
			//调用接口删除流程信息
			this.removeFlowInfo(act.getYmWorkFrom());
			//数据库中删除维护信息
			this.tblContractTypeActivityMapper.deleteById(activityId);
			return ResponseFormat.retParam(1, 200, null);
		}
		
		
	}
	
	
	@Override
	public JsonBean singleSignMothed(String token, String origin) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return DealUserToken.getYmTokenMothod(loginStaff,origin);
	}

	
	
	@Override
	public JsonBean synchronizeOrgInfo(String token, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		headerMap.put("Content-Type","application/json;charset=UTF-8");
		//同步所有公司信息到流程平台
		//1.公司主键为空，同步所有数据；公司主键不为空，同步当前传入公司主键的数据信息
		if(orgId != null) {
				//获取当前传入的公司信息；
				TblOrganization company = this.tblOrganizationMapper.selectCompanyByIdForYm(orgId);
				//同步至业务中台系统
				this.insertSynchronizeOrgInfo(company,headerMap,loginStaff);
				//重新查询 获取公司同步在业务中台中的主键信息
				company = this.tblOrganizationMapper.selectCompanyByIdForYm(orgId);
				//获取当前公司下的部门
				List<TblOrganization> deptList = this.tblOrganizationMapper.selectDeptListByOrgId(company.getOrgid());
				//调用方法将部门同步至业务中台系统中
				this.synchronizeDeptInfo(deptList,headerMap,company.getPkYmOrgId(),loginStaff);
		}else {
				//获取根级节点下所有的公司信息 同步至业务中台系统中
				List<TblOrganization> companyList = this.tblOrganizationMapper.selectCompanyListParentId(BigDecimal.valueOf(-1));
				this.insertSynchronizeOrgInfo(companyList,headerMap,null,loginStaff);
				List<TblOrganization> allComList = this.tblOrganizationMapper.selectAllCompanyList();
				List<TblOrganization> deptList = null;
				for (TblOrganization com : allComList) {
					deptList = this.tblOrganizationMapper.selectDeptListByOrgId(com.getOrgid());
					this.synchronizeDeptInfo(deptList,headerMap,com.getPkYmOrgId(),loginStaff);
				}
		}
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean synchronizeStaffInfo(String token, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("Content-Type","application/json;charset=UTF-8");
		String result = null;
		HashMap<String, Object> filedMap = null;
		List<List<String>> organizeIdsTree = new ArrayList<List<String>>(0);
		List<String> orgIdList = null;
		String pkYmStaffId = null;
		String pkYmOrgId = null;
		List<TblRole> listOrg = null;
		TblRole role = null;
		String roleNames = "";
		String ymZsStaffid = null;
			//获取当前组织在业务中台的主键
			pkYmOrgId = this.tblOrganizationMapper.selectYmPkOrgid(orgId);
			orgIdList = new ArrayList<String>(0);
			orgIdList.add(pkYmOrgId);
			organizeIdsTree.add(orgIdList);
			
			//获取当前组织下的所有人员
			List<TblStaff> staffList = this.tblStaffMapper.selectAllYmStaffList(orgId);
			//循环人员为每个人员处理角色
			for (TblStaff staff : staffList) {
				roleNames = "";
				if(staff.getChargeLeaderStaffId() != null) {
					ymZsStaffid = this.tblStaffMapper.selectbmfzrByStaffId(staff.getChargeLeaderStaffId());
				}
				if(null!=staff.getRoleIdStrs() && !"".equals(staff.getRoleIdStrs())) {
					listOrg = this.tblStaffMapper.selectListRoleInId(staff.getRoleIdStrs());
					if(null != listOrg) {
						for (int k = 0; k < listOrg.size(); k++) {
							role = listOrg.get(k);
							roleNames += role.getPkYmRoleId();
							if((listOrg.size()-1) > k) {
								roleNames += ",";
							}
						}
					}
				}
				filedMap = new HashMap<String,Object>(0);
				//判断此用户有没有同步过业务中台中
				if(staff.getPkYmStaffId() != null && !"".equals(staff.getPkYmStaffId())) {
					//修改
					//id
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("organizeId", pkYmOrgId);
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("sortCode", 0);
					filedMap.put("id", staff.getPkYmStaffId());
					filedMap.put("managerId", ymZsStaffid);
					//调用业务中台修改方法
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateStaffUrl+"/"+staff.getPkYmStaffId(),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}else {
					//新增
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("organizeId", pkYmOrgId);
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("positionId", staff.getJobName());
					filedMap.put("sortCode", 0);
					filedMap.put("managerId", ymZsStaffid);
					//调用业务中台新增方法
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveStaffUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					//获取同步后用户在业务中台的主键 并同步系统用户中
					pkYmStaffId = this.getYmPkStaffIdByStaffId(staff.getUsername());
					this.tblStaffMapper.updatePkYmStaffIdByStaffId(pkYmStaffId,staff.getStaffid());
					//清空参数 初始化用户密码
					filedMap.clear();
					filedMap.put("id", pkYmStaffId);
					filedMap.put("userPassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
					filedMap.put("validatePassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updatePasswordUrl.replace("#{id}", pkYmStaffId),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				}
			}
			//递归处理当前组织的其他的组织用户
			this.synchronizeDeptStaffInfo(orgId,pkYmOrgId,organizeIdsTree,headerMap,loginStaff,pkYmOrgId);
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public void synchronizeUniqueStaffInfo(TblStaffUtil loginStaff, BigDecimal staffId) throws Exception {
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("Content-Type","application/json;charset=UTF-8");
		HashMap<String, Object> filedMap = null;
		List<List<String>> organizeIdsTree = new ArrayList<List<String>>(0);
		List<String> orgIdList = new ArrayList<String>(0);
		String pkYmStaffId = null;
		String pkYmOrgId = null;
		List<TblRole> listOrg = null;
		TblRole role = null;
		String roleNames = "";
			TblStaff staff = this.tblStaffMapper.selectAllYmStaffInfo(staffId);
				if(null!=staff.getRoleIdStrs() && !"".equals(staff.getRoleIdStrs())) {
					listOrg = this.tblStaffMapper.selectListRoleInId(staff.getRoleIdStrs());
					if(null != listOrg) {
						for (int k = 0; k < listOrg.size(); k++) {
							role = listOrg.get(k);
							roleNames += role.getPkYmRoleId();
							if((listOrg.size()-1) > k) {
								roleNames += ",";
							}
						}
					}
				}
				orgIdList.add(pkYmOrgId);
				this.setYmOrgIdList(staff.getOrgid(),orgIdList);
				List<String> ymOrgIdList = null;
				int k = 0 ;
				int totalCount = orgIdList.size()-1;
				for (int j = 0; j <= totalCount ; j++) {
					ymOrgIdList = new ArrayList<String>(0);
					for (int i = 0; i <= k; i++) {
						ymOrgIdList.add(orgIdList.get(totalCount-i));
					}
					organizeIdsTree.add(ymOrgIdList);
					
				}
				//查询直属领导
				String ymZsStaffid = "";
				if(staff.getChargeLeaderStaffId() != null) {
					ymZsStaffid = this.tblStaffMapper.selectbmfzrByStaffId(staff.getChargeLeaderStaffId());
				}
				
				filedMap = new HashMap<String,Object>(0);
				if(staff.getPkYmStaffId() != null && !"".equals(staff.getPkYmStaffId())) {
					//修改
					//id
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("organizeId", pkYmOrgId);
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("sortCode", 0);
					filedMap.put("id", staff.getPkYmStaffId());
					filedMap.put("managerId", ymZsStaffid);
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateStaffUrl+"/"+staff.getPkYmStaffId(),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}else {
					//新增
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("organizeId", pkYmOrgId);
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("positionId", staff.getJobName());
					filedMap.put("sortCode", 0);
					filedMap.put("managerId", ymZsStaffid);
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveStaffUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					pkYmStaffId = this.getYmPkStaffIdByStaffId(staff.getUsername());
					this.tblStaffMapper.updatePkYmStaffIdByStaffId(pkYmStaffId,staff.getStaffid());
					filedMap.clear();
					filedMap.put("id", pkYmStaffId);
					filedMap.put("userPassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
					filedMap.put("validatePassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updatePasswordUrl.replace("#{id}", pkYmStaffId),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				}
	}
	
	
	private void setYmOrgIdList(BigDecimal orgid, List<String> orgIdList) throws Exception {
		TblOrganization father = this.tblOrganizationMapper.selectFatherOrgIdInfoByOrgId(orgid);
		orgIdList.add(father.getPkYmOrgId());
		if(father.getFatherorgid().intValue() != -1) {
			setYmOrgIdList(father.getFatherorgid(), orgIdList);
		}
	}

	public void synchronizeDeptStaffInfo(BigDecimal orgId, String pkYmOrgId,List<List<String>> organizeIdsTree, Map<String, String> headerMap, TblStaffUtil loginStaff, String pkYmComId) throws Exception {
		String orgYmIds = "";
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		List<String> orgIdList = null;
		String pkYmStaffId = null;
		List<TblOrganization> orgList = this.tblOrganizationMapper.selectDeptListByOrgId(orgId);
		List<TblRole> listOrg = null;
		TblRole role = null;
		String roleNames = "";
		List<List<String>> deptIdsTree = null;
		for (TblOrganization org : orgList) {
			deptIdsTree = new ArrayList<List<String>>(0);
			orgYmIds = org.getPkYmOrgId()+","+pkYmOrgId;
			List<TblStaff> staffList = this.tblStaffMapper.selectAllYmStaffList(org.getOrgid());
			orgIdList = new ArrayList<String>(0);
			String[] pkOrgYmIds = pkYmOrgId.split(",");
			for (String pkymId : pkOrgYmIds) {
				orgIdList.add(pkymId);
			}
			orgIdList.add(org.getPkYmOrgId());
			for (List<String> orgIdsTree : organizeIdsTree) {
				deptIdsTree.add(orgIdsTree);
			}
			deptIdsTree.add(orgIdList);
			for (TblStaff staff : staffList) {
				roleNames = "";
				if(null!=staff.getRoleIdStrs() && !"".equals(staff.getRoleIdStrs())) {
					
					listOrg = this.tblStaffMapper.selectListRoleInId(staff.getRoleIdStrs());
					if(null != listOrg) {
						for (int k = 0; k < listOrg.size(); k++) {
							role = listOrg.get(k);
							roleNames += role.getPkYmRoleId();
							if((listOrg.size()-1) > k) {
								roleNames += ",";
							}
						}
					}
				}
				
				if(staff.getPkYmStaffId() != null && !"".equals(staff.getPkYmStaffId())) {
					//修改
					//id
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", deptIdsTree);
					filedMap.put("organizeId", org.getPkYmOrgId());
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("sortCode", 0);
					filedMap.put("id", staff.getPkYmStaffId());
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateStaffUrl+"/"+staff.getPkYmStaffId(),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}else {
					//新增
					filedMap.put("account",staff.getUsername());
					filedMap.put("email", staff.getEmail());
					filedMap.put("enabledMark", 1);
					filedMap.put("gender", 3);
					filedMap.put("mobilePhone", staff.getMiblephone());
					filedMap.put("organizeIdsTree", deptIdsTree);
					filedMap.put("organizeId", org.getPkYmOrgId());
					filedMap.put("realName", staff.getRealname());
					filedMap.put("roleId", roleNames);
					filedMap.put("sortCode", 0);
					filedMap.put("positionId", staff.getJobName());
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveStaffUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					pkYmStaffId = this.getYmPkStaffIdByStaffId(staff.getUsername());
					this.tblStaffMapper.updatePkYmStaffIdByStaffId(pkYmStaffId,staff.getStaffid());
					filedMap.clear();
					filedMap.put("id", pkYmStaffId);
					filedMap.put("userPassword", YMDifferentVConfig.getYmLoginPwd());
					filedMap.put("validatePassword", YMDifferentVConfig.getYmLoginPwd());
					DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updatePasswordUrl.replace("#{id}", pkYmStaffId),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				}
			}
			this.synchronizeDeptStaffInfo(org.getOrgid(),orgYmIds,deptIdsTree,headerMap,loginStaff,pkYmComId);
		}
		
		
	}


	public String getYmPkStaffIdByStaffId(String username) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			
			YMParam param = new YMParam(username);
			String sql = YMDifferentVConfig.getUserFidByAccountSql(param);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}


	public String getYmPkStaffIdByStaffId2(String username) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		System.out.println("SELECT F_Id FROM base_user WHERE F_Account = '"+username+"'");
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_Id FROM base_user WHERE F_Account = '"+username+"'");
			rs = ps.executeQuery();

			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}

	@Override
	public JsonBean synchronizeRoleInfo(String token,BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("Content-Type","application/json;charset=utf-8");
		List<List<String>> organizeIdsTree = null;
		List<String> orgIdList = null;
		String pkYmRoleId = null;
		JSONObject reJson = null;
		JSONObject dataJson = null;
		
		//访问接口获取所有权限
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("moduleIds","");
		filedMap.put("type","system");
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONArray systemArray = null;
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		systemArray = dataJson.getJSONArray("all");
		
		
		filedMap.put("moduleIds","309228585019769285");
		filedMap.put("type","module");
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray moduleArray = dataJson.getJSONArray("all");
		String moduleStrIds = "";
		for (int i = 0 ; i < moduleArray.size() ; i++) {
			moduleStrIds += ","+moduleArray.get(i);
		}
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","button");
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray buttonArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","column");
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray columnArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","form");
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray formArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","resource");
		result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray resourceArray = dataJson.getJSONArray("all");
		
			List<TblRole> roleList = this.tblRoleMapper.selectAllRoleListToYM(orgId);
			for (TblRole role : roleList) {
				filedMap = new HashMap<String,Object>(0);
				organizeIdsTree = new ArrayList<List<String>>(0);
				/*orgIdList = new ArrayList<String>(0);
				orgIdList.add(role.getPkYmOrgId());
				organizeIdsTree.add(orgIdList);*/
				if(!"".equals(role.getPkYmRoleId()) && role.getPkYmRoleId() != null) {
					//修改
					filedMap.put("description",role.getRdesc());
					filedMap.put("enabledMark", role.getRstatus());
					filedMap.put("enCode", role.getRid());
					filedMap.put("fullName", role.getOrgName());
					filedMap.put("globalMark", 1);
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("sortCode", 0);
					filedMap.put("id", role.getPkYmRoleId());
					//调用业务中台修改角色信息方法
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateRoleUrl+"/"+role.getPkYmRoleId(),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
					filedMap = new HashMap<String,Object>(0);
					filedMap.put("objectType","Role");
					filedMap.put("module", moduleArray);
					filedMap.put("button", buttonArray);
					filedMap.put("column", columnArray);
					filedMap.put("form", formArray);
					filedMap.put("resource", resourceArray);
					filedMap.put("systemIds", systemArray);
					//对角色而进行授权
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.grantRoleRight.replace("#{id}", role.getPkYmRoleId()),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}else {
					//新增
					filedMap.put("description",role.getRdesc());
					filedMap.put("enabledMark", role.getRstatus());
					filedMap.put("enCode", role.getRid());
					filedMap.put("fullName", role.getOrgName());
					filedMap.put("globalMark", 1);
					filedMap.put("organizeIdsTree", organizeIdsTree);
					filedMap.put("sortCode", 0);
					//调用业务中台新增角色方法
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveRoleUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					//获取业务中台此角色的主键 回写至角色信息中
					pkYmRoleId = this.getYmPkRoleIdByRid(role.getPkYmOrgId(),role.getRid());
					this.tblRoleMapper.updatePkYmbyRoleId(pkYmRoleId,role.getRid());
					filedMap = new HashMap<String,Object>(0);
					filedMap.put("objectType","Role");
					filedMap.put("module", moduleArray);
					filedMap.put("button", buttonArray);
					filedMap.put("column", columnArray);
					filedMap.put("form", formArray);
					filedMap.put("resource", resourceArray);
					filedMap.put("systemIds", systemArray);
					//对新增角色进行授权
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.grantRoleRight.replace("#{id}", pkYmRoleId),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}
			}
		return ResponseFormat.retParam(1, 200, null);
	}
	
	@Override
	public JsonBean synchronizeJobInfo(String token, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
				
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("Content-Type","application/json;charset=utf-8");
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String result = null;
		String pkYmJobId = null;
			//获取当前组织下的所有岗位
			List<TblJob> jobList = this.tblJobDao.selectJobListByOrgId(orgId);
			for (TblJob tblJob : jobList) {
				//判断当前岗位是否同步至业务中台
				if (tblJob.getPkymJobId() != null) {
					filedMap.put("description", tblJob.getDescription());
					filedMap.put("enabledMark", 1);
					filedMap.put("enCode", tblJob.getJobid());
					filedMap.put("fullName", tblJob.getJobname());
					filedMap.put("id", tblJob.getPkymJobId());
					filedMap.put("organizeId", tblJob.getPkymOrgId());
					filedMap.put("sortCode", 0);
					filedMap.put("type", 1);
					//调用业务中台修改方法
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateJobUrl.replace("#{id}", tblJob.getPkymJobId()),filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
				}else {
					filedMap.put("description", tblJob.getDescription());
					filedMap.put("enabledMark", 1);
					filedMap.put("enCode", tblJob.getJobid());
					filedMap.put("fullName", tblJob.getJobname());
					filedMap.put("id", "");
					filedMap.put("organizeId", tblJob.getPkymOrgId());
					filedMap.put("sortCode", 0);
					filedMap.put("type", 1);
					//调用业务中台新增方法同步岗位
					result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveJobUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					//查询同步后业务中台的岗位主键，并回写至系统岗位信息中
					pkYmJobId = this.getYmPkRoleIdByRoleName(tblJob.getJobid(),tblJob.getPkymOrgId());
					this.tblJobDao.updatePkYmbyRoleId(pkYmJobId,tblJob.getJobid());
				}
			}
		return ResponseFormat.retParam(1, 200, null);
	}
	
	
	public String getYmPkRoleIdByRoleName(BigDecimal jobid, String pkymOrgId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			
			YMParam param = new YMParam(jobid,pkymOrgId);
			String sql = YMDifferentVConfig.getJobFidSql(param);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}

	/**
	 * 同步公司下的所有部门信息
	 * @param allComList
	 * @param headerMap
//	 * @param object
	 */
	public void synchronizeDeptInfo(List<TblOrganization> allComList, Map<String, String> headerMap,String fatherYmPk,TblStaffUtil loginStaff) throws Exception {
		List<TblOrganization> orgList = null;
		HashMap<String, Object> filedMap = null;
		String ymPkOrgId = null;
		//String manageId = null;
		for (TblOrganization com : allComList) {
			//manageId = this.tblStaffMapper.selectDeptManagerId("%,"+com.getOrgid()+",%");
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("fullName", com.getOrgname());
			filedMap.put("description", com.getMemo());
			filedMap.put("enCode", com.getOrgnumber());
			filedMap.put("enabledMark", 1);
			filedMap.put("parentId", fatherYmPk);
			/*filedMap.put("managerId", manageId);*/
			filedMap.put("sortCode", com.getOrderid());
			String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveDeptUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
			this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
			orgList = this.tblOrganizationMapper.selectDeptListByOrgId(com.getOrgid());
			if(orgList != null && orgList.size() != 0) {
				this.synchronizeDeptInfo(orgList,headerMap,ymPkOrgId,loginStaff);
			}
		}
	}

	@Override
	public void removeOrgInfo(TblStaffUtil loginStaff, String pkYmOrgId) throws Exception {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("Content-Type","application/json;charset=utf-8");
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl+"/"+pkYmOrgId,filedMap,headerMap,loginStaff,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
	}
	
	
	@Override
	public void insertSynchronizeOrgInfo(TblOrganization com, Map<String, String> headerMap,TblStaffUtil loginStaff) throws Exception {
		if(headerMap == null) {
			headerMap = new HashMap<String,String>(0);
			headerMap.put("Authorization",loginStaff.getYmToken()==null?"":loginStaff.getYmToken()); 
			headerMap.put("Content-Type","application/json;charset=UTF-8");
		}
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
			
		filedMap.put("fullName", com.getOrgname());
		filedMap.put("description", com.getMemo());
		filedMap.put("enCode", com.getOrgnumber());
		String fatherYmPk = null;
		//放入父级公司的业务中台的主键
		if(com.getFatherorgid().intValue() == -1) {
			filedMap.put("parentId", -1);
		}else {
			fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(com.getFatherorgid());
			filedMap.put("parentId", fatherYmPk);
		}
		filedMap.put("sortCode", com.getOrderid());
		filedMap.put("enabledMark", 1);
		//调用业务中台的新增方法 ，并将新增后业务中台的组织主键回写到组织信息表中 
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
		this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
	}
	
	
	public void insertSynchronizeOrgInfo(List<TblOrganization> companyList, Map<String, String> headerMap,String fatherYmPk,TblStaffUtil loginStaff) throws Exception {
		List<TblOrganization> orgList = null;
		HashMap<String, Object> filedMap = null;
		String ymPkOrgId = null;
		for (TblOrganization com : companyList) {
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("fullName", com.getOrgname());
			filedMap.put("description", com.getMemo());
			filedMap.put("enCode", com.getOrgnumber());
			if(fatherYmPk == null) {
				filedMap.put("parentId", -1);
			}else {
				filedMap.put("parentId", fatherYmPk);
			}
			filedMap.put("sortCode", com.getOrderid());
			filedMap.put("enabledMark", 1);
			String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			System.out.println(result);
			ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
			this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
			orgList = this.tblOrganizationMapper.selectCompanyListParentId(com.getOrgid());
			
			if(orgList != null && orgList.size() != 0) {
				this.insertSynchronizeOrgInfo(orgList,headerMap,ymPkOrgId,loginStaff);
			}
		}
		
		
	}
	public String getYmPkOrgIdByOrgName(String fatherId, String orgNumber, String orgName) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			YMParam param = new YMParam(fatherId,orgNumber,orgName);
			String sql = YMDifferentVConfig.getYmPkOrgIdByOrgNameSql(param);
			
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}
	
	public String getYmPkRoleIdByRid(String pkYmOrgId, BigDecimal rid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			YMParam param = new YMParam(rid);
			String sql = YMDifferentVConfig.getRoleIdSql(param);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}
	
	public String getYmFlowFormId(String keys, String fformid) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
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
		return fid;
	}
	
	@Override
	public void updateSynchronizeOrgInfo(TblOrganization com, Map<String, String> headerMap,TblStaffUtil loginStaff) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
			
		filedMap.put("fullName", com.getOrgname());
		filedMap.put("description", com.getMemo());
		filedMap.put("enCode", com.getOrgnumber());
		String fatherYmPk = null;
		if(com.getFatherorgid().intValue() == -1) {
			filedMap.put("parentId", -1);
		}else {
			fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(com.getFatherorgid());
			filedMap.put("parentId", fatherYmPk);
		}
		filedMap.put("sortCode", com.getOrderid());
		filedMap.put("enabledMark", 1);
		String ymOrgKey = this.tblOrganizationMapper.selectYmPkOrgid(com.getOrgid());
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl+"/"+ymOrgKey,filedMap,headerMap,loginStaff,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
		ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
		this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
	}
	
	
	@Override
	public void synchronizeUniqueDeptInfo(TblStaffUtil staff, BigDecimal orgid) throws Exception {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",staff.getYmToken()); 
		headerMap.put("Content-Type","application/json;charset=UTF-8");
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
		
			TblOrganization dept = this.tblOrganizationMapper.selectCompanyByIdForYm(orgid);
			String manageId = this.tblStaffMapper.selectDeptManagerId(dept.getOrgid());
			String fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(dept.getFatherorgid());
			if(dept.getPkYmOrgId() != null && !"".equals(dept.getPkYmOrgId())) {
				filedMap.put("fullName", dept.getOrgname());
				filedMap.put("description", dept.getMemo());
				filedMap.put("enCode", dept.getOrgnumber());
				filedMap.put("enabledMark", 1);
				filedMap.put("parentId", fatherYmPk);
				filedMap.put("sortCode", dept.getOrderid());
				filedMap.put("managerId", manageId);
				String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveDeptUrl+"/"+dept.getPkYmOrgId(),filedMap,headerMap,staff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			}else {
				filedMap.put("fullName", dept.getOrgname());
				filedMap.put("description", dept.getMemo());
				filedMap.put("enCode", dept.getOrgnumber());
				filedMap.put("enabledMark", 1);
				filedMap.put("parentId", fatherYmPk);
				filedMap.put("sortCode", dept.getOrderid());
				filedMap.put("managerId", manageId);
				String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveDeptUrl,filedMap,headerMap,staff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk, dept.getOrgnumber(),dept.getOrgname());
				this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,orgid);
			}
	}

	@Override
	public JsonBean synchronizeDeptManage(String token, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		headerMap.put("Content-Type","application/json;charset=UTF-8");
		//通过公司ID 获取公司 和 部门集合，调用方法同步部门负责人
			TblOrganization company = this.tblOrganizationMapper.selectCompanyByIdForYm(orgId);
			List<TblOrganization> deptList = this.tblOrganizationMapper.selectDeptListByOrgId(company.getOrgid());
			this.synchronizeDeptMangeInfo(deptList,headerMap,loginStaff);
		return ResponseFormat.retParam(1, 200, null);
	}

	private void synchronizeDeptMangeInfo(List<TblOrganization> allComList, Map<String, String> headerMap,
			TblStaffUtil loginStaff) throws Exception{
		List<TblOrganization> orgList = null;
		String manageId = null;
		//循环传入的部门集合，同步每个部门的部门负责人 ，通过递归处理二级 三级 等部门
		for (TblOrganization com : allComList) {
			manageId = this.tblStaffMapper.selectDeptManagerId(com.getOrgid());
			this.setYmOrgManageInfo(manageId,com.getPkYmOrgId());
			orgList = this.tblOrganizationMapper.selectDeptListByOrgId(com.getOrgid());
			if(orgList != null && orgList.size() != 0) {
				this.synchronizeDeptMangeInfo(orgList,headerMap,loginStaff);
			}
		}
	}

	public void setYmOrgManageInfo(String manageId, String pkYmOrgId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("UPDATE base_organize SET F_ManagerId = '"+manageId+"' WHERE F_Id = '"+pkYmOrgId+"'");
			ps.execute();
		}finally {
			BaseDao.getInstance().close(con,null,ps);
		}
	}

	@Override
	public JsonBean synchronizeStaffManageInfo(String token, BigDecimal orgId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		String ymZsStaffid = null;
			List<TblStaff> staffList = this.tblStaffMapper.selectAllYmStaffList(orgId);
			for (TblStaff staff : staffList) {
				ymZsStaffid = this.tblStaffMapper.selectbmfzrByOrgId(staff.getOrgid().toString());
				this.setStaffBmfzrInfo(ymZsStaffid,staff.getPkYmStaffId());
			}
			this.synchronizeManageStaffInfo(orgId);
		return ResponseFormat.retParam(1, 200, null);
	}
	
	private void synchronizeManageStaffInfo(BigDecimal orgId) throws Exception {
		List<TblOrganization> orgList = this.tblOrganizationMapper.selectDeptListByOrgId(orgId);
		List<TblStaff> staffList = null;
		String ymZsStaffid = null;
		for (TblOrganization tblOrganization : orgList) {
			staffList = this.tblStaffMapper.selectAllYmStaffList(tblOrganization.getOrgid());
			for (TblStaff staff : staffList) {
				ymZsStaffid = this.tblStaffMapper.selectbmfzrByOrgId(staff.getOrgid().toString());
				this.setStaffBmfzrInfo(ymZsStaffid,staff.getPkYmStaffId());
			}
			this.synchronizeManageStaffInfo(tblOrganization.getOrgid());
		}
	}

	private void setStaffBmfzrInfo(String ymZsStaffid, String pkYmStaffId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		String fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("UPDATE base_user SET F_ManagerId = '"+ymZsStaffid+"' WHERE F_Id = '"+pkYmStaffId+"'");
			ps.execute();
		}finally {
			BaseDao.getInstance().close(con,null,ps);
		}
	}
	
	@Override
	public JsonBean getListForId(String token,String processId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
	 
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
	 
//		if(flowName != null) {
//			filedMap.put("keyword",flowName);
//		}
		//pathValue = 1 时 查询我的待办
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workList+"1",filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		JSONArray arr=dataJson.getJSONArray("list");
		for(Object obj:arr){  //循环找到该流程表单待办数据
			JSONObject json=(JSONObject)obj;
			if(json.get("processId")!=null&&json.get("processId").toString().equals(processId)){
				return this.getInfo(token, json.get("id").toString(), json.get("thisStepId").toString(), json.get("processId").toString(), json.get("flowId").toString(),"3");
			}
		} 
		return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public JsonBean copyApprovalStaffList(String token, String processId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		
		//获取需要抄送的流程审批人员
		List<TblStaff> staffList = this.tblStaffMapper.selectCopyStaffList(processId);
		
		dataMap.put("staffList", staffList);
		return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public JsonBean informInfoList(String token, Integer currentPage, Integer pageSize, String informStaffName,
			Integer isRead, String id, String flowId, String createStaffName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		if(currentPage == null) {
			currentPage = 1;
		}
		if(pageSize == null) {
			pageSize = 15;
		}
		
		PageInfo<TblFlowInformInfo> pageInfo = new PageInfo<TblFlowInformInfo>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPageSize(pageSize);
		
		TblFlowInformInfo inform = new TblFlowInformInfo();
		inform.setId(id);
		inform.setFlowId(flowId);
		inform.setIsRead(isRead);
		inform.setInformStaffName(informStaffName);
		inform.setCreateStaffName(createStaffName);
		pageInfo.setCondition(inform);
		
		Page<TblFlowInformInfo> page = new Page<TblFlowInformInfo>(currentPage,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblFlowInformInfo> pageList = this.tblFlowInformInfoMapper.selectPageListByFlowInfo(page,inform);
		
		//查询流程知会信息列表分页
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		
	
		return ResponseFormat.retParam(1, 200, pageInfo);
	}

	@Override
	public JsonBean getFlowTaskInfo(String token, String tableId, String formId, String typeName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		String flowId = null;
		BigDecimal typeId = null;
		if(StringUtils.isNotBlank(typeName)) {
			typeId = this.tblContractTypeActivityMapper.selectTypeIdByTypeName(typeName);
			flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
			if(StringUtils.isBlank(flowId)) {
				//查找当前合同类型上级有没有绑定流程
				typeId = this.tblContractTypeActivityMapper.selectFatherTypeIdByTypeId(typeId);
				flowId = this.tblContractTypeActivityMapper.selectYmFlowIdByTypeId(tableId,typeId,loginStaff.getCurrentOrg().getOrgid());
			}
			if(StringUtils.isBlank(flowId)) {
				//合同类型 没有绑定流程，则执行流程设计里面的流程
				flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
			}
		}else {
			//通过模块主键 和公司获取到 当前需要调用的流程主键
			if(StringUtils.isNotBlank(tableId)) {
				flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId,loginStaff.getCurrentOrg().getOrgid());
			}
		}
		
		//获取流程主键
		flowId = this.tblSystemSheetTableMapper.selectFlowIdQyByTableId(tableId, loginStaff.getCurrentOrg().getOrgid());
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(flowId == null) {
			resultMap.put("isFlowInfo", 0);//有流程信息不可以提交审批
			return ResponseFormat.retParam(0, "无权操作", resultMap);
		}
		
		//获取流程实例主键
		String id = this.tblSystemSheetTableMapper.selectYmFormIdByFlowId(formId,flowId);
		
		if(id != null && !"".equals(id)) {
			resultMap.put("flowId", flowId);
			resultMap.put("id", id);
			resultMap.put("isFlowInfo", 1);//有流程信息可以提交审批
		}else {
			resultMap.put("isFlowInfo", 0);//有流程信息不可以提交审批
		}
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean fileUpload(MultipartFile[] file, String token, String flowTaskOperatorId, String flowTaskId) throws Exception {
		String attPath = "";
        TblYmFlowRecordAtt att = new TblYmFlowRecordAtt();
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        for (MultipartFile multipartFile : file) {
            try {
            	//上传附件
                InputStream inputStream = multipartFile.getInputStream();
                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
                attPath = FtpUtil.uploadFlowFilePath(imageName + fileName.substring(fileName.indexOf("."), fileName.length()), inputStream);
                if (StrUtil.isEmpty(attPath)) {
                	return ResponseFormat.retParam(0, "文件上传失败", null);
                }
                //tblAttachmentEntity.setAttid(new BigDecimal(snowflakeIdWorker.nextId()));
                String ftpPath = attPath;
                att.setAttpath(imageName + fileName.substring(fileName.indexOf("."), fileName.length()));
                att.setAttsize(Double.valueOf(multipartFile.getSize()/1024));
                att.setUploadtime(new Date());
                att.setUploader(loginStaff.getStaffid());
                att.setAttname(fileName);
                att.setAttpath(attPath);
                att.setFlowtaskid(flowTaskId);
                att.setOperatorid(flowTaskOperatorId);
                //存储附件信息
                att.setAttid(RandomUtil.uuLongId());
                tblYmFlowRecordAttMapper.insertEntity(att);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseFormat.retParam(0, "文件上传失败", null);
            }
        }
        //返回当前添加的文件 前端回显
        return ResponseFormat.retParam(1,200, att);
	}
	
	@Value("${file.upload.path}")
    private String filePath;
    @Value("${file.ca.secret}")
    private String caSecret;
    @Value("${file.previewUrl}")
    private String previewUrl;
    @Value("${file.downloadUrl}")
    private String downloadUrl;
	
	@Override
	public JsonBean fileuploadZH(MultipartFile[] file, String token, String flowTaskOperatorId, String flowTaskId) throws Exception {
		List<FileUploadRes> fileUploads = new ArrayList<>();
		String fileFullPath;
		boolean isCa = true; 
        TblYmFlowRecordAtt att = new TblYmFlowRecordAtt();
        TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        for (MultipartFile multipartFile : file) {
            try {
            	 long id = RandomUtil.uuLongId();
//            	BigDecimal id = RandomUtil.uuBigDecimalId(); 
                att.setAttid(id);
                
                try {
                    String fileName = new String(Objects.requireNonNull(multipartFile.getOriginalFilename()).getBytes()).replace(".enc","");
                    fileFullPath = filePath + "/" + DateUtil.today() + "/" + id;
                    File dest = new File(fileFullPath);
                    if (!dest.getParentFile().exists()) {
                        if (!dest.getParentFile().mkdirs()) {
//                            log.info("创建目录:[{}]失败", fileFullPath);
                            throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
                        }
                    }
                    String fileId = String.valueOf(id);
                    String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
                    att.setJmurl(encodedPart);
                    saveFileLocal(isCa, multipartFile, dest);

                    att.setFlowtaskid(flowTaskId);
                    att.setOperatorid(flowTaskOperatorId);
                    att.setAttname(fileName);
                    att.setAttpath(fileFullPath);
                    double size = (double) multipartFile.getSize() / 1024;
                    // 明确单位为 KiB（二进制千字节）
                    att.setAttsize(size); 
                    att.setUploadtime(new Date());
                    att.setUploader(loginStaff.getStaffid());
//                    this.save(att);
                    //存储附件信息
                    tblYmFlowRecordAttMapper.insertEntity(att);
                    
                    fileUploads.add(new FileUploadRes(fileId, fileName, fileFullPath, size, loginStaff.getRealname(),
                            DateUtil.format(att.getUploadtime(), "yyyy-MM-dd HH:mm:ss"),
                            getPreviewUrl(fileId, fileName), "1")
                    ); 
                } catch (Exception e) {
//                    log.error("文件上传失败", e);
                    throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
                }
            	
//            	//上传附件
//                InputStream inputStream = multipartFile.getInputStream();
//                long imageName = snowflakeIdWorker.nextId(); //雪花算法生成图片名称
//                String fileName = new String(multipartFile.getOriginalFilename().getBytes()); //重新编码
//                attPath = FtpUtil.uploadFlowFilePath(imageName + fileName.substring(fileName.indexOf("."), fileName.length()), inputStream);
//                if (StrUtil.isEmpty(attPath)) {
//                	return ResponseFormat.retParam(0, "文件上传失败", null);
//                }
//                String ftpPath = attPath;
//                att.setAttpath(imageName + fileName.substring(fileName.indexOf("."), fileName.length()));
//                att.setAttsize(Double.valueOf(multipartFile.getSize()/1024));
//                att.setUploadtime(new Date());
//                att.setUploader(loginStaff.getStaffid());
//                att.setAttname(fileName);
//                att.setAttpath(attPath);
//                att.setFlowtaskid(flowTaskId);
//                att.setOperatorid(flowTaskOperatorId);
//                //存储附件信息
//                att.setAttid(RandomUtil.uuBigDecimalId());
//                tblYmFlowRecordAttMapper.insertEntity(att);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseFormat.retParam(0, "文件上传失败", null);
            }
        }
        //返回当前添加的文件 前端回显
        return ResponseFormat.retParam(1,200, fileUploads);
	}
	
	private void saveFileLocal(Boolean isCa, MultipartFile multipartFile, File dest) {
        if (isCa) {
            try (InputStream inputStream = multipartFile.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                // 读取输入流到字节数组
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
                // 获取字节数组
                byte[] fileBytes = byteArrayOutputStream.toByteArray();
                // 加密字节数组
                String encrypt = AESUtil.encrypt(fileBytes, caSecret);
                FileUtil.writeString(encrypt, dest, StandardCharsets.UTF_8);
            } catch (Exception e) {
//                log.error("文件加密保存失败", e);
                throw new FileException(ErrorCodeEnum.FILE_CA_SAVE_FAIL);
            }
            return;
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            FileUtil.writeFromStream(inputStream, dest);
        } catch (Exception e) {
//            log.error("文件上传失败(写入本地目录:[{}]失败)", dest.getPath(), e);
            throw new FileException(ErrorCodeEnum.FILE_UP_FAIL);
        }
    }
	
	/**
     * 提取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名（如 .docx）
     */
    public static String extractExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        // 找到最后一个 '.' 的位置
        int lastDotIndex = fileName.lastIndexOf('.');

        // 如果没有找到 '.', 返回空字符串
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return "";
        }

        // 截取从 '.' 到字符串末尾的部分
        return fileName.substring(lastDotIndex);
    }
    
    /**
     * 获取预览地址
     *
     * @param fileId 文件id
     */
    public String getPreviewUrl(String fileId, String fileName) {
        // 空值检查
        if (StrUtil.isEmpty(previewUrl) || StrUtil.isEmpty(downloadUrl) || StrUtil.isEmpty(fileId)) {
            return "";
        }
        // 将 downloadUrl 和 fileId 拼接并编码
        String encodedPart = Base64Utils.encodeToString((downloadUrl + fileId + "&fullfilename=" + fileId+extractExtension(fileName)).getBytes(StandardCharsets.UTF_8));
        return previewUrl + encodedPart;
    }

	@Override
	public JsonBean fildDownload(String token, String attId, HttpServletResponse response) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			 return ResponseFormat.retParam(0, 20006, null);
		}else {
			TblYmFlowRecordAtt tblAttachmentEntity = tblYmFlowRecordAttMapper.selectEntityById(attId);
	        if (tblAttachmentEntity == null) {
	        	return ResponseFormat.retParam(0, "附件不存在或已删除", null);
	        }else {
	        	com.hbfk.entity.TblAttachment tblAttachment = new com.hbfk.entity.TblAttachment();
		        tblAttachment.setAttname(tblAttachmentEntity.getAttname());
		        tblAttachment.setAttpath(tblAttachmentEntity.getAttpath());
		        tblAttachment.setFileName(tblAttachmentEntity.getAttname());
		        tblAttachment.setAttsize(tblAttachmentEntity.getAttsize());
		        FtpUtil.downUploadFlowFile(tblAttachment, response);
		        return ResponseFormat.retParam(1,200, null);
	        }
		}
    	
		
	}
	
	
	public void fileDownLoadZH(HttpServletResponse response, String attId, Boolean isCa) throws Exception{
		TblYmFlowRecordAtt file = tblYmFlowRecordAttMapper.selectEntityById(attId);
        if (file == null) {
            log.error("文件不存在");
            throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST);
        }
        File fileBase = new File(file.getAttpath());
        if (!fileBase.exists()) {
            throw new FileException(ErrorCodeEnum.FILE_NOT_EXIST_LOCAL);
        }
        String fileName = file.getAttname();
        String encodedFileName = fileName;
        try { 
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            // 替换空格，防止编码后的 "+" 号变成空格
            encodedFileName = encodedFileName.replaceAll("\\+", "%20");
        } catch (Exception e) {
        	System.out.println();
            log.error("文件名字encode失败", e);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", new StrBuilder("attachment; filename=")
                .append("\"").append(encodedFileName).append("\"").toString());
        if (isCa && file.getIsEncrypted()) {

            try (BufferedReader reader = new BufferedReader(new FileReader(fileBase));
                 ServletOutputStream outputStream = response.getOutputStream()) {

                String base64Line;

                while ((base64Line = reader.readLine()) != null) {
                    // 解密每一行 Base64 数据
                    byte[] decryptedData = AESUtil.decrypt(base64Line, caSecret);
                    // 写入到响应流
                    outputStream.write(decryptedData);
                }

                outputStream.flush();
            } catch (Exception e) {
                log.error("文件下载失败", e);
                throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
            }
        } else {
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(fileBase.toPath()));
                 ServletOutputStream outputStream = response.getOutputStream()) {

                // 直接复制字节流，避免逐行读取和编码转换
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (Exception e) {
                log.error("文件下载失败", e);
                throw new FileException(ErrorCodeEnum.FILE_WRITE_RESP_FAIL);
            }
        }

    }

	@Override
	public JsonBean fileRemove(String token, String attId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
    	
		TblYmFlowRecordAtt tblAttachmentEntity = tblYmFlowRecordAttMapper.selectEntityById(attId);
        if (tblAttachmentEntity == null) {
        	return ResponseFormat.retParam(0, "附件不存在或已删除", null);
        }
        FtpUtil.removeFile(tblAttachmentEntity.getAttpath(),FtpUtil.Xmlfilepath);
        this.tblYmFlowRecordAttMapper.deleteEntityById(attId);
        return ResponseFormat.retParam(1,200, null);
	}

	@Override
	public JsonBean fileList(String token, String flowTaskOperatorId, String flowTaskId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if(loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		List<TblYmFlowRecordAtt> attList = this.tblYmFlowRecordAttMapper.selectAllList(flowTaskOperatorId,flowTaskId);
		
		return ResponseFormat.retParam(1,200, attList);
	}

	@Override
	public void dealUniqueRoleInfo(BigDecimal rid) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					this.dealRole501Version(rid);
					break;
				default:
					this.dealRole342Version(rid);
					break;
			}
			
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	

	@Override
	public void removeRoleInfo(TblStaffUtil loginStaff, String pkYmRoleId) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateRoleUrl+"/"+pkYmRoleId,filedMap,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	@Override
	public void dealUniqueJobInfo(BigDecimal jobId) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
			String result = null;
			String pkYmJobId = null;
				TblJob job = this.tblJobDao.selectJobInfoByJobId(jobId);
					if (job.getPkymJobId() != null) {
						filedMap.put("description", job.getDescription());
						filedMap.put("enabledMark", 1);
						filedMap.put("enCode", job.getJobid());
						filedMap.put("fullName", job.getJobname());
						filedMap.put("id", job.getPkymJobId());
						filedMap.put("organizeId", job.getPkymOrgId());
						filedMap.put("sortCode", 0);
						filedMap.put("type", 1);
						result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateJobUrl.replace("#{id}", job.getPkymJobId()),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
					}else {
						filedMap.put("description", job.getDescription());
						filedMap.put("enabledMark", 1);
						filedMap.put("enCode", job.getJobid());
						filedMap.put("fullName", job.getJobname());
						filedMap.put("id", "");
						filedMap.put("organizeId", job.getPkymOrgId());
						filedMap.put("sortCode", 0);
						filedMap.put("type", 1);
						result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveJobUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
						pkYmJobId = this.getYmPkRoleIdByRoleName(job.getJobid(),job.getPkymOrgId());
						this.tblJobDao.updatePkYmbyRoleId(pkYmJobId,job.getJobid());
					}
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	@Override
	public void removeUniqueJobInfo(BigDecimal jobid) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			String result = null;
			String jobPkYmId = "";
				TblJob job = this.tblJobDao.selectJobInfoByJobId(jobid);
				jobPkYmId = job.getPkymJobId();
			
			if(jobPkYmId != null && !"".equals(jobPkYmId)) {
				result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateJobUrl.replace("#{id}", jobPkYmId),null,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
			}
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	
	@Override
	public void dealUniqueOrgInfo(TblOrganization org, Integer orgDeal) throws Exception {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			switch (orgDeal) {
				case 1:
						this.insertUniqueOrgInfo(org);
					break;
				case 2:
						this.updateUniqueOrgInfo(org);
					break;
				case 3:
						this.deleteUniqueOrgInfo(org.getPkYmOrgId());
					break;
				case 4:
						this.insertUniqueDeptInfo(org);
					break;
				case 5:
						this.updateUniqueDeptInfo(org);
					break;
				case 6:
						this.deleteUniqueOrgInfo(org.getPkYmOrgId());
					break;
				default:
					break;
			}
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
		
	private void updateUniqueDeptInfo(TblOrganization org) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String manageId = this.tblStaffMapper.selectDeptManagerId(org.getOrgid());
		String fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(org.getFatherorgid());
		TblOrganization company = this.tblOrganizationMapper.selectCompanyInfoByDeptId(org.getOrgid());
		filedMap.put("fullName", org.getOrgname());
		filedMap.put("description", org.getMemo());
		filedMap.put("enCode", company.getOrgnumber()+"-"+org.getOrgnumber());
		filedMap.put("enabledMark", 1);
		filedMap.put("parentId", fatherYmPk);
		filedMap.put("sortCode", org.getOrderid());
		filedMap.put("managerId", manageId);
		if(StringUtils.isNotBlank(org.getOrganizationTrees())) {
			List<String> ymOrgIdTress = this.tblOrganizationMapper.selectYmPkOrgIdTrees(org.getOrganizationTrees().substring(0, org.getOrganizationTrees().lastIndexOf(",")));
			filedMap.put("organizeIdTree",ymOrgIdTress.toArray(new String[0]));
		}
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveDeptUrl+"/"+org.getPkYmOrgId(),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
	}

	private void insertUniqueDeptInfo(TblOrganization org) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
		String manageId = this.tblStaffMapper.selectDeptManagerId(org.getOrgid());
		String fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(org.getFatherorgid());
		TblOrganization company = this.tblOrganizationMapper.selectCompanyInfoByDeptId(org.getOrgid());
		filedMap.put("fullName", org.getOrgname());
		filedMap.put("description", org.getMemo());
		filedMap.put("enCode", company.getOrgnumber()+"-"+org.getOrgnumber());
		filedMap.put("enabledMark", 1);
		filedMap.put("parentId", fatherYmPk);
		filedMap.put("sortCode", org.getOrderid());
		filedMap.put("managerId", manageId);
		if(StringUtils.isNotBlank(org.getOrganizationTrees())) {
			List<String> ymOrgIdTress = this.tblOrganizationMapper.selectYmPkOrgIdTrees(org.getOrganizationTrees().substring(0, org.getOrganizationTrees().lastIndexOf(",")));
			filedMap.put("organizeIdTree",ymOrgIdTress.toArray(new String[0]));
		}
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveDeptUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk, company.getOrgnumber()+"-"+org.getOrgnumber(),org.getOrgname());
		this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,org.getOrgid());
	}

	public void deleteUniqueOrgInfo(String pkYmOrgId) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl+"/"+pkYmOrgId,filedMap,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,HttpClient.PARAMBODY);
	}

	public void updateUniqueOrgInfo(TblOrganization com) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
		
		com.setPkYmOrgId(this.tblOrganizationMapper.selectYmPkOrgid(com.getOrgid()));
		filedMap.put("fullName", com.getOrgname());
		filedMap.put("description", com.getMemo());
		filedMap.put("enCode", com.getOrgnumber());
		String fatherYmPk = null;
		if(com.getFatherorgid().intValue() == -1) {
			filedMap.put("parentId", -1);
		}else {
			fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(com.getFatherorgid());
			filedMap.put("parentId", fatherYmPk);
		}
		
		if(StringUtils.isNotBlank(com.getOrganizationTrees())) {
			List<String> ymOrgIdTress = this.tblOrganizationMapper.selectYmPkOrgIdTrees(com.getOrganizationTrees().substring(0, com.getOrganizationTrees().lastIndexOf(",")));
			filedMap.put("organizeIdTree",ymOrgIdTress.toArray(new String[0]));
		}
		
		filedMap.put("sortCode", com.getOrderid());
		filedMap.put("enabledMark", 1);
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl+"/"+com.getPkYmOrgId(),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
		ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
		this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
	}
	
	public void insertUniqueOrgInfo(TblOrganization com) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String ymPkOrgId = null;
			
		filedMap.put("fullName", com.getOrgname());
		filedMap.put("description", com.getMemo());
		filedMap.put("enCode", com.getOrgnumber());
		String fatherYmPk = null;
		if(com.getFatherorgid().intValue() == -1) {
			filedMap.put("parentId", -1);
		}else {
			fatherYmPk = this.tblOrganizationMapper.selectYmPkOrgid(com.getFatherorgid());
			filedMap.put("parentId", fatherYmPk);
		}
		if(StringUtils.isNotBlank(com.getOrganizationTrees())) {
			List<String> ymOrgIdTress = this.tblOrganizationMapper.selectYmPkOrgIdTrees(com.getOrganizationTrees().substring(0, com.getOrganizationTrees().lastIndexOf(",")));
			filedMap.put("organizeIdTree",ymOrgIdTress.toArray(new String[0]));
		}
		
		filedMap.put("sortCode", com.getOrderid());
		filedMap.put("enabledMark", 1);
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveOrgUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		ymPkOrgId = this.getYmPkOrgIdByOrgName(fatherYmPk,com.getOrgnumber(),com.getOrgname());
		this.tblOrganizationMapper.updateYmPkOrgid(ymPkOrgId,com.getOrgid());
		
	}

	@Override
	public void dealSetLoginOrgInfo(String pkYmOrgId, TblStaffUtil staffUtil) throws Exception {
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			YMUrlStatic.headerMap.put("Authorization",staffUtil.getYmToken()); 
			filedMap.put("majorId", pkYmOrgId);
			filedMap.put("majorType", "Organize");
			String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.changeOrgUrl,filedMap,YMUrlStatic.headerMap,staffUtil,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public void dealUniqueStaffInfo(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			String result = null;
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			HashMap<String, Object> filedMap = null;
			List<List<String>> organizeIdsTree = new ArrayList<List<String>>(0);
			String pkYmStaffId = null;
			List<TblRole> listOrg = null;
			TblRole role = null;
			String roleNames = "";
			
				TblStaff staff = this.tblStaffMapper.selectAllYmStaffInfo(staffId);
					if(null!=staff.getRoleIdStrs() && !"".equals(staff.getRoleIdStrs())) {
						listOrg = this.tblStaffMapper.selectListRoleInId(staff.getRoleIdStrs());
						if(null != listOrg) {
							for (int k = 0; k < listOrg.size(); k++) {
								role = listOrg.get(k);
								roleNames += role.getPkYmRoleId();
								if((listOrg.size()-1) > k) {
									roleNames += ",";
								}
							}
						}
					}
					List<String> ymOrgIdList = null;
					String[] ymOrgIds=null;
					String majorYmOrgId = "";
					for (TblUserOrgRelation rela :  relaList) {
						ymOrgIds = rela.getOrgYmStrIds().split(",");
						ymOrgIdList = new ArrayList<String>(0);
						for (int i = 0 ; i < ymOrgIds.length ; i ++) {
							ymOrgIdList.add(ymOrgIds[i]);
						}
						majorYmOrgId += ymOrgIds[ymOrgIds.length-1]+",";
						organizeIdsTree.add(ymOrgIdList);
					}
					majorYmOrgId = majorYmOrgId.substring(0, majorYmOrgId.length()-1);
					//查询直属领导
					String ymZsStaffid = "";
					if(staff.getChargeLeaderStaffId() != null) {
						ymZsStaffid = this.tblStaffMapper.selectbmfzrByStaffId(staff.getChargeLeaderStaffId());
					}
					filedMap = new HashMap<String,Object>(0);
					if(staff.getPkYmStaffId() != null && !"".equals(staff.getPkYmStaffId())) {
						//修改
						//id
						filedMap.put("account",staff.getUsername());
						filedMap.put("email", staff.getEmail());
						filedMap.put("enabledMark", 1);
						filedMap.put("gender", 3);
						filedMap.put("mobilePhone", staff.getMiblephone());
						filedMap.put("organizeIdsTree", organizeIdsTree);
						filedMap.put("organizeId", majorYmOrgId);
						filedMap.put("realName", staff.getRealname());
						filedMap.put("roleId", roleNames);
						filedMap.put("sortCode", 0);
						filedMap.put("id", staff.getPkYmStaffId());
						filedMap.put("managerId", ymZsStaffid);
						result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateStaffUrl+"/"+staff.getPkYmStaffId(),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
					}else {
						//新增
						filedMap.put("account",staff.getUsername());
						filedMap.put("email", staff.getEmail());
						filedMap.put("enabledMark", 1);
						filedMap.put("gender", 3);
						filedMap.put("mobilePhone", staff.getMiblephone());
						filedMap.put("organizeIdsTree", organizeIdsTree);
						filedMap.put("organizeId", majorYmOrgId);
						filedMap.put("realName", staff.getRealname());
						filedMap.put("roleId", roleNames);
						filedMap.put("positionId", staff.getJobName());
						filedMap.put("sortCode", 0);
						filedMap.put("managerId", ymZsStaffid);
						result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveStaffUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
						pkYmStaffId = this.getYmPkStaffIdByStaffId(staff.getUsername());
						if(pkYmStaffId == null || "".equals(pkYmStaffId)) {
							pkYmStaffId = this.getYmPkStaffIdByStaffId2(staff.getUsername());
						}
						this.tblStaffMapper.updatePkYmStaffIdByStaffId(pkYmStaffId,staff.getStaffid());
						filedMap.clear();
						filedMap.put("id", pkYmStaffId);
						filedMap.put("userPassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
						filedMap.put("validatePassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
						DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updatePasswordUrl.replace("#{id}", pkYmStaffId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
					}
					System.out.println("业务中台用户信息同步结果："+result);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public void dealUniqueStaffInfo2(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getJedis();
			String result = null;
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken);
			HashMap<String, Object> filedMap = null;
			List<List<String>> organizeIdsTree = new ArrayList<List<String>>(0);
			String pkYmStaffId = null;
			List<TblRole> listOrg = null;
			TblRole role = null;
			String roleNames = "";

			TblStaff staff = this.tblStaffMapper.selectAllYmStaffInfo(staffId);
			if(null!=staff.getRoleIdStrs() && !"".equals(staff.getRoleIdStrs())) {
				listOrg = this.tblStaffMapper.selectListRoleInId(staff.getRoleIdStrs());
				if(null != listOrg) {
					for (int k = 0; k < listOrg.size(); k++) {
						role = listOrg.get(k);
						roleNames += role.getPkYmRoleId();
						if((listOrg.size()-1) > k) {
							roleNames += ",";
						}
					}
				}
			}
			List<String> ymOrgIdList = null;
			String[] ymOrgIds=null;
			String majorYmOrgId = "";
			for (TblUserOrgRelation rela :  relaList) {
				ymOrgIds = rela.getOrgYmStrIds().split(",");
				ymOrgIdList = new ArrayList<String>(0);
				for (int i = 0 ; i < ymOrgIds.length ; i ++) {
					ymOrgIdList.add(ymOrgIds[i]);
				}
				majorYmOrgId += ymOrgIds[ymOrgIds.length-1]+",";
				organizeIdsTree.add(ymOrgIdList);
			}
			majorYmOrgId = majorYmOrgId.substring(0, majorYmOrgId.length()-1);
			//查询直属领导
			String ymZsStaffid = "";
			if(staff.getChargeLeaderStaffId() != null) {
				ymZsStaffid = this.tblStaffMapper.selectbmfzrByStaffId(staff.getChargeLeaderStaffId());
			}
			filedMap = new HashMap<String,Object>(0);
			if(staff.getPkYmStaffId() != null && !"".equals(staff.getPkYmStaffId())) {
				System.out.println("业务中台已有该用户！");
//				//修改
//				//id
//				filedMap.put("account",staff.getUsername());
//				filedMap.put("email", staff.getEmail());
//				filedMap.put("enabledMark", 1);
//				filedMap.put("gender", 3);
//				filedMap.put("mobilePhone", staff.getMiblephone());
//				filedMap.put("organizeIdsTree", organizeIdsTree);
//				filedMap.put("organizeId", majorYmOrgId);
//				filedMap.put("realName", staff.getRealname());
//				filedMap.put("roleId", roleNames);
//				filedMap.put("sortCode", 0);
//				filedMap.put("id", staff.getPkYmStaffId());
//				filedMap.put("managerId", ymZsStaffid);
//				result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateStaffUrl+"/"+staff.getPkYmStaffId(),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
			}else {
				//新增
				filedMap.put("account",staff.getUsername());
				filedMap.put("email", staff.getEmail());
				filedMap.put("enabledMark", 1);
				filedMap.put("gender", 3);
				filedMap.put("mobilePhone", staff.getMiblephone());
				filedMap.put("organizeIdsTree", organizeIdsTree);
				filedMap.put("organizeId", majorYmOrgId);
				filedMap.put("realName", staff.getRealname());
				filedMap.put("roleId", roleNames);
				filedMap.put("positionId", staff.getJobName());
				filedMap.put("sortCode", 0);
				filedMap.put("managerId", ymZsStaffid);
				result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveStaffUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				System.out.println("业务中台用户信息同步结果："+result);
				pkYmStaffId = this.getYmPkStaffIdByStaffId(staff.getUsername());
				if(pkYmStaffId == null || "".equals(pkYmStaffId)) {
					pkYmStaffId = this.getYmPkStaffIdByStaffId2(staff.getUsername());
				}
				if (pkYmStaffId != null){
					this.tblStaffMapper.updatePkYmStaffIdByStaffId(pkYmStaffId,staff.getStaffid());
				filedMap.clear();
				filedMap.put("id", pkYmStaffId);
				filedMap.put("userPassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
				filedMap.put("validatePassword", YMMd5Util.getStringMd5(YMUrlStatic.ymPassword).toLowerCase());
				DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updatePasswordUrl.replace("#{id}", pkYmStaffId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				}
			}
			System.out.println("业务中台用户信息同步结果："+result);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	@Override
	public JsonBean getFlowPkInfo(String token, String tableId, String formId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
			TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFormId(tableId,formId,loginStaff.getCurrentOrg().getOrgid());
			dataMap.put("id", sheet.getYmWorkFrom());//流程实例主键
			dataMap.put("flowId", sheet.getFlowId());//流程信息主键
		return ResponseFormat.retParam(1, 200, dataMap);
	}
	
	@Override
	public JsonBean getFlowMessage(String token) throws Exception {
		Jedis jedis = null;
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			jedis = JedisUtil.getJedis();
			String message = jedis.get(loginStaff.getStaffid()+JedisUtil.SUBMITMESSAGE);
			
			if(message != null && !"".equals(message)) {
				messageList = JSONObject.parseArray(message, FlowMessageVo.class);
			}
			
			jedis.del(loginStaff.getStaffid()+JedisUtil.SUBMITMESSAGE);
		} finally {
			JedisUtil.returnResource(jedis);
		}
		
		return ResponseFormat.retParam(1, 200, messageList);
	}
	
	@Override
	public JsonBean press(String token, String id, String flowId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		Jedis jedis = null; 
		try {
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			Map<String, String> headerMap = new HashMap<>();
			headerMap.put("Authorization",loginStaff.getYmToken()); 
			
			String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workInfo+"/"+id,null,headerMap,loginStaff,HttpClient.HTTPGET,null);
			JSONObject reJson = JSONObject.parseObject(result);
			Integer code = reJson.getInteger("code");
			if(code != 200) {
				String msg = reJson.getString("msg");
				return ResponseFormat.retParam(0, msg, null);
			}
			JSONObject flowDataJson = reJson.getJSONObject("data");
			
			
			JSONObject taskInfoJson = flowDataJson.getJSONObject("flowTaskInfo");
			JSONArray flowTaskNodeList = flowDataJson.getJSONArray("flowTaskNodeList");
			String thisStepId = taskInfoJson.getString("thisStepId");
			String taskTitle = taskInfoJson.getString("fullName");
			String processId = taskInfoJson.getString("processId");
			JSONObject taskObject = null;
			
			
			String[] thisStepIds = thisStepId.split(",");
			String[] approvalUsers = null;
			String nextApproval = null;
			String[] operators = null;
			String[] userInfo = new String[2];
			String account = null;
			TblStaff approvalStaff = null;
			String pkYmStaffId = null;
			FlowMessageVo messagevo = null;
			List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
			
			TblFlowMessage message = null;
			jedis = JedisUtil.getJedis();
			
			for (String stepId : thisStepIds) {
				for (int i = 0 ; i < flowTaskNodeList.size() ; i++) {
					taskObject = flowTaskNodeList.getJSONObject(i);
					if(stepId.equals(taskObject.getString("nodeCode"))) {
						nextApproval = taskObject.getString("userName");
						break;
					}
				}
				
				String submitMessage = null;
				approvalUsers = nextApproval.split(",");
				for (String appUserInfo : approvalUsers) {
					userInfo = appUserInfo.split("/");
					account = userInfo[1];
					pkYmStaffId =  this.getYmPkStaffIdByAccount(account);
					approvalStaff = tblStaffMapper.selectUserName(account);
					operators = this.getOperatorInfo(pkYmStaffId,processId,stepId);
					message = new TblFlowMessage();
					message.setCreateTime(new Date());
					message.setFlowId(flowId);
					message.setFlowTaskId(id);
					message.setIsRead(0);
					message.setMessageTiel(taskTitle);
					message.setOperatorId(operators[0]);
					message.setProcessId(processId);
					message.setTaskNodeId(operators[1]);
					message.setThisStepId(thisStepId);
					message.setSponsor(loginStaff.getStaffid());
					message.setRecipient(approvalStaff.getStaffid());
					message.setMessageId(RandomUtil.uuBigDecimalId());
					this.tblFlowMessageMapper.insertReturnId(message);
					
					submitMessage = jedis.get(approvalStaff.getStaffid() + JedisUtil.PRESSKEY);
					if(submitMessage != null && !"".equals(submitMessage)) {
						messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
					}
					messagevo = new FlowMessageVo();
					messagevo.setMuessage(taskTitle+"已被催办。");
					messagevo.setDoType(1);
					messagevo.setFlowUrl("/ymWrok/getInfo");
					messagevo.setFlowId(flowId);
					messagevo.setId(operators[0]);
					messagevo.setProcessId(processId);
					messagevo.setThisStepId(operators[1]);
					messagevo.setOperatorId(operators[0]);
					messageList.add(messagevo);
					jedis.set(approvalStaff.getStaffid() + JedisUtil.PRESSKEY, JSONObject.toJSONString(messageList));
				}
				
			}
			return ResponseFormat.retParam(1, "催办成功", null);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
		
	}
	
	@Override
	public JsonBean loginGetPress(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		FlowMessageVo messagevo = null;
		List<FlowMessageVo> messageVoList = new ArrayList<FlowMessageVo>(0);
		
		List<TblFlowMessage> messageList = this.tblFlowMessageMapper.selectPressInfoList(loginStaff.getStaffid());
		
		for (TblFlowMessage message : messageList) {
			messagevo = new FlowMessageVo();
			messagevo.setMuessage(message.getMessageTiel()+"已被催办。");
			messagevo.setDoType(1);
			messagevo.setFlowUrl("/ymWrok/getInfo");
			messagevo.setFlowId(message.getFlowId());
			messagevo.setId(message.getOperatorId());
			messagevo.setProcessId(message.getProcessId());
			messagevo.setThisStepId(message.getTaskNodeId());
			messagevo.setOperatorId(message.getOperatorId());
			messageVoList.add(messagevo);
			
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("messageList", messageVoList);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean getPressInfo(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		Jedis jedis = null; 
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		
		try {
			if (loginStaff == null) {
			    return ResponseFormat.retParam(0, 20006, null);
			}
			
			
			jedis = JedisUtil.getJedis();
			String submitMessage = jedis.get(loginStaff.getStaffid() + JedisUtil.PRESSKEY);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			jedis.del(loginStaff.getStaffid() + JedisUtil.PRESSKEY);
			Map<String,Object> resultMap = new HashMap<String,Object>(0);
			resultMap.put("messageList", messageList);
			
			return ResponseFormat.retParam(1, 200, resultMap);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@Override
	public void dealUserRoleRelation(String roleid) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			String result = null;
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
				List<String> ymStaffList = this.tblStaffMapper.selectYmPkStaffIdByRoleId(roleid);
				String ymRoleId = this.tblRoleMapper.selectYmRoleIdByRoleId(roleid);
				filedMap.put("objectId", ymRoleId);
				filedMap.put("objectType", "Role");
				filedMap.put("userIds", ymStaffList);
				result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveRoleUserUrl.replace("#{id}", ymRoleId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
				System.out.println(result);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	
	private String getYmPkStaffIdByAccount(String account) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_Id FROM base_user WHERE F_Account = '"+account+"' LIMIT 0,1");
			rs = ps.executeQuery();
			while (rs.next()) {
				fid = rs.getString("F_Id");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}
	
	private String[] getOperatorInfo(String pkYmStaffId, String processId, String thisStepId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] opeatorS = new String[2];
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_ID,F_TaskNodeId FROM flow_taskoperator where F_NodeCode = '"+thisStepId+"' AND F_TaskId = '"+processId+"' AND F_HandleId = '"+pkYmStaffId+"' ORDER BY F_CreatorTime DESC LIMIT 0,1");
			rs = ps.executeQuery();
			while (rs.next()) {
				opeatorS[0] = rs.getString("F_Id");
				opeatorS[1] = rs.getString("F_TaskNodeId");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return opeatorS;
	}

	@Override
	public JsonBean saveFlowTemplate(String token, String taskNodeId, String flowId, String tempTitle, String tempMemo)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblFlowTemplate temp = new TblFlowTemplate();
		temp.setFlowId(flowId);
		temp.setTaskNodeId(taskNodeId);
		temp.setTempTitle(tempTitle);
		temp.setTempMemo(tempMemo);
		temp.setLinkStaff(loginStaff.getStaffid());
		temp.setTempId(RandomUtil.uuBigDecimalId());
		this.tblFlowTemplateMapper.insertReturnId(temp);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean modifyFlowTemplate(String token, String tempTitle, String tempMemo, BigDecimal tempId)
			throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblFlowTemplate temp = new TblFlowTemplate();
		temp.setTempTitle(tempTitle);
		temp.setTempMemo(tempMemo);
		temp.setTempId(tempId);
		this.tblFlowTemplateMapper.updateEntity(temp);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean removeFlowTemplate(String token, BigDecimal tempId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		this.tblFlowTemplateMapper.deleteEntity(tempId);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public JsonBean getFlowTemplateList(String token, String taskNodeId, String flowId, Integer currentPage,
			Integer pageSize, String tempTitle, String tempMemo) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		PageInfo<TblFlowTemplate> pageInfo = new PageInfo<TblFlowTemplate>();
		if(currentPage == null) {
			currentPage = 1;
		}
		if(pageSize == null) {
			pageSize = 5;
		}
		
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPageSize(pageSize);
		
		TblFlowTemplate temp = new TblFlowTemplate();
		temp.setTempTitle(tempTitle);
		temp.setTempMemo(tempMemo);
		temp.setLinkStaff(loginStaff.getStaffid());
		temp.setFlowId(flowId);
		temp.setTaskNodeId(taskNodeId);
		
		pageInfo.setCondition(temp);
		
		Page<TblFlowTemplate> page = new Page<TblFlowTemplate>(currentPage,pageSize);
		page.setOptimizeCountSql(false); // 禁用自动优化
		IPage<TblFlowTemplate> pageList = this.tblFlowTemplateMapper.selectListByPageInfo(page,temp);
		pageInfo.setTlist(pageList.getRecords());
		pageInfo.setTotalRecord((int)pageList.getTotal());
		
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("pageInfo", pageInfo);
		return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getFlowTemplateInfo(String token, BigDecimal tempId) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		TblFlowTemplate temp = this.tblFlowTemplateMapper.findById(tempId);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		resultMap.put("temp", temp);
		
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	private Integer getTaskStatusByProcessId(String processId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer taskStatus = 0;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_Status FROM flow_task WHERE F_ProcessId = '"+processId+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				taskStatus = rs.getInt("F_Status");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return taskStatus;
	}

	@Override
	public JsonBean paikeSingLogin(String token) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		 
		 long timestamp = System.currentTimeMillis();
	     String sign = Md5util.stringToMD5(SystemStaticValue.PKMD5KEY + SystemStaticValue.PKUSER + timestamp);
		 
		 Map<String,Object> resultMap = new HashMap<String,Object>(0);
		 resultMap.put("time", timestamp);
		 resultMap.put("sign", sign);
		 resultMap.put("name", SystemStaticValue.PKUSER);
		 resultMap.put("loginUrl", SystemStaticValue.PKLOGINURL);
		 return ResponseFormat.retParam(1, 200, resultMap);
	}

	@Override
	public JsonBean getFLowInfo(String id) throws Exception {
		
		TblSystemFormFlow formFlow = this.tblSystemFormFlowMapper.selectByYmformId(id);
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		TblSystemSheetTable sheet = null;
		if(formFlow!=null) {
			if(formFlow.getTableId() != null){
				sheet = this.tblSystemSheetTableMapper.selectByPK(formFlow.getTableId());
			}else {
				// 5.0 版本撤回重提交后 flowId 可能为实例 flowId，用 formId 兜底回定义 flowId，避免空指针
				BigDecimal defFormId = null;
				if(formFlow.getFormId() != null && !"".equals(formFlow.getFormId())) {
					defFormId = new BigDecimal(formFlow.getFormId());
				}
				sheet = this.resolveSheet(formFlow.getFlowId(), null, defFormId, null);
			}
			resultMap.put("formId", formFlow.getFormId());
		}else {
			sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(id,null);
		}
		if (sheet == null) {
			return ResponseFormat.retParam(0, "未找到流程对应的表单配置，请联系管理员", null);
		}
		resultMap.put("dataType", sheet.getClassName());
		resultMap.put("tableType", sheet.getTableType());
		 return ResponseFormat.retParam(1, 200, resultMap); 
	}

	@Override
	public JsonBean batchList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("currentPage",currentPage);
		filedMap.put("pageSize",pageSize);
		filedMap.put("sort", "desc");
		if(flowName != null) {
			filedMap.put("keyword",flowName);
		}
		// 5.0+：批量审批列表查待办 /api/workflow/operator/List/1（category=1，signTime NOT NULL AND startHandleTime IS NULL）
		// 3.x：已处理列表用 /api/workflow/Engine/FlowBefore/List/4
		String ymListUrl = "5.0+".equals(YMUrlStatic.YMVERSION)
				? YMUrlStatic.interfaceUrl + YMUrlStatic.operatorListUrl + "1"
				: YMUrlStatic.interfaceUrl + YMUrlStatic.workList + "4";
		String result = DealUserToken.dealYmUrlMethod(ymListUrl, filedMap, headerMap, loginStaff, HttpClient.HTTPGET, null);
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
	public JsonBean batchCandidate(String token, String flowId, String id) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken()); 
		
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("flowId",flowId);
		// 5.0+ 参数名为 operatorId，3.x 为 taskOperatorId
		if("5.0+".equals(YMUrlStatic.YMVERSION)) {
			filedMap.put("operatorId", id);
		} else {
			filedMap.put("taskOperatorId", id);
		}

		/**
	     * 1.有分支 //2.没有分支有候选人 //3.没有分支也没有候选人
	     */

		// 5.0+：/api/workflow/operator/BatchCandidate
		// 3.x ：/api/workflow/Engine/FlowBefore/BatchCandidate
		String batchCandidateUrl = "5.0+".equals(YMUrlStatic.YMVERSION)
				? YMUrlStatic.batchCandidateUrlNew
				: YMUrlStatic.batchCandidateUrl;
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+batchCandidateUrl,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		return ResponseFormat.retParam(1, 200, reJson.getJSONObject("data"));
	}

	@Override
	public JsonBean batchCandidateUser(String token, String id, String nodeCode,String flowId, Integer currentPage, Integer pageSize) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		Map<String,Object> dataMap = new HashMap<String,Object>(0);
		//封装参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> formMap = new HashMap<String,Object>(0);
		formMap.put("data", "{}");
		formMap.put("flowId", flowId);
		
		HashMap<String, Object> formDataMap = new HashMap<String,Object>(0);
		formDataMap.put("formData",formMap);
		formDataMap.put("currentPage",1);
		formDataMap.put("pageSize",100);
		formDataMap.put("nodeCode",nodeCode);
		
		//获取流程候选人接口
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getCandidateUser.replace("#{id}", id),formDataMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		JSONObject dataJson = reJson.getJSONObject("data");
		JSONArray pageArray = dataJson.getJSONArray("list");
		Integer total = 1;
		
		JSONObject pageJson = dataJson.getJSONObject("pagination");
		/*dataMap.put("currentPage", pageJson.getInteger("currentPage"));
		dataMap.put("pageSize", pageJson.getInteger("pageSize"));
		total = pageJson.getInteger("total");*/
		
		if(loginStaff.getCurrentOrg().getUseSecrect() != null && loginStaff.getCurrentOrg().getUseSecrect() == 1) {
			if(pageArray == null || pageArray.size() == 0) {
				dataMap.put("list", pageArray);
			}else {
				//获取审批单据信息
				TblSystemSheetTable sheet = this.tblSystemSheetTableMapper.selectEntityByProcessId(id);
				if(StringUtils.isBlank(sheet.getSecrectColumn())) {
					dataMap.put("list", pageArray);
					dataMap.put("currentPage", 1);
					dataMap.put("pageSize", 100);
					dataMap.put("totalPage", 1);
					return ResponseFormat.retParam(1, 200, dataMap);
				}
				//获取审批单据的密级范围与知悉范围人员
				String secSql = "SELECT SECRECYSTAFFSCOPE FROM TBL_SECRECT_LEVEL WHERE LEVELID = (SELECT "+sheet.getSecrectColumn()+" FROM " +sheet.getTableName()+ " WHERE "+sheet.getPrimaryColumn()+" = '"+sheet.getFormId()+"') ";
				String secrectId = this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(secSql);
				
				if(StringUtils.isBlank(secrectId)) {
					dataMap.put("list", pageArray);
					dataMap.put("currentPage", 1);
					dataMap.put("pageSize", 100);
					dataMap.put("totalPage", 1);
					return ResponseFormat.retParam(1, 200, dataMap);
				}
				
				//获取所有用户的pkStaffId;
				List<String> pkYmStaffIdList = IntStream.range(0, pageArray.size()).mapToObj(pageArray::getJSONObject).map(obj -> obj.getString("id")).collect(Collectors.toList());
				
				String scoSql = "SELECT "+sheet.getStaffScopeColumn()+" FROM " +sheet.getTableName()+ " WHERE "+sheet.getPrimaryColumn()+" = '"+sheet.getFormId()+"' ";
				String scopeStaffIds = this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(scoSql);
				//通过以上三个条件查询出符合条件的候选人
				List<String> rePkStaffIdList = this.tblStaffMapper.selectFlowHxrBySecrect(pkYmStaffIdList,secrectId,scopeStaffIds);
				JSONObject sob = null;
				JSONArray midArray = new JSONArray();
				for (int i = 0; i < pageArray.size(); i++) {
					sob = pageArray.getJSONObject(i);
					for (String pkymId : rePkStaffIdList) {
						if(pkymId.equals(sob.getString("id"))) {
							midArray.add(sob);
							break;
						}
					}
				}
				pageArray = midArray;
			}
		}
		dataMap.put("list", pageArray);
		/*Integer totalPage = total/pageSize;
		if(total%pageSize != 0) {
			totalPage++;
		}
		dataMap.put("totalCount", total);*/
		dataMap.put("currentPage", 1);
		dataMap.put("pageSize", 100);
		dataMap.put("totalPage", 1);
		
		return ResponseFormat.retParam(1, 200, dataMap);
	}

	@Override
	public JsonBean batchOperation(String token, FlowModel flowModel) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
		    return ResponseFormat.retParam(0, 20006, null);
		}
		
		
		//封装参数
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization",loginStaff.getYmToken());
		headerMap.put("content-type","application/json;charset=utf-8");
		
		HashMap<String, Object> formDataMap = new HashMap<String,Object>(0);
		
		formDataMap.put("batchType", flowModel.getBatchType());
		formDataMap.put("branchList", flowModel.getBranchList());
		formDataMap.put("candidateType", flowModel.getCandidateType());
		formDataMap.put("enCode", flowModel.getEnCode());
		formDataMap.put("freeApproverUserId", flowModel.getPkYmStaffId());
		formDataMap.put("handleOpinion", flowModel.getHandleOpinion());
		formDataMap.put("ids", flowModel.getIds());
		formDataMap.put("signImg", flowModel.getSignImg());
		formDataMap.put("candidateList", flowModel.getCandidateList());
		formDataMap.put("copyIds",flowModel.getCopyIds());
		
		//调用流程审批通过接口
		// 5.0+：/api/workflow/operator/BatchOperation
		// 3.x ：/api/workflow/Engine/FlowBefore/BatchOperation
		String batchOperationUrl = "5.0+".equals(YMUrlStatic.YMVERSION)
				? YMUrlStatic.batchOperationUrlNew
				: YMUrlStatic.batchOperationUrl;

		// 5.0+：批量审批前先调 Transact（开始办理），将 startHandleTime 设置上
		//   流程平台 checkOperator 要求 startHandleTime != null，否则报 WF088"经办未开始办理"
		//   Transact 接口幂等，已经开始办理的记录不会重复更新
		if ("5.0+".equals(YMUrlStatic.YMVERSION)) {
			HashMap<String, Object> transactMap = new HashMap<>(2);
			transactMap.put("ids", flowModel.getIds());
			String transactResult = DealUserToken.dealYmUrlMethod(
					YMUrlStatic.interfaceUrl + YMUrlStatic.flowTransactUrl,
					transactMap, headerMap, loginStaff, HttpClient.HTTPPOST, HttpClient.PARAMBODY);
			JSONObject transactJson = JSONObject.parseObject(transactResult);
			Integer transactCode = transactJson.getInteger("code");
			if (transactCode != null && transactCode != 200) {
				String transactMsg = transactJson.getString("msg");
				log.warn("batchOperation: Transact 开始办理失败，msg={}, ids={}", transactMsg, flowModel.getIds());
				// Transact 失败不阻断，继续尝试批量审批（部分数据可能已经开始办理）
			}
		}
		String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+batchOperationUrl,formDataMap,headerMap,loginStaff,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		
		JSONObject reJson = JSONObject.parseObject(result);
		Integer code = reJson.getInteger("code");
		if(code != 200) {
			String msg = reJson.getString("msg");
			return ResponseFormat.retParam(0, msg, null);
		}
		
		//处理流程待办、办理信息
		tblFlowTaskInfoService.insertBatchOperationInfo(loginStaff,flowModel);
		
		return ResponseFormat.retParam(1, 200, null);
	}

	@Override
	public void dealUserRoleRelationUniqueRight(String roleid, String staffids) throws Exception {
		Jedis jedis = null;
		
		try {
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			
			String result = null;
			HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
				List<String> ymStaffList = this.tblStaffMapper.selectYmPkStaffIdByStaffIds(staffids);
				String ymRoleId = this.tblRoleMapper.selectYmRoleIdByRoleId(roleid);
				if(StringUtils.isBlank(ymRoleId)) {
					return;
				}
				YMUrlStatic.headerMap.put("Authorization",adminToken); 
				filedMap.put("objectId", ymRoleId);
				filedMap.put("objectType", "Role");
				filedMap.put("userIds", String.join(",", ymStaffList));
				result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveRoleUserUrl.replace("#{id}", ymRoleId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}
	
	
	//业务中台3.4.2版本处理角色信息
	private void dealRole342Version(BigDecimal rid) throws Exception {
		List<List<String>> organizeIdsTree = null;
		String pkYmRoleId = null;
		JSONObject reJson = null;
		JSONObject dataJson = null;
		
		//访问接口获取所有权限
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		filedMap.put("moduleIds","");
		filedMap.put("type","system");
		String result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		JSONArray systemArray = null;
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		systemArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds","309228585019769285");
		filedMap.put("type","module");
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray moduleArray = dataJson.getJSONArray("all");
		String moduleStrIds = "";
		for (int i = 0 ; i < moduleArray.size() ; i++) {
			moduleStrIds += ","+moduleArray.get(i);
		}
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","button");
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray buttonArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","column");
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray columnArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","form");
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray formArray = dataJson.getJSONArray("all");
		
		filedMap.put("moduleIds",moduleStrIds);
		filedMap.put("type","resource");
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.authorityAllUrl.replace("#{id}", "0"),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		reJson = JSONObject.parseObject(result);
		dataJson = reJson.getJSONObject("data");
		JSONArray resourceArray = dataJson.getJSONArray("all");
		
		TblRole role = this.tblRoleMapper.selectAllRoleInfoToYM(rid);
		filedMap = new HashMap<String,Object>(0);
		organizeIdsTree = new ArrayList<List<String>>(0);
		/*orgIdList = new ArrayList<String>(0);
		orgIdList.add(role.getPkYmOrgId());
		organizeIdsTree.add(orgIdList);*/
		if(!"".equals(role.getPkYmRoleId()) && role.getPkYmRoleId() != null) {
			//修改
			this.updateYMRoleInfo(filedMap,role,organizeIdsTree,result);
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("objectType","Role");
			filedMap.put("module", moduleArray);
			filedMap.put("button", buttonArray);
			filedMap.put("column", columnArray);
			filedMap.put("form", formArray);
			filedMap.put("resource", resourceArray);
			filedMap.put("systemIds", systemArray);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.grantRoleRight.replace("#{id}", role.getPkYmRoleId()),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
		}else {
			//新增
			pkYmRoleId = this.createYMRoleInfo(filedMap,role,organizeIdsTree,result);
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("objectType","Role");
			filedMap.put("module", moduleArray);
			filedMap.put("button", buttonArray);
			filedMap.put("column", columnArray);
			filedMap.put("form", formArray);
			filedMap.put("resource", resourceArray);
			filedMap.put("systemIds", systemArray);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.grantRoleRight.replace("#{id}", pkYmRoleId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
		}
	}

	//业务中台5.0.1版本处理角色信息
	private void dealRole501Version(BigDecimal rid) throws Exception {
		List<List<String>> organizeIdsTree = new ArrayList<List<String>>(0);
		HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
		String result = null;
		String pkYmRoleId = null;
		
		TblRole role = this.tblRoleMapper.selectAllRoleInfoToYM(rid);
		if(!"".equals(role.getPkYmRoleId()) && role.getPkYmRoleId() != null) {
			//修改
			this.updateYMRoleInfo(filedMap,role,organizeIdsTree,result);
		}else {
			//新增
			JSONObject reJson = null;
			JSONObject dataJson = null;
			JSONObject memberJson = null;
			List<String> ids = new ArrayList<String>(0);
			
			
			pkYmRoleId = this.createYMRoleInfo(filedMap,role,organizeIdsTree,result);
			filedMap = new HashMap<String,Object>(0);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.premissionGroupMember.replace("#{id}", YMUrlStatic.premissionGroupId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPGET,null);
			reJson = JSONObject.parseObject(result);
			dataJson = reJson.getJSONObject("data");
			JSONArray listJson = dataJson.getJSONArray("list");
			for (int i = 0 ; i < listJson.size() ; i++) {
				memberJson = listJson.getJSONObject(i);
				ids.add(memberJson.getString("id")+"--"+memberJson.getString("type"));
			}
			ids.add(pkYmRoleId+"--role");
			
			filedMap = new HashMap<String,Object>(0);
			filedMap.put("id", YMUrlStatic.premissionGroupId);
			filedMap.put("ids", ids);
			result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.premissionGroupMember.replace("#{id}", YMUrlStatic.premissionGroupId),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
			System.out.println(result);
		}
	}
	
	
	private String createYMRoleInfo(HashMap<String, Object> filedMap,TblRole role,List<List<String>> organizeIdsTree,String result) throws Exception {
		filedMap.put("description",role.getRdesc());
		filedMap.put("enabledMark", role.getRstatus());
		filedMap.put("enCode", role.getRid());
		filedMap.put("fullName", role.getRname());
		filedMap.put("globalMark", 1);
		filedMap.put("organizeIdsTree", organizeIdsTree);
		filedMap.put("sortCode", 0);
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.saveRoleUrl,filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPOST,HttpClient.PARAMBODY);
		String pkYmRoleId = this.getYmPkRoleIdByRid(role.getPkYmOrgId(),role.getRid());
		this.tblRoleMapper.updatePkYmbyRoleId(pkYmRoleId,role.getRid());
		return pkYmRoleId;
	}
	
	private void updateYMRoleInfo(HashMap<String, Object> filedMap,TblRole role,List<List<String>> organizeIdsTree,String result) throws Exception {
		filedMap.put("description",role.getRdesc());
		filedMap.put("enabledMark", role.getRstatus());
		filedMap.put("enCode", role.getRid());
		filedMap.put("fullName", role.getRname());
		filedMap.put("globalMark", 1);
		filedMap.put("organizeIdsTree", organizeIdsTree);
		filedMap.put("sortCode", 0);
		filedMap.put("id", role.getPkYmRoleId());
		result = DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.updateRoleUrl+"/"+role.getPkYmRoleId(),filedMap,YMUrlStatic.headerMap,HttpClient.HTTPPUT,HttpClient.PARAMBODY);
	}
	
	@Override
	public void removeFlowInfo(String ymWorkId) throws Exception {
		Jedis jedis = null;
		try {
			
			jedis = JedisUtil.getJedis();
			String adminToken = jedis.get(YMUrlStatic.YMSPACKEY);
			if(adminToken == null || "".equals(adminToken)) {
				adminToken = DealUserToken.getYmTokenMothod(YMUrlStatic.SUPERACCOUNT);
			}
			YMUrlStatic.headerMap.put("Authorization",adminToken); 
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.dealFlowBaseInfo+"/"+ymWorkId,null,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,null);
					break;
				default:
					DealUserToken.dealYmUniqueMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.getWorkFlowInfo+"/"+ymWorkId,null,YMUrlStatic.headerMap,HttpClient.HPPTDELETE,null);
					break;
			}
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

}
