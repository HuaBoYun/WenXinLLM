package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.flow.FlowModel;

public interface YMBusinessService {

	JsonBean singleSignMothed(String token, String origin) throws Exception;

	JsonBean synchronizeOrgInfo(String token, BigDecimal orgId) throws Exception;

	JsonBean synchronizeStaffInfo(String token, BigDecimal orgId) throws Exception;

	JsonBean synchronizeRoleInfo(String token, BigDecimal orgId) throws Exception;

	JsonBean saveWorkFlowFormInfo(String token, String workName, BigDecimal tableId, String ymWorkId, String typeId, Integer flowType) throws Exception;
	
	JsonBean removeWorkFlowFormInfo(String token, BigDecimal tableId, String ymWorkId) throws Exception;

	JsonBean getWorkFlowList(String token, String workName, BigDecimal tableId, Integer currentPage, Integer pageSize) throws Exception;

	JsonBean getSystemFlowList(String token, TblSystemSheetTable sheet) throws Exception;

	JsonBean submit(String token, BigDecimal fromId, String tableId, String ymFromId, String candidateType, String branchStrs, String nodeCode,String candidateList, String flowId, Integer status, String typeName, String eventType) throws Exception;
	
	JsonBean getFlowLaunch(String token, Integer currentPage, Integer pageSize, String flowName, String status) throws Exception;

	JsonBean getDealt(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;
	
	JsonBean getDealtMH(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean getInfo(String token, String id, String thisStepId, String processId, String flowId, String opType) throws Exception;

	JsonBean reject(String token, String flowId, String handleOpinion, String signImg, String copyIds,
			String candidateType, String enCode, String branchList, String id, String operatorId, String rejectStep, String rejectType, String thisStepId, String handleStatus) throws Exception;

	JsonBean getEditInfo(String token, String id, String flowId) throws Exception;

	JsonBean actionsWithdraw(String token, String id, String flowId) throws Exception;

	JsonBean audit(String token, String flowId, String handleOpinion, String signImg, String copyIds, String enCode,
			String branchStrs, String id, String operatorId, String freeApproverUserId, String candidateType, String nodeCode, String candidateList, String nextStepId, String thisStepId) throws Exception;

	JsonBean transfer(String token, String flowTaskInfoOperatorId, String handleOpinion, String transferStaffId, String signImg, String id, String flowId) throws Exception;

	JsonBean getSystemFlowType(String token) throws Exception;

	JsonBean synchronizeJobInfo(String token, BigDecimal orgId) throws Exception;

	JsonBean getYmFormData(BigDecimal tableId) throws Exception;

	JsonBean alreadyList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean copyInfoList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean copyInfoDetail(String token, String id, String thisStepId, String flowId, String operatorId) throws Exception;

	JsonBean alreadyRecall(String token, String flowTaskOperatorRecordListId, String freeApproverUserId,
			String handleOpinion, String signImg, String flowId, String processId) throws Exception;

	JsonBean candidates(String token, String flowTaskOperatorId, String id, String flowId, BigDecimal fromId, String tableId) throws Exception;

	JsonBean getWorkCount(String token) throws Exception;

	void insertSynchronizeOrgInfo(TblOrganization org, Map<String, String> headerMap, TblStaffUtil staff) throws Exception;

	void updateSynchronizeOrgInfo(TblOrganization organization, Map<String, String> headerMap, TblStaffUtil staff) throws Exception;

	void removeUniqueJobInfo(BigDecimal jobid) throws Exception;

	void removeOrgInfo(TblStaffUtil loginStaff, String pkYmOrgId) throws Exception;

	void removeRoleInfo(TblStaffUtil loginStaff, String pkYmRoleId) throws Exception;

	void synchronizeUniqueDeptInfo(TblStaffUtil staff, BigDecimal orgid) throws Exception;

	void synchronizeUniqueStaffInfo(TblStaffUtil loginStaff, BigDecimal staffId) throws Exception;

	JsonBean candidateUser(String token, String flowTaskOperatorId, String id, String flowId, BigDecimal fromId,
			Integer currentPage, Integer pageSize, String keyword, String nodeCode, String tableId) throws Exception;

	JsonBean synchronizeDeptManage(String token, BigDecimal orgId) throws Exception;

	JsonBean synchronizeStaffManageInfo(String token, BigDecimal orgId) throws Exception;

	JsonBean startYmWorkFlow(String token, BigDecimal tablId, String ymWorkForm, Integer qystatus) throws Exception;

	public JsonBean getListForId(String token,String processId) throws Exception ;

	JsonBean copyApprovalStaffList(String token, String processId) throws Exception;

	JsonBean informInfoList(String token, Integer currentPage, Integer pageSize, String informStaffName,
			Integer isRead, String id, String flowId, String createStaffName) throws Exception;

	JsonBean getFlowTaskInfo(String token, String tableId, String formId, String typeName) throws Exception;

	JsonBean fileUpload(MultipartFile[] file, String token, String flowTaskOperatorId, String flowTaskId) throws Exception;
	
	JsonBean fileuploadZH(MultipartFile[] file, String token, String flowTaskOperatorId, String flowTaskId) throws Exception;

	JsonBean fildDownload(String token, String attId, HttpServletResponse response) throws Exception;

	JsonBean fileList(String token, String flowTaskOperatorId, String flowTaskId) throws Exception;

	JsonBean fileRemove(String token, String attId) throws Exception;
	
	public void dealSetLoginOrgInfo(String pkYmOrgId, TblStaffUtil staffUtil) throws Exception;

	void dealUniqueOrgInfo(TblOrganization org, Integer orgDeal) throws Exception;

	void dealUniqueJobInfo(BigDecimal jobId) throws Exception;

	void dealUniqueRoleInfo(BigDecimal rid) throws Exception;

	void dealUniqueStaffInfo(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception;

	void dealUniqueStaffInfo2(BigDecimal staffId, List<TblUserOrgRelation> relaList) throws Exception;

	JsonBean getFlowPkInfo(String token, String tableId, String formId) throws Exception;

	JsonBean getFlowMessage(String token) throws Exception;

	JsonBean press(String token, String id, String flowId) throws Exception;

	JsonBean loginGetPress(String token) throws Exception;

	JsonBean getPressInfo(String token) throws Exception;

	JsonBean saveFlowTemplate(String token, String taskNodeId, String flowId, String tempTitle, String tempMemo) throws Exception;

	JsonBean modifyFlowTemplate(String token, String tempTitle, String tempMemo, BigDecimal tempId) throws Exception;

	JsonBean removeFlowTemplate(String token, BigDecimal tempId) throws Exception;

	JsonBean getFlowTemplateList(String token, String taskNodeId, String flowId, Integer currentPage, Integer pageSize, String tempTitle, String tempMemo) throws Exception;

	JsonBean getFlowTemplateInfo(String token, BigDecimal tempId) throws Exception;

	JsonBean rejectList(String token, String operatorId) throws Exception;

	void dealUserRoleRelation(String roleid) throws Exception;

	JsonBean copyFlowInfo(String token, String ymWorkFrom, BigDecimal tableId, String orgIds) throws Exception;

	JsonBean getProcessInfoList() throws Exception;

	JsonBean paikeSingLogin(String token) throws Exception;

	JsonBean getFLowInfo(String id) throws Exception;

	JsonBean removeWorkFlowContractType(String token, BigDecimal activityId) throws Exception;

	JsonBean getContractTypeFlowList(String token, String workName, BigDecimal tableId, BigDecimal typeId,
			Integer currentPage, Integer pageSize) throws Exception;

	JsonBean copyContractTypeFlow(String token, String activityId, String orgIds) throws Exception;

	JsonBean startContractTypeFlow(String token, String activityId, Integer qystatus) throws Exception;

	JsonBean batchList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean batchCandidate(String token, String flowId, String id) throws Exception;

	JsonBean batchCandidateUser(String token, String id, String nodeCode, String flowId, Integer currentPage, Integer pageSize) throws Exception;

	JsonBean batchOperation(String token, FlowModel flowModel) throws Exception;

	void dealUserRoleRelationUniqueRight(String substring, String staffids) throws Exception;
	
	void fileDownLoadZH(HttpServletResponse response, String fileId, Boolean isCa) throws Exception;

	JsonBean delete(String token, String ymFromId, String flowId) throws Exception;

	void removeFlowInfo(String ymWorkId) throws Exception;

}
