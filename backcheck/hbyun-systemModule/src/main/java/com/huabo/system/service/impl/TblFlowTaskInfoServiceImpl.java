package com.huabo.system.service.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.huabo.system.controller.ZhSynchronizationController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.config.YMDifferentVConfig;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.BaseDao;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.controller.MessageToDoZzController;
import com.huabo.system.entity.TblFlowApproverInfo;
import com.huabo.system.entity.TblFlowInformInfo;
import com.huabo.system.entity.TblFlowMessage;
import com.huabo.system.entity.TblFlowTaskInfo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemFormFlow;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.flow.FlowTask;
import com.huabo.system.flow.FlowModel;
import com.huabo.system.flow.FlowTaskNode;
import com.huabo.system.flow.FlowTaskOperator;
import com.huabo.system.mapper.TblFlowApproverInfoMapper;
import com.huabo.system.mapper.TblFlowInformInfoMapper;
import com.huabo.system.mapper.TblFlowMessageMapper;
import com.huabo.system.mapper.TblFlowTaskInfoMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.mapper.TblSystemFormFlowMapper;
import com.huabo.system.mapper.TblSystemSheetTableMapper;
import com.huabo.system.oracle.vo.FlowMessageVo;
import com.huabo.system.service.TblAuthorizationRecordService;
import com.huabo.system.service.TblFlowTaskInfoService;

import redis.clients.jedis.Jedis;


@Service
public class TblFlowTaskInfoServiceImpl implements TblFlowTaskInfoService {
	
	@Resource
	private TblFlowTaskInfoMapper tblFlowTaskInfoMapper;
	
	@Resource
	private TblStaffMapper tblStaffMapper;

	@Resource
	private MessageToDoZzController messageToDoZzController;

	@Resource
	private ZhSynchronizationController zhSynchronizationController;
	
	@Resource
	private TblSystemSheetTableMapper tblSystemSheetTableMapper;
	
	@Resource
	private TblFlowMessageMapper tblFlowMessageMapper;
	
	@Resource
	private TblFlowApproverInfoMapper tblFlowApproverInfoMapper;
	
	@Resource
	private TblFlowInformInfoMapper tblFlowInformInfoMapper;
	
	@Resource
	private TblSystemFormFlowMapper tblSystemFormFlowMapper;
	
	@Resource
	private TblAuthorizationRecordService tblAuthorizationRecordService;
	
	@Resource
	private TblFlowTaskInfoService tblFlowTaskInfoService;

	
	/**
	 * 保存提交流程信息
	 */
	@Override
	public void insertSubmitInfo(String ymFromId, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal fromId) throws Exception {
		TblFlowTaskInfo taskInfo = null;
		Integer groupId = 1;
		
		FlowTask flowTask = this.getFlowTaskById(ymFromId);
		
		String taskTitle = flowTask.getFullName();//流程名称
		String flowCode = flowTask.getFlowCode();//流程编码
		String processId = flowTask.getProcessId();//当前流程进程Id
		String flowId = flowTask.getFlowId();//流程ID
		String id = flowTask.getId();//当前流程任务Id
		
		//获取下一步所有人员的操作节点信息 通过flowTaskId 和 node.nextNode 和未处理状态 = 0 的信息
		List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(ymFromId);
		TblStaff nextStaff = null;
		
		//通过当前登录用户获取流程信息表中的待办数据
		TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(fromId,flowId,id,sheet.getTableType(),loginStaff.getStaffid());
		
		if(preTaskInfo != null) {
			//如果之前有办理信息  关闭其信息 并将分组 加1 
			groupId = preTaskInfo.getTaskGroupId()+1;
			this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
		}
		
		//循环待处理信息，并插入 taskInfo表中
		for (FlowTaskOperator oper : operList) {
			nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(oper.getHandleId());
			taskInfo = new TblFlowTaskInfo();
			taskInfo.setTaskTitle(taskTitle);
			taskInfo.setCurrentStaffId(loginStaff.getStaffid());
			taskInfo.setCurrenRole("提交人");
			taskInfo.setOperation("提交");
			taskInfo.setCommont("提交");
			taskInfo.setFlowTaskId(ymFromId);
			taskInfo.setThisStepId(oper.getNodeCode());
			taskInfo.setTaskNo(flowCode);
			taskInfo.setModuleType(sheet.getTableType());
			taskInfo.setProcessId(ymFromId);
			taskInfo.setFromId(fromId.toString());
			taskInfo.setFlowId(flowId);
			taskInfo.setNextStaffId(nextStaff.getStaffid());
			taskInfo.setNextRole(oper.getNodeName());
			taskInfo.setOperatorId(oper.getFid());
			taskInfo.setTaskNodeId(oper.getTaskNodeId());
			taskInfo.setTaskStatus(0);
			taskInfo.setTaskGroupId(groupId);
			this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
			if(HttpClient.ifSendOa){  //待页面传递参数未完全
				TblStaff submitStaff = this.tblStaffMapper.selectByUserId(loginStaff.getStaffid().toString());//信息同步使用
//				messageToDoZzController.sendEndMessage(taskInfo, submitStaff,nextStaff,0,loginStaff);
//				messageToDoZzController.sendSubmitInfo(taskInfo,submitStaff,nextStaff);
				zhSynchronizationController.saveTask(taskInfo,submitStaff,nextStaff);
			}
		}

		if(preTaskInfo != null && HttpClient.ifSendOa) {
//			messageToDoZzController.sendDealResult(preTaskInfo,1,0);
			//发送待办状态改变消息
			zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
		}
		
	}
	

	/**
	 *  保存拒绝流程信息
	 */
	@Override
	public void insertRejectInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String handleOpinion, String oldStepId, String operatorId) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		try {
			
			TblStaff nextStaff = null;
			String thisStep = null;
			TblFlowTaskInfo taskInfo = null;
			String taskTitle = null;
			
			jedis = JedisUtil.getJedis();
			
			//flowtask主键 获取提交审批节点，taskNode信息
			FlowTask task = this.getFlowTaskById(id);
			String flowId = task.getFlowId();
			taskTitle = task.getFullName();
			
			String processId = task.getProcessId();
			
			
			//查找流程发起人，通过表单主键和taskId 和 flowId
			TblStaff startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,id,flowId);
			
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,id,sheet.getTableType(),loginStaff.getStaffid());
			
			//查找当前流程中所有未处理的流程节点信息
			List<TblFlowTaskInfo> unDealInfoList = this.tblFlowTaskInfoMapper.selectUntreatedInfoList(id);
			List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(id);
			
			//对比出未存入的待处理信息
			boolean isDeal = false;
			List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(); 
			for (FlowTaskOperator op : operList) {
				isDeal = unDealInfoList.stream().anyMatch(t -> t.getOperatorId().equals(op.getFid()));
				if(!isDeal) {
					newOperList.add(op);
				}
			}
			
			boolean isStart = false;
			//获取通知消息
			String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			if(newOperList != null && newOperList.size() > 0) {
				//拒绝到指定节点导致多人审批
				for (FlowTaskOperator oper : newOperList) {
					nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(oper.getHandleId());
					thisStep = oper.getNodeName();
					
					taskInfo = new TblFlowTaskInfo();
					taskInfo.setTaskTitle(task.getFullName());
					taskInfo.setCurrentStaffId(loginStaff.getStaffid());
					taskInfo.setCurrenRole(preTaskInfo.getNextRole());
					taskInfo.setOperation("驳回");
					taskInfo.setCommont(handleOpinion);
					taskInfo.setFlowTaskId(id);
					taskInfo.setThisStepId(oper.getNodeCode());
					taskInfo.setTaskNo(task.getFlowCode());
					taskInfo.setModuleType(sheet.getTableType());
					taskInfo.setProcessId(processId);
					taskInfo.setFromId(formId.toString());
					taskInfo.setFlowId(task.getFlowId());
					taskInfo.setNextStaffId(nextStaff.getStaffid());
					taskInfo.setNextRole(thisStep);
					taskInfo.setOperatorId(oper.getFid());
					taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
					taskInfo.setTaskStatus(0);
					taskInfo.setTaskNodeId(oper.getTaskNodeId());
					this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
					if(HttpClient.ifSendOa){  //待页面传递参数未完全
//						messageToDoZzController.sendDealResult(preTaskInfo,1,3);
//						messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
//						messageToDoZzController.sendEndMessage(taskInfo,startStaff, nextStaff,0,loginStaff);
						zhSynchronizationController.changeTaskStatus(taskInfo,2,loginStaff);
					}
					if("start".equals(oper.getNodeCode())) {
						isStart = true;
					}
					
				}
				if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
					//获取所有需要改变状态的集合
					List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
					for (TblFlowTaskInfo pre : preTaskInfoList) {
//						messageToDoZzController.sendEndMessage(pre, startStaff,startStaff,2,loginStaff);
					}
				}
				this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"驳回",processId);
				this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
				if(!isStart) {
					//通知指定人员
					message.setMuessage(taskTitle+"已由审批人"+loginStaff.getRealname()+"驳回至"+thisStep+"。");
					message.setDoType(1);
					message.setFlowUrl("/ymWrok/getEditInfo");
					message.setFlowId(flowId);
					message.setId(processId);
					messageList.add(message);
					jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				}
			}else {
				isStart = true;
				//退回至发起人 修改表单状态为需调整
				String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_XTZ+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
				this.tblSystemSheetTableMapper.executeSql(sql);
					nextStaff = this.tblStaffMapper.selectByUserId(startStaff.getStaffid().toString());
					taskInfo = new TblFlowTaskInfo();
					taskInfo.setTaskTitle(task.getFullName());
					taskInfo.setCurrentStaffId(loginStaff.getStaffid());
					taskInfo.setCurrenRole(preTaskInfo.getNextRole());
					taskInfo.setOperation("驳回");
					taskInfo.setCommont(handleOpinion);
					taskInfo.setFlowTaskId(id);
					taskInfo.setTaskNo(task.getFlowCode());
					taskInfo.setModuleType(sheet.getTableType());
					taskInfo.setProcessId(processId);
					taskInfo.setFromId(formId.toString());
					taskInfo.setFlowId(task.getFlowId());
					taskInfo.setTaskStatus(0);
					taskInfo.setThisStepId("start");
					taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
					taskInfo.setNextStaffId(startStaff.getStaffid());
					taskInfo.setNextRole("提交人");
					taskInfo.setTaskNodeId("");
					this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
					if(HttpClient.ifSendOa){  //待页面传递参数未完全
//						messageToDoZzController.sendDealResult(preTaskInfo,1,3);
//						messageToDoZzController.sendEndMessage(taskInfo, startStaff,nextStaff,0,loginStaff);
						zhSynchronizationController.changeTaskStatus(taskInfo,2 ,loginStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
					}
			}
			
			
			if(isStart) {
				//通知发起人
				message.setMuessage(taskTitle+"已被"+loginStaff.getRealname()+"驳回，请及时调整。");
				message.setDoType(0);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				
				if(HttpClient.ifSendOa){
					for (TblFlowTaskInfo unt : unDealInfoList) {
//						messageToDoZzController.sendDealResult(unt,1,3);
						zhSynchronizationController.changeTaskStatus(unt,2,loginStaff);
					}
				}
				//将flowTaskInfo中左右未处理的信息全部变更为已处理
				this.tblFlowTaskInfoMapper.updateTaskStatusTodeal(id,"start");
			}
			
			
			//处理催办消息集合
			this.dealPressMessageInfo(flowId,processId,loginStaff.getStaffid());
			this.tblFlowApproverInfoMapper.stopEntity(oldStepId,id,flowId);
			
			if(sheet.getRewirteFlowInfo() != null && sheet.getRewirteFlowInfo() == 1) {
				this.rewriteFlwoInfoToEntity(sheet,formId,preTaskInfo,taskInfo,loginStaff);
			}
			
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	private void dealPressMessageInfo(String flowId, String processId, BigDecimal staffid) throws Exception {
		//获取是否处理第催办信息
		Integer pressCount = this.tblFlowMessageMapper.selectPressCount(flowId,processId,staffid);
		if(pressCount > 0){
			this.tblFlowMessageMapper.updateIsReadStatus(flowId,processId,staffid);
		}
	}


	/**
	 * 流程撤回时，删除掉所有办理信息
	 */
	@Override
	public void insertActionsWithdrawInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId, String flowId)
			throws Exception {
		
		Jedis jedis = null;
		List<FlowMessageVo> messageVoList = new ArrayList<FlowMessageVo>(0);
		List<FlowMessageVo> voList = new ArrayList<FlowMessageVo>(0);
		List<TblFlowMessage> messageList = new ArrayList<TblFlowMessage>(0);
		
		try {
			if(HttpClient.ifSendOa){  //待页面传递参数未完全
				List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectPreTaskInfoByTaskId(formId,id,sheet.getTableType());
				for (TblFlowTaskInfo preTaskInfo : preTaskInfoList) {
					zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
				}
			}
			
			messageList = this.tblFlowMessageMapper.selectNoDealMessageList(id,flowId);
			String submitMessage = null;
			jedis = JedisUtil.getJedis();
			//循环获取未处理催办消息详情将其从redis中移除
			for (TblFlowMessage message : messageList) {
				submitMessage = jedis.get(message.getRecipient() + JedisUtil.SUBMITMESSAGE);
				if(submitMessage != null && !"".equals(submitMessage)) {
					voList = new ArrayList<FlowMessageVo>(0);
					messageVoList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
					//循环redis中未处理的消息 清除这些催办信息
					for (FlowMessageVo vo : messageVoList) {
						if(!(vo.getFlowId().equals(message.getFlowId()) && vo.getProcessId().equals(message.getProcessId()))) {
							voList.add(vo);
						}
					}
					jedis.set(message.getRecipient() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(voList));
				}
			}

			//删除掉流程对应的催办信息 与 办理信息
			this.tblFlowMessageMapper.deleteNoDealMessageInfo(id,flowId);
			this.tblFlowTaskInfoMapper.deleteTaskInfoByTaskId(formId,id,sheet.getTableType());
		} finally {
			if(jedis != null) {
				JedisUtil.returnResource(jedis);
			}
		}
	}

	/**
	 * 处理通过的信息
	 */
	
	@Override
	public void insertAuditInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String freeApproverUserId, String handleOpinion, String[] approverUsers, TblFlowApproverInfo currentApp, TblFlowApproverInfo nextApp, 
			FlowTask task, FlowTaskOperator preOper, FlowTaskNode taskNode,TblStaff startStaff) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		TblFlowTaskInfo taskInfo = null;
		TblStaff nextStaff = null;
		
		String[] operators = null;
		
		TblFlowApproverInfo insertApp = null;
		try {
			String taskTitle = task.getFullName();
			String flowCode = task.getFlowCode();
			String processId = task.getProcessId();
			String flowId = task.getFlowId();
			
			//查找上一步审批办理人信息
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,id,sheet.getTableType(),loginStaff.getStaffid());
			
			
			jedis = JedisUtil.getJedis();
			String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			
			//查找flow_task 表中的状态是否是审批已完成；
			Integer count = this.getEndStatusByFlowTask(task.getId());
			
			if(count == 1) {
				//判断为流程结束状态
					String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_YWC+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
					this.tblSystemSheetTableMapper.executeSql(sql);
					if(sheet.getSubTableName() != null && !"".equals(sheet.getSubTableName()) && sheet.getSubtablesCol() != null && !"".equals(sheet.getSubtablesCol()) && sheet.getSubtableRela() != null && !"".equals(sheet.getSubtableRela() )) {
							if(!"".equals(sheet.getMiddleRela()) && sheet.getMiddleRela() != null && !"".equals(sheet.getMiddleTable() ) && sheet.getMiddleTable() != null && !"".equals(sheet.getMiddleColumn() ) && sheet.getMiddleColumn() != null){
								sql = "UPDATE "+sheet.getSubTableName()+" SET "+sheet.getSubtablesCol()+" = '"+sheet.getSubtableStatus()+"' WHERE "+sheet.getSubtableRela()+" IN ( SELECT "+sheet.getMiddleRela()+" FROM "+sheet.getMiddleTable()+" WHERE "+sheet.getMiddleColumn()+" = '"+formId+"')";
							}else {
								sql = "UPDATE "+sheet.getSubTableName()+" SET "+sheet.getSubtablesCol()+" = '"+sheet.getSubtableStatus()+"' WHERE "+sheet.getSubtableRela()+" = '"+formId+"'";
							}
						this.tblSystemSheetTableMapper.executeSql(sql);
					}

				taskInfo = new TblFlowTaskInfo();
				taskInfo.setTaskTitle(taskTitle);
				taskInfo.setCurrentStaffId(loginStaff.getStaffid());
				taskInfo.setCurrenRole(preTaskInfo.getNextRole());
				taskInfo.setOperation("完成");
				taskInfo.setCommont(handleOpinion);
				taskInfo.setFlowTaskId(id);
				taskInfo.setTaskNo(flowCode);
				taskInfo.setModuleType(sheet.getTableType());
				taskInfo.setProcessId(processId);
				taskInfo.setFromId(formId.toString());
				taskInfo.setFlowId(flowId);
				taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
				taskInfo.setTaskStatus(1);
				taskInfo.setThisStepId("end");
				this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
				if(HttpClient.ifSendOa){  //待页面传递参数未完全
					zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//					messageToDoZzController.sendEndMessage(taskInfo, startStaff,startStaff,3,loginStaff);
				}
				message.setMuessage(taskTitle+"已由"+loginStaff.getRealname()+"同意");
				message.setDoType(1);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				
				
				if("XTXGQR".equals(sheet.getClassName())) {
					//系统修改组织用户信息流程，审批通过后要修改数据；
					this.tblAuthorizationRecordService.modifyFlowEndModifyOperationData(formId.toString(),loginStaff);
				}
				if(sheet.getRewirteFlowInfo() != null && sheet.getRewirteFlowInfo() == 1) {
					this.modifyRelationFrom(sheet,formId);
				}
			}else {
				String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_SPZ+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
				this.tblSystemSheetTableMapper.executeSql(sql);
				if(freeApproverUserId != null) {
					//判断为加签状态
					nextStaff = tblStaffMapper.selectByUserId(freeApproverUserId.toString());
					operators = this.getOperatorInfo(nextStaff.getPkYmStaffId(),processId,preOper.getNodeCode());
					taskInfo = new TblFlowTaskInfo();
					taskInfo.setTaskTitle(taskTitle);
					taskInfo.setCurrentStaffId(loginStaff.getStaffid());
					taskInfo.setCurrenRole(preTaskInfo.getNextRole());
					taskInfo.setOperation("通过");
					taskInfo.setCommont(handleOpinion);
					taskInfo.setFlowTaskId(id);
					taskInfo.setThisStepId(preTaskInfo.getThisStepId());
					taskInfo.setTaskNo(flowCode);
					taskInfo.setModuleType(sheet.getTableType());
					taskInfo.setProcessId(processId);
					taskInfo.setFromId(formId.toString());
					taskInfo.setFlowId(flowId);
					taskInfo.setNextStaffId(nextStaff.getStaffid());
					taskInfo.setNextRole("加签人");
					taskInfo.setOperatorId(operators[0]);
					taskInfo.setTaskStatus(0);
					taskInfo.setTaskNodeId(operators[1]);
					taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId());
					this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
					if(HttpClient.ifSendOa){  //待页面传递参数未完全
						zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//						messageToDoZzController.sendEndMessage(taskInfo,startStaff, nextStaff,0,loginStaff);
//						messageToDoZzController.sendEndMessage(preTaskInfo,startStaff, startStaff,3,loginStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
					}
				}else {
					//正常流程审批通过下一阶段
					
					//包含则当前审批节点未结束，关闭当前审批人员信息
					this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
					//获取flowTaskoperator 未处理的事项
					List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(id);
					//通过flowtask的fid 获取当前taskInfo所有信息集合
					List<TblFlowTaskInfo> taskInfoList = this.tblFlowTaskInfoMapper.selectAllTaskInfoListByTaskId(id);
					
					//从taskInfoList 中 获取所有未处理的taskInfo 信息
					List<TblFlowTaskInfo> unTaskInfoList = taskInfoList.stream().filter(e -> e.getTaskStatus() == 0).collect(Collectors.toList());
					
					
					if("5.0+".equals(YMUrlStatic.YMVERSION)) {
						//查询当前节点已处理的通过节点
						List<FlowTaskOperator> alredyList = this.getAlredyOperatorListInfo(id,preOper.getNodeCode());
						
						//处理当前人办理的 和 自动通过的数据
						for (TblFlowTaskInfo unt : unTaskInfoList) {
							for (FlowTaskOperator ope : alredyList) {
								if(ope.getFid().equals(unt.getOperatorId())) {
									//自动处理通过
									this.tblFlowTaskInfoMapper.updateTaskStatus(unt);
								}
							}
						}
						Integer groupId = preTaskInfo.getTaskGroupId()+1;
						int index = 1;
						//当前审批节点已通过 进入下一审批节点
						List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(0);
						//查找业务中台数据库中是否有自动审批通过的记录
						List<FlowTaskOperator> autoOperList = this.getAutoAuditOperaListByTaskId(id,preOper.getNodeCode());
						boolean isDeal = false;
						//循环比对当前FlowTask taskId  所有的taksInfo中是否已存入这些自动审批记录 未存入的重新存入
						if(autoOperList != null && autoOperList.size() > 0) {
							for (FlowTaskOperator autoOper : autoOperList) {
								isDeal = taskInfoList.stream().anyMatch(t -> StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(autoOper.getFid()));
								if(!isDeal) {
									newOperList.add(autoOper);
								}
							}
						}
						
						//比较未处理的operList 和 已插入并且未处理的taskInfoList 获取到需要处理的节点
						for (FlowTaskOperator op : operList) {
							isDeal = unTaskInfoList.stream().anyMatch(t -> (StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(op.getFid())));
							if(!isDeal) {
								newOperList.add(op);
							}
						}
						
						//循环插入未存入的审批记录信息 F_Automation = 1 的 为自动同意审批通过的记录
						for (FlowTaskOperator dop : newOperList) {
							nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(dop.getHandleId());
							taskInfo = new TblFlowTaskInfo();
							taskInfo.setTaskTitle(taskTitle);
							taskInfo.setFlowTaskId(id);
							taskInfo.setThisStepId(dop.getNodeCode());
							taskInfo.setCurrenRole(dop.getNodeName());
							taskInfo.setTaskNo(flowCode);
							taskInfo.setModuleType(sheet.getTableType());
							taskInfo.setProcessId(processId);
							taskInfo.setFromId(formId.toString());
							taskInfo.setFlowId(flowId);
							taskInfo.setOperatorId(dop.getFid());
							taskInfo.setTaskGroupId(groupId);
							taskInfo.setTaskNodeId(dop.getTaskNodeId());
							if("1".equals(dop.getAutomation())) {
								taskInfo.setCurrentStaffId(nextStaff.getStaffid());
								taskInfo.setOperation("自动审批通过");
								taskInfo.setCommont("自动审批通过");
								taskInfo.setTaskStatus(1);
							}else {
								taskInfo.setCurrentStaffId(loginStaff.getStaffid());
								taskInfo.setCurrenRole(preOper.getNodeName());
								taskInfo.setNextStaffId(nextStaff.getStaffid());
								taskInfo.setNextRole(dop.getNodeName());
								taskInfo.setTaskStatus(0);
								taskInfo.setOperation("通过");
								taskInfo.setCommont(handleOpinion);
							}
							
							this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
							groupId = groupId + index;
							
							if(HttpClient.ifSendOa && !"1".equals(dop.getAutomation())){  //待页面传递参数未完全
								//OA对接发送待办信息
								zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
								zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
							}
						}
						
						if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
							//获取所有需要改变状态的集合
							List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
							for (TblFlowTaskInfo tblFlowTaskInfo : preTaskInfoList) {
								zhSynchronizationController.changeTaskStatus(tblFlowTaskInfo,2,loginStaff);
							}
						}
					}else {
						//如果operList 里的nodeNode 包含当前taskOperation操作的nodeCode 则当前审批节点未结束  流程还停在当前节点，无需插入下次催办消息
						boolean isNext = operList.stream().anyMatch(oper -> oper.getNodeCode().contains(preOper.getNodeCode()));
						if(isNext) {
								//插入当前办理人审批信息  但无下一步审批节点
								taskInfo = new TblFlowTaskInfo();
								taskInfo.setTaskTitle(taskTitle);
								taskInfo.setCurrentStaffId(loginStaff.getStaffid());
								taskInfo.setCurrenRole(preOper.getNodeName());
								taskInfo.setOperation("通过");
								taskInfo.setCommont(handleOpinion);
								taskInfo.setFlowTaskId(id);
								taskInfo.setThisStepId(preOper.getNodeCode());
								taskInfo.setTaskNo(flowCode);
								taskInfo.setModuleType(sheet.getTableType());
								taskInfo.setProcessId(processId);
								taskInfo.setFromId(formId.toString());
								taskInfo.setFlowId(flowId);
								taskInfo.setOperatorId(preOper.getFid());
								taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
								taskInfo.setTaskStatus(2);
								taskInfo.setTaskNodeId(preOper.getTaskNodeId());
								this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
								if(HttpClient.ifSendOa){  //待页面传递参数未完全
									//OA对接发送待办信息
									zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
									zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
								}
						}else {
							HashMap<String, Object> filedMap = new HashMap<String,Object>(0);
							Map<String, String> headerMap = new HashMap<>();
							headerMap.put("Authorization",loginStaff.getYmToken());
							headerMap.put("content-type","application/json;charset=utf-8");
							String result = DealUserToken.dealYmUrlMethod(YMUrlStatic.interfaceUrl+YMUrlStatic.workInfo+id,filedMap,headerMap,loginStaff,HttpClient.HTTPGET,null);
							System.out.println("++++++++++++++++++++++++++++++++++++++++++result;+"+result);
							JSONObject taskObject = null;
							String nextApproval = null;
							String nextRole = null;
							JSONObject flowJson = JSONObject.parseObject(result);
							JSONObject flowDataJson = flowJson.getJSONObject("data");
							JSONObject taskInfoJson = flowDataJson.getJSONObject("flowTaskInfo");
							JSONArray flowTaskNodeList = flowDataJson.getJSONArray("flowTaskNodeList");
							String thisStepId = taskInfoJson.getString("thisStepId");
							String[] thisStepIds = thisStepId.split(",");
							String[] approvalUsers = null;
							String account = null;
							String pkYmStaffId = null;
							String startApproval = null;
							String[] userInfo = new String[2];
							for (String stepId : thisStepIds) {
								
									for (int i = 0 ; i < flowTaskNodeList.size() ; i++) {
										taskObject = flowTaskNodeList.getJSONObject(i);
										if(stepId.equals(taskObject.getString("nodeCode"))) {
											nextApproval = taskObject.getString("userName");
											nextRole =  taskObject.getString("nodeName");
											break;
										}
									}
									approvalUsers = nextApproval.split(",");
									for (String appUserInfo : approvalUsers) {
										userInfo = appUserInfo.split("/");
										account = userInfo[1];
										pkYmStaffId =  this.getYmPkStaffIdByAccount(account);
										nextStaff = tblStaffMapper.selectUserName(account);
										operators = this.getOperatorInfo(pkYmStaffId,processId,stepId);
										
										taskInfo = new TblFlowTaskInfo();
										taskInfo.setTaskTitle(taskTitle);
										taskInfo.setCurrentStaffId(loginStaff.getStaffid());
										taskInfo.setCurrenRole(preTaskInfo.getNextRole());
										taskInfo.setOperation("通过");
										taskInfo.setCommont(handleOpinion);
										taskInfo.setFlowTaskId(id);
										taskInfo.setThisStepId(stepId);
										taskInfo.setTaskNo(flowCode);
										taskInfo.setModuleType(sheet.getTableType());
										taskInfo.setProcessId(processId);
										taskInfo.setFromId(formId.toString());
										taskInfo.setFlowId(flowId);
										taskInfo.setNextStaffId(nextStaff.getStaffid());
										taskInfo.setNextRole(nextRole);
										taskInfo.setOperatorId(operators[0]);
										taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
										taskInfo.setTaskStatus(0);
										taskInfo.setTaskNodeId(operators[1]);
										this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
									}
								}
						
//							Integer groupId = preTaskInfo.getTaskGroupId()+1;
//							int index = 1;
//							
//							
//							//当前审批节点已通过 进入下一审批节点
//							List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(0);
//							
//							//查找业务中台数据库中是否有自动审批通过的记录
//							List<FlowTaskOperator> autoOperList = this.getAutoAuditOperaListByTaskId(id,preOper.getNodeCode());
//							boolean isDeal = false;
//							//循环比对当前FlowTask taskId  所有的taksInfo中是否已存入这些自动审批记录 未存入的重新存入
//							if(autoOperList != null && autoOperList.size() > 0) {
//								for (FlowTaskOperator autoOper : autoOperList) {
//									isDeal = taskInfoList.stream().anyMatch(t -> StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(autoOper.getFid()));
//									if(!isDeal) {
//										newOperList.add(autoOper);
//									}
//								}
//							}
//							
//							//比较未处理的operList 和 已插入并且未处理的taskInfoList 获取到需要处理的节点
//							for (FlowTaskOperator op : operList) {
//								isDeal = unTaskInfoList.stream().anyMatch(t -> (StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(op.getFid())));
//								if(!isDeal) {
//									newOperList.add(op);
//								}
//							}
							
							//循环插入未存入的审批记录信息 F_Automation = 1 的 为自动同意审批通过的记录
//							for (FlowTaskOperator dop : newOperList) {
//								nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(dop.getHandleId());
//								taskInfo = new TblFlowTaskInfo();
//								taskInfo.setTaskTitle(taskTitle);
//								taskInfo.setFlowTaskId(id);
//								taskInfo.setThisStepId(dop.getNodeCode());
//								taskInfo.setCurrenRole(dop.getNodeName());
//								taskInfo.setTaskNo(flowCode);
//								taskInfo.setModuleType(sheet.getTableType());
//								taskInfo.setProcessId(processId);
//								taskInfo.setFromId(formId.toString());
//								taskInfo.setFlowId(flowId);
//								taskInfo.setOperatorId(dop.getFid());
//								taskInfo.setTaskGroupId(groupId);
//								taskInfo.setTaskNodeId(dop.getTaskNodeId());
//								if("1".equals(dop.getAutomation())) {
//									taskInfo.setCurrentStaffId(nextStaff.getStaffid());
//									taskInfo.setOperation("自动审批通过");
//									taskInfo.setCommont("自动审批通过");
//									taskInfo.setTaskStatus(1);
//								}else {
//									taskInfo.setCurrentStaffId(loginStaff.getStaffid());
//									taskInfo.setCurrenRole(preOper.getNodeName());
//									taskInfo.setNextStaffId(nextStaff.getStaffid());
//									taskInfo.setNextRole(dop.getNodeName());
//									taskInfo.setTaskStatus(0);
//									taskInfo.setOperation("通过");
//									taskInfo.setCommont(handleOpinion);
//								}
//								
//								this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
//								groupId = groupId + index;
//								
//								if(HttpClient.ifSendOa && !"1".equals(dop.getAutomation())){  //待页面传递参数未完全
//									//OA对接发送待办信息
//									zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
//									zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//								}
//							}
							
							if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
								//获取所有需要改变状态的集合
								List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
								for (TblFlowTaskInfo tblFlowTaskInfo : preTaskInfoList) {
									zhSynchronizationController.changeTaskStatus(tblFlowTaskInfo,2,loginStaff);
								}
							}
							this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"通过",processId);
						}
					}
				}
				message.setMuessage(taskTitle+"已由"+loginStaff.getRealname()+"通过。");
				message.setDoType(1);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
			}
			
			this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
			if(sheet.getRewirteFlowInfo() != null && sheet.getRewirteFlowInfo() == 1) {
				this.rewriteFlwoInfoToEntity(sheet,formId,preTaskInfo,taskInfo,loginStaff);
			}
			
			//处理催办消息集合
			this.dealPressMessageInfo(flowId,processId,loginStaff.getStaffid());
			
			//处理多人加签
			if(currentApp != null) {
				this.tblFlowApproverInfoMapper.updateEntity(currentApp);
				if(approverUsers != null) {
					for(int i = 0 ; i < approverUsers.length ; i++) {
						insertApp = new TblFlowApproverInfo();
						insertApp.setStaffId(new BigDecimal(approverUsers[i]));
						insertApp.setApporDer(currentApp.getApporDer());
						insertApp.setAppStatus(0);
						insertApp.setFlowId(flowId);
						insertApp.setProcessId(id);
						insertApp.setThisStepId(preOper.getNodeCode());
						insertApp.setApproverId(RandomUtil.uuBigDecimalId());
						this.tblFlowApproverInfoMapper.insertEntity(insertApp);
					}
				}
			}else {
				if(approverUsers != null) {
					for(int i = 0 ; i < approverUsers.length ; i++) {
						insertApp = new TblFlowApproverInfo();
						insertApp.setStaffId(new BigDecimal(approverUsers[i]));
						insertApp.setApporDer(i+1);
						insertApp.setAppStatus(0);
						insertApp.setFlowId(flowId);
						insertApp.setProcessId(id);
						insertApp.setThisStepId(preOper.getNodeCode());
						insertApp.setApproverId(RandomUtil.uuBigDecimalId());
						this.tblFlowApproverInfoMapper.insertEntity(insertApp);
					}
				}
			}
			
			//处理知会数据preStepId
			/*List<String> ymStaffAccountList = this.getCirculateStaffId(id,flowId,preOper.getNodeCode());
			TblStaff staff = null;
			TblFlowInformInfo info = null;
			for (String ymStaffAccount : ymStaffAccountList) {
				staff = this.tblStaffMapper.selectUserName(ymStaffAccount);
				if(staff != null) {
					info = new TblFlowInformInfo();
					info.setInfoId(RandomUtil.uuBigDecimalId());
					info.setCreateStaff(loginStaff.getStaffid());
					info.setCreateTime(new Date());
					info.setInformStaffId(staff.getStaffid());
					info.setId(id);
					info.setFlowId(flowId);
					info.setFormId(formId.toString());
					info.setThisStepId(preOper.getTaskNodeId());
					info.setIsRead(0);
					this.tblFlowInformInfoMapper.insert(info);
				}
//				if(HttpClient.ifSendOa){  //知会事宜推送至OA
//					messageToDoZzController.sendEndMessage(preTaskInfo, startStaff,staff,6,loginStaff);
//				}
			}*/
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	/**
	 * 修改其需要相关处理的表单
	 * @param sheet
	 * @param formId
	 */
	private void modifyRelationFrom(TblSystemSheetTable sheet, BigDecimal formId) throws Exception {
		String sql = "";
		switch (sheet.getClassName()) {
			case "ZGPJ":
				//整改评价全部审批完成后  修改整改方案状态为已完成；
				sql = "SELECT PLANID FROM TBL_RECTIFICATION_ISSUES WHERE RELAID = (SELECT RELAID FROM TBL_ZGZZ_RECTIFICATIONIMPL WHERE IMPLID = (SELECT IMPLID FROM TBL_ZGZZ_RCTEVALUATION WHERE EVALID = "+formId+"))";
				String planId = this.tblSystemSheetTableMapper.executeFindSqlReturnUnique(sql);//查询整改方案主键
				
				sql = "SELECT COUNT(0) FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = '"+planId+"'";//查询当前整改方案下所有的落实信息
				Integer totalCount = this.tblSystemSheetTableMapper.executeFindSqlReturnInteger(sql);
				
				sql = "SELECT COUNT(0) FROM TBL_ZGZZ_RCTEVALUATION WHERE STATUS = 6 AND IMPLID IN (" + 
						"SELECT IMPLID FROM TBL_ZGZZ_RECTIFICATIONIMPL WHERE RELAID IN (SELECT RELAID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = '"+planId+"'))";
				Integer ypjCount = this.tblSystemSheetTableMapper.executeFindSqlReturnInteger(sql);
				
				if(ypjCount >= totalCount) {
					//表示当前整改方案下的所有整改清单均已评价完成，修改整改方案状态为已完成
					sql = "UPDATE TBL_ZGZZ_RECTIFICATIONPLAN SET STATUS = 10 WHERE PLANID = '"+planId+"'";
					this.tblSystemSheetTableMapper.executeSql(sql);
				}
			break;
			default:
				break;
		}
	}


	private List<String> getCirculateStaffId(String id, String flowId, String preStepId) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> isList = new ArrayList<String>(0);
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_Account FROM base_user WHERE F_Id IN (SELECT F_ObjectId FROM flow_taskcirculate WHERE F_TaskId = '"+id+"' AND F_TaskNodeId = '"+preStepId+"')");
			rs = ps.executeQuery();
			while (rs.next()) {
				isList.add(rs.getString("F_Account"));
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return isList;
	}


	/**
	 *     转审方法
	 */
	@Override
	public void insertTransferInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String transferStaffId, String handleOpinion, String flowTaskInfoOperatorId) throws Exception {
			Jedis jedis = null;
			FlowMessageVo message = new FlowMessageVo();
			List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
			TblStaff startStaff = null;
			TblFlowTaskInfo taskInfo = null;
			String[] operators = null;
			TblStaff nextStaff = null;
			try {
				
				//获取审批流程记录信息
				
				nextStaff = tblStaffMapper.selectByUserId(transferStaffId.toString());
				
				//flowtask主键 获取提交审批节点，taskNode信息
				FlowTask task = this.getFlowTaskById(id);
				
				//通过operatorId 获取当前操作信息
				FlowTaskOperator preOper = this.selectCurrentOperator(flowTaskInfoOperatorId);
				
				//查找流程发起人，通过表单主键和taskId 和 flowId
				startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,id,task.getFlowId());
				
				//获取上一步办理信息
				TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,task.getFlowId(),id,sheet.getTableType(),loginStaff.getStaffid());
				/*Integer submitStaffId = this.tblFlowTaskInfoMapper.selectCurrentStaffId(formId,processId,flowId,id,sheet.getTableType());
				TblStaff submitStaff = this.tblStaffMapper.selectByUserId(submitStaffId.toString());*/
				
				//存储新的办理消息
				operators = this.getOperatorInfo(nextStaff.getPkYmStaffId(),task.getProcessId(),preOper.getNodeCode());
				taskInfo = new TblFlowTaskInfo();
				taskInfo.setTaskTitle(task.getFullName());
				taskInfo.setCurrentStaffId(loginStaff.getStaffid());
				taskInfo.setCurrenRole(preTaskInfo.getNextRole());
				taskInfo.setOperation("转审");
				taskInfo.setCommont(handleOpinion);
				taskInfo.setFlowTaskId(id);
				taskInfo.setThisStepId(preOper.getNodeCode());
				taskInfo.setTaskNo(task.getFlowCode());
				taskInfo.setModuleType(sheet.getTableType());
				taskInfo.setProcessId(task.getProcessId());
				taskInfo.setFromId(formId.toString());
				taskInfo.setFlowId(task.getFlowId());
				taskInfo.setNextStaffId(nextStaff.getStaffid());
				taskInfo.setNextRole("转审人");
				taskInfo.setOperatorId(operators[0]);
				taskInfo.setTaskStatus(0);
				taskInfo.setTaskNodeId(operators[1]);
				taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId());
				this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
				this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
				if(HttpClient.ifSendOa){  //待页面传递参数未完全
					zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//					messageToDoZzController.sendEndMessage(preTaskInfo, startStaff,startStaff,4,loginStaff);
//					messageToDoZzController.sendEndMessage(taskInfo, startStaff,nextStaff,0,loginStaff);
					zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
				}
				jedis = JedisUtil.getJedis();
				String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
				if(submitMessage != null && !"".equals(submitMessage)) {
					messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
				}
				
				//通知发起人流程转审信息
				message.setMuessage(task.getFullName()+"已由"+loginStaff.getRealname()+"转审至"+nextStaff.getRealname()+"。");
				message.setDoType(1);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(task.getFlowId());
				message.setProcessId(task.getProcessId());
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				
				//处理催办消息集合
				this.dealPressMessageInfo(task.getFlowId(),task.getProcessId(),loginStaff.getStaffid());
			} finally {
				if (jedis != null) {
					jedis.close();
				}
			}
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

	
	private void rewriteFlwoInfoToEntity(TblSystemSheetTable sheet, BigDecimal formId, TblFlowTaskInfo preTaskInfo,
			TblFlowTaskInfo taskInfo, TblStaffUtil loginStaff) throws Exception {
		String sql = null;
		switch (sheet.getClassName()) {
			case "ZDSH": //制度审核反写审批消息
					sql = this.rewriteFlwoInfoToZDSH(sheet, formId, preTaskInfo, taskInfo,loginStaff);
				break;
			case "JYSXSH": //制度审核反写审批消息
				sql = this.rewriteFlwoInfoToJYSXSH(sheet, formId, preTaskInfo, taskInfo,loginStaff);
				break;
			case "JFDJ": //制度审核反写审批消息
				sql = this.rewriteFlwoInfoToJFDJ(sheet, formId, preTaskInfo, taskInfo,loginStaff);
				break;
			default:
				break;
		}
		
		if(sql != null && !"".equals(sql)) {
			this.tblSystemSheetTableMapper.executeSql(sql);
		}
	}

	private String rewriteFlwoInfoToJFDJ(TblSystemSheetTable sheet, BigDecimal formId, TblFlowTaskInfo preTaskInfo,
			TblFlowTaskInfo taskInfo, TblStaffUtil loginStaff) throws Exception {
		String sql = null;
		if("发起者部门负责人".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_LEGAL_DISPUTREGISTRATION SET LEGALEXAM = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE DISPUTEID = "+formId;
		}else if("发起者部门分管领导".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_LEGAL_DISPUTREGISTRATION SET COUNSELEXAM = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE DISPUTEID = "+formId;
		}else if("总经理审批".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_LEGAL_DISPUTREGISTRATION SET GMANEXAM = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE DISPUTEID = "+formId;
		}else if("董事长".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_LEGAL_DISPUTREGISTRATION SET CHAIRMANEXAM = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE DISPUTEID = "+formId;
		}
		return sql;
	}


	private String rewriteFlwoInfoToJYSXSH(TblSystemSheetTable sheet, BigDecimal formId, TblFlowTaskInfo preTaskInfo,
			TblFlowTaskInfo taskInfo, TblStaffUtil loginStaff) throws Exception {
		String sql = null;
		if("部门合规管理员".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET HOSTDEPARTMENTOPINION = "+DataBaseSqlConfig.getConcatColumn("'"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"\n'", "HOSTDEPARTMENTOPINION")+" WHERE INSTITUTIONAUDITID = "+formId;
		}else if("发起者部门负责人".equals(taskInfo.getCurrenRole()) && "部门合规管理员".equals(preTaskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET HOSTDEPARTMENTOPINION = "+DataBaseSqlConfig.getConcatColumn("HOSTDEPARTMENTOPINION","'\n"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"'")+" WHERE INSTITUTIONAUDITID = "+formId;
		}else if("发起者部门分管领导".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET TEAMLEADEROPINION = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}else if("需要部门负责人会办".equals(taskInfo.getCurrenRole())) {
			List<TblFlowTaskInfo> taskList = this.tblFlowTaskInfoMapper.selectApprovalMemo(taskInfo.getProcessId(),"('需要部门负责人会办')");
			StringBuffer sb = new StringBuffer("");
			for (TblFlowTaskInfo task : taskList) {
				 sb.append(task.getCurrentName()+"："+task.getCommont()+"\n");
			}
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET RELATEDDEPARTMENTOPINION = '"+sb.toString()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}else if("需要加签领导会办".equals(taskInfo.getCurrenRole())) {
			List<TblFlowTaskInfo> taskList = this.tblFlowTaskInfoMapper.selectApprovalMemo(taskInfo.getProcessId(),"('需要加签领导会办')");
			StringBuffer sb = new StringBuffer("");
			for (TblFlowTaskInfo task : taskList) {
				 sb.append(task.getCurrentName()+"："+task.getCommont()+"\n");
			}
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET FIRMLEADEROPINION = '"+sb.toString()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}
		return sql;
	}


	private String rewriteFlwoInfoToZDSH(TblSystemSheetTable sheet, BigDecimal formId, TblFlowTaskInfo preTaskInfo,
			TblFlowTaskInfo taskInfo, TblStaffUtil loginStaff) throws Exception {
		String sql = null;
		if("发起部门合规管理员".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET DRAFTADMINISTRATOROPINION = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}else if("发起部门负责人".equals(taskInfo.getCurrenRole())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET DRAFTDEPARTMENTOPINION = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}else if("浙资运营下属公司征求意见".equals(taskInfo.getCurrenRole())) {
			List<TblFlowTaskInfo> taskList = this.tblFlowTaskInfoMapper.selectApprovalMemo(taskInfo.getProcessId(),"('浙资运营下属公司征求意见') ");
			StringBuffer sb = new StringBuffer("");
			for (TblFlowTaskInfo task : taskList) {
				 sb.append(task.getCurrentName()+"："+task.getCommont()+"\n");
			}
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET DEPARTMENTBELONGGROUPOPINION = '"+sb.toString()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}else if("浙资运营本部征求意见".equals(taskInfo.getCurrenRole()) || "需要各部门负责人会办".equals(taskInfo.getCurrenRole())) {
			List<TblFlowTaskInfo> taskList = this.tblFlowTaskInfoMapper.selectApprovalMemo(taskInfo.getProcessId(),"('浙资运营本部征求意见') ");
			StringBuffer sb = new StringBuffer("");
			for (TblFlowTaskInfo task : taskList) {
				 sb.append(task.getCurrentName()+"："+task.getCommont()+"\n");
			}
			taskList = this.tblFlowTaskInfoMapper.selectApprovalMemo(taskInfo.getProcessId(),"('需要各部门负责人会办') ");
			for (TblFlowTaskInfo task : taskList) {
				 sb.append(task.getCurrentName()+"："+task.getCommont()+"\n");
			}
			
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET COMPANYGROUPOPINION = '"+sb.toString()+"' WHERE INSTITUTIONAUDITID = "+formId;
		} if("end".equals(taskInfo.getThisStepId())) {
			sql = "UPDATE TBL_FWGL_INSTITUTION_AUDIT SET DRAFTDEPARTMENTOPINIONSUCK = '"+loginStaff.getRealname()+"："+taskInfo.getCommont()+"' WHERE INSTITUTIONAUDITID = "+formId;
		}
		return sql;
	}


	@Override
	public void insertAlreadyRecall(String processId, TblStaffUtil loginStaff, TblSystemSheetTable sheet,
			BigDecimal formId, String flowId, String handleOpinion, String operatorId) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		TblFlowTaskInfo taskInfo = null;
		String[] operators = null;
		String[] userInfo = new String[2];
		String account = null;
		TblStaff nextStaff = null;
		TblStaff startStaff = null;
		List<FlowMessageVo> messageVoList = new ArrayList<FlowMessageVo>(0);
		List<FlowMessageVo> voList = new ArrayList<FlowMessageVo>(0);
		List<TblFlowMessage> messageList = new ArrayList<TblFlowMessage>(0);
		
		try {
			jedis = JedisUtil.getJedis();
			
			List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(0);
			
			//处理上一步办理消息接口
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,processId,sheet.getTableType(),loginStaff.getStaffid());
			
			//flowtask主键 获取提交审批节点，taskNode信息
			FlowTask task = this.getFlowTaskById(processId);
			
			//通过operatorId 获取当前操作信息
			FlowTaskOperator preOper = this.selectCurrentOperator(operatorId);
			
			//查找流程发起人，通过表单主键和taskId 和 flowId
			startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,processId,flowId);
			
			//获取flowTaskoperator 未处理的事项
			List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(processId);
			
			//从taskInfoList 中 获取所有未处理的taskInfo 信息
			List<TblFlowTaskInfo> unTaskInfoList = this.tblFlowTaskInfoMapper.selectUntreatedInfoList(processId);
			
			boolean isDeal = false;
			//比较未处理的operList 和 已插入并且未处理的taskInfoList 获取到需要处理的节点
			for (FlowTaskOperator op : operList) {
				isDeal = unTaskInfoList.stream().anyMatch(t -> t.getOperatorId().equals(op.getFid()));
				if(!isDeal) {
					newOperList.add(op);
				}
			}
			
			//清除掉撤回前 审批人所有 的 催办消息
			messageList = this.tblFlowMessageMapper.selectNoDealMessageList(processId,flowId);
			String submitMessage = null;
			//循环获取未处理催办消息详情将其从redis中移除
			for (TblFlowMessage mes : messageList) {
				submitMessage = jedis.get(mes.getRecipient() + JedisUtil.SUBMITMESSAGE);
				if(submitMessage != null && !"".equals(submitMessage)) {
					voList = new ArrayList<FlowMessageVo>(0);
					messageVoList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
					//循环redis中未处理的消息 清除这些催办信息
					for (FlowMessageVo vo : messageVoList) {
						if(!(vo.getFlowId().equals(mes.getFlowId()) && vo.getProcessId().equals(mes.getProcessId()))) {
							voList.add(vo);
						}
					}
					jedis.set(mes.getRecipient() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(voList));
				}
			}
			this.tblFlowMessageMapper.deleteNoDealMessageInfo(processId,flowId);
			
			
			
			//获取发起人的催办消息  并通知发起人已被退回
			submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				voList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			if(HttpClient.ifSendOa){  
				List<TblFlowTaskInfo> preList = this.tblFlowTaskInfoMapper.selectPreTaskInfoListByProcessIdFlowId(processId,flowId,sheet.getTableType());
				for (TblFlowTaskInfo info : preList) {
					zhSynchronizationController.changeTaskStatus(info,2,loginStaff);
//					messageToDoZzController.sendDealResult(info,1,0);
				}
			}
			//将撤回前所有审批的流程信息关闭掉
			this.tblFlowTaskInfoMapper.updateTaskStatusAlreadyRecall(processId,flowId,sheet.getTableType());
			
			Integer maxGroupId = this.tblFlowTaskInfoMapper.selectMaxGrouIdByFlowIdProcessId(processId,flowId,sheet.getTableType());
			Integer count = 0;
			//循环插入未存入的审批记录信息 F_Automation = 1 的 为自动同意审批通过的记录
			for (FlowTaskOperator dop : newOperList) {
				nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(dop.getHandleId());
				taskInfo = new TblFlowTaskInfo();
				taskInfo.setTaskTitle(task.getFullName());
				taskInfo.setFlowTaskId(processId);
				taskInfo.setThisStepId(dop.getNodeCode());
				taskInfo.setCurrenRole(dop.getNodeName());
				taskInfo.setTaskNo(task.getFlowCode());
				taskInfo.setModuleType(sheet.getTableType());
				taskInfo.setProcessId(processId);
				taskInfo.setFromId(formId.toString());
				taskInfo.setFlowId(flowId);
				taskInfo.setOperatorId(dop.getFid());
				taskInfo.setTaskGroupId(maxGroupId+1);
				taskInfo.setTaskNodeId(dop.getTaskNodeId());
				taskInfo.setCurrentStaffId(loginStaff.getStaffid());
				taskInfo.setCurrenRole(preOper.getNodeName());
				taskInfo.setNextStaffId(nextStaff.getStaffid());
				taskInfo.setNextRole(dop.getNodeCode());
				taskInfo.setTaskStatus(0);
				taskInfo.setOperation("撤回");
				taskInfo.setCommont(handleOpinion);
				
				this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
				
				if(HttpClient.ifSendOa){  //待页面传递参数未完全
					zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//					messageToDoZzController.sendDealResult(preTaskInfo,1,0);
//					messageToDoZzController.sendEndMessage(preTaskInfo, startStaff,startStaff,5,loginStaff);
					// 判断 流程分流  审批会签 或签 在当前节点是否有其他人还未审批  count= 1 时 当前审批节点未结束，不需要插入下条办理信息
					count = this.tblFlowTaskInfoMapper.selectSameLevelInfoCount(dop.getNodeCode(),0,preTaskInfo.getTaskGroupId(),preTaskInfo.getFlowId(),preTaskInfo.getProcessId());
					if(!preTaskInfo.getThisStepId().equals(dop.getNodeCode()) && count == 0) {
//						messageToDoZzController.sendEndMessage(taskInfo,startStaff,nextStaff,0,loginStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
//						messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
					}
				}
			}
			
			
			
			//通知发起人流程已由审批人撤回
			message.setMuessage(task.getFullName()+"已由"+loginStaff.getRealname()+"撤回。");
			message.setDoType(1);
			message.setFlowUrl("/ymWrok/getEditInfo");
			message.setFlowId(flowId);
			message.setId(processId);
			voList.add(message);
			jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(voList));
			
			if(sheet.getRewirteFlowInfo() != null && sheet.getRewirteFlowInfo() == 1) {
				this.rewriteFlwoInfoToEntity(sheet,formId,preTaskInfo,taskInfo,loginStaff);
			}
			
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	@Override
	public void insertBatchOperationInfo(TblStaffUtil loginStaff, FlowModel flowModel) throws Exception {
		
		List<FlowTaskOperator> opList = flowModel.getTaskList();
		TblSystemFormFlow formFlow = null;
		TblSystemSheetTable sheet = null;
		TblFlowTaskInfo preTaskInfo = null;
		Integer nodealCount = 0;
		FlowTaskOperator ope = null;
		TblFlowTaskInfo taskInfo = null;
		String formId = "";
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		TblStaff startStaff = null;
		TblStaff nextStaff = null;
		String sql = null;
		List<FlowTaskOperator> noList = null;
		List<String> nodeCodeList = null;
		Map<String,TblStaff> nextStaffMap = new HashMap<String,TblStaff>(0);
		TblFlowInformInfo info = null;
		
		try {
			jedis = JedisUtil.getJedis();
			
			//获取抄送人集合
			List<TblStaff> copyStaffList = null;
			if(StringUtils.isNotBlank(flowModel.getCopyIds())) {
				copyStaffList = this.tblStaffMapper.selectCopyStaffListByYmId("'"+flowModel.getCopyIds().replace(",", "','")+"'");
			}
			
			for (FlowTaskOperator operator : opList) {
				//1.通过flowId和 processId 获取相应的表单和流程信息
				formFlow = this.tblSystemFormFlowMapper.selectFormIdByProcessId(flowModel.getFlowId(),operator.getTaskId());
				if(formFlow == null) {
					continue;
				}
				startStaff = tblStaffMapper.selectUniqueStaffInfoByStaffId(formFlow.getUserId());
				
				//2.获取系统流程配置信息 和 上一步流程处理信息；
				sheet = this.tblSystemSheetTableMapper.selectSheetTableInfoByFlowId(flowModel.getFlowId(), formFlow.getOrgId());
				preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(new BigDecimal(formFlow.getFormId()),flowModel.getFlowId(),operator.getTaskId(),sheet.getTableType(),loginStaff.getStaffid());
				
				//3.获取业务表单主键
				formId = preTaskInfo.getFromId();
				
				//4.获取当前流程办理节点信息
				ope = this.selectCurrentOperator(operator.getFid());
				
				String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
				if(submitMessage != null && !"".equals(submitMessage)) {
					messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
				}
				
				if(ope.getHandleStatus() == 0) {
					//审批拒绝，退回到发起人
				}else {
					//审批通过，后续处理逻辑
					//5.获取后续未处理的流程数量
					nodealCount = this.selectNoDealOperator(operator.getTaskId());
					if(nodealCount == 0) {
						//当前流程审批已结束
						//判断为流程结束状态
						if(sheet.getSubTableName() != null && !"".equals(sheet.getSubTableName()) && sheet.getSubtablesCol() != null && !"".equals(sheet.getSubtablesCol()) && sheet.getSubtableRela() != null && !"".equals(sheet.getSubtableRela() )) {
							sql = "UPDATE "+sheet.getSubTableName()+" SET "+sheet.getSubtablesCol()+" = '"+sheet.getSubtableStatus()+"' WHERE "+sheet.getSubtableRela()+" = '"+formId+"'";
							this.tblSystemSheetTableMapper.executeSql(sql);
						}
						sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_YWC+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
						taskInfo = new TblFlowTaskInfo();
						taskInfo.setTaskTitle(preTaskInfo.getTaskTitle());
						taskInfo.setCurrentStaffId(loginStaff.getStaffid());
						taskInfo.setCurrenRole(preTaskInfo.getNextRole());
						taskInfo.setOperation("完成");
						taskInfo.setCommont(flowModel.getHandleOpinion());
						taskInfo.setFlowTaskId(operator.getFid());
						taskInfo.setTaskNo(preTaskInfo.getTaskNo());
						taskInfo.setModuleType(sheet.getTableType());
						taskInfo.setProcessId(operator.getTaskId());
						taskInfo.setFromId(formId.toString());
						taskInfo.setFlowId(flowModel.getFlowId());
						taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
						taskInfo.setTaskStatus(1);
						taskInfo.setThisStepId("end");
						this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
						if(HttpClient.ifSendOa){  //待页面传递参数未完全
							zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//							messageToDoZzController.sendDealResult(preTaskInfo,1,0);
//							messageToDoZzController.sendEndMessage(taskInfo, startStaff,startStaff,3,loginStaff);
						}
						message.setMuessage(taskInfo.getTaskTitle()+"已由"+loginStaff.getRealname()+"同意");
						message.setDoType(1);
						message.setFlowUrl("/ymWrok/getEditInfo");
						message.setFlowId(taskInfo.getFlowId());
						message.setId(taskInfo.getProcessId());
						messageList.add(message);
						jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
					}else {
						//流程审批未结束
						
						//1.获取所有未处理的审批节点序号
						nodeCodeList = this.selectNodeCodeGroupeList(operator.getTaskId());
						if(nodeCodeList.contains(preTaskInfo.getThisStepId())) {
							//当前流程流程审批节点未结束，只将当前待办消息结束
							if(HttpClient.ifSendOa){  //待页面传递参数未完全
								zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//								messageToDoZzController.sendDealResult(preTaskInfo,2,0);
//								messageToDoZzController.sendEndMessage(taskInfo,startStaff, nextStaff,0,loginStaff);
//								messageToDoZzController.sendEndMessage(preTaskInfo,startStaff, startStaff,3,loginStaff);
//								messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
								zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
							}
						}else {
							//当前流程审批节点已结束，存入下一步待办审批节点
							//获取所有未处理的流程审批节点信息
							noList = this.selectAllNoDealOperatorList(operator.getTaskId());
							sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_SPZ+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
							
							if(StringUtils.isNotBlank(flowModel.getPkYmStaffId())) {
								//判断为加签状态
								nextStaff = tblStaffMapper.selectByUserId(flowModel.getFreeApproverStaffId().toString());
								for (FlowTaskOperator no : noList) {
									taskInfo = new TblFlowTaskInfo();
									taskInfo.setTaskTitle(preTaskInfo.getTaskTitle());
									taskInfo.setCurrentStaffId(loginStaff.getStaffid());
									taskInfo.setCurrenRole(preTaskInfo.getNextRole());
									taskInfo.setOperation("通过");
									taskInfo.setCommont(flowModel.getHandleOpinion());
									taskInfo.setFlowTaskId(operator.getTaskId());
									taskInfo.setThisStepId(preTaskInfo.getThisStepId());
									taskInfo.setTaskNo(preTaskInfo.getTaskNo());
									taskInfo.setModuleType(sheet.getTableType());
									taskInfo.setProcessId(operator.getTaskId());
									taskInfo.setFromId(formId.toString());
									taskInfo.setFlowId(flowModel.getFlowId());
									taskInfo.setNextStaffId(nextStaff.getStaffid());
									taskInfo.setNextRole("加签人");
									taskInfo.setOperatorId(no.getFid());
									taskInfo.setTaskStatus(0);
									taskInfo.setTaskNodeId(no.getTaskNodeId());
									taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId());
									this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
									if(HttpClient.ifSendOa){  //待页面传递参数未完全
										zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//										messageToDoZzController.sendDealResult(preTaskInfo,1,0);
//										messageToDoZzController.sendEndMessage(taskInfo,startStaff, nextStaff,0,loginStaff);
//										messageToDoZzController.sendEndMessage(preTaskInfo,startStaff, startStaff,3,loginStaff);
										zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
//										messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
									}
								}
							}else {
								
								for (FlowTaskOperator no : noList) {
									if(!nextStaffMap.containsKey(no.getHandleId())) {
										nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(no.getHandleId());
										nextStaffMap.put(no.getHandleId(), nextStaff);
									}else{
										nextStaff = nextStaffMap.get(no.getHandleId());
									}
									taskInfo = new TblFlowTaskInfo();
									taskInfo.setTaskTitle(preTaskInfo.getTaskTitle());
									taskInfo.setCurrentStaffId(loginStaff.getStaffid());
									taskInfo.setCurrenRole(preTaskInfo.getNextRole());
									taskInfo.setOperation("通过");
									taskInfo.setCommont(flowModel.getHandleOpinion());
									taskInfo.setFlowTaskId(no.getTaskId());
									taskInfo.setThisStepId(no.getNodeCode());
									taskInfo.setTaskNo(preTaskInfo.getTaskNo());
									taskInfo.setModuleType(sheet.getTableType());
									taskInfo.setProcessId(no.getTaskId());
									taskInfo.setFromId(formId.toString());
									taskInfo.setFlowId(flowModel.getFlowId());
									taskInfo.setNextStaffId(nextStaff.getStaffid());
									taskInfo.setNextRole(no.getNodeName());
									taskInfo.setOperatorId(no.getFid());
									taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
									taskInfo.setTaskStatus(0);
									taskInfo.setTaskNodeId(no.getTaskNodeId());
									this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
									this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"通过",no.getTaskId());
									if(HttpClient.ifSendOa){  //待页面传递参数未完全
										zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
//										messageToDoZzController.sendEndMessage(preTaskInfo,startStaff,startStaff,3,loginStaff);
//										messageToDoZzController.sendEndMessage(taskInfo,startStaff,nextStaff,0,loginStaff);
//										messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
										zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
										//获取所有需要改变状态的集合
										List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
										for (TblFlowTaskInfo tblFlowTaskInfo : preTaskInfoList) {
											messageToDoZzController.sendDealResult(tblFlowTaskInfo,1,0);
										}
									}
									message.setMuessage(taskInfo.getTaskTitle()+"已由"+loginStaff.getRealname()+"通过。");
									message.setDoType(1);
									message.setFlowUrl("/ymWrok/getEditInfo");
									message.setFlowId(taskInfo.getFlowId());
									message.setId(taskInfo.getProcessId());
									messageList.add(message);
									jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
								}
							}
						}
					}
					this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
					this.tblSystemSheetTableMapper.executeSql(sql);
					//处理催办信息
					this.dealPressMessageInfo(preTaskInfo.getFlowId(),preTaskInfo.getProcessId(),loginStaff.getStaffid());
				}
				
				//处理抄送信息
				if(StringUtils.isNotBlank(flowModel.getCopyIds())) {
					for (TblStaff cs : copyStaffList) {
						info = new TblFlowInformInfo();
						info.setInfoId(RandomUtil.uuBigDecimalId());
						info.setCreateStaff(loginStaff.getStaffid());
						info.setCreateTime(new Date());
						info.setInformStaffId(cs.getStaffid());
						info.setId(operator.getTaskId());
						info.setFlowId(preTaskInfo.getFlowId());
						info.setFormId(formId.toString());
						info.setThisStepId(preTaskInfo.getThisStepId());
						info.setIsRead(0);
						this.tblFlowInformInfoMapper.insert(info);
					}
				}
			}
		} finally{
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	@Override
	public void insertAddSignInfo(TblSystemSheetTable sheet, BigDecimal formId, FlowTask task, TblStaffUtil loginStaff,
			String id, String flowid, String taskid, String signImg, String handleOpinion, String addSignType,
			Integer counterSign, Integer auditRatio, String addSignUserIds, TblStaff startStaff, FlowTaskOperator preOper) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		TblFlowTaskInfo taskInfo = null;
		TblStaff nextStaff = null;
		
		try {
			String taskTitle = task.getFullName();
			String flowCode = task.getFlowCode();
			String processId = task.getProcessId();
			String flowId = task.getFlowId();
			
			//查找上一步审批办理人信息
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,taskid,sheet.getTableType(),loginStaff.getStaffid());
			
			jedis = JedisUtil.getJedis();
			String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			
					//获取flowTaskoperator 未处理的事项
					List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(taskid);
					//通过flowtask的fid 获取当前taskInfo所有信息集合
					List<TblFlowTaskInfo> taskInfoList = this.tblFlowTaskInfoMapper.selectAllTaskInfoListByTaskId(taskid);
					
					//从taskInfoList 中 获取所有未处理的taskInfo 信息
					List<TblFlowTaskInfo> unTaskInfoList = taskInfoList.stream().filter(e -> e.getTaskStatus() == 0).collect(Collectors.toList());
					
						Integer groupId = preTaskInfo.getTaskGroupId()+1;
						int index = 1;
						
						//当前审批节点已通过 进入下一审批节点
						List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(0);
						
						//查找业务中台数据库中是否有自动审批通过的记录
						List<FlowTaskOperator> autoOperList = this.getAutoAuditOperaListByTaskId(taskid,preOper.getNodeCode());
						
						boolean isDeal = false;
						//循环比对当前FlowTask taskId  所有的taksInfo中是否已存入这些自动审批记录 未存入的重新存入
						if(autoOperList != null && autoOperList.size() > 0) {
							for (FlowTaskOperator autoOper : autoOperList) {
								isDeal = taskInfoList.stream().anyMatch(t -> StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(autoOper.getFid()));
								if(!isDeal) {
									newOperList.add(autoOper);
								}
							}
						}
						
						//比较未处理的operList 和 已插入并且未处理的taskInfoList 获取到需要处理的节点
						for (FlowTaskOperator op : operList) {
							isDeal = unTaskInfoList.stream().anyMatch(t -> (StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(op.getFid())));
							if(!isDeal) {
								newOperList.add(op);
							}
						}
						
						//循环插入未存入的审批记录信息 F_Automation = 1 的 为自动同意审批通过的记录
						for (FlowTaskOperator dop : newOperList) {
							nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(dop.getHandleId());
							taskInfo = new TblFlowTaskInfo();
							taskInfo.setTaskTitle(taskTitle);
							taskInfo.setFlowTaskId(id);
							taskInfo.setThisStepId(dop.getNodeCode());
							taskInfo.setCurrenRole(dop.getNodeName());
							taskInfo.setTaskNo(flowCode);
							taskInfo.setModuleType(sheet.getTableType());
							taskInfo.setProcessId(processId);
							taskInfo.setFromId(formId.toString());
							taskInfo.setFlowId(flowId);
							taskInfo.setOperatorId(dop.getFid());
							taskInfo.setTaskGroupId(groupId);
							taskInfo.setTaskNodeId(dop.getTaskNodeId());
							taskInfo.setCurrentStaffId(loginStaff.getStaffid());
							taskInfo.setCurrenRole(preOper.getNodeName());
							taskInfo.setNextStaffId(nextStaff.getStaffid());
							taskInfo.setNextRole(dop.getNodeName());
							taskInfo.setTaskStatus(0);
							taskInfo.setOperation("加签");
							taskInfo.setCommont(handleOpinion);
							
							this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
							groupId = groupId + index;
							
							if(HttpClient.ifSendOa && !"1".equals(dop.getAutomation())){  //待页面传递参数未完全
								//OA对接发送待办信息
								zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
								zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
							}
						}
						
						if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
							//获取所有需要改变状态的集合
							List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
							for (TblFlowTaskInfo tblFlowTaskInfo : preTaskInfoList) {
								zhSynchronizationController.changeTaskStatus(tblFlowTaskInfo,2,loginStaff);
							}
						}
						this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
						this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"通过",processId);
						
				message.setMuessage(taskTitle+"已由"+loginStaff.getRealname()+"通过。");
				message.setDoType(1);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
			
			//处理催办消息集合
			this.dealPressMessageInfo(flowId,processId,loginStaff.getStaffid());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	@Override
	public void insertTransferInfo(TblSystemSheetTable sheet, BigDecimal formId, FlowTask task, TblStaffUtil loginStaff,
			String id, String flowid, String taskid, String signImg, String handleOpinion, List<String> ymStaffIds,
			TblStaff startStaff, FlowTaskOperator preOper) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		TblFlowTaskInfo taskInfo = null;
		TblStaff nextStaff = null;
		
		try {
			String taskTitle = task.getFullName();
			String flowCode = task.getFlowCode();
			String processId = task.getProcessId();
			String flowId = task.getFlowId();
			
			//查找上一步审批办理人信息
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,taskid,sheet.getTableType(),loginStaff.getStaffid());
			
			
			jedis = JedisUtil.getJedis();
			String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			
					//获取flowTaskoperator 未处理的事项
					List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(taskid);
					//通过flowtask的fid 获取当前taskInfo所有信息集合
					List<TblFlowTaskInfo> taskInfoList = this.tblFlowTaskInfoMapper.selectAllTaskInfoListByTaskId(taskid);
					
					//从taskInfoList 中 获取所有未处理的taskInfo 信息
					List<TblFlowTaskInfo> unTaskInfoList = taskInfoList.stream().filter(e -> e.getTaskStatus() == 0).collect(Collectors.toList());
					
						Integer groupId = preTaskInfo.getTaskGroupId()+1;
						int index = 1;
						
						//当前审批节点已通过 进入下一审批节点
						List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(0);
						
						//查找业务中台数据库中是否有自动审批通过的记录
						List<FlowTaskOperator> autoOperList = this.getAutoAuditOperaListByTaskId(taskid,preOper.getNodeCode());
						
						boolean isDeal = false;
						//循环比对当前FlowTask taskId  所有的taksInfo中是否已存入这些自动审批记录 未存入的重新存入
						if(autoOperList != null && autoOperList.size() > 0) {
							for (FlowTaskOperator autoOper : autoOperList) {
								isDeal = taskInfoList.stream().anyMatch(t -> StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(autoOper.getFid()));
								if(!isDeal) {
									newOperList.add(autoOper);
								}
							}
						}
						
						//比较未处理的operList 和 已插入并且未处理的taskInfoList 获取到需要处理的节点
						for (FlowTaskOperator op : operList) {
							isDeal = unTaskInfoList.stream().anyMatch(t -> (StringUtils.isNotBlank(t.getOperatorId()) && t.getOperatorId().equals(op.getFid())));
							if(!isDeal) {
								newOperList.add(op);
							}
						}
						
						//循环插入未存入的审批记录信息 F_Automation = 1 的 为自动同意审批通过的记录
						for (FlowTaskOperator dop : newOperList) {
							nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(dop.getHandleId());
							taskInfo = new TblFlowTaskInfo();
							taskInfo.setTaskTitle(taskTitle);
							taskInfo.setFlowTaskId(id);
							taskInfo.setThisStepId(dop.getNodeCode());
							taskInfo.setCurrenRole(dop.getNodeName());
							taskInfo.setTaskNo(flowCode);
							taskInfo.setModuleType(sheet.getTableType());
							taskInfo.setProcessId(processId);
							taskInfo.setFromId(formId.toString());
							taskInfo.setFlowId(flowId);
							taskInfo.setOperatorId(dop.getFid());
							taskInfo.setTaskGroupId(groupId);
							taskInfo.setTaskNodeId(dop.getTaskNodeId());
							taskInfo.setCurrentStaffId(loginStaff.getStaffid());
							taskInfo.setCurrenRole(preOper.getNodeName());
							taskInfo.setNextStaffId(nextStaff.getStaffid());
							taskInfo.setNextRole(dop.getNodeName());
							taskInfo.setTaskStatus(0);
							taskInfo.setOperation("转审");
							taskInfo.setCommont(handleOpinion);
							
							this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
							groupId = groupId + index;
							
							if(HttpClient.ifSendOa && !"1".equals(dop.getAutomation())){  //待页面传递参数未完全
								//OA对接发送待办信息
								zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
								zhSynchronizationController.changeTaskStatus(preTaskInfo,2,loginStaff);
							}
						}
						
						if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
							//获取所有需要改变状态的集合
							List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
							for (TblFlowTaskInfo tblFlowTaskInfo : preTaskInfoList) {
								zhSynchronizationController.changeTaskStatus(tblFlowTaskInfo,2,loginStaff);
							}
						}
						this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
						this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"通过",processId);
						
				message.setMuessage(taskTitle+"已由"+loginStaff.getRealname()+"通过。");
				message.setDoType(1);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
			
			//处理催办消息集合
			this.dealPressMessageInfo(flowId,processId,loginStaff.getStaffid());
		}finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	private List<String> selectNodeCodeGroupeList(String taskId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> opList = new ArrayList<String>(0);
		String nodeCode = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_NodeCode FROM flow_taskoperator WHERE F_TaskId = ? AND F_HandleTime IS NULL GROUP BY F_NodeCode");
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				nodeCode = rs.getString("F_NodeCode");
				opList.add(nodeCode);
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return opList;
	}


	private List<FlowTaskOperator> selectAllNoDealOperatorList(String taskId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FlowTaskOperator> opList = new ArrayList<FlowTaskOperator>(0);
		FlowTaskOperator operator = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_Id,F_HandleType,F_HandleId,F_HandleStatus,F_HandleTime,F_NodeCode,F_NodeName,F_Completion,F_CreatorTime,F_TaskNodeId,F_TaskId,F_Type,F_State,F_ParentId"
					+ ",F_Automation,F_SortCode,F_Reject,F_Automation FROM flow_taskoperator WHERE F_TaskId = ? AND F_HandleTime IS NULL ");
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				operator = new FlowTaskOperator();
				operator.setFid(rs.getString("F_Id"));
				operator.setHandleType(rs.getString("F_HandleType"));
				operator.setHandleId(rs.getString("F_HandleId"));
				operator.setHandleStatus(rs.getInt("F_HandleStatus"));
				operator.setHandleTime(rs.getDate("F_HandleTime"));
				operator.setNodeCode(rs.getString("F_NodeCode"));
				operator.setNodeName(rs.getString("F_NodeName"));
				operator.setCompletion(rs.getInt("F_Completion"));
				operator.setCreatorTime(rs.getDate("F_CreatorTime"));
				operator.setTaskNodeId(rs.getString("F_TaskNodeId"));
				operator.setTaskId(rs.getString("F_TaskId"));
				operator.setType(rs.getString("F_Type"));
				operator.setState(rs.getString("F_State"));
				operator.setParentId(rs.getString("F_ParentId"));
				operator.setAutomation(rs.getString("F_Automation"));
				operator.setSortCode(rs.getInt("F_SortCode"));
				operator.setReject(rs.getString("F_Reject"));
				operator.setAutomation(rs.getString("F_Automation"));
				opList.add(operator);
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return opList;
	}


	@Override
	public FlowTaskOperator selectCurrentOperator(String fid) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FlowTaskOperator operator = new FlowTaskOperator();
		try {
			String sql = YMDifferentVConfig.getFlowOperatorSql();
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, fid);
			rs = ps.executeQuery();
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					while (rs.next()) {
						operator.setFid(rs.getString("f_id"));
						operator.setHandleId(rs.getString("f_handle_id"));
						operator.setHandleStatus(rs.getInt("f_handle_status"));
						operator.setHandleTime(rs.getDate("f_handle_time"));
						operator.setNodeCode(rs.getString("f_node_code"));
						operator.setNodeName(rs.getString("f_node_name"));
						operator.setCompletion(rs.getInt("f_completion"));
						operator.setTaskNodeId(rs.getString("f_id"));
						operator.setTaskId(rs.getString("f_task_id"));
						operator.setState(rs.getString("f_status"));
						operator.setParentId(rs.getString("f_parent_id"));
					}
					break;
				default:
					while (rs.next()) {
						operator.setFid(rs.getString("F_Id"));
						operator.setHandleType(rs.getString("F_HandleType"));
						operator.setHandleId(rs.getString("F_HandleId"));
						operator.setHandleStatus(rs.getInt("F_HandleStatus"));
						operator.setHandleTime(rs.getDate("F_HandleTime"));
						operator.setNodeCode(rs.getString("F_NodeCode"));
						operator.setNodeName(rs.getString("F_NodeName"));
						operator.setCompletion(rs.getInt("F_Completion"));
						operator.setCreatorTime(rs.getDate("F_CreatorTime"));
						operator.setTaskNodeId(rs.getString("F_TaskNodeId"));
						operator.setTaskId(rs.getString("F_TaskId"));
						operator.setType(rs.getString("F_Type"));
						operator.setState(rs.getString("F_State"));
						operator.setParentId(rs.getString("F_ParentId"));
						operator.setAutomation(rs.getString("F_Automation"));
						operator.setSortCode(rs.getInt("F_SortCode"));
						operator.setReject(rs.getString("F_Reject"));
					}
					break;
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return operator;
	}


	public Integer selectNoDealOperator(String taskId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer fid = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT count(0) FROM flow_taskoperator WHERE F_TaskId = ? AND F_HandleTime IS NULL ");
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				fid = rs.getInt(1);
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return fid;
	}
	
	
	@Override
	public FlowTask getFlowTaskById(String ymFromId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FlowTask flowTask = null;
		try {
			String sql = YMDifferentVConfig.getFlowTaskInfoSql();
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, ymFromId);
			rs = ps.executeQuery();
			
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					while (rs.next()) {
						flowTask = new FlowTask();
						flowTask.setId(rs.getString("f_id"));
						flowTask.setProcessId(rs.getString("f_id"));
						flowTask.setEnCode(rs.getString("f_en_code"));
						flowTask.setFullName(rs.getString("f_full_name"));
						flowTask.setFlowUrgent(rs.getInt("f_urgent"));
						flowTask.setFlowId(rs.getString("f_flow_id"));
						flowTask.setFlowCode(rs.getString("f_flow_code"));
						flowTask.setFlowName(rs.getString("f_flow_name"));
						flowTask.setFlowType(rs.getInt("f_flow_type"));
						flowTask.setFlowCategory(rs.getString("f_flow_category"));
						flowTask.setFlowVersion(rs.getString("f_flow_version"));
						flowTask.setThisStep(rs.getString("f_current_node_code"));
						flowTask.setThisStepId(rs.getString("f_instance_id"));
						flowTask.setStatus(rs.getInt("f_status"));
						flowTask.setParentId(rs.getString("f_parent_id"));
						flowTask.setIsAsync(rs.getInt("f_is_async"));
						flowTask.setRejectId(rs.getString("f_reject_data_id"));
					}
					break;
				default:
					while (rs.next()) {
						flowTask = new FlowTask();
						flowTask.setId(rs.getString("F_Id"));
						flowTask.setProcessId(rs.getString("F_ProcessId"));
						flowTask.setEnCode(rs.getString("F_EnCode"));
						flowTask.setFullName(rs.getString("F_FullName"));
						flowTask.setFlowUrgent(rs.getInt("F_FlowUrgent"));
						flowTask.setFlowId(rs.getString("F_FlowId"));
						flowTask.setFlowCode(rs.getString("F_FlowCode"));
						flowTask.setFlowName(rs.getString("F_FlowName"));
						flowTask.setFlowType(rs.getInt("F_FlowType"));
						flowTask.setFlowCategory(rs.getString("F_FlowCategory"));
						flowTask.setFlowVersion(rs.getString("F_FlowVersion"));
						flowTask.setThisStep(rs.getString("F_ThisStep"));
						flowTask.setThisStepId(rs.getString("F_ThisStepId"));
						flowTask.setStatus(rs.getInt("F_Status"));
						flowTask.setCompletion(rs.getInt("F_Completion"));
						flowTask.setCreatorUserId(rs.getString("F_CreatorUserId"));
						flowTask.setParentId(rs.getString("F_ParentId"));
						flowTask.setIsAsync(rs.getInt("F_IsAsync"));
						flowTask.setIsBatch(rs.getInt("F_IsBatch"));
						flowTask.setTaskNodeId(rs.getString("F_TaskNodeId"));
						flowTask.setFormType(rs.getInt("F_FormType"));
						flowTask.setRejectId(rs.getString("F_RejectDataId"));
					}
					break;
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return flowTask;
	}
	
	
	/**
	 * 流程提交审批时获取taskNode
	 * @param taskId 
	 * @return flowTaskNode
	 */
	private FlowTaskNode getSubmitTaskNodeInfo(String taskId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FlowTaskNode taskNode = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_ID,F_NodeCode,F_NodeName,F_NodeType,F_NodeUp,F_NodeNext,F_Completion,F_SortCode,F_TaskId FROM flow_tasknode WHERE F_TaskId = ? AND F_NodeType = 'start' AND F_SortCode = 1");
			ps.setString(1, taskId);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				taskNode = new FlowTaskNode();
				taskNode.setFid(rs.getString("F_Id"));
				taskNode.setNodeCode(rs.getString("F_NodeCode"));
				taskNode.setNodeName(rs.getString("F_NodeName"));
				taskNode.setNodeType(rs.getString("F_NodeType"));
				taskNode.setNodeup(rs.getString("F_NodeUp"));
				taskNode.setNodeNext(rs.getString("F_NodeNext"));
				taskNode.setCompletion(rs.getInt("F_Completion"));
				taskNode.setSortCode(rs.getInt("F_SortCode"));
				taskNode.setTaskId(rs.getString("F_TaskId"));
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return taskNode;
	}
	
	@Override
	public FlowTaskNode getFlowTaskNodeByTaskIdNodeCode(String id, String nodeCode) throws Exception{
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FlowTaskNode taskNode = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_ID,F_NodeCode,F_NodeName,F_NodeType,F_NodeUp,F_NodeNext,F_Completion,F_SortCode,F_TaskId FROM flow_tasknode WHERE F_TaskId = ? AND F_NodeCode = ? ");
			ps.setString(1, id);
			ps.setString(2, nodeCode);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				taskNode = new FlowTaskNode();
				taskNode.setFid(rs.getString("F_Id"));
				taskNode.setNodeCode(rs.getString("F_NodeCode"));
				taskNode.setNodeName(rs.getString("F_NodeName"));
				taskNode.setNodeType(rs.getString("F_NodeType"));
				taskNode.setNodeup(rs.getString("F_NodeUp"));
				taskNode.setNodeNext(rs.getString("F_NodeNext"));
				taskNode.setCompletion(rs.getInt("F_Completion"));
				taskNode.setSortCode(rs.getInt("F_SortCode"));
				taskNode.setTaskId(rs.getString("F_TaskId"));
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return taskNode;
	}

	
	/**
	 * 通过task的主键和taskNode的nodeNext 获取所有需要下一步办理人办理的信息；
	 * @param ymFromId
	 * @param status 
	 * @return
	 */
	private List<FlowTaskOperator> getNextTaskOperatorListInfo(String ymFromId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FlowTaskOperator> operList = new ArrayList<FlowTaskOperator>(0);
		FlowTaskOperator oper = null;
		try {
			String sql = YMDifferentVConfig.getNextTaskOperatorListSql(ymFromId);
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, ymFromId);
			rs = ps.executeQuery();
			
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("f_id"));
						oper.setHandleId(rs.getString("f_handle_id"));
						oper.setHandleStatus(rs.getInt("f_handle_status"));
						oper.setNodeCode(rs.getString("f_node_code"));
						oper.setNodeName(rs.getString("f_node_name"));
						oper.setCompletion(rs.getInt("f_completion"));
						oper.setTaskNodeId(rs.getString("f_node_id"));
						oper.setTaskId(rs.getString("f_task_id"));
						oper.setState(rs.getString("f_status"));
						oper.setParentId(rs.getString("f_parent_id"));
						operList.add(oper);
					}
					break;
				default:
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("F_Id"));
						oper.setHandleType(rs.getString("F_HandleType"));
						oper.setHandleId(rs.getString("F_HandleId"));
						oper.setHandleStatus(rs.getInt("F_HandleStatus"));
						oper.setNodeCode(rs.getString("F_NodeCode"));
						oper.setNodeName(rs.getString("F_NodeName"));
						oper.setCompletion(rs.getInt("F_Completion"));
						oper.setTaskNodeId(rs.getString("F_TaskNodeId"));
						oper.setTaskId(rs.getString("F_TaskId"));
						oper.setType(rs.getString("F_Type"));
						oper.setState(rs.getString("F_State"));
						oper.setParentId(rs.getString("F_ParentId"));
						oper.setReject(rs.getString("F_Reject"));
						oper.setAutomation(rs.getString("F_Automation"));
						operList.add(oper);
					}
					break;
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return operList;
	}
	
	/**
	 * 通过taskID 获取所有自动审批通过的信息记录
	 * @param id
	 * @param nodeCode 
	 * @return
	 */
	private List<FlowTaskOperator> getAutoAuditOperaListByTaskId(String id, String nodeCode) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FlowTaskOperator> operList = new ArrayList<FlowTaskOperator>(0);
		FlowTaskOperator oper = null;
		try {
			
			String sql = YMDifferentVConfig.getAutoAuditOperaListSql();
			
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, nodeCode);
			rs = ps.executeQuery();
			
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("f_id"));
						oper.setHandleId(rs.getString("f_handle_id"));
						oper.setHandleStatus(rs.getInt("f_handle_status"));
						oper.setNodeCode(rs.getString("f_node_code"));
						oper.setNodeName(rs.getString("f_node_name"));
						oper.setCompletion(rs.getInt("f_completion"));
						oper.setTaskId(rs.getString("f_task_id"));
						oper.setState(rs.getString("f_status"));
						oper.setParentId(rs.getString("f_parent_id"));
						operList.add(oper);
					}
					break;
				default:
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("F_Id"));
						oper.setHandleType(rs.getString("F_HandleType"));
						oper.setHandleId(rs.getString("F_HandleId"));
						oper.setHandleStatus(rs.getInt("F_HandleStatus"));
						oper.setNodeCode(rs.getString("F_NodeCode"));
						oper.setNodeName(rs.getString("F_NodeName"));
						oper.setCompletion(rs.getInt("F_Completion"));
						oper.setTaskNodeId(rs.getString("F_TaskNodeId"));
						oper.setTaskId(rs.getString("F_TaskId"));
						oper.setType(rs.getString("F_Type"));
						oper.setState(rs.getString("F_State"));
						oper.setParentId(rs.getString("F_ParentId"));
						oper.setReject(rs.getString("F_Reject"));
						oper.setAutomation(rs.getString("F_Automation"));
						operList.add(oper);
					}
					break;
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return operList;
	}
	
	
	private Integer getEndStatusByFlowTask(String id) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer count = 0;
		try {
			String sql = YMDifferentVConfig.getEndFlowTaskCount();
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				count = rs.getInt("ENDCO");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return count;
	}


	@Override
	public void insertSendBacknfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String handleOpinion, String backNodeCode, String backNodeName, String taskid, String flowid) throws Exception {
		Jedis jedis = null;
		FlowMessageVo message = new FlowMessageVo();
		List<FlowMessageVo> messageList = new ArrayList<FlowMessageVo>(0);
		try {
			
			TblStaff nextStaff = null;
			String thisStep = null;
			TblFlowTaskInfo taskInfo = null;
			String taskTitle = null;
			
			jedis = JedisUtil.getJedis();
			
			//flowtask主键 获取提交审批节点，taskNode信息
			FlowTask task = this.getFlowTaskById(taskid);
			String flowId = task.getFlowId();
			taskTitle = task.getFullName();
			
			String processId = task.getProcessId();
			
			
			//查找流程发起人，通过表单主键和taskId 和 flowId
			TblStaff startStaff = this.tblStaffMapper.selectSubmitStaffByFormIdTaskId(formId,taskid,flowId);
			
			TblFlowTaskInfo preTaskInfo = this.tblFlowTaskInfoMapper.selectPreTaskInfo(formId,flowId,taskid,sheet.getTableType(),loginStaff.getStaffid());
			
			//查找当前流程中所有未处理的流程节点信息
			List<TblFlowTaskInfo> unDealInfoList = this.tblFlowTaskInfoMapper.selectUntreatedInfoList(taskid);
			List<FlowTaskOperator> operList = this.getNextTaskOperatorListInfo(taskid);
			
			//对比出未存入的待处理信息
			boolean isDeal = false;
			List<FlowTaskOperator> newOperList = new ArrayList<FlowTaskOperator>(); 
			for (FlowTaskOperator op : operList) {
				isDeal = unDealInfoList.stream().anyMatch(t -> t.getOperatorId().equals(op.getFid()));
				if(!isDeal) {
					newOperList.add(op);
				}
			}
			
			boolean isStart = false;
			//获取通知消息
			String submitMessage = jedis.get(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE);
			if(submitMessage != null && !"".equals(submitMessage)) {
				messageList = JSONObject.parseArray(submitMessage, FlowMessageVo.class);
			}
			
			if("流程发起".equals(backNodeName)) {
				isStart = true;
			}
			
			if(newOperList != null && newOperList.size() > 0) {
				//拒绝到指定节点导致多人审批
				for (FlowTaskOperator oper : newOperList) {
					nextStaff = this.tblStaffMapper.selectStaffInfoByYmStaffId(oper.getHandleId());
					thisStep = oper.getNodeName();
					
					taskInfo = new TblFlowTaskInfo();
					taskInfo.setTaskTitle(task.getFullName());
					taskInfo.setCurrentStaffId(loginStaff.getStaffid());
					taskInfo.setCurrenRole(preTaskInfo.getNextRole());
					taskInfo.setOperation("退回");
					taskInfo.setCommont(handleOpinion);
					taskInfo.setFlowTaskId(taskid);
					taskInfo.setThisStepId(oper.getNodeCode());
					taskInfo.setTaskNo(task.getFlowCode());
					taskInfo.setModuleType(sheet.getTableType());
					taskInfo.setProcessId(processId);
					taskInfo.setFromId(formId.toString());
					taskInfo.setFlowId(task.getFlowId());
					taskInfo.setNextStaffId(nextStaff.getStaffid());
					taskInfo.setNextRole(thisStep);
					taskInfo.setOperatorId(oper.getFid());
					taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
					taskInfo.setTaskStatus(0);
					taskInfo.setTaskNodeId(oper.getTaskNodeId());
					this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
					if(HttpClient.ifSendOa){  //待页面传递参数未完全
//						messageToDoZzController.sendDealResult(preTaskInfo,1,3);
//						messageToDoZzController.sendSubmitInfo(taskInfo,startStaff,nextStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
//						messageToDoZzController.sendEndMessage(taskInfo,startStaff, nextStaff,0,loginStaff);
						zhSynchronizationController.changeTaskStatus(taskInfo,2,loginStaff);
					}
					
					
				}
				if(HttpClient.ifSendOa){  //批量将其他OA消息全部改为 通过状态
					//获取所有需要改变状态的集合
					List<TblFlowTaskInfo> preTaskInfoList = this.tblFlowTaskInfoMapper.selectNoDealTaskInfo(preTaskInfo.getThisStepId(),preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getProcessId());
					for (TblFlowTaskInfo pre : preTaskInfoList) {
//						messageToDoZzController.sendEndMessage(pre, startStaff,startStaff,2,loginStaff);
					}
				}
				if(!isStart) {
					//通知指定人员
					message.setMuessage(taskTitle+"已由审批人"+loginStaff.getRealname()+"驳回至"+thisStep+"。");
					message.setDoType(1);
					message.setFlowUrl("/ymWrok/getEditInfo");
					message.setFlowId(flowId);
					message.setId(processId);
					messageList.add(message);
					jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				}
			}else {
				isStart = true;
				//退回至发起人 修改表单状态为需调整
				String sql = "UPDATE "+sheet.getTableName()+" SET "+sheet.getStatusPro()+" = '"+YMUrlStatic.STATE_XTZ+"' WHERE "+sheet.getPrimaryColumn()+" = '"+formId+"'";
				this.tblSystemSheetTableMapper.executeSql(sql);
					nextStaff = this.tblStaffMapper.selectByUserId(startStaff.getStaffid().toString());
					taskInfo = new TblFlowTaskInfo();
					taskInfo.setTaskTitle(task.getFullName());
					taskInfo.setCurrentStaffId(loginStaff.getStaffid());
					taskInfo.setCurrenRole(preTaskInfo.getNextRole());
					taskInfo.setOperation("退回");
					taskInfo.setCommont(handleOpinion);
					taskInfo.setFlowTaskId(taskid);
					taskInfo.setTaskNo(task.getFlowCode());
					taskInfo.setModuleType(sheet.getTableType());
					taskInfo.setProcessId(processId);
					taskInfo.setFromId(formId.toString());
					taskInfo.setFlowId(task.getFlowId());
					taskInfo.setTaskStatus(0);
					taskInfo.setThisStepId("start");
					taskInfo.setTaskGroupId(preTaskInfo.getTaskGroupId()+1);
					taskInfo.setNextStaffId(startStaff.getStaffid());
					taskInfo.setNextRole("提交人");
					taskInfo.setTaskNodeId("");
					this.tblFlowTaskInfoMapper.saveEntity(taskInfo);
					if(HttpClient.ifSendOa){  //待页面传递参数未完全
//						messageToDoZzController.sendDealResult(preTaskInfo,1,3);
//						messageToDoZzController.sendEndMessage(taskInfo, startStaff,nextStaff,0,loginStaff);
						zhSynchronizationController.changeTaskStatus(taskInfo,2 ,loginStaff);
						zhSynchronizationController.saveTask(taskInfo,startStaff,nextStaff);
					}
			}
			this.tblFlowTaskInfoMapper.updateTaskStatusGroupId(preTaskInfo.getTaskGroupId(),preTaskInfo.getTaksId(),preTaskInfo.getThisStepId(),"驳回",processId);
			this.tblFlowTaskInfoMapper.updateTaskStatus(preTaskInfo);
			
			if(isStart) {
				//通知发起人
				message.setMuessage(taskTitle+"已被"+loginStaff.getRealname()+"驳回，请及时调整。");
				message.setDoType(0);
				message.setFlowUrl("/ymWrok/getEditInfo");
				message.setFlowId(flowId);
				message.setId(processId);
				messageList.add(message);
				jedis.set(startStaff.getStaffid() + JedisUtil.SUBMITMESSAGE, JSONObject.toJSONString(messageList));
				
				if(HttpClient.ifSendOa){
					for (TblFlowTaskInfo unt : unDealInfoList) {
//						messageToDoZzController.sendDealResult(unt,1,3);
						zhSynchronizationController.changeTaskStatus(unt,2,loginStaff);
					}
				}
				//将flowTaskInfo中左右未处理的信息全部变更为已处理
				this.tblFlowTaskInfoMapper.updateTaskStatusTodeal(taskid,"start");
			}
			
			
			//处理催办消息集合
			this.dealPressMessageInfo(flowId,processId,loginStaff.getStaffid());
			this.tblFlowApproverInfoMapper.stopEntity(backNodeCode,taskid,flowId);
			
			if(sheet.getRewirteFlowInfo() != null && sheet.getRewirteFlowInfo() == 1) {
				this.rewriteFlwoInfoToEntity(sheet,formId,preTaskInfo,taskInfo,loginStaff);
			}
			
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}


	private List<FlowTaskOperator> getAssistNodeInfo(String taskid, String id) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FlowTaskOperator> operList = new ArrayList<FlowTaskOperator>(0);
		FlowTaskOperator oper = null;
		try {
			
			String sql = "SELECT f_id,f_handle_id,f_handle_status,f_handle_time,f_node_code,f_node_name,f_completion,f_task_id,f_status,f_parent_id FROM workflow_operator WHERE f_task_id = ? AND f_parent_id = ? AND f_status = 7";
			
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, taskid);
			ps.setString(1, id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				oper = new FlowTaskOperator();
				oper.setFid(rs.getString("f_id"));
				oper.setHandleId(rs.getString("f_handle_id"));
				oper.setHandleStatus(rs.getInt("f_handle_status"));
				oper.setNodeCode(rs.getString("f_node_code"));
				oper.setNodeName(rs.getString("f_node_name"));
				oper.setCompletion(rs.getInt("f_completion"));
				oper.setTaskId(rs.getString("f_task_id"));
				oper.setState(rs.getString("f_status"));
				oper.setParentId(rs.getString("f_parent_id"));
				operList.add(oper);
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return operList;
	}
	
	
	private List<FlowTaskOperator> getAlredyOperatorListInfo(String id, String nodeCode) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FlowTaskOperator> operList = new ArrayList<FlowTaskOperator>(0);
		FlowTaskOperator oper = null;
		try {
			String sql = YMDifferentVConfig.getAlredyOperatorListSql();
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, id);
			ps.setString(2, nodeCode);
			rs = ps.executeQuery();
			
			switch (YMUrlStatic.YMVERSION) {
				case "5.0+":
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("f_id"));
						oper.setHandleId(rs.getString("f_handle_id"));
						oper.setHandleStatus(rs.getInt("f_handle_status"));
						oper.setNodeCode(rs.getString("f_node_code"));
						oper.setNodeName(rs.getString("f_node_name"));
						oper.setCompletion(rs.getInt("f_completion"));
						oper.setTaskNodeId(rs.getString("f_node_id"));
						oper.setTaskId(rs.getString("f_task_id"));
						oper.setState(rs.getString("f_status"));
						oper.setParentId(rs.getString("f_parent_id"));
						operList.add(oper);
					}
					break;
				default:
					while (rs.next()) {
						oper = new FlowTaskOperator();
						oper.setFid(rs.getString("F_Id"));
						oper.setHandleType(rs.getString("F_HandleType"));
						oper.setHandleId(rs.getString("F_HandleId"));
						oper.setHandleStatus(rs.getInt("F_HandleStatus"));
						oper.setNodeCode(rs.getString("F_NodeCode"));
						oper.setNodeName(rs.getString("F_NodeName"));
						oper.setCompletion(rs.getInt("F_Completion"));
						oper.setTaskNodeId(rs.getString("F_TaskNodeId"));
						oper.setTaskId(rs.getString("F_TaskId"));
						oper.setType(rs.getString("F_Type"));
						oper.setState(rs.getString("F_State"));
						oper.setParentId(rs.getString("F_ParentId"));
						oper.setReject(rs.getString("F_Reject"));
						oper.setAutomation(rs.getString("F_Automation"));
						operList.add(oper);
					}
					break;
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return operList;
	}

}
