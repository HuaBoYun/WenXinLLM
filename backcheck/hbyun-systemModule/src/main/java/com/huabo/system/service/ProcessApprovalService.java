package com.huabo.system.service;

import com.hbfk.util.JsonBean;

public interface ProcessApprovalService {

	JsonBean getDepartmentHeader(String taskId, String taskNodeId) throws Exception;

	JsonBean getDirectLeader(String taskId, String taskNodeId) throws Exception;

	JsonBean getDepartmentHeadByContract(String taskId, String taskNodeId) throws Exception;

	JsonBean getDirectLeaderByContract(String taskId, String taskNodeId) throws Exception;

	JsonBean getContractperson(String taskId, String taskNodeId) throws Exception;

	JsonBean dsoDheadContract(String taskId, String taskNodeId) throws Exception;

	JsonBean directLeaderDsoDheadContract(String taskId, String taskNodeId) throws Exception;

	JsonBean getDeputyDirectorOfDeptCounerSign(String taskId, String taskNodeId) throws Exception;

	JsonBean getApproverByNodeName(String taskId, String taskNodeId) throws Exception;

	JsonBean getDepartmentHeadByPreviousApprover(String taskId, String taskNodeId) throws Exception;

	JsonBean getDirectSupervisorByPreviousApprover(String taskId, String taskNodeId) throws Exception;

	JsonBean getTwoDsoDheadContract(String taskId, String taskNodeId) throws Exception;

	JsonBean getCandidateApproverInfo(String taskId, String taskNodeId) throws Exception;

}