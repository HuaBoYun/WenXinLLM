package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.system.mappersql.ProcessApprovalMapperSqlConfig;

public interface ProcessApprovalMapper {

	@SelectProvider(method = "selectDepartmentHeader" , type = ProcessApprovalMapperSqlConfig.class)
	String selectDepartmentHeader(String taskId) throws Exception;
	
	@SelectProvider(method = "selectDirectLeader" , type = ProcessApprovalMapperSqlConfig.class)
	String selectDirectLeader(String taskId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} ))")
	String selectDepartmentHeaderByContract(String taskId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = ( SELECT USERID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} )")
	String selectSubmitUser(String taskId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT CHARGELEADERSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} ))")
	String selectDirectLeaderByContract(String taskId) throws Exception;

	@Select("SELECT RID FROM TBL_ROLE WHERE RNAME = #{rname}")
	BigDecimal selectRoleIdByRname(String rname) throws Exception;

	@SelectProvider(method = "selectContractPerson" , type = ProcessApprovalMapperSqlConfig.class)
	List<String> selectContractPerson(String taskId, String rid) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} )))")
	List<String> selectDsoDHeadContract(String taskId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} )))))")
	String selectDirectLeaderDsoDheadContract(String taskId);

	@Select("SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId}")
	BigDecimal selectStartFlowDeptIdByTaskId(String taskId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN (SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID IN (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID IN (${deptIds})))")
	List<String> selectDeputyDirectorOfDeptCounerSign(@Param("deptIds")String deptIds) throws Exception;

	@SelectProvider(method = "selectApproverByRoleNameOrgId" , type = ProcessApprovalMapperSqlConfig.class)
	List<String> selectApproverByRoleNameOrgId(BigDecimal orgId, BigDecimal rid) throws Exception;

	@Select("SELECT PKYMSTAFFID)FROM TBL_STAFF WHERE STAFFID IN (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID IN (${deptIds}))")
	List<String> selectDepartmentHeadByPreviousApprover(String deptIds) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN (SELECT PRINCIPALSTAFFID FROM TBL_STAFF WHERE USERNAME IN (${preUserAccounts}))")
	List<String> selectDirectSupervisorByPreviousApprover(String preUserAccounts);

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN ( SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT CHARGELEADERSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId} )))))")
	List<String> selectTwoDsoDheadContract(String taskId);

	@Select("SELECT ORGID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = #{taskId}")
	BigDecimal selectStartFlowOrgIdByTaskId(String taskId) throws Exception;

	@Select("SELECT YMSTAFFIDS FROM TBL_YMFLOW_APPROVER WHERE TASKID = #{taskId} AND NODCODE IN (#{nodeCodes})")
	List<String> selectApproverInfoByNodeCodeTaskId(@Param("nodeCodes")String nodeCodes, @Param("taskId")String taskId) throws Exception;

}
