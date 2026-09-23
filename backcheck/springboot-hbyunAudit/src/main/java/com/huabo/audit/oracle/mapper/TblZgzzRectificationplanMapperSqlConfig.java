package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblZgzzRectificationplan;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblZgzzRectificationplanVo;

public class TblZgzzRectificationplanMapperSqlConfig {
	
	public String  selectAllListByExportt(TblZgzzRectificationplanVo plan) throws Exception {
		StringBuffer sqlSb = new StringBuffer("")
				.append(" SELECT TZR.*,ZRR.REALNAME AS ZRRREALNAME , CTS.REALNAME AS CREATESTAFFNAME,HTS.REALNAME AS HANDLERNAME"
						+ ",TNP.PRJOECTNAME AS SJNAME,TTP.PLANNAME AS NKNAME,TNW.PROJECTNAME AS WBNAME "
						+ "FROM TBL_ZGZZ_RECTIFICATIONPLAN TZR ")
				.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZR.PROJECTID = TNP.PROJECTID "
						+ "LEFT JOIN TBL_TESTPLAN TTP ON TZR.PROJECTID = TTP.TESTPLANID "
						+ "LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZR.PROJECTID = TNW.PROJECTID ")
				.append(" LEFT JOIN TBL_STAFF ZRR ON TZR.RESPONSE = ZRR.STAFFID "
						+ "LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID "
						+ "LEFT JOIN TBL_STAFF HTS ON TZR.HANDLERID = HTS.STAFFID "
						+ "WHERE 1 = 1");
		
		if(plan.getResponse() != null) {
			sqlSb.append(" AND TZR.RESPONSE = ").append(plan.getResponse());
		}else if(StringUtils.isNotBlank(plan.getDeptIds())) {
			sqlSb.append(" AND ( TZR.CREATESTAFF = ").append(plan.getCreateStaff()).append(" OR TZR.LINKDEPTID IN (").append(plan.getDeptIds()).append("))");
		}else {
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(plan.getPlanType() != null) {
			sqlSb.append(" AND TZR.PLANTYPE = ").append(plan.getPlanType());
		}
		if(StringUtils.isNotBlank(plan.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(plan.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(plan.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(plan.getProjectName()).append("%')");
		}
		if(StringUtils.isNotBlank(plan.getZrrRealName())) {
			sqlSb.append(" AND ZRR.REALNAME LIKE '%").append(plan.getZrrRealName()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(plan.getHandlerName()).append("%'");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(plan.getDeadlineStart() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME >= TO_DATE('").append(DateUtil.parseDate(plan.getDeadlineStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getDeadlineEnd() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME <= TO_DATE('").append(DateUtil.parseDate(plan.getDeadlineEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getCreateTimeStart() != null) {
			sqlSb.append(" AND TZR.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(plan.getCreateTimeStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getCreateTimeEnd() != null) {
			sqlSb.append(" AND TZR.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(plan.getCreateTimeEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		
		sqlSb.append(" ORDER BY TZR.PLANID DESC ");
		System.out.println("导出sql"+sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectPageInfoList(TblZgzzRectificationplanVo plan,String sqlStr,TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT T2.*,CASE WHEN T2.PLANTYPE = 1 THEN TNP.PRJOECTNAME WHEN T2.PLANTYPE = 2 THEN TTP.PLANNAME WHEN T2.PLANTYPE = 3 OR T2.PLANTYPE = 4 THEN TNW.PROJECTNAME ELSE '' END AS PROJECTNAME  FROM (")
				.append(" SELECT TZR.*,ZRR.REALNAME AS ZRRREALNAME , CTS.REALNAME AS CREATESTAFFNAME,HTS.REALNAME AS HANDLERNAME FROM TBL_ZGZZ_RECTIFICATIONPLAN TZR ")
				.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZR.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZR.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZR.PROJECTID = TNW.PROJECTID ")
				.append(" LEFT JOIN TBL_STAFF ZRR ON TZR.RESPONSE = ZRR.STAFFID LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF HTS ON TZR.HANDLERID = HTS.STAFFID WHERE 1 = 1");
		
//		if(plan.getUseSecrect() == 1) {
//			//知悉范围查询
//			sqlSb.append(" AND ( "+DataBaseSqlConfig.getWhereColumnInStr("TZR.STAFFSCOPEIDS", plan.getSecrectStaff().toString(),",")+" OR TZR.CREATESTAFF = "+plan.getSecrectStaff()+" OR TZR.STAFFSCOPEIDS IS NULL OR TZR.STAFFSCOPEIDS = '' )");
//			
//			//密级查询
//			 if(StringUtils.isNotBlank(plan.getSecrectScopeIds())) {
//				 sqlSb.append(" AND (TZR.SECRECTLEVELID IN (").append(plan.getSecrectScopeIds()).append(") OR TZR.SECRECTLEVELID IS NULL OR TZR.SECRECTLEVELID = ''  )");
//		     }else {
//		    	 sqlSb.append(" AND (TZR.SECRECTLEVELID IS NULL OR SECRECTLEVELID = ''  )");
//		     }
//		}
		
		if(null!=loginStaff) {
			sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TZR.LINKORGID", "TZR.LINKDEPTID", "TZR.CREATESTAFF", "TZR.SECRECTLEVELID", "TZR.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		}
		
		if(plan.getResponse() != null) {
			sqlSb.append(" AND (TZR.RESPONSE = ").append(plan.getResponse());
			sqlSb.append(" OR TZR.CREATESTAFF = ").append(plan.getResponse()).append(" ) ");
		}else if(StringUtils.isNotBlank(plan.getDeptIds())) {
			sqlSb.append(" AND ( TZR.CREATESTAFF = ").append(plan.getCreateStaff()).append(" OR TZR.LINKDEPTID IN (").append(plan.getDeptIds()).append("))");
		}else if(plan.getCreateStaff() != null){
			sqlSb.append(" AND TZR.CREATESTAFF = ").append(plan.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(sqlStr)){
			sqlSb.append(sqlStr);
		}
		
		if(StringUtils.isNotBlank(plan.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(plan.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(plan.getPlanName()).append("%'");
		}
		if(plan.getPlanType() != null) {
			sqlSb.append(" AND TZR.PLANTYPE = ").append(plan.getPlanType());
		}
		if(StringUtils.isNotBlank(plan.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(plan.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(plan.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(plan.getProjectName()).append("%')");
		}
		if(StringUtils.isNotBlank(plan.getZrrRealName())) {
			sqlSb.append(" AND ZRR.REALNAME LIKE '%").append(plan.getZrrRealName()).append("%'");
		}
		if(StringUtils.isNotBlank(plan.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(plan.getHandlerName()).append("%'");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(plan.getStatus());
		}
		if(plan.getDeadlineStart() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME >= TO_DATE('").append(DateUtil.parseDate(plan.getDeadlineStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getDeadlineEnd() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME <= TO_DATE('").append(DateUtil.parseDate(plan.getDeadlineEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getCreateTimeStart() != null) {
			sqlSb.append(" AND TZR.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(plan.getCreateTimeStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(plan.getCreateTimeEnd() != null) {
			sqlSb.append(" AND TZR.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(plan.getCreateTimeEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		
		sqlSb.append(" ORDER BY TZR.PLANID DESC  ");
//		if(plan.getResponse() != null) {
//			sqlSb.append(" ,TZR.STATUS ASC  ");
//		}
		sqlSb.append("  ) ").append(DataBaseSqlConfig.setTableAliasName("T2"))
		.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON T2.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON T2.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON T2.PROJECTID = TNW.PROJECTID"
				+ " ORDER BY T2.PLANID DESC  ");
		return sqlSb.toString();
	}
	
	public String selectPageInfoCount(PageInfo<TblZgzzRectificationplanVo> pageInfo) {
		TblZgzzRectificationplanVo condition = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer(" SELECT COUNT(0) FROM TBL_ZGZZ_RECTIFICATIONPLAN TZR ")
				.append(" LEFT JOIN TBL_NBSJ_PROJECT TNP ON TZR.PROJECTID = TNP.PROJECTID LEFT JOIN TBL_TESTPLAN TTP ON TZR.PROJECTID = TTP.TESTPLANID LEFT JOIN TBL_NBSJ_WBPROJECT TNW ON TZR.PROJECTID = TNW.PROJECTID ")
				.append(" LEFT JOIN TBL_STAFF ZRR ON TZR.RESPONSE = ZRR.STAFFID LEFT JOIN TBL_STAFF CTS ON TZR.CREATESTAFF = CTS.STAFFID LEFT JOIN TBL_STAFF HTS ON TZR.HANDLERID = HTS.STAFFID WHERE ");
		
		if(StringUtils.isNotBlank(condition.getDeptIds())) {
			sqlSb.append(" ( TZR.CREATESTAFF = ").append(condition.getCreateStaff()).append(" OR TZR.LINKDEPTID IN (").append(condition.getDeptIds()).append("))");
		}else {
			sqlSb.append(" TZR.CREATESTAFF = ").append(condition.getCreateStaff());
		}
		
		if(StringUtils.isNotBlank(pageInfo.getSqlStr())) {
			sqlSb.append(pageInfo.getSqlStr());
		}
		
		if(StringUtils.isNotBlank(condition.getPlanCode())) {
			sqlSb.append(" AND TZR.PLANCODE LIKE '%").append(condition.getPlanCode()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getPlanName())) {
			sqlSb.append(" AND TZR.PLANNAME LIKE '%").append(condition.getPlanName()).append("%'");
		}
		if(condition.getPlanType() != null) {
			sqlSb.append(" AND TZR.PLANTYPE = ").append(condition.getPlanType());
		}
		if(StringUtils.isNotBlank(condition.getProjectName())) {
			sqlSb.append(" AND ( TNP.PRJOECTNAME LIKE '%").append(condition.getProjectName()).append("%' OR TTP.PLANNAME LIKE '%").append(condition.getProjectName()).append("%' OR TNW.PROJECTNAME LIKE '%").append(condition.getProjectName()).append("%')");
		}
		if(StringUtils.isNotBlank(condition.getZrrRealName())) {
			sqlSb.append(" AND ZRR.REALNAME LIKE '%").append(condition.getZrrRealName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getHandlerName())) {
			sqlSb.append(" AND HTS.REALNAME LIKE '%").append(condition.getHandlerName()).append("%'");
		}
		if(condition.getStatus() != null) {
			sqlSb.append(" AND TZR.STATUS = ").append(condition.getStatus());
		}
		if(condition.getDeadlineStart() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME >= TO_DATE('").append(DateUtil.parseDate(condition.getDeadlineStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(condition.getDeadlineEnd() != null) {
			sqlSb.append(" AND TZR.DEADLINETIME <= TO_DATE('").append(DateUtil.parseDate(condition.getDeadlineEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(condition.getCreateTimeStart() != null) {
			sqlSb.append(" AND TZR.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(condition.getCreateTimeStart(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		if(condition.getCreateTimeEnd() != null) {
			sqlSb.append(" AND TZR.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(condition.getCreateTimeEnd(), DateUtil.DATE_SMALL_STR)).append("','YYYY-MM-DD')");
		}
		return sqlSb.toString();
	}
	
	public String checkRepeatPlanName(TblZgzzRectificationplan rectification) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_ZGZZ_RECTIFICATIONPLAN WHERE PLANNAME = '").append(rectification.getPlanName()).append("'");
		
		if(rectification.getPlanId() != null) {
			sqlSb.append(" AND PLANID != '").append(rectification.getPlanId()).append("'");
		}
		
		return sqlSb.toString();
	}
	
	public String checkRepeatPlanCode(TblZgzzRectificationplan rectification) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_ZGZZ_RECTIFICATIONPLAN WHERE PLANCODE = '").append(rectification.getPlanCode()).append("'");
		
		if(rectification.getPlanId() != null) {
			sqlSb.append(" AND PLANID != '").append(rectification.getPlanId()).append("'");
		}
		
		return sqlSb.toString();
	}
	
	public String selectPageInfoNkProjectList( TblZgzzProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT TESTPLANID,PLANNUMBER,PLANNAME FROM TBL_TESTPLAN WHERE TESTPLANID IN (SELECT PROJECTID FROM TBL_ZGZZ_ISSUESILIST WHERE ISSUESTYPE = 2)");
		
		if(StringUtils.isNotBlank(project.getPlanCode())) {
			sqlSb.append(" AND PLANNUMBER LIKE '%").append(project.getPlanCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(project.getPlanName())) {
			sqlSb.append(" AND PLANNAME LIKE '%").append(project.getPlanName()).append("%'");
		}
		
		sqlSb.append("ORDER BY TESTPLANID DESC ");
		return sqlSb.toString();
	}
	
}
