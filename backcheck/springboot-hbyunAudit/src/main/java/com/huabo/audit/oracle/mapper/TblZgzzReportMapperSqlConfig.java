package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.vo.TblZgzzReportVo;

public class TblZgzzReportMapperSqlConfig {
	
	public String selectListByPageInfo(TblZgzzReportVo report,TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer(" SELECT TZR.*,TS.REALNAME AS CREATESTAFFNAME,DEPT.ORGNAME AS DEPTNAME ")
		.append(" FROM TBL_ZGZZ_REPORT TZR LEFT JOIN TBL_STAFF TS ON TZR.CREATESTAFF = TS.STAFFID LEFT JOIN TBL_ORGANIZATION DEPT ON TZR.LINKDEPT = DEPT.ORGID WHERE ");
		
		if(StringUtils.isNotBlank(report.getDeptIds())) {
			sqlSb.append(" (TZR.CREATESTAFF = ").append(report.getCreatestaff()).append(" OR TZR.LINKORG IN (").append(report.getDeptIds()).append(") OR TZR.LINKDEPT IN (").append(report.getDeptIds()).append("))");
		}else {
			sqlSb.append(" TZR.CREATESTAFF = ").append(report.getCreatestaff());
		}
		
		
//		if(report.getUseSecrect() == 1) {
//			//知悉范围查询
//			sqlSb.append(" AND ( "+DataBaseSqlConfig.getWhereColumnInStr("TZR.STAFFSCOPEIDS", report.getSecrectStaff().toString(),",")+" OR TZR.CREATESTAFF = "+report.getSecrectStaff()+" OR TZR.STAFFSCOPEIDS IS NULL OR TZR.STAFFSCOPEIDS = '' )");
//			
//			//密级查询
//			 if(StringUtils.isNotBlank(report.getSecrectScopeIds())) {
//				 sqlSb.append(" AND (TZR.SECRECTLEVELID IN (").append(report.getSecrectScopeIds()).append(") OR TZR.SECRECTLEVELID IS NULL OR TZR.SECRECTLEVELID = ''  )");
//		     }else {
//		    	 sqlSb.append(" AND (TZR.SECRECTLEVELID IS NULL OR SECRECTLEVELID = ''  )");
//		     }
//		}
		
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TZR.LINKORG", "TZR.LINKDEPT", "TZR.CREATESTAFF", "TZR.SECRECTLEVELID", "TZR.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(StringUtils.isNotBlank(report.getReportcode())) {
			sqlSb.append(" AND TZR.REPORTCODE LIKE '%").append(report.getReportcode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(report.getReportname())) {
			sqlSb.append(" AND TZR.REPORTNAME LIKE '%").append(report.getReportname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(report.getCreateStaffName())) {
			sqlSb.append(" AND TS.REALNAME LIKE '%").append(report.getCreateStaffName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(report.getLinkDeptName())) {
			sqlSb.append(" AND DEPT.ORGNAME LIKE '%").append(report.getLinkDeptName()).append("%'");
		}
		
		if(report.getReporttype() != null) {
			sqlSb.append(" AND TZR.REPORTTYPE = ").append(report.getReporttype());
		}
		
		if(report.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(report.getStatus());
		}
		
		if(report.getOrgid() != null) {
			sqlSb.append(" AND TZR.LINKORG = ").append(report.getOrgid());
		}
		
		if(report.getStartDate() != null) {
			sqlSb.append(" AND TZR.CREATEDATE >= ").append(DataBaseSqlConfig.getDateStrFormat(report.getStartDate()));
		}
		
		if(report.getEndDate() != null) {
			sqlSb.append(" AND TZR.CREATEDATE <= ").append(DataBaseSqlConfig.getDateStrFormat(report.getEndDate()));
		}
		
		sqlSb.append(" ORDER BY TZR.CREATEDATE DESC ");
		return sqlSb.toString();
	}
	
}
