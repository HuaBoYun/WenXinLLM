package com.huabo.system.service.impl;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hbfk.util.BaseDao;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.mapper.ProcessApprovalMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblRoleMapper;
import com.huabo.system.service.ProcessApprovalService;

@Service("processApprovalService")
public class ProcessApprovalServiceImpl implements ProcessApprovalService {

	@Resource
	private ProcessApprovalMapper processApprovalMapper;
	
	@Resource
	private TblOrganizationMapper tblOrganizationMapper;
	
	@Override
	public JsonBean getDepartmentHeader(String taskId, String taskNodeId) throws Exception {
		String ymStaffId = this.processApprovalMapper.selectDepartmentHeader(taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", ymStaffId);
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean dsoDheadContract(String taskId, String taskNodeId) throws Exception {
		List<String> ymStaffId = this.processApprovalMapper.selectDsoDHeadContract(taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", ymStaffId));
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean getTwoDsoDheadContract(String taskId, String taskNodeId) throws Exception {
		List<String> ymStaffId = this.processApprovalMapper.selectTwoDsoDheadContract(taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", ymStaffId));
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean directLeaderDsoDheadContract(String taskId, String taskNodeId) throws Exception {
		String ymStaffId = this.processApprovalMapper.selectDirectLeaderDsoDheadContract(taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", ymStaffId);
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getDirectLeader(String taskId, String taskNodeId) throws Exception {
		String ymStaffId = this.processApprovalMapper.selectDirectLeader(taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", ymStaffId);
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getDepartmentHeadByContract(String taskId, String taskNodeId) throws Exception {
		String ymStaffId = this.processApprovalMapper.selectDepartmentHeaderByContract(taskId);
		if(StringUtils.isBlank(ymStaffId)) {
			ymStaffId = this.processApprovalMapper.selectSubmitUser(taskId);
		}
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", ymStaffId);
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getDirectLeaderByContract(String taskId, String taskNodeId) throws Exception {
		String ymStaffId = this.processApprovalMapper.selectDirectLeaderByContract(taskId);
		if(StringUtils.isBlank(ymStaffId)) {
			ymStaffId = this.processApprovalMapper.selectSubmitUser(taskId);
		}
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", ymStaffId);
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getContractperson(String taskId, String taskNodeId) throws Exception {
		String fRoleName = this.selectFNodeNameByTaskNodeId(taskNodeId);
		BigDecimal rid = this.processApprovalMapper.selectRoleIdByRname(fRoleName);
		
		List<String> ymStaffId = this.processApprovalMapper.selectContractPerson(taskId,rid.toString());
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", ymStaffId));
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getDeputyDirectorOfDeptCounerSign(String taskId, String taskNodeId) throws Exception {
		List<String> deptIds = new ArrayList<String>(0);
		//1. 获取上一步会签科室的人员
		String preUserAccounts = this.selectPreAccountByTaskNodeId(taskNodeId);
		
		//2.获取流程发起时所属的部门
		BigDecimal deptId = this.processApprovalMapper.selectStartFlowDeptIdByTaskId(taskId);
		
		if(StringUtils.isNotBlank(preUserAccounts)) {
			//3.递归获取流程所在的公司
			BigDecimal orgId = this.processApprovalMapper.selectStartFlowOrgIdByTaskId(taskId);
			//4.根据会签科室人员和公司ID，查找出会签的科室orgId;
			preUserAccounts = "'"+preUserAccounts.replace(",", "','")+"'";
			deptIds = this.tblOrganizationMapper.selectDeptIdsByUserOrganization(preUserAccounts,orgId);
			deptIds.add(deptId.toString());
		}else {
			deptIds.add(deptId.toString());
		}
		Map<String,String> resultMap = new HashMap<String, String>(0);
		//5.根据会签的部门ID 查找 部门负责人科长，在查找其直属主管，副部长 或 部长；
		List<String> pkYmStaffIds = this.processApprovalMapper.selectDeputyDirectorOfDeptCounerSign(String.join(",", deptIds));
		
		if(pkYmStaffIds == null) {
			resultMap.put("handleId", "");
			return ResponseFormat.retParam(0, resultMap);
		}
		resultMap.put("handleId", String.join(",", pkYmStaffIds));
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean getApproverByNodeName(String taskId, String taskNodeId) throws Exception {
		//1.通过taskId 获取节点名称，节点名称为角色名称，通过角色名称获取角色Id
		String fRoleName = this.selectFNodeNameByTaskNodeId(taskNodeId);
		BigDecimal rid = this.processApprovalMapper.selectRoleIdByRname(fRoleName);
		//2.递归获取流程所在的公司
		BigDecimal orgId = this.processApprovalMapper.selectStartFlowOrgIdByTaskId(taskId);
		//3.根据所在公司和角色ID获取到审批人
		List<String> ymStaffId = this.processApprovalMapper.selectApproverByRoleNameOrgId(orgId,rid);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", ymStaffId));
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean getDepartmentHeadByPreviousApprover(String taskId, String taskNodeId) throws Exception {
		List<String> deptIds = new ArrayList<String>(0);
		List<String> pkYmStaffIds = new ArrayList<String>(0);
		//1. 获取上一步审批人员
		String preUserAccounts = this.selectPreAccountByTaskNodeId(taskNodeId);
		if(StringUtils.isNotBlank(preUserAccounts)) {
			//2.获取流程所在的公司
			BigDecimal orgId = this.processApprovalMapper.selectStartFlowOrgIdByTaskId(taskId);
			preUserAccounts = "'"+preUserAccounts.replace(",", "','")+"'";
			deptIds = this.tblOrganizationMapper.selectDeptIdsByUserOrganization(preUserAccounts,orgId);
			pkYmStaffIds = this.processApprovalMapper.selectDepartmentHeadByPreviousApprover(String.join(",", deptIds));
		}
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", pkYmStaffIds));
		return ResponseFormat.retParam(200, resultMap);
	}

	@Override
	public JsonBean getDirectSupervisorByPreviousApprover(String taskId, String taskNodeId) throws Exception {
		List<String> pkYmStaffIds = new ArrayList<String>(0);
		//1. 获取上一步审批人员
		String preUserAccounts = this.selectPreAccountByTaskNodeId(taskNodeId);
		//2.获取上一步审批人员的直属主管
		if(StringUtils.isNotBlank(preUserAccounts)) {
			preUserAccounts = "'"+preUserAccounts.replace(",", "','")+"'";
			pkYmStaffIds = this.processApprovalMapper.selectDirectSupervisorByPreviousApprover(preUserAccounts);
		}
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", pkYmStaffIds));
		return ResponseFormat.retParam(200, resultMap);
	}
	
	@Override
	public JsonBean getCandidateApproverInfo(String taskId, String taskNodeId) throws Exception {
		//1.获取上一步审批的nodeCode;
		String nodeCodes = this.selectPreNodeCode(taskId,taskNodeId);
		//2.根据nodeCodes 和taskId 获取审批人
		nodeCodes = "'"+nodeCodes.replace(",", "','")+"'";
		List<String> pkYmStaffIds = this.processApprovalMapper.selectApproverInfoByNodeCodeTaskId(nodeCodes,taskId);
		Map<String,String> resultMap = new HashMap<String, String>(0);
		resultMap.put("handleId", String.join(",", pkYmStaffIds));
		return ResponseFormat.retParam(200, resultMap);
	}

	private String selectPreNodeCode(String taskId, String taskNodeId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT GROUP_CONCAT(F_NodeCode) AS F_NodeCode FROM flow_tasknode WHERE F_TaskId = '"+taskId+"' AND F_NodeNext IN (SELECT F_NodeCode FROM flow_tasknode WHERE F_TaskId = '"+taskId+"' AND F_Id = '"+taskNodeId+"')");
			rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getString("F_NodeCode");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return name;
	}

	private String selectPreAccountByTaskNodeId(String taskNodeId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String accounts = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT GROUP_CONCAT(F_Account) AS ACCOUNTS FROM base_user WHERE F_ID IN (SELECT F_HandleId FROM flow_taskoperator WHERE F_NodeCode IN (SELECT F_NodeCode FROM flow_tasknode WHERE F_NodeNext IN ( SELECT F_NodeCode FROM flow_tasknode WHERE F_Id = '"+taskNodeId+"')))");
			rs = ps.executeQuery();
			while (rs.next()) {
				accounts = rs.getString("ACCOUNTS");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return accounts;
	}

	private String selectFNodeNameByTaskNodeId(String taskNodeId) throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = null;
		try {
			con = BaseDao.getInstance().getConnection();
			ps = con.prepareStatement("SELECT F_NodeName FROM flow_tasknode WHERE F_Id = '"+taskNodeId+"'");
			rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getString("F_NodeName");
			}
		}finally {
			BaseDao.getInstance().close(con,rs,ps);
		}
		return name;
	}

}
