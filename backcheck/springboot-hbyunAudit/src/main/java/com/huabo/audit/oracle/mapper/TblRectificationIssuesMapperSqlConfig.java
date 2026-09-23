package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.oracle.vo.TblZgzzRctevaluationVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationimplVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;
import com.huabo.audit.vo.param.MaxNumberParam;

import lombok.Data;
import net.sf.jsqlparser.schema.Database;

public class TblRectificationIssuesMapperSqlConfig {
	
	public String selectListByReport(TblRectificationIssuesVo rela) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TRI.RELAID,TRI.RECTIFICATIONPLAN,TRI.PLANID,TRI.DEADLINE,TRI.RESPONSIBLEPERSON,"
				+ "RTS.REALNAME AS RESPONSIBLEPERSONNAME,TRI.RESPONSIBLEDEPT,RORG.ORGNAME AS RESPONSIBLEDEPTNAME,TRI.IMPLEMENTER,"
				+ "ITS.REALNAME AS IMPLEMENTERNAME,TZI.ISSUESID,TZI.ISSUESCODE,TZI.CREATETIME AS TZICREATETIME,TZI.ISSUESNAME,TZI.CREATESTAFF,CTS.REALNAME AS CREATESTAFFNAME,"
				+ "TZI.ISSUESITEM,TZI.ISSUESTITLE,TZI.QUESTIONMEMO,TZRI.SITUATIONOVERVIEW "
				+ "FROM TBL_RECTIFICATION_ISSUES TRI")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) "+DataBaseSqlConfig.setTableAliasName("T1")+" ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION")
				.append(" LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF CTS ON CTS.STAFFID = TZI.CREATESTAFF")
				.append(" LEFT JOIN TBL_STAFF RTS ON RTS.STAFFID = TRI.RESPONSIBLEPERSON LEFT JOIN TBL_ORGANIZATION RORG ON RORG.ORGID = TRI.RESPONSIBLEDEPT")
				.append(" LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID "
						+ " LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID "
						+ "WHERE TRI.PLANID = '").append(rela.getPlanId()).append("' ");
		TblZgzzIssuesilistVo issues = rela.getIssues();
		if(rela.getImplementer() != null) {
			sqlSb.append(" AND TRI.IMPLEMENTER = ").append(rela.getImplementer());
		}
		if(issues != null) {
			if(StringUtils.isNotBlank(issues.getIssuesCode())) {
				sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
			}
			if(StringUtils.isNotBlank(issues.getIssuesName())) {
				sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
			}
			if(StringUtils.isNotBlank(issues.getQuestionMemo())) {
				sqlSb.append(" AND TZI.QUESTIONMEMO LIKE '%").append(issues.getQuestionMemo()).append("%'");
			}
		}
		
//		sqlSb.append(" AND TZI.ISSUESID NOT IN (SELECT ISSUESID FROM TBL_ZGZZ_REPORTISSUES WHERE PLANID = '").append(rela.getPlanId())
//		.append("'  AND CREATESTAFF = ").append(rela.getHandlerId()).append(") ORDER BY TRI.RELAID ASC ");
		sqlSb.append(" ORDER BY TRI.RELAID ASC ");
		return sqlSb.toString();
	}
	
	public String selectRectificationIssuesLedgetPageInfoList(PageInfo<TblRectificationIssuesVo> pageInfo, Integer isAll) {
		TblRectificationIssuesVo condition = pageInfo.getCondition();
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		
		StringBuffer sqlSb = new StringBuffer("SELECT T3.* FROM (SELECT T2.*,ROWNUM RN FROM (")
				.append(" SELECT TZI.ISSUESID,TZI.ISSUESCODE,TZI.ISSUESNAME,TZI.CREATESTAFF,TZI.ISSUESITEM,TZI.ISSUESTITLE,TZI.STATUS,TRI.RELAID,TRI.HANDLERID,TRI.IMPLEMENTER,TRI.PLANID,")
				.append(" TZRP.PLANCODE,TZRP.PLANNAME,TZI.CREATETIME,CTS.REALNAME AS CREATESTAFFNAME,HTS.REALNAME AS HANDLERNAME,ITS.REALNAME AS IMPLEMENTERNAME")
				.append(" FROM TBL_ZGZZ_ISSUESILIST TZI ");
		if(isAll.compareTo(0) == 0) {
			sqlSb.append(" LEFT JOIN ( SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.IMPLEMENTER FROM TBL_RECTIFICATION_ISSUES TRI INNER JOIN (SELECT MAX(VERSION) VERSION,PLANID,ISSUESID FROM TBL_RECTIFICATION_ISSUES GROUP BY PLANID,ISSUESID ) T1 ON TRI.PLANID = T1.PLANID AND TRI.ISSUESID = T1.ISSUESID AND TRI.VERSION = T1.VERSION ) TRI ON TZI.ISSUESID = TRI.ISSUESID ");
		}else {
			sqlSb.append(" LEFT JOIN ( SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.IMPLEMENTER FROM TBL_RECTIFICATION_ISSUES TRI INNER JOIN (SELECT MAX(CREATETIME) CREATETIME,ISSUESID FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.CREATETIME = T1.CREATETIME ) TRI ON TZI.ISSUESID = TRI.ISSUESID ");
		}
		sqlSb.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZRP ON TRI.PLANID = TZRP.PLANID LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF HTS ON TRI.HANDLERID = HTS.STAFFID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID WHERE ");
		
		if(StringUtils.isNotBlank(issues.getDeptIds())) {
			sqlSb.append(" (TZI.CREATESTAFF = ").append(issues.getCreateStaff()).append(" OR TZI.LINKORGID IN (").append(issues.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(issues.getDeptIds()).append("))");
		}else {
			sqlSb.append(" TZI.CREATESTAFF = ").append(issues.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZRP.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZRP.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issues.getStatusStr())) {
			sqlSb.append(" AND TZI.STATUS IN (").append(issues.getStatusStr()).append(")");
		}
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
		if(StringUtils.isNotBlank(issues.getCreateStaffName())) {
			sqlSb.append(" AND CTS.REALNAME LIKE '%").append(issues.getCreateStaffName()).append("%'");
		}
		
		if(issues.getStartDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(issues.getStartDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(issues.getEndDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(issues.getEndDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		
		
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		
		sqlSb.append(" ORDER BY TZI.ISSUESID DESC ) T2 WHERE ROWNUM < ").append((pageInfo.getCurrentRecord()+pageInfo.getPageSize())).append("  ) T3 WHERE RN > ").append(pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectRectificationIssuesLedgetPageInfoCount(PageInfo<TblRectificationIssuesVo> pageInfo, Integer isAll) {
		TblRectificationIssuesVo condition = pageInfo.getCondition();
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		
		StringBuffer sqlSb = new StringBuffer(" SELECT COUNT(0) FROM TBL_ZGZZ_ISSUESILIST TZI ");
		
		if(isAll.compareTo(0) == 0) {
			sqlSb.append(" LEFT JOIN ( SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.IMPLEMENTER FROM TBL_RECTIFICATION_ISSUES TRI INNER JOIN (SELECT MAX(VERSION) VERSION,PLANID,ISSUESID FROM TBL_RECTIFICATION_ISSUES GROUP BY PLANID,ISSUESID ) T1 ON TRI.PLANID = T1.PLANID AND TRI.ISSUESID = T1.ISSUESID AND TRI.VERSION = T1.VERSION ) TRI ON TZI.ISSUESID = TRI.ISSUESID ");
		}else {
			sqlSb.append(" LEFT JOIN ( SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.IMPLEMENTER FROM TBL_RECTIFICATION_ISSUES TRI INNER JOIN (SELECT MAX(CREATETIME) CREATETIME,ISSUESID FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.CREATETIME = T1.CREATETIME ) TRI ON TZI.ISSUESID = TRI.ISSUESID ");
		}
		sqlSb.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZRP ON TRI.PLANID = TZRP.PLANID LEFT JOIN TBL_STAFF CTS ON TZI.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF HTS ON TRI.HANDLERID = HTS.STAFFID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID WHERE ");
		
		if(StringUtils.isNotBlank(issues.getDeptIds())) {
			sqlSb.append(" (TZI.CREATESTAFF = ").append(issues.getCreateStaff()).append(" OR TZI.LINKORGID IN (").append(issues.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(issues.getDeptIds()).append("))");
		}else {
			sqlSb.append(" TZI.CREATESTAFF = ").append(issues.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZRP.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZRP.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		
		
		if(issues.getStartDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(issues.getStartDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(issues.getEndDate() != null) {
			sqlSb.append(" AND TZI.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(issues.getEndDate(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(StringUtils.isNotBlank(issues.getStatusStr())) {
			sqlSb.append(" AND TZI.STATUS IN (").append(issues.getStatusStr()).append(")");
		}
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
		if(StringUtils.isNotBlank(issues.getCreateStaffName())) {
			sqlSb.append(" AND CTS.REALNAME LIKE '%").append(issues.getCreateStaffName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		
		return sqlSb.toString();
	}
	
	
	public String selectMyRectificationExportList(TblRectificationIssuesVo condition) throws Exception {
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		TblZgzzRectificationimplVo impl = condition.getReimpl();
		
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* ,CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO,	 CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME FROM ( SELECT T1.* FROM ( ")
				.append(" SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TRI.IMPLEMENTER,"
						+ "TRI.RESPONSIBLEPERSON,TRI.RESPONSIBLEDEPT,TRI.STATUS,TZR.PLANCODE,TZR.PLANNAME,TZI.ISSUESCODE,TZI.ISSUESNAME,"
						+ "TZI.AUDITOBJECTID,TZI.AUDITOBJECTTYPE,TZI.ISSUESTYPE,TZI.PROJECTID,HTS.REALNAME AS HANDLERNAME,"
						+ "ITS.REALNAME AS IMPLEMENTERNAME , RTS.REALNAME AS RESPONSIBLEPERSONNAME , RORG.ORGNAME AS RESPONSIBLEDETPNAME,"
						+ "TZRI.IMPLID,TZRI.STATUS AS TZRISTATUS,TZRV.STATUS AS TZRVSTATUS,TZRV.RESULTSTATUS AS TZRVRSTATUS,"
						+ " TZRI.RECTIFICATIONMEASURES,TZRI.SITUATIONOVERVIEW,TZRI.ACHIVEMENT,TZRI.DEADLINE TZRIDEADLINE,TZRI.CONCLUSION,TZRI.NEXTMEASURES,TZRI.FINISHTIME,TZRI.ONEORGNAME," 
						+ "	TZRI.PROBLEMSRC,TZRI.REPORTYEAR,TZRI.PROBLEMTYPE,TZRI.ONETITLE,TZRI.TWOTITLE,TZRI.THREETITLE,TZRI.REPORTEXPRESSION,TZRI.QUEEXPRESSION," 
						+ "	TZRI.QUEMONEY,TZRI.SUPERVISION,TZRI.RECTTYPE,TZRI.LEGALBASIS,TZRI.RECTDEMAND,TZRI.RECTTIMELIMIT,TZRI.FIRSTRESPONSTAFFNAME,TZRI.ASSISTLEADER," 
						+ "	TZRI.MAINDEPTHEADTEL,TZRI.ASSISTDEPTHEADTEL,TZRI.AUDITDEPTHEADTEL,TZRI.PJCNT,TZRI.RECTMONEY,TZRI.RECOVERYMONEY,TZRI.BACKMONEY,TZRI.OVERALLMONEY," 
						+ "	TZRI.ACCELERATEMONEY,TZRI.RETRIEVEMONEY,TZRI.ADJUSTMONEY,TZRI.STOPMONEY,TZRI.REISSUEMONEY,TZRI.OTHERMONEY,TZRI.OTHERWAY,TZRI.LANDAREA," 
						+ "	TZRI.MINERALS,TZRI.ORGCNT,TZRI.FAMILYCNT,TZRI.PERSONCNT,TZRI.HOUSECNT,TZRI.ACCOUNTABILITYINFO,TZRI.ACCOUNTABILITYCNT,TZRI.INSTITUTIONCNT,TZRI.INSTITUTIONINFO,"
						+ " TZRV.EVALID AS TZRVEVALID,TZRI.ISXH  "
						+ " FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZR ON TRI.PLANID = TZR.PLANID LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID  ")
				.append(" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID ")
				.append(" LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_STAFF HTS ON HTS.STAFFID = TRI.HANDLERID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID ")
				.append(" LEFT JOIN TBL_STAFF RTS ON TRI.RESPONSIBLEPERSON = RTS.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TRI.RESPONSIBLEDEPT = RORG.ORGID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID ")
				.append(" WHERE 1 = 1");
		
		
		 //角色数据范围，工作交接 本人创建数据查询
		if(StringUtils.isNotBlank(condition.getDeptIds())) {
			sqlSb.append(" AND (TRI.IMPLEMENTER = ").append(condition.getImplementer()).append(" OR TZI.LINKORGID IN (").append(condition.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(condition.getDeptIds()).append(") )");
		}else {
			sqlSb.append(" AND TRI.IMPLEMENTER = ").append(condition.getImplementer());
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getOneorgname())) {
			sqlSb.append(" AND TZRI.ONEORGNAME LIKE '%").append(impl.getOneorgname()).append("%' ");
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getReportyear())) {
			sqlSb.append(" AND TZRI.REPORTYEAR LIKE '%").append(impl.getReportyear()).append("%' ");
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getProblemsrc())) {
			sqlSb.append(" AND TZRI.PROBLEMSRC LIKE '%").append(impl.getProblemsrc()).append("%' ");
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getProblemtype())) {
			sqlSb.append(" AND TZRI.PROBLEMTYPE LIKE '%").append(impl.getProblemtype()).append("%' ");
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getRecttype())) {
			sqlSb.append(" AND TZRI.RECTTYPE LIKE '%").append(impl.getRecttype()).append("%' ");
		}
		
		if(impl != null && StringUtils.isNotBlank(impl.getConclusion())) {
			sqlSb.append(" AND TZRI.CONCLUSION LIKE '%").append(impl.getConclusion()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(plan.getStatusStrs()) ) {
			sqlSb.append(" AND TZR.STATUS IN (").append(plan.getStatusStrs()).append(") ");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TRI.STATUS = ").append(condition.getStatus());
		}
		if(impl != null && impl.getStatus() != null) {
			sqlSb.append(" AND TZRI.STATUS = ").append(impl.getStatus());
		}
		
		if(plan != null && plan.getCreateStaff() != null) {
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsiblePersonName())) {
			sqlSb.append(" AND RTS.REALNAME LIKE '%").append(condition.getResponsiblePersonName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsibleDetpName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getResponsibleDetpName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getAuditObjectName())) {
			sqlSb.append(" AND ( ATS.REALNAME LIKE '%").append(issues.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(issues.getAuditObjectName()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(issues.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(issues.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(issues.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%') ");
		}
		
		sqlSb.append("   ) ").append(DataBaseSqlConfig.setTableAliasName("T1"))
		.append(" ) "+DataBaseSqlConfig.setTableAliasName("T2")+" LEFT JOIN TBL_STAFF ATS ON T2.AUDITOBJECTID = ATS.STAFFID  LEFT JOIN TBL_ORGANIZATION AOG ON T2.AUDITOBJECTID = AOG.ORGID ")
		.append(" LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID ")
		.append(" ORDER BY T2.relaId DESC ");
		
		return sqlSb.toString();
	}
	
	public String selectMyRectificationPageInfoList(TblRectificationIssuesVo condition) throws Exception {
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		TblZgzzRectificationimplVo impl = condition.getReimpl();
		
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* ,CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO,	 CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME FROM ( SELECT T1.* FROM ( ")
				.append(" SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TRI.IMPLEMENTER,"
						+ "TRI.RESPONSIBLEPERSON,TRI.RESPONSIBLEDEPT,TRI.STATUS,TZR.PLANCODE,TZR.PLANNAME,TZI.ISSUESCODE,TZI.ISSUESNAME,"
						+ "TZI.AUDITOBJECTID,TZI.AUDITOBJECTTYPE,TZI.ISSUESTYPE,TZI.PROJECTID,HTS.REALNAME AS HANDLERNAME,"
						+ "ITS.REALNAME AS IMPLEMENTERNAME , RTS.REALNAME AS RESPONSIBLEPERSONNAME , RORG.ORGNAME AS RESPONSIBLEDETPNAME,"
						+ "TZRI.IMPLID,TZRI.STATUS AS TZRISTATUS,TZRV.STATUS AS TZRVSTATUS,TZRV.RESULTSTATUS AS TZRVRSTATUS,"
						+ "TZRV.EVALID AS TZRVEVALID  "
						+ "FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZR ON TRI.PLANID = TZR.PLANID LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID  ")
				.append(" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID ")
				.append(" LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_STAFF HTS ON HTS.STAFFID = TRI.HANDLERID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID ")
				.append(" LEFT JOIN TBL_STAFF RTS ON TRI.RESPONSIBLEPERSON = RTS.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TRI.RESPONSIBLEDEPT = RORG.ORGID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID ")
				.append(" WHERE 1 = 1");
		
		//整改评价 安全保密条件
		
		
		if(StringUtils.isNotBlank(plan.getStatusStrs()) ) {
			sqlSb.append(" AND TZR.STATUS IN (").append(plan.getStatusStrs()).append(") ");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TRI.STATUS = ").append(condition.getStatus());
		}
		if(impl != null && impl.getStatus() != null) {
			sqlSb.append(" AND TZRI.STATUS = ").append(impl.getStatus());
		}
		if(condition.getImplementer() != null) {
			sqlSb.append(" AND TRI.IMPLEMENTER = ").append(condition.getImplementer());
		}
		if(plan != null && plan.getCreateStaff() != null) {
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsiblePersonName())) {
			sqlSb.append(" AND RTS.REALNAME LIKE '%").append(condition.getResponsiblePersonName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsibleDetpName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getResponsibleDetpName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getAuditObjectName())) {
			sqlSb.append(" AND ( ATS.REALNAME LIKE '%").append(issues.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(issues.getAuditObjectName()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(issues.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(issues.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(issues.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%') ");
		}
		
		sqlSb.append("   ) ").append(DataBaseSqlConfig.setTableAliasName("T1"))
		.append(" ) "+DataBaseSqlConfig.setTableAliasName("T2")+" LEFT JOIN TBL_STAFF ATS ON T2.AUDITOBJECTID = ATS.STAFFID  LEFT JOIN TBL_ORGANIZATION AOG ON T2.AUDITOBJECTID = AOG.ORGID ")
		.append(" LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID ")
		.append(" ORDER BY  T2.relaId DESC ");
		
		return sqlSb.toString();
	}
	
	public String selectPageInfoListByRectificaionAllocation(TblRectificationIssuesVo issuesVo) throws Exception {
		TblZgzzIssuesilistVo issues = issuesVo.getIssues();
		TblZgzzRctevaluationVo valua = issuesVo.getValua();
		TblZgzzRectificationplanVo plan = issuesVo.getPlan();
		StringBuffer sqlSb = new StringBuffer("SELECT TZRP.PLANCODE,TZRP.PLANNAME,TRI.PLANID,TRI.RELAID,TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TRI.RESPONSIBLEPERSON,RTS.REALNAME AS RESPONSIBLEPERSONNAME,TRI.RESPONSIBLEDEPT,RORG.ORGNAME AS RESPONSIBLEDEPTNAME, ")
				.append("TRI.IMPLEMENTER,ITS.REALNAME AS IMPLEMENTERNAME,TZI.ISSUESID,TZI.ISSUESCODE,TZI.ISSUESNAME,TZI.CREATESTAFF,CTS.REALNAME AS CREATESTAFFNAME,TZI.ISSUESITEM,TZI.ISSUESTITLE,TZI.QUESTIONMEMO ")
				.append(",TZRV.EVALID,TZRV.STATUS,TZRV.RESULTSTATUS,TZRI.IMPLID,TRI.STATUS AS TRISTATUS,TZRI.STATUS AS TZRISTATUS FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append("INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append("LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF CTS ON CTS.STAFFID = TZI.CREATESTAFF LEFT JOIN TBL_STAFF RTS ON RTS.STAFFID = TRI.RESPONSIBLEPERSON LEFT JOIN TBL_ORGANIZATION RORG ON RORG.ORGID = TRI.RESPONSIBLEDEPT ")
				.append("LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZRP ON TRI.PLANID = TZRP.PLANID WHERE  1 = 1 ");
		
		if(StringUtils.isNotBlank(issuesVo.getPlanId())) {
			sqlSb.append(" AND TRI.PLANID = '").append(issuesVo.getPlanId()).append("' ");
		}else if(StringUtils.isNotBlank(issues.getDeptIds())) {
			sqlSb.append(" AND (TZI.CREATESTAFF = ").append(issues.getCreateStaff()).append(" OR TZI.LINKORGID IN (").append(issues.getDeptIds()).append(") OR TZI.LINKDEPTID IN (").append(issues.getDeptIds()).append("))");
		}else {
			sqlSb.append(" AND TZI.CREATESTAFF = ").append(issues.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZRP.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZRP.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		
		if(StringUtils.isNotBlank(issuesVo.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(issuesVo.getImplementerName()).append("%'");
		}
		
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
		
		if(issues.getStatus() != null) {
			sqlSb.append(" AND TZI.STATUS = ").append(issues.getStatus());
		}
		
		if(valua.getStatus() != null) {
			sqlSb.append(" AND TZRV.STATUS = ").append(valua.getStatus());
		}
		if(valua.getResultStatus() != null) {
			sqlSb.append(" AND TZRV.RESULTSTATUS = ").append(valua.getResultStatus());
		}
		sqlSb.append(" ORDER BY  TRI.RELAID DESC ");
		return sqlSb.toString();
	}
	
	public String selectListByExample(TblRectificationIssuesVo issues) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TRI.RELAID,TRI.RECTIFICATIONPLAN,TRI.PLANID,TRI.DEADLINE,TRI.RESPONSIBLEPERSON,RTS.REALNAME AS RESPONSIBLEPERSONNAME,TRI.RESPONSIBLEDEPT,RORG.ORGNAME AS RESPONSIBLEDEPTNAME,TRI.IMPLEMENTER,ITS.REALNAME AS IMPLEMENTERNAME,TZI.ISSUESID,TZI.ISSUESCODE,TZI.ISSUESNAME,TZI.CREATESTAFF,CTS.REALNAME AS CREATESTAFFNAME,TZI.ISSUESITEM,TZI.ISSUESTITLE,TZI.QUESTIONMEMO,TZI.CREATETIME FROM TBL_RECTIFICATION_ISSUES TRI")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) "+DataBaseSqlConfig.setTableAliasName("T1")+" ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION")
				.append(" LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF CTS ON CTS.STAFFID = TZI.CREATESTAFF")
				.append(" LEFT JOIN TBL_STAFF RTS ON RTS.STAFFID = TRI.RESPONSIBLEPERSON LEFT JOIN TBL_ORGANIZATION RORG ON RORG.ORGID = TRI.RESPONSIBLEDEPT")
				.append(" LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID WHERE TRI.PLANID = '").append(issues.getPlanId()).append("' ");
		if(issues.getHandlerId() != null) {
			sqlSb.append(" AND TRI.HANDLERID = ").append(issues.getHandlerId());
		}
		sqlSb.append(" ORDER BY TRI.RELAID ASC ");
		return sqlSb.toString();
	}
	
	
	public String selectListByIssuesId(String issuesId, String relaId) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TZRP.PLANCODE,TZRP.PLANNAME,TRI.PLANID,TRI.RELAID,TRI.RESULTMEMO,TRI.RECTIFICATIONMEASURES,TRI.RECTIFICATIONPLAN,TZRI.DEADLINE,TRI.RESPONSIBLEPERSON,RTS.REALNAME AS RESPONSIBLEPERSONNAME,TRI.RESPONSIBLEDEPT,RORG.ORGNAME AS RESPONSIBLEDEPTNAME, ")
				.append("TRI.IMPLEMENTER,ITS.REALNAME AS IMPLEMENTERNAME,TZI.ISSUESID,TZI.ISSUESCODE,TZI.ISSUESNAME,TZI.CREATESTAFF,CTS.REALNAME AS CREATESTAFFNAME,TZI.ISSUESITEM,TZI.ISSUESTITLE,TZI.QUESTIONMEMO ")
				.append(",TZRV.EVALID,TZRV.STATUS,TZRV.RESULTSTATUS,TZRI.IMPLID,TRI.STATUS AS TRISTATUS,TZRI.RECTIFICATIONMEASURES AS TZRIRECTIFICATIONMEASURES,TZRI.SITUATIONOVERVIEW AS TZRISITUATIONOVERVIEW,TZRI.ACHIVEMENT AS TZRIACHIVEMENT FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append("INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) "+DataBaseSqlConfig.setTableAliasName("T1")+" ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append("LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF CTS ON CTS.STAFFID = TZI.CREATESTAFF LEFT JOIN TBL_ORGANIZATION RORG ON RORG.ORGID = TRI.RESPONSIBLEDEPT ")
				.append("LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZRP ON TRI.PLANID = TZRP.PLANID "
						+ "LEFT JOIN TBL_STAFF RTS ON RTS.STAFFID = TZRP.RESPONSE WHERE  1 = 1 ");
		
		if(StringUtils.isNotBlank(issuesId)){
			sqlSb.append(" AND TRI.ISSUESID = '").append(issuesId).append("'");
		}
		if(StringUtils.isNotBlank(relaId)){
			sqlSb.append(" AND TRI.RELAID != '").append(relaId).append("'");
		}
		
		
		sqlSb.append(" ORDER BY  TZI.ISSUESID DESC ,TZRI.DEADLINE ASC");
		return sqlSb.toString();
	}
	
	public String getMaxNumberFor(MaxNumberParam p) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT   max(REPLACE(").append(p.getColumn()).append(",REPLACE('").append(p.getJgf()).append("','%',''),'')) FROM ").append(p.getTblName()).append(" WHERE 1=1 ") ;
//				<if test="queryParam.orgList!=null and queryParam.orgList.size()>0">
//					and ${queryParam.orgCol } IN
//					<foreach collection="queryParam.orgList" open="(" close=")"
//						item="item" separator=",">
//						#{item}
//					</foreach>
//				</if>
		if(StringUtils.isNotBlank(p.getSep())){
		  sqlSb.append(" AND "+p.getColumn()+" LIKE '"+p.getJgf()+"' AND (REPLACE(REPLACE("+p.getColumn()+",REPLACE('"+p.getJgf()+"','"+p.getSep()+"%',''),''),REPLACE(REPLACE("+p.getColumn()+",REPLACE('"+p.getJgf()+"','"+p.getSep()+"%',''),''),'"+p.getSep()+"','')) = '"+p.getSep()+"' )");
		}
		if(StringUtils.isNotBlank(p.getChChoiceCol())){
			  sqlSb.append(" and "+p.getChChoiceCol()+p.getBjf()+p.getChChoiceCol());
	         }
		return sqlSb.toString();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String selectHxzgRectificationPageInfoList(TblRectificationIssuesVo condition) throws Exception {
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		TblZgzzRectificationimplVo impl = condition.getReimpl();
		
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* ,CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO,	 CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME FROM ( SELECT T1.* FROM ( ")
				.append(" SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TRI.IMPLEMENTER,"
						+ "TRI.RESPONSIBLEPERSON,TRI.RESPONSIBLEDEPT,TRI.STATUS,TZR.PLANCODE,TZR.PLANNAME,TZI.ISSUESCODE,TZI.ISSUESNAME,"
						+ "TZI.AUDITOBJECTID,TZI.AUDITOBJECTTYPE,TZI.ISSUESTYPE,TZI.PROJECTID,HTS.REALNAME AS HANDLERNAME,"
						+ "ITS.REALNAME AS IMPLEMENTERNAME , RTS.REALNAME AS RESPONSIBLEPERSONNAME , RORG.ORGNAME AS RESPONSIBLEDETPNAME,"
						+ "TZRI.IMPLID,TZRI.STATUS AS TZRISTATUS,TZRV.STATUS AS TZRVSTATUS,TZRV.RESULTSTATUS AS TZRVRSTATUS,"
						+ "TZRV.EVALID AS TZRVEVALID  "
						+ "FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZR ON TRI.PLANID = TZR.PLANID LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID  ")
				.append(" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID ")
				.append(" LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_STAFF HTS ON HTS.STAFFID = TRI.HANDLERID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID ")
				.append(" LEFT JOIN TBL_STAFF RTS ON TRI.RESPONSIBLEPERSON = RTS.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TRI.RESPONSIBLEDEPT = RORG.ORGID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID ")
				.append(" WHERE 1 = 1");
		
		//整改评价 安全保密条件
		
		
		if(StringUtils.isNotBlank(plan.getStatusStrs()) ) {
			sqlSb.append(" AND TZR.STATUS IN (").append(plan.getStatusStrs()).append(") ");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TRI.STATUS = ").append(condition.getStatus());
		}
		if(impl != null && impl.getStatus() != null) {
			sqlSb.append(" AND TZRI.STATUS = ").append(impl.getStatus());
		}
		if(condition.getImplementer() != null) {
			sqlSb.append(" AND TRI.IMPLEMENTER = ").append(condition.getImplementer());
		}
		if(plan != null && plan.getCreateStaff() != null) {
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsiblePersonName())) {
			sqlSb.append(" AND RTS.REALNAME LIKE '%").append(condition.getResponsiblePersonName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsibleDetpName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getResponsibleDetpName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getAuditObjectName())) {
			sqlSb.append(" AND ( ATS.REALNAME LIKE '%").append(issues.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(issues.getAuditObjectName()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(issues.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(issues.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(issues.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%') ");
		}
		
		sqlSb.append("   ) ").append(DataBaseSqlConfig.setTableAliasName("T1"))
		.append(" ) "+DataBaseSqlConfig.setTableAliasName("T2")+" LEFT JOIN TBL_STAFF ATS ON T2.AUDITOBJECTID = ATS.STAFFID  LEFT JOIN TBL_ORGANIZATION AOG ON T2.AUDITOBJECTID = AOG.ORGID ")
		.append(" LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID ")
		.append(" ORDER BY  T2.relaId DESC ");
		
		return sqlSb.toString();
	}
	
	
	
	
	
	
	public String selectHxzgPageInfoList(TblRectificationIssuesVo condition) throws Exception {
		TblZgzzIssuesilistVo issues = condition.getIssues();
		TblZgzzRectificationplanVo plan = condition.getPlan();
		TblZgzzRectificationimplVo impl = condition.getReimpl();
		
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* ,CASE WHEN AUDITOBJECTTYPE = 3 THEN ATS.REALNAME ELSE AOG.ORGNAME END AS AUDITOBJECTNAME,CASE WHEN ISSUESTYPE = 1 THEN TNP.PROJECTCODE WHEN ISSUESTYPE = 2 THEN TTP.PLANNUMBER WHEN ISSUESTYPE = 3 THEN TNW.PROJECTCODE ELSE '' END AS PROJECTNO,	 CASE WHEN ISSUESTYPE = 1 THEN TNP.PRJOECTNAME WHEN ISSUESTYPE = 2 THEN TTP.PLANNAME WHEN ISSUESTYPE = 3 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME FROM ( SELECT T1.* FROM ( ")
				.append(" SELECT TRI.RELAID,TRI.ISSUESID,TRI.PLANID,TRI.HANDLERID,TRI.RECTIFICATIONPLAN,TRI.DEADLINE,TRI.IMPLEMENTER,"
						+ "TRI.RESPONSIBLEPERSON,TRI.RESPONSIBLEDEPT,TRI.STATUS,TZR.PLANCODE,TZR.PLANNAME,TZI.ISSUESCODE,TZI.ISSUESNAME,"
						+ "TZI.AUDITOBJECTID,TZI.AUDITOBJECTTYPE,TZI.ISSUESTYPE,TZI.PROJECTID,HTS.REALNAME AS HANDLERNAME,"
						+ "ITS.REALNAME AS IMPLEMENTERNAME , RTS.REALNAME AS RESPONSIBLEPERSONNAME , RORG.ORGNAME AS RESPONSIBLEDETPNAME,"
						+ "TZRI.IMPLID,TZRI.STATUS AS TZRISTATUS,TZRV.STATUS AS TZRVSTATUS,TZRV.RESULTSTATUS AS TZRVRSTATUS,"
						+ "TZRV.EVALID AS TZRVEVALID  "
						+ "FROM TBL_RECTIFICATION_ISSUES TRI ")
				.append(" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION ")
				.append(" LEFT JOIN TBL_ZGZZ_RECTIFICATIONPLAN TZR ON TRI.PLANID = TZR.PLANID LEFT JOIN TBL_ZGZZ_ISSUESILIST TZI ON TRI.ISSUESID = TZI.ISSUESID LEFT JOIN TBL_STAFF ATS ON TZI.AUDITOBJECTID = ATS.STAFFID  ")
				.append(" LEFT JOIN TBL_ORGANIZATION AOG ON TZI.AUDITOBJECTID = AOG.ORGID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZI.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZI.PROJECTID = TNP.PROJECTID ")
				.append(" LEFT JOIN TBL_TESTPLAN TTP ON TZI.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_STAFF HTS ON HTS.STAFFID = TRI.HANDLERID LEFT JOIN TBL_STAFF ITS ON TRI.IMPLEMENTER = ITS.STAFFID ")
				.append(" LEFT JOIN TBL_STAFF RTS ON TRI.RESPONSIBLEPERSON = RTS.STAFFID LEFT JOIN TBL_ORGANIZATION RORG ON TRI.RESPONSIBLEDEPT = RORG.ORGID LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL TZRI ON TRI.RELAID = TZRI.RELAID LEFT JOIN TBL_ZGZZ_RCTEVALUATION TZRV ON TZRI.IMPLID = TZRV.IMPLID ")
				.append(" WHERE 1 = 1 AND TZRI.CONCLUSION='未整改'   and  (TZRI.STATUS=6  or TZRI.ZCSTATUS=1 )");
		
		
		
		if(StringUtils.isNotBlank(plan.getStatusStrs()) ) {
			sqlSb.append(" AND TZR.STATUS IN (").append(plan.getStatusStrs()).append(") ");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TRI.STATUS = ").append(condition.getStatus());
		}
		if(impl != null && impl.getStatus() != null) {
			sqlSb.append(" AND TZRI.STATUS = ").append(impl.getStatus());
		}
		if(condition.getImplementer() != null) {
			sqlSb.append(" AND TRI.IMPLEMENTER = ").append(condition.getImplementer());
		}
		if(plan != null && plan.getCreateStaff() != null) {
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(plan != null && StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesCode())) {
			sqlSb.append(" AND TZI.ISSUESCODE LIKE '%").append(issues.getIssuesCode()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getIssuesName())) {
			sqlSb.append(" AND TZI.ISSUESNAME LIKE '%").append(issues.getIssuesName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getImplementerName())) {
			sqlSb.append(" AND ITS.REALNAME LIKE '%").append(condition.getImplementerName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsiblePersonName())) {
			sqlSb.append(" AND RTS.REALNAME LIKE '%").append(condition.getResponsiblePersonName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getResponsibleDetpName())) {
			sqlSb.append(" AND RORG.ORGNAME LIKE '%").append(condition.getResponsibleDetpName()).append("%'");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getAuditObjectName())) {
			sqlSb.append(" AND ( ATS.REALNAME LIKE '%").append(issues.getAuditObjectName()).append("%' OR AOG.ORGNAME LIKE '%").append(issues.getAuditObjectName()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectNo())) {
			sqlSb.append(" AND ( TNP.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%' OR TTP.PLANNUMBER LIKE '%").append(issues.getProjectNo()).append("%' OR TNW.PROJECTCODE LIKE '%").append(issues.getProjectNo()).append("%') ");
		}
		if(issues != null && StringUtils.isNotBlank(issues.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(issues.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(issues.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(issues.getProjectName()).append("%') ");
		}
		
		sqlSb.append("   ) ").append(DataBaseSqlConfig.setTableAliasName("T1"))
		.append(" ) "+DataBaseSqlConfig.setTableAliasName("T2")+" LEFT JOIN TBL_STAFF ATS ON T2.AUDITOBJECTID = ATS.STAFFID  LEFT JOIN TBL_ORGANIZATION AOG ON T2.AUDITOBJECTID = AOG.ORGID ")
		.append(" LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID ")
		.append(" ORDER BY  T2.relaId DESC ");
		
		return sqlSb.toString();
	}
	
}
