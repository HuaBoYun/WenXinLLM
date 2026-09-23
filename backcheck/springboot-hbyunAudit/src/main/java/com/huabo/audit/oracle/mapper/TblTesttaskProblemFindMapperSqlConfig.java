package com.huabo.audit.oracle.mapper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblTesttaskProblemFind;
import com.huabo.audit.oracle.vo.TblTesttaskProblemFindVo;

public class TblTesttaskProblemFindMapperSqlConfig {

	
	public String selectPageInfoByIssues(TblTesttaskProblemFind problem) {
		StringBuffer sqlSb = new StringBuffer("SELECT TTP.*,TS.REALNAME,TORG.ORGNAME,TASK.PLANID,PL.PLANNUMBER,PL.PLANNAME,RS.RISKNUMBER,2 AS AUDITOBJECTTYPE from TBL_TESTTASK_PROBLEMFIND TTP ")
				.append(" LEFT JOIN TBL_TESTTASK task on TTP.TESTTASKID=TASK.TESTTASKID LEFT JOIN TBL_RISK rs on TTP.RISKNUMBERID=RS.RISKID LEFT JOIN TBL_TESTPLAN pl on task.planid=pl.testplanid ")
				.append(" LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG where TTP.STATUS in (6)")
				.append(" AND TTP.FINDID NOT IN (SELECT QUESITIONID FROM TBL_ZGZZ_ISSUESILIST WHERE ISSUESTYPE = 2 AND QUESITIONID IS NOT NULL)");
		
		if(problem.getTestYear() != null) {
			sqlSb.append(" AND TTP.TESTYEAR = ").append(problem.getTestYear() );
		}
		
		if(StringUtils.isNotBlank(problem.getPlanname())) {
			sqlSb.append(" AND PL.PLANNAME LIKE '%").append(problem.getPlanname()).append("%'");
		}
		if(StringUtils.isNotBlank(problem.getPlannumber())) {
			sqlSb.append(" AND PL.PLANNUMBER LIKE '%").append(problem.getPlannumber()).append("%'");
		}
		if(StringUtils.isNotBlank(problem.getRisknumber())) {
			sqlSb.append(" AND RS.RISKNUMBER LIKE '%").append(problem.getRisknumber()).append("%'");
		}
		if(StringUtils.isNotBlank(problem.getProblemtype())) {
			sqlSb.append(" AND TTP.PROBLEMTYPE LIKE '%").append(problem.getProblemtype()).append("%'");
		}
		if(StringUtils.isNotBlank(problem.getOneprocess())){
			sqlSb.append(" AND TTP.ONEPROCESS LIKE '%").append(problem.getOneprocess()).append("%'");
		}
		sqlSb.append(" ORDER BY FINDID DESC ");
		return sqlSb.toString();
	}
	
	public String selectListByPageInfo(com.huabo.audit.util.PageInfo<TblTesttaskProblemFind> pageInfo,TblTesttaskProblemFindVo tblTesttaskProblemFindVo) {
		StringBuffer sbSql = new StringBuffer("SELECT TTP.*,TS.REALNAME,TORG.ORGNAME from TBL_TESTTASK_PROBLEMFIND TTP"
				+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID"
				+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG"
				+ " where 1=1 and TTP.status in (6)");
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getDefectlevel())) {
			sbSql.append(" and TTP.DEFECTLEVEL LIKE '%" + tblTesttaskProblemFindVo.getDefectlevel() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getProblemtype())) {
			sbSql.append(" and TTP.PROBLEMTYPE LIKE '%" + tblTesttaskProblemFindVo.getProblemtype() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getOneprocess())) {
			sbSql.append(" and TTP.ONEPROCESS LIKE '%" + tblTesttaskProblemFindVo.getOneprocess() +"%' ");
		}
		if (tblTesttaskProblemFindVo.getTestYear()>0) {
			sbSql.append(" and TTP.TESTYEAR = " + tblTesttaskProblemFindVo.getTestYear() +" ");
		}
 		sbSql.append(" and TTP.CREATESTAFFID = " + tblTesttaskProblemFindVo.getCreatestaffid() +" ");
 		return sbSql.toString();
	}
	
	
	public String selectCountByPageInfo(com.huabo.audit.util.PageInfo<TblTesttaskProblemFind> pageInfo,TblTesttaskProblemFindVo tblTesttaskProblemFindVo) {
		StringBuffer sbSql = new StringBuffer("SELECT count(0) from TBL_TESTTASK_PROBLEMFIND TTP"
				+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID"
				+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG"
				+ " where 1=1 and TTP.status in (6)");
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getDefectlevel())) {
			sbSql.append(" and TTP.DEFECTLEVEL LIKE '%" + tblTesttaskProblemFindVo.getDefectlevel() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getProblemtype())) {
			sbSql.append(" and TTP.PROBLEMTYPE LIKE '%" + tblTesttaskProblemFindVo.getProblemtype() +"%' ");
		}
		if (StringUtils.isNotBlank(tblTesttaskProblemFindVo.getOneprocess())) {
			sbSql.append(" and TTP.ONEPROCESS LIKE '%" + tblTesttaskProblemFindVo.getOneprocess() +"%' ");
		}
		if (tblTesttaskProblemFindVo.getTestYear()>0) {
			sbSql.append(" and TTP.TESTYEAR = " + tblTesttaskProblemFindVo.getTestYear() +" ");
		}
 		sbSql.append(" and TTP.CREATESTAFFID = " + tblTesttaskProblemFindVo.getCreatestaffid() +" ");
 		return sbSql.toString();
	}
	
}
