package com.huabo.system.mappersql;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;

import com.hbfk.util.database.DataBaseSqlConfig;

public class ProcessApprovalMapperSqlConfig {
	
	public String selectDepartmentHeader(String taskId) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(DataBaseSqlConfig.getNullColumn("HEADER.PKYMSTAFFID", "SUBMITER.PKYMSTAFFID"));
		sqlSb.append(" FROM TBL_SYSTEM_FORMFLOW TSF LEFT JOIN TBL_STAFF SUBMITER ON TSF.USERID = SUBMITER.STAFFID LEFT JOIN TBL_ORGANIZATION DEPT ON TSF.DEPTID = DEPT.ORGID LEFT JOIN TBL_STAFF")
		.append(" HEADER ON DEPT.PRINCIPALSTAFFID = HEADER.STAFFID WHERE TSF.YMFORMID = '").append(taskId).append("'");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectDirectLeader(String taskId) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(DataBaseSqlConfig.getNullColumn("HEADER.PKYMSTAFFID", "SUBMITER.PKYMSTAFFID"));
		sqlSb.append(" FROM TBL_SYSTEM_FORMFLOW TSF LEFT JOIN TBL_STAFF SUBMITER ON TSF.USERID = SUBMITER.STAFFID LEFT JOIN TBL_ORGANIZATION DEPT ON TSF.DEPTID = DEPT.ORGID ")
		.append(" LEFT JOIN TBL_STAFF HEADER ON DEPT.CHARGELEADERSTAFFID = HEADER.STAFFID WHERE TSF.YMFORMID = '").append(taskId).append("'");
		String sql = sqlSb.toString();
		return sql;
		
	}
	
	public String selectContractPerson(String taskId, String rid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN ");
		sqlSb.append(" (SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT FATHERORGID FROM TBL_ORGANIZATION WHERE ORGID = ( SELECT DEPTID FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = '")
		.append(taskId).append("'  )))) AND ").append(DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", rid, ","));
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectApproverByRoleNameOrgId(BigDecimal orgId, BigDecimal rid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE ORGID = ").append(orgId);
		sqlSb.append("  AND ").append(DataBaseSqlConfig.getWhereColumnInStr("ROLEIDSTRS", rid.toString(), ","));
		String sql = sqlSb.toString();
		return sql;
		
	}
}
