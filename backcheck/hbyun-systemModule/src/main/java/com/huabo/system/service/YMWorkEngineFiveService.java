package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.flow.FlowModel;
import com.huabo.system.util.OrganizeAdminIsTratorCrForm;

public interface YMWorkEngineFiveService {

	/**
	 * 业务中台5.0版本 保存流程基础信息
	 * @param description	流程描述
	 * @param enCode		流程编号
	 * @param fullName		流程名称
	 * @param tableid
	 * @param typeid
	 * @param category
	 * @param flowType      流程类型：0-标准流程 1-简单流程 2-任务流程
	 * @return
	 */
	JsonBean saveFlowInfoFive(String description, String enCode, String fullName, String id, BigDecimal tableid, String typeid, String category, Integer flowType) throws Exception;

	JsonBean getDictionaryData() throws Exception;
	
	JsonBean saveWorkFlowEngineInfo(String token, String flowId, String tableId, String id, String typeId) throws Exception;

	JsonBean transact(String[] ids) throws Exception;

	JsonBean sendBack(String id, String flowid, String backNodeCode, String handleOpinion, String signImg, String copyIds, String candidateList, String backType, String taskid, String backNodeName) throws Exception;

	JsonBean assist(String id, String flowid, String handleIds, String handleOpinion, String signImg, String nodeCode,
			String pause, String taskid) throws Exception;

	JsonBean addSign(String id, String flowid, String taskid, String signImg, String handleOpinion, String addSignType,
			Integer counterSign, Integer auditRatio, String addSignUserIds) throws Exception;

	JsonBean transfer(String id, String flowid, String taskid, String signImg, String handleOpinion, String handleIds,
			String nodeCode, Integer pause) throws Exception;

	JsonBean getInProgressList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean getPendingSignList(String token, Integer currentPage, Integer pageSize, String flowName) throws Exception;

	JsonBean getFLowTemplateInfo(String id) throws Exception;

	JsonBean getAdministratoList(String keyword, Integer currentPage, Integer pageSize) throws Exception;

	JsonBean getAdministratoSelector(String staffId) throws Exception;

	JsonBean saveAdministratorInfo(@Valid OrganizeAdminIsTratorCrForm organizeAdminIsTratorCrForm, String id) throws Exception;

	JsonBean removeAdministratorInfo(String id) throws Exception;

}
