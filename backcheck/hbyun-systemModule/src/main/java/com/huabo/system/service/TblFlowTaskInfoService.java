package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import com.alibaba.excel.exception.ExcelAnalysisException;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblFlowApproverInfo;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.flow.FlowTask;
import com.huabo.system.flow.FlowModel;
import com.huabo.system.flow.FlowTaskNode;
import com.huabo.system.flow.FlowTaskOperator;

public interface TblFlowTaskInfoService {
	void insertSubmitInfo(String ymFromId, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal fromId) throws Exception;

	void insertAuditInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String freeApproverUserId, String handleOpinion, String[] approverUsers, TblFlowApproverInfo currentApp, TblFlowApproverInfo nextApp,
			FlowTask task, FlowTaskOperator preOper, FlowTaskNode taskNode,TblStaff startStaff) throws Exception;

	void insertTransferInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String transferStaffId, String handleOpinion, String flowTaskInfoOperatorId) throws Exception;

	void insertRejectInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String handleOpinion, String oldStepId, String operatorId) throws Exception;

	void insertActionsWithdrawInfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId, String flowId) throws Exception;

	void insertAlreadyRecall(String processId, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String flowId, String handleOpinion, String flowTaskOperatorRecordListId) throws Exception;

	void insertBatchOperationInfo(TblStaffUtil loginStaff, FlowModel flowModel) throws Exception;

	/**
	 * 根据flow_task 表主键获取 flowtask信息
	 * @param ymFromId  -- flow_task表f_id
	 * @return FlowTask
	 */
	FlowTask getFlowTaskById(String id) throws Exception;

	/**
	 * 通过operatorId主键获取当前流程操作信息
	 * @param operatorId
	 * @return
	 * @throws Exception
	 */
	FlowTaskOperator selectCurrentOperator(String operatorId) throws Exception;

	/**
	 * 通过flowtask主键和nodeCode 获取flowtasknode信息
	 * @param id   flow_taks  的 主键 fid
	 * @param nodeCode 	
	 * @return
	 * @throws Exception
	 */
	FlowTaskNode getFlowTaskNodeByTaskIdNodeCode(String id, String nodeCode) throws Exception;

	void insertSendBacknfo(String id, TblStaffUtil loginStaff, TblSystemSheetTable sheet, BigDecimal formId,
			String handleOpinion, String backNodeCode, String backNodeName, String taskid, String flowid) throws Exception;

	void insertAddSignInfo(TblSystemSheetTable sheet, BigDecimal formId, FlowTask task, TblStaffUtil loginStaff,
			String id, String flowid, String taskid, String signImg, String handleOpinion, String addSignType,
			Integer counterSign, Integer auditRatio, String addSignUserIds, TblStaff startStaff, FlowTaskOperator preOper) throws Exception;

	void insertTransferInfo(TblSystemSheetTable sheet, BigDecimal formId, FlowTask task, TblStaffUtil loginStaff,
			String id, String flowid, String taskid, String signImg, String handleOpinion, List<String> ymStaffIds,
			TblStaff startStaff, FlowTaskOperator preOper) throws Exception;

}
