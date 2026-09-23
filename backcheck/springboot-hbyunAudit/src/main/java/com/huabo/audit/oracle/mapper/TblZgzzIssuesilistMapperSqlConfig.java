package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblBeforeZgzzListEntity;
import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblWgzzWghcBg;
import com.huabo.audit.oracle.entity.TblWgzzWgjyYs;
import com.huabo.audit.oracle.entity.TblZgzzIssuesilist;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;

public class TblZgzzIssuesilistMapperSqlConfig {
	
	public String selectNoChooseAuditOrg(String orgIds, BigDecimal auditOrgId, BigDecimal sheetId) throws Exception{
		
		StringBuffer sqlSb = new StringBuffer("SELECT ORGID,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (").append(orgIds).append(")");
		sqlSb.append(" AND ORGID NOT IN (SELECT AUDITOBJECTID FROM TBL_ZGZZ_ISSUESILIST WHERE QUESITIONID = ").append(sheetId);
		
		if(auditOrgId != null) {
			sqlSb.append(" AND AUDITOBJECTID != ").append(auditOrgId);
		}
		
		sqlSb.append(")");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectCountByIssuesCode(String issuesCode, BigDecimal questionId, String issuesId) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_ZGZZ_ISSUESILIST WHERE ISSUESCODE = '").append(issuesCode).append("'");
		
		if(questionId != null) {
			sqlSb.append(" AND QUESITIONID != ").append(questionId);
		}
		if(StringUtils.isNotBlank(issuesId)) {
			sqlSb.append(" AND ISSUESID != ").append(issuesId);
		}
		return sqlSb.toString();
	}
	
	public String selectListByPageInfo(TblZgzzIssuesilistVo condition, TblStaffUtil loginStaff) throws Exception {
		// 判断是否需要关联整改落实表（用于整改问题一览表的查询）
		boolean needRectificationJoin = StringUtils.isNotBlank(condition.getQueryType());

		StringBuffer sqlSb = new StringBuffer("SELECT T2.*,CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO, ")
		.append(" CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME, ")
		.append(" CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME FROM (")
		.append(" SELECT TZI.ISSUESID,TZI.ISSUESCODE,TZI.ISSUESNAME,TZI.CREATETIME,TZI.ISSUESITEM,TZI.QUESTIONMEMO,TZI.ISSUESTITLE,TZI.ISSUESTYPE,TZI.STATUS,TZI.AUDITOBJECTID,TZI.PROJECTID,TZI.AUDITOBJECTTYPE,CTS.REALNAME AS CREATESTAFFNAME,RST.REALNAME AS RESPONSIBLEPERSONNAME,RORG.ORGNAME AS RESPONSIBLEDEPTNAME ");

		// 如果需要关联整改落实表，添加整改状态和销号状态字段
		if(needRectificationJoin) {
			sqlSb.append(",TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TZRI.CONCLUSION,TZRI.ISXH ");
		}

		sqlSb.append(" FROM TBL_ZGZZ_ISSUESILIST TZI LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID ")
		.append(" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID ")
		.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID  ")
		.append(" LEFT JOIN TBL_STAFF RST ON TZI.RESPONSIBLEPERSON = RST.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TZI.RESPONSIBLEDEPT = RORG.ORGID ");

		// 如果需要关联整改落实表
		if(needRectificationJoin) {
			sqlSb.append(" LEFT JOIN TBL_RECTIFICATION_ISSUES TRI ON TZI.ISSUESID = TRI.ISSUESID ")
			.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID ");
		}

		sqlSb.append(" WHERE 1 = 1 ");

		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TZI.LINKORGID", "TZI.LINKDEPTID", "TZI.CREATESTAFF", "TZI.SECRECTLEVELID", "TZI.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		
		if(StringUtils.isNotBlank(condition.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(condition.getIssuesCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(condition.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(condition.getIssuesName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(condition.getIssuesItem())) {
			sqlSb.append(" AND TZI.ISSUESITEM LIKE '%").append(condition.getIssuesItem()).append("%'");
		}
		
		if(StringUtils.isNotBlank(condition.getIssuesTitle())) {
			sqlSb.append(" AND TZI.ISSUESTITLE LIKE '%").append(condition.getIssuesTitle()).append("%'");
		}
		
		if(condition.getIssuesType() != null) {
			sqlSb.append(" AND TZI.ISSUESTYPE = ").append(condition.getIssuesType());
		}
		
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TZI.STATUS = ").append(condition.getStatus());
		}
		
		if(StringUtils.isNotBlank(condition.getStatusStr())) {
			sqlSb.append(" AND TZI.STATUS IN (").append(condition.getStatusStr()).append(")");
		}
		
		if(condition.getProjectId() != null) {
			sqlSb.append(" AND TZI.PROJECTID = ").append(condition.getProjectId());
		}
		
		if(StringUtils.isNotBlank(condition.getAuditObjectName())) {
			sqlSb.append(" AND (ATS.REALNAME LIKE '%").append(condition.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(condition.getAuditObjectName()).append("%')");
		}
		
		if(StringUtils.isNotBlank(condition.getProjectName())) {
			sqlSb.append(" AND (TNP.PRJOECTNAME LIKE '%").append(condition.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(condition.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(condition.getProjectName()).append("%' )");
		}
		
		if(StringUtils.isNotBlank(condition.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(condition.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(condition.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(condition.getProjectNo()).append("%' )");
		}
		
		if(StringUtils.isNotBlank(condition.getResponsiblePersonName())) {
			sqlSb.append(" AND RST.REALNAME LIKE '%").append(condition.getResponsiblePersonName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(condition.getResponsibleDeptName())) {
//			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getResponsibleDeptName()).append("%'");
			sqlSb.append(" AND ( ATS.REALNAME='").append(condition.getResponsibleDeptName()).append("' or AOG.ORGNAME='"+condition.getResponsibleDeptName()+"' ) ");
		}

		if(condition.getStartDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= ").append(DataBaseSqlConfig.getDateStrFormat(DateUtil.parseDate(condition.getStartDate(), DateUtil.DATE_SMALL_STR)));
		}

		if(condition.getEndDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME <= ").append(DataBaseSqlConfig.getDateStrFormat(DateUtil.parseDate(condition.getEndDate(), DateUtil.DATE_SMALL_STR)));
		}

		if(StringUtils.isNotBlank(condition.getQuestionMemo())) {
			sqlSb.append(" AND TZI.QUESTIONMEMO LIKE '%").append(condition.getQuestionMemo()).append("%'");
		}

		// 新增：年度过滤条件
		if(StringUtils.isNotBlank(condition.getYear())) {
			sqlSb.append(" AND EXTRACT(YEAR FROM TZI.CREATETIME) = ").append(condition.getYear());
		}

		// 新增：主管部门名称过滤条件（通过责任部门）
		if(StringUtils.isNotBlank(condition.getOrgName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getOrgName()).append("%'");
		}

		// 新增：审计对象类型过滤条件
		if(condition.getAuditObjectType() != null) {
			sqlSb.append(" AND TZI.ISSUESTYPE = ").append(condition.getAuditObjectType());
		}

		// 新增：根据查询类型过滤整改状态和销号状态
		if(needRectificationJoin && StringUtils.isNotBlank(condition.getQueryType())) {
			String queryType = condition.getQueryType();
			if("yzg".equals(queryType)) {
				// 已整改：CONCLUSION IN ('已整改', '已整改到位')
				sqlSb.append(" AND TZRI.CONCLUSION IN ('已整改', '已整改到位')");
			} else if("wzg".equals(queryType)) {
				// 未整改：CONCLUSION NOT IN ('已整改', '已整改到位') 或者 CONCLUSION IS NULL
				sqlSb.append(" AND (TZRI.CONCLUSION NOT IN ('已整改', '已整改到位') OR TZRI.CONCLUSION IS NULL)");
			} else if("yxh".equals(queryType)) {
				// 已销号：ISXH='1'
				sqlSb.append(" AND TZRI.ISXH = '1'");
			} else if("wxh".equals(queryType)) {
				// 未销号：ISXH='0' OR ISXH IS NULL
				sqlSb.append(" AND (TZRI.ISXH = '0' OR TZRI.ISXH IS NULL)");
			}
			// zs(整改总数) 和 xhzs(销号总数) 不需要额外过滤条件
		}

		if(condition.getSortFlag()!=null && condition.getSortField()!=null) {
			if(condition.getSortFlag()==1) {
				sqlSb.append(" ORDER BY TZI."+condition.getSortField()+" ASC ");
			}
			if(condition.getSortFlag()==2) {
				sqlSb.append(" ORDER BY TZI."+condition.getSortField()+" DESC ");
			}
		}else {
			sqlSb.append(" ORDER BY TZI.CREATETIME DESC ");
		}
		
		sqlSb.append(" ) ").append(DataBaseSqlConfig.setTableAliasName("T2")).append(" LEFT JOIN TBL_STAFF ATS ON T2.AUDITOBJECTID = ATS.STAFFID LEFT JOIN TBL_ORGANIZATION AOG ON T2.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID ")
		.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID "
				+ "ORDER BY T2.CREATETIME DESC");
		return sqlSb.toString();
	}
	
	
	public String selectAllListByIssues(TblZgzzIssuesilistVo issues,TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TZI.*,RST.REALNAME AS RESPONSIBLEPERSONNAME,RORG.ORGNAME AS RESPONSIBLEDEPTNAME, ")
				.append("CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO, ")
				.append("CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 OR ISSUESTYPE = 4 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME, ")
				.append("CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CTS.REALNAME AS CREATESTAFFNAME ")
				.append("FROM TBL_ZGZZ_ISSUESILIST TZI LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID ")
				.append("LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID ")
				.append("LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_STAFF RST ON TZI.RESPONSIBLEPERSON = RST.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TZI.RESPONSIBLEDEPT = RORG.ORGID "
						+ "WHERE 1 = 1 ");
		/*if(StringUtils.isNotBlank(issues.getDeptIds())) {
			sqlSb.append(" AND (TZI.CREATESTAFF = ").append(issues.getCreateStaff()).append(" OR TZI.LINKORGID IN (").append(issues.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(issues.getDeptIds()).append("))");
		}else {
			if(null!=issues.getCreateStaff()) {
				sqlSb.append(" AND TZI.CREATESTAFF = ").append(issues.getCreateStaff());
			}
		}*/
		if(loginStaff!=null) {
			sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TZI.LINKORGID", "TZI.LINKDEPTID", "TZI.CREATESTAFF", "TZI.SECRECTLEVELID", "TZI.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		}

		


		if(StringUtils.isNotBlank(issues.getQuestionMemo())) {
			sqlSb.append(" AND TZI.QUESTIONMEMO LIKE '%").append(issues.getQuestionMemo()).append("%'");
		}
		
		/*if(null!=loginStaff.getStaffid()) {
			sqlSb.append(" AND TZI.CREATESTAFF= "+loginStaff.getStaffid());
		}*/
		
		if(StringUtils.isNotBlank(issues.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issues.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issues.getIssuesItem())) {
			sqlSb.append(" AND TZI.ISSUESITEM LIKE '%").append(issues.getIssuesItem()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issues.getIssuesTitle())) {
			sqlSb.append(" AND TZI.ISSUESTITLE LIKE '%").append(issues.getIssuesTitle()).append("%'");
		}
		
		if(issues.getIssuesType() != null) {
			sqlSb.append(" AND TZI.ISSUESTYPE = ").append(issues.getIssuesType());
		}
		
		if(issues.getStatus() != null) {
			sqlSb.append(" AND TZI.STATUS = ").append(issues.getStatus());
		}
		
		if(StringUtils.isNotBlank(issues.getStatusStr())) {
			sqlSb.append(" AND TZI.STATUS IN (").append(issues.getStatusStr()).append(")");
		}
		
		if(StringUtils.isNotBlank(issues.getAuditObjectName())) {
			sqlSb.append(" AND (ATS.REALNAME LIKE '%").append(issues.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(issues.getAuditObjectName()).append("%')");
		}
		
		if(StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND (TNP.PRJOECTNAME LIKE '%").append(issues.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(issues.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%' )");
		}
		
		if(StringUtils.isNotBlank(issues.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(issues.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' )");
		}
		
		if(issues.getStartDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(issues.getStartDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		
		if(issues.getEndDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(issues.getEndDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		
		if(issues.getProjectId() != null) {
			sqlSb.append(" AND TZI.PROJECTID = ").append(issues.getProjectId());
		}

		if(StringUtils.isNotBlank(issues.getResponsibleDeptName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(issues.getResponsibleDeptName()).append("%'");
		}
		
		sqlSb.append(" ORDER BY TZI.CREATETIME DESC ");
		return sqlSb.toString();
	}



	public String getBeforeZgzzList(TblBeforeZgzzListEntity tblbzl) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_BEFOREZGZZ_LIST WHERE 1 = 1 ");

		if(tblbzl.getID()!= null) {
			sqlSb.append(" AND  ID LIKE '%").append(tblbzl.getID()).append("%'");
		}

		if(StringUtils.isNotBlank(tblbzl.getPROBLEMTYPE())) {
			sqlSb.append(" AND PROBLEMTYPE LIKE '%").append(tblbzl.getPROBLEMTYPE()).append("%'");
		}
		if(StringUtils.isNotBlank(tblbzl.getSOURCEPROBLEM())) {
			sqlSb.append(" AND SOURCEPROBLEM LIKE '%").append(tblbzl.getSOURCEPROBLEM()).append("%'");
		}
		if(StringUtils.isNotBlank(tblbzl.getCORTYPE())) {
			sqlSb.append(" AND CORTYPE LIKE '%").append(tblbzl.getCORTYPE()).append("%'");
		}
		return sqlSb.toString();
	}
}
