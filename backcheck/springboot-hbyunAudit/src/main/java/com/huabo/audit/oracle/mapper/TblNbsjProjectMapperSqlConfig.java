package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import org.apache.commons.lang.StringUtils;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjArchiveEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.vo.TblGkProjectVo;
import com.huabo.audit.oracle.vo.TblGkQuestionVo;
import com.huabo.audit.oracle.vo.TblGkZgQuestionVo;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectRwfpVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.oracle.vo.TblnbsjProjectZXYLVo;
import com.huabo.audit.oracle.vo.ZgwtDetailVo;
import com.huabo.audit.util.BaseVo;
import com.huabo.audit.util.PageInfo;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.annotations.Param;

public class TblNbsjProjectMapperSqlConfig {
	
	public String selectZgListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) {
//		StringBuffer sqlSb = new StringBuffer("SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,(ENDDATE-STARTDATE)+1 DAYS FROM (");
//		sqlSb.append(" SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,ROWNUM RN FROM (");
//		sqlSb.append(" SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
//		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS != 4 AND P.ORGID = "+project.getOrgId());
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT DISTINCT TNA.*,TS.REALNAME,TAS.REALNAME auditStaffName,TNA.orgIdNames auditOrgName "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.PROJECTID = T.PROJECTID "
				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID "
				+ " LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID "
				+ " LEFT JOIN TBL_STAFF TAS ON TNA.AUDITSTAFFID = TAS.STAFFID "
				+ " LEFT JOIN TBL_ORGANIZATION TD ON TNA.AUDITORGID = TD.ORGID "
				+ "WHERE TNA.examineType= 6  ");//AND TNA.ORGID = "+project.getOrgId()
		
		if(StrUtil.isNotBlank(project.getRolename())&&("审计部负责人".equals(project.getRolename())||"审计责任人".equals(project.getRolename()))) {
			sqlSb.append(" AND TNA.ORGID = "+project.getOrgId());
			sqlSb.append(" or (TNS.STAFFID = "+project.getStaffId()+" OR TNA.CREATESTAFFID="+project.getStaffId()+" ");
			sqlSb.append(" OR TNA.PMID = "+project.getStaffId());
			sqlSb.append(")");
		}else{
			sqlSb.append(" AND (TNS.STAFFID = "+project.getStaffId()+" OR TNA.CREATESTAFFID="+project.getStaffId()+" ");
			sqlSb.append(" OR TNA.PMID = "+project.getStaffId());
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND TNA.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND TNA.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND TNA.STATUS = "+project.getStatus());
		}
		if(project.getStartDate() !=null && !"".equals(project.getStartDate())){
			sqlSb.append(" AND TNA.STARTDATE >= TO_DATE('"+project.getStartDate()+"', 'yyyy-MM-dd')");
		}
		if(project.getEndDate() !=null && !"".equals(project.getEndDate())){
			sqlSb.append(" AND TNA.ENDDATE <= TO_DATE('"+project.getEndDate()+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(" ORDER BY TNA.PROJECTID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectZgCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM ( SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.examineType= 6  ");
		
		if(StrUtil.isNotBlank(project.getRolename())&&("审计部负责人".equals(project.getRolename())||"审计责任人".equals(project.getRolename()))) {
			sqlSb.append(" AND TNA.ORGID = "+project.getOrgId());
			sqlSb.append(" or (TNS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId()+" ");
			sqlSb.append(" OR P.PMID = "+project.getStaffId());
			sqlSb.append(")");
		}else{
			sqlSb.append(" AND (TNS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId()+" ");
			sqlSb.append(" OR P.PMID = "+project.getStaffId());
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		if(project.getStartDate() !=null && !"".equals(project.getStartDate())){
			sqlSb.append(" AND P.STARTDATE >= TO_DATE('"+project.getStartDate()+"', 'yyyy-MM-dd')");
		}
		if(project.getEndDate() !=null && !"".equals(project.getEndDate())){
			sqlSb.append(" AND P.ENDDATE <= TO_DATE('"+project.getEndDate()+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(")");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	
	
	
	public String selectProjectgdListByPageInfo( TblnbsjProjectVo project) {
//		StringBuffer sqlSb = new StringBuffer("SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,(ENDDATE-STARTDATE)+1 DAYS FROM (");
//		sqlSb.append(" SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,ROWNUM RN FROM (");
//		sqlSb.append(" SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
//		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS = 4  ");
//	
//	
//	
//		if(project.getStaffId() != null) {
//			sqlSb.append(" AND (TS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId());
//			if(project.getPmId() == null) {
//				sqlSb.append(" OR P.PMID = "+project.getStaffId());
//			}
//			sqlSb.append(")");
//		}
//		if(project.getPmId() != null ) {
//			sqlSb.append(" AND P.PMID = "+project.getPmId());
//		}
//		
//		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
//			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
//		}
//		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
//			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
//		}
//		if(project.getStatus() != null) {
//			sqlSb.append(" AND P.STATUS = "+project.getStatus());
//		}
//		if(projectStartDate !=null && !"".equals(projectStartDate)){
//			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectStartDate+"', 'yyyy-MM-dd')");
//		}
//		if(projectEndDate !=null && !"".equals(projectEndDate)){
//			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectEndDate+"', 'yyyy-MM-dd')");
//		}
//		sqlSb.append(" ORDER BY P.PROJECTID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		StringBuffer sqlSb = new StringBuffer("SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS = 4  ");
	
	
	
		if(project.getStaffId() != null) {
			sqlSb.append(" AND (TS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId());
			if(project.getPmId() == null) {
				sqlSb.append(" OR P.PMID = "+project.getStaffId());
			}
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		sqlSb.append(" ORDER BY P.PROJECTID DESC");
		
		return sqlSb.toString();
	}

	
	
	public String selectProjectgdListByPageCount(String projectStartDate, String projectEndDate, TblnbsjProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT count(*) FROM (");
		sqlSb.append(" SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS = 4  ");
		
		if(project.getStaffId() != null) {
			sqlSb.append(" AND (TS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId());
			if(project.getPmId() == null) {
				sqlSb.append(" OR P.PMID = "+project.getStaffId());
			}
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		if(projectStartDate !=null && !"".equals(projectStartDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectStartDate+"', 'yyyy-MM-dd')");
		}
		if(projectEndDate !=null && !"".equals(projectEndDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectEndDate+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(" ORDER BY P.PROJECTID DESC ) T1  ");
		return sqlSb.toString();
	
	}
	
	public String selectAuditFileListPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,String projectName, BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* FROM ( SELECT T1.*,ROWNUM RN FROM ( SELECT NP.PROJECTID,NP.PRJOECTNAME,TS.STAFFID PMSTAFFID,TS.REALNAME PMREALNAME,AUORG.ORGID AUORGID,AUORG.ORGNAME AUORGNAME,NP.AUDITTYPE,NP.PROJECTSOURCE,NP.STARTDATE,NP.ENDDATE,NP.ASSIGBEDCONTROLTIME,NP.STATUS FROM TBL_NBSJ_PROJECT NP LEFT JOIN TBL_STAFF TS ON NP.PMID = TS.STAFFID LEFT JOIN TBL_ORGANIZATION AUORG ON AUORG.ORGID = NP.AUDITORGID WHERE NP.STATUS = "+TblNbsjProject.GD_STATUS+" AND NP.ORGID = "+orgid);
		if(projectName != null && !"".equals(projectName)) {
			sqlSb.append(" AND NP.PRJOECTNAME LIKE '%"+projectName+"%'");
		}
		sqlSb.append(" ORDER BY NP.STARTDATE DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectAuditFileCountByPage(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectName,BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_PROJECT NP LEFT JOIN TBL_STAFF TS ON NP.PMID = TS.STAFFID LEFT JOIN TBL_ORGANIZATION AUORG ON AUORG.ORGID = NP.AUDITORGID WHERE NP.STATUS = "+TblNbsjProject.GD_STATUS+" AND NP.ORGID = "+orgid);
		if(projectName != null && !"".equals(projectName)) {
			sqlSb.append(" AND NP.PRJOECTNAME LIKE '%"+projectName+"%'");
		}
		return sqlSb.toString();
	}
	
	
	public String selectProjectListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,String projectStartDate, String projectEndDate, TblnbsjProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,(ENDDATE-STARTDATE)+1 DAYS FROM (");
		sqlSb.append(" SELECT PROJECTID,PRJOECTNAME,PROJECTCODE,PROJECTSOURCE,STATUS,STARTDATE,ENDDATE,COSTS,STAFFID,REALNAME,EXAMINETYPE,ROWNUM RN FROM (");
		sqlSb.append(" SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS != 4 AND P.ORGID = "+project.getOrgId());
		
		if(project.getStaffId() != null) {
			sqlSb.append(" AND (TS.STAFFID = "+project.getStaffId()+" OR TS.STAFFID IS NULL");
			if(project.getPmId() == null) {
				sqlSb.append(" OR P.PMID = "+project.getStaffId());
			}
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		if(projectStartDate !=null && !"".equals(projectStartDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectStartDate+"', 'yyyy-MM-dd')");
		}
		if(projectEndDate !=null && !"".equals(projectEndDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectEndDate+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(" ORDER BY P.PROJECTID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectProjectCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String projectStartDate,String projectEndDate, TblnbsjProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM ( SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS != 4 AND P.ORGID = "+project.getOrgId());
		
		if(project.getStaffId() != null) {
			sqlSb.append(" AND (TS.STAFFID = "+project.getStaffId()+" OR TS.STAFFID IS NULL");
			if(project.getPmId() == null) {
				sqlSb.append(" OR P.PMID = "+project.getStaffId());
			}
			sqlSb.append(")");
		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		if(projectStartDate !=null && !"".equals(projectStartDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectStartDate+"', 'yyyy-MM-dd')");
		}
		if(projectEndDate !=null && !"".equals(projectEndDate)){
			sqlSb.append(" AND P.STARTDATE >= =TO_DATE('"+projectEndDate+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(")");
		return sqlSb.toString();
	}
	
	
	public String selectListByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT DISTINCT TNA.*,CRESTAFF.REALNAME REALNAME,ORG.ORGNAME auditOrgName,TS.REALNAME auditStaffName "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 ");
//				+ "AND TNA.PROJECTID = "+projectId);
		
		
		if(tblnbsjProjectVo.getProjectId()!=null) {
			sb.append(" AND dd.PROJECTID = '"+tblnbsjProjectVo.getProjectId()+"'");
		}
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
//		TblNbsjProject plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM ("
				+ " SELECT  DISTINCT TNA.*,CRESTAFF.REALNAME REALNAME,ORG.ORGNAME auditOrgName,TS.REALNAME auditStaffName FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 ");
		
		if(tblnbsjProjectVo.getProjectId()!=null) {
			sb.append(" AND dd.PROJECTID = '"+tblnbsjProjectVo.getProjectId()+"'");
		}
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		
		sb.append(" ) ");
		return sb.toString();
	}
	
	
	
	
	
	//==档案列表
	public String selectSjgdNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT DISTINCT TNA.PROJECTID,TNA.projectcode,TNA.PRJOECTNAME,TNA.AUDITTYPE,TNA.PLANYEAR,TNA.ORGIDNAMES,SJORG.ORGNAME auditOrg "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = TNA.ORGID "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS);
		
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		System.out.println(sb.toString());
		return sb.toString();
	}
	public String selectSjgdNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS);
		
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		
		return sb.toString();
	}
	//==档案列表   个人
	public String selectSjgdNewListByStaffidPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT DISTINCT TNA.PROJECTID,TNA.projectcode,TNA.PRJOECTNAME,TNA.AUDITTYPE,TNA.PLANYEAR,TNA.ORGIDNAMES,SJORG.ORGNAME auditOrg "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = TNA.ORGID "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS);
		
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		if (tblnbsjProjectVo.getStaffId()!=null) {
			sb.append(" and (TNA.PMID="+tblnbsjProjectVo.getStaffId());
		}
		
		
		sb.append(" OR TNA.PROJECTID IN (SELECT PROJECTID FROM TBL_NBSJ_BORROWRECORD WHERE STATUS =6 AND RETURNDATE >=SYSDATE AND STAFFID="+tblnbsjProjectVo.getStaffId()+"))");
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectSjgdNewCountByStaffidPageInfo(PageInfo<TblNbsjProject> pageInfo,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS);
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		if (tblnbsjProjectVo.getStaffId()!=null) {
			sb.append(" and TNA.PMID="+tblnbsjProjectVo.getStaffId());
		}
		sb.append(" OR TNA.PROJECTID IN (SELECT PROJECTID FROM TBL_NBSJ_BORROWRECORD WHERE STATUS =4 AND RETURNDATE >=SYSDATE AND STAFFID="+tblnbsjProjectVo.getStaffId()+")");
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	//==档案借阅  
	public String selectDajyNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,BigDecimal orgid,BigDecimal staffid,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT  DISTINCT NP.PROJECTID,NP.projectcode,NP.PRJOECTNAME,NP.AUDITTYPE,NP.PLANYEAR,NP.ORGIDNAMES,SJORG.ORGNAME auditOrg,(CASE WHEN TD.DAYNUMBER >0 THEN 1 WHEN TD.DAYNUMBER <0 THEN 0 WHEN TNB.STATUS > 0 AND TNB.STATUS < 6 THEN 2 ELSE 0 END) pStatus from TBL_NBSJ_PROJECT NP "
				+ "LEFT JOIN ( SELECT PROJECTID, (MAX( RETURNDATE )-SYSDATE) DAYNUMBER FROM TBL_NBSJ_BORROWRECORD  WHERE 1 = 1 AND STATUS=6 AND STAFFID="+staffid+" GROUP BY PROJECTID  ) TD ON NP.PROJECTID = TD.PROJECTID	"
				+ "LEFT JOIN (SELECT PROJECTID,MAX(BORROWID) BORROWID FROM TBL_NBSJ_BORROWRECORD WHERE 1 = 1 AND STAFFID = "+staffid+" GROUP BY PROJECTID ) TE ON NP.PROJECTID = TE.PROJECTID "
				+ "LEFT JOIN TBL_NBSJ_BORROWRECORD TNB ON TE.BORROWID =TNB.BORROWID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = NP.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = NP.AUDITORGID "
				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = NP.ORGID "
				+ " where  NP.STATUS="+TblNbsjProject.GD_STATUS+ " and NP.orgId = "+orgid );//
//				+ "(SELECT DISTINCT TNA.*,CRESTAFF.REALNAME REALNAME,ORG.ORGNAME,SJORG.ORGNAME SJORGNAME "
//				+ "FROM TBL_NBSJ_PROJECT TNA "
//				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
//				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = TNA.ORGID "
//				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS);
		
//		if(tblnbsjProjectVo.getPStatus()!=null) {
//			sb.append(" AND pStatus = '"+tblnbsjProjectVo.getPStatus()+"'");
//		}
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND NP.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND NP.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND NP.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND NP.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		
		sb.append(" ORDER BY NP.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectDajyNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,BigDecimal orgid,BigDecimal staffid,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ " from TBL_NBSJ_PROJECT NP"
				+ " where  NP.STATUS="+TblNbsjProject.GD_STATUS+ " and np.orgId = "+orgid );//
		
//				if(tblnbsjProjectVo.getPStatus()!=null) {
//					sb.append(" AND pStatus = '"+tblnbsjProjectVo.getPStatus()+"'");
//				}
				if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
					sb.append(" AND NP.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
				}
				if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
					sb.append(" AND NP.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
				}
				if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
					sb.append(" AND NP.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
				}
				if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
					sb.append(" AND NP.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
				}
		return sb.toString();
	}
	
	//==借阅日志
	public String selectJyrzNewListByPageInfo(PageInfo<TblNbsjProject> pageInfo,Integer orgid,Integer staffid,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				
//				+ "(SELECT NP.PROJECTID,NP.PRJOECTNAME,NP.PLANYEAR,NP.PROJECTSOURCE,NP.STARTDATE,NP.ENDDATE,NP.PMID,NP.TEMPID,NP.COSTS,NP.PURPOSE,NP.SCOPES,NP.PURSUANT,NP.COMMENTS,NP.UMPIREID,NP.CONTROLID,NP.AUDITTYPE,NP.PROJECTCODE,NP.STATUS,NP.CREATETIME,NP.UPDATETIME,NP.CURRENTSTATRE,NP.CREATESTAFFID,NP.ASSIGBEDPMTIME,NP.ASSIGBEDUMPETIME,NP.ASSIGBEDCONTROLTIME,NP.PLANID,NP.AUDITORGID,NP.TEMPZYID,NP.UPDATESTATUS,NP.ORGID,NP.FINISHTIME,NP.IMPLEMENTTIME,NP.EXAMINETYPE,NP.PRO_DESC,NP.PRO_SJFS,NP.AUDITSTAFFID,NP.SJLX,NP.SJZR,NP.YQJCQK,NP.EJFHR,NP.TEMPLETEID,NP.STAFFID,NP.AUDITORG,NP.FPSTATUS,NP.PLANPROJECTID,NP.PPROJECTNAME,NP.TARGETNAME,NP.ORGIDS,NP.ORGIDNAMES,NP.EXTERNALASSIG,NP.FILCODE,NP.FILNAME,NP.PSTATUS,TC.PCOUNT PCNT from TBL_NBSJ_PROJECT NP LEFT JOIN (SELECT PROJECTID,COUNT(PROJECTID) PCOUNT FROM TBL_NBSJ_BORROWRECORD TB WHERE TB.STATUS=4  GROUP BY PROJECTID) TC ON NP.PROJECTID=TC.PROJECTID"
//				+ " where  NP.STATUS="+TblNbsjProject.GD_STATUS
//				+ " and np.orgId = "+orgid);
				
				+ "(SELECT DISTINCT TNA.PROJECTID,TNA.projectcode,TNA.PRJOECTNAME,TNA.AUDITTYPE,TNA.PLANYEAR,TNA.ORGIDNAMES,SJORG.ORGNAME auditOrg,TC.PCOUNT pcount "
				+ " FROM TBL_NBSJ_PROJECT TNA "
				+ "LEFT JOIN (SELECT PROJECTID,COUNT(PROJECTID) PCOUNT FROM TBL_NBSJ_BORROWRECORD TB WHERE 1=1  GROUP BY PROJECTID) TC ON TNA.PROJECTID=TC.PROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = TNA.ORGID "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS
				+ " AND TNA.ORGID= "+orgid);
		
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectJyrzNewCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,Integer orgid,Integer staffid,TblnbsjProjectVo tblnbsjProjectVo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ "LEFT JOIN (SELECT PROJECTID,COUNT(PROJECTID) PCOUNT FROM TBL_NBSJ_BORROWRECORD TB WHERE 1=1  GROUP BY PROJECTID) TC ON TNA.PROJECTID=TC.PROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "LEFT JOIN TBL_ORGANIZATION SJORG ON SJORG.ORGID = TNA.ORGID "
				+ "WHERE TNA.STATUS="+TblNbsjProject.GD_STATUS
				+ " AND TNA.ORGID= "+orgid);
		
		if(tblnbsjProjectVo.getPrjoectName()!=null && tblnbsjProjectVo.getPrjoectName().length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectVo.getProjectCode()!=null && tblnbsjProjectVo.getProjectCode().length()>0) {
			sb.append(" AND TNA.projectcode LIKE '%"+tblnbsjProjectVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectVo.getAuditType()!=null && tblnbsjProjectVo.getAuditType().length()>0) {
			sb.append(" AND TNA.AUDITTYPE LIKE '%"+tblnbsjProjectVo.getAuditType()+"%'");
		}
		if(tblnbsjProjectVo.getProjectYear()!=null && tblnbsjProjectVo.getProjectYear().length()>0) {
			sb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectVo.getProjectYear()+"%'");
		}
		
		return sb.toString();
	}
	
	
//==审计项目管理=====================BEGIN
	public String selectPlanCodeByOrgid(TblNbsjProject plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_PROJECT WHERE 1=1 AND PROJECTCODE='"+plan.getProjectCode()+"' ");

		if(plan.getProjectId() != null) {
			sb.append(" AND PROJECTID != "+plan.getProjectId());
		}
		return sb.toString();
	}
	
	public String selectPJListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project, String sortFields, String sortFlag, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT DISTINCT TNA.*,TS.REALNAME,TAS.REALNAME auditStaffName "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.PROJECTID = T.PROJECTID "
				+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID "
				+ " LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID "
				+ " LEFT JOIN TBL_STAFF TAS ON TNA.AUDITSTAFFID = TAS.STAFFID "
				+ "WHERE  TNA.ORGID = "+project.getOrgId());


//		String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", "TNA.CREATESTAFFID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
//		sqlSb.append(sql);

		if(null==project.getIsZgfa()) {
			sqlSb.append(" AND TNA.STATUS != 4 ");
		}
		
//		if(StrUtil.isNotBlank(project.getRolename())&&("审计部负责人".equals(project.getRolename())||"审计责任人".equals(project.getRolename()))) {
//
//		}else{
//			sqlSb.append(" AND (TNS.STAFFID = "+project.getStaffId()+" OR TNA.CREATESTAFFID="+project.getStaffId()+" ");
//			sqlSb.append(" OR TNA.PMID = "+project.getStaffId());
//			sqlSb.append(")");
//		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND TNA.PMID = "+project.getPmId());
		}
		
		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND TNA.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND TNA.STATUS = "+project.getStatus());
		}
		if(project.getStartDate() !=null && !"".equals(project.getStartDate())){
			sqlSb.append(" AND TNA.STARTDATE >= TO_DATE('"+project.getStartDate()+"', 'yyyy-MM-dd')");
		}
		if(project.getEndDate() !=null && !"".equals(project.getEndDate())){
			sqlSb.append(" AND TNA.ENDDATE <= TO_DATE('"+project.getEndDate()+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		
//		sqlSb.append("  ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
//		//字段排序处理start
//		sqlSb.append(BaseVo.getOrderSqlByFields(sortFields, sortFlag, "PROJECTID"));
//		//字段排序处理end
		return sqlSb.toString();
	}
	
	public String selectPJCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectVo project, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM ( SELECT DISTINCT P.PROJECTID,P.PRJOECTNAME,P.PROJECTCODE,P.PROJECTSOURCE,P.STATUS,P.STARTDATE,P.ENDDATE,P.COSTS,TS.STAFFID,TS.REALNAME,P.EXAMINETYPE FROM TBL_NBSJ_PROJECT P");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_PRO_TEAM T ON P.PROJECTID = T.PROJECTID LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID LEFT JOIN TBL_STAFF TS ON P.PMID = TS.STAFFID WHERE P.STATUS != 4 AND P.ORGID = "+project.getOrgId());

//		String sql = GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "P.ORGID", "P.ORGID", "P.CREATESTAFFID", "P.SECRECTLEVELID", "P.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds());
//		sqlSb.append(sql);

//		if(StrUtil.isNotBlank(project.getRolename())&&("审计部负责人".equals(project.getRolename())||"审计责任人".equals(project.getRolename()))) {
//
//		}else{
//			sqlSb.append(" AND (TNS.STAFFID = "+project.getStaffId()+" OR P.CREATESTAFFID="+project.getStaffId()+" ");
//			sqlSb.append(" OR P.PMID = "+project.getStaffId());
//			sqlSb.append(")");
//		}
		if(project.getPmId() != null ) {
			sqlSb.append(" AND P.PMID = "+project.getPmId());
		}

		if(project.getPrjoectName() != null && !"".equals(project.getPrjoectName())) {
			sqlSb.append(" AND P.PRJOECTNAME LIKE '%"+project.getPrjoectName()+"%'");
		}
		if(project.getProjectSource() != null && !"".equals(project.getProjectSource())) {
			sqlSb.append(" AND P.PROJECTSOURCE LIKE '%"+project.getProjectSource()+"%'");
		}
		if(project.getStatus() != null) {
			sqlSb.append(" AND P.STATUS = "+project.getStatus());
		}
		if(project.getStartDate() !=null && !"".equals(project.getStartDate())){
			sqlSb.append(" AND P.STARTDATE >= TO_DATE('"+project.getStartDate()+"', 'yyyy-MM-dd')");
		}
		if(project.getEndDate() !=null && !"".equals(project.getEndDate())){
			sqlSb.append(" AND P.ENDDATE <= TO_DATE('"+project.getEndDate()+"', 'yyyy-MM-dd')");
		}
		sqlSb.append(")");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}

	/**
	 * 查询审计项目数详细数据 - 统一接口(支持按公司/审计类型/状态/月份查询)
	 * 根据 year, companyName, auditType, status, month 五个参数动态构建查询条件
	 */
	public String selectPlancountDetailList(String username,String year, String companyName, String auditType, String status, String month) throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT DISTINCT TNA.*, TS.REALNAME, TAS.REALNAME auditStaffName ");
		sqlSb.append("FROM TBL_NBSJ_PROJECT TNA ");
		sqlSb.append("LEFT JOIN TBL_STAFF TS ON TNA.PMID = TS.STAFFID ");
		sqlSb.append("LEFT JOIN TBL_STAFF TAS ON TNA.AUDITSTAFFID = TAS.STAFFID ");
		sqlSb.append("WHERE 1=1 ");

		// 添加年份条件
		if(year != null && !"".equals(year)) {
			sqlSb.append("AND TNA.PLANYEAR = '").append(year).append("' ");
		}

		// 添加被审计单位名称条件(按公司查询)
		if(companyName != null && !"".equals(companyName)) {
//			sqlSb.append("AND TNA.ORGIDNAMES LIKE '%").append(companyName).append("%' ");
			sqlSb.append("AND TNA.ORGIDNAMES = '").append(companyName).append("' ");
		}

		// 添加审计类型条件(按类型查询)
		if(auditType != null && !"".equals(auditType)) {
			sqlSb.append("AND TNA.AUDITTYPE = '").append(auditType).append("' ");
		}

		// 添加完成状态条件(按状态查询)
		// STATUS字段存储的是数字: 0-未启动, 1-启动, 2-实施, 3-完成, 4-归档
		if(status != null && !"".equals(status)) {
			// 支持status参数传递逗号分隔的多个状态值,例如: "0,1,2" 或 "3,4"
			if(status.contains(",")) {
				// 处理逗号分隔的状态值
				String[] statusArray = status.split(",");
				sqlSb.append("AND TNA.STATUS IN (");
				for(int i = 0; i < statusArray.length; i++) {
					if(i > 0) sqlSb.append(",");
					sqlSb.append("'").append(statusArray[i].trim()).append("'");
				}
				sqlSb.append(") ");
			} else {
				// 单个状态值,支持中文状态名称映射
				// 未完成 -> status != 3 和 status != 4
				if("未完成".equals(status)) {
					sqlSb.append("AND (TNA.STATUS != '3' AND TNA.STATUS != '4') ");
				}
				// 已完成/完成 -> status = 3 或 4
				else if("已完成".equals(status) || "完成".equals(status)) {
					sqlSb.append("AND TNA.STATUS IN ('3', '4') ");
				}
				// 未启动 -> 0
				else if("未启动".equals(status)) {
					sqlSb.append("AND TNA.STATUS = '0' ");
				}
				// 已启动/启动 -> 1
				else if("已启动".equals(status) || "启动".equals(status)) {
					sqlSb.append("AND TNA.STATUS = '1' ");
				}
				// 实施中/实施 -> 2
				else if("实施中".equals(status) || "实施".equals(status)) {
					sqlSb.append("AND TNA.STATUS = '2' ");
				}
				// 归档 -> 4
				else if("归档".equals(status)) {
					sqlSb.append("AND TNA.STATUS = '4' ");
				}
				else {
					// 如果传来的就是数字字符串,直接使用
					sqlSb.append("AND TNA.STATUS = '").append(status).append("' ");
				}
			}
		}

		// 添加月份条件(按月查询)
		if(month != null && !"".equals(month)) {
			// month参数格式为 "1月", "2月" 等,需要提取数字
			// STARTDATE是Date类型,使用TO_CHAR转换为字符串再提取月份
//			String monthNum = month.replaceAll("[^0-9]", ""); // 提取数字
			String monthNum = month.replaceAll("月", ""); // 提取数字
			if(!"".equals(monthNum)) {
				// 对单个月份补充前导零,例如: "1" -> "01"
				if(monthNum.length() == 1) {
					monthNum = "0" + monthNum;
				}
				sqlSb.append("AND TO_CHAR(TNA.CREATETIME, 'MM') = '").append(monthNum).append("' ");
			}
		}

		if(username != null && !"".equals(username)) {
			sqlSb.append(" AND (EXISTS (SELECT 1 FROM TBL_SECRECT_LEVEL WHERE LEVELID = (SELECT SECRECTLEVELID FROM TBL_STAFF WHERE " +
					" USERNAME = '").append(username).append("'  ) AND ','||SECRECYMENUSCOPE||',' LIKE '%,'||TNA.SECRECTLEVELID||',%') " +
					" OR TNA.SECRECTLEVELID IS NULL) ");
		}

		sqlSb.append("ORDER BY TNA.PROJECTID DESC");

		return sqlSb.toString();
	}
	
	public String updateEntity(TblNbsjProject plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECT SET PROJECTCODE = '"+plan.getProjectCode()+"' ");
		
		if(plan.getPlanId() != null && !"".equals(plan.getPlanId())) {
			sqlSb.append(" ,PLANID = '"+plan.getPlanId()+"'");
		}
		if(plan.getPlanProjectId() != null && !"".equals(plan.getPlanProjectId())) {
			sqlSb.append(" ,PLANPROJECTID = '"+plan.getPlanProjectId()+"'");
		}
		if(plan.getPprojectName() != null && !"".equals(plan.getPprojectName())) {
			sqlSb.append(" ,PPROJECTNAME = '"+plan.getPprojectName()+"'");
		}
		if(plan.getPrjoectName() != null && !"".equals(plan.getPrjoectName())) {
			sqlSb.append(" ,PRJOECTNAME = '"+plan.getPrjoectName()+"'");
		}
		if(plan.getTargetName() != null && !"".equals(plan.getTargetName())) {
			sqlSb.append(" ,TARGETNAME = '"+plan.getTargetName()+"'");
		}
		if(plan.getAuditOrgId() != null && !"".equals(plan.getAuditOrgId())) {
			sqlSb.append(" ,AUDITORGID = '"+plan.getAuditOrgId()+"'");
			sqlSb.append(" ,AUDITSTAFFID = ''");
		}
		if(plan.getAuditStaffId() != null && !"".equals(plan.getAuditStaffId())) {
			sqlSb.append(" ,AUDITSTAFFID = '"+plan.getAuditStaffId()+"'");
			sqlSb.append(" ,AUDITORGID = ''");
		}
		if(plan.getPlanYear() != null && !"".equals(plan.getPlanYear())) {
			sqlSb.append(" ,PLANYEAR = '"+plan.getPlanYear()+"'");
		}
		if(plan.getAuditType() != null && !"".equals(plan.getAuditType())) {
			sqlSb.append(" ,AUDITTYPE = '"+plan.getAuditType()+"'");
		}
		if(plan.getProjectSource() != null && !"".equals(plan.getProjectSource())) {
			sqlSb.append(" ,PROJECTSOURCE = '"+plan.getProjectSource()+"'");
		}
		if(plan.getPmId() != null) {
			sqlSb.append(" ,PMID = '"+plan.getPmId()+"'");
		}
		if(plan.getPlanStartDate() != null && !"".equals(plan.getPlanStartDate())) {
			sqlSb.append(" ,STARTDATE =TO_DATE('"+plan.getPlanStartDate()+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getPlanEndDate() != null && !"".equals(plan.getPlanEndDate())) {
			sqlSb.append(" ,ENDDATE = TO_DATE('"+plan.getPlanEndDate()+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
			sqlSb.append(" ,TEMPID = '"+plan.getTempId()+"'");
		}
		if(plan.getCosts() != null && !"".equals(plan.getCosts())) {
			sqlSb.append(" ,COSTS = '"+plan.getCosts()+"'");
		}
		if(plan.getTempzyId() != null && !"".equals(plan.getTempzyId())) {
			sqlSb.append(" ,TEMPZYID = '"+plan.getTempzyId()+"'");
		}
		if(plan.getProSjfs() != null && !"".equals(plan.getProSjfs())) {
			sqlSb.append(" ,PRO_SJFS = '"+plan.getProSjfs()+"'");
		}
		if(plan.getExternAlassig() != null && !"".equals(plan.getExternAlassig())) {
			sqlSb.append(" ,EXTERNALASSIG = '"+plan.getExternAlassig()+"'");
		}
		if(plan.getPurpose() != null && !"".equals(plan.getPurpose())) {
			sqlSb.append(" ,PURPOSE = '"+plan.getPurpose()+"'");
		}
		if(plan.getScopes() != null && !"".equals(plan.getScopes())) {
			sqlSb.append(" ,SCOPES = '"+plan.getScopes()+"'");
		}
		if(plan.getPursuant() != null && !"".equals(plan.getPursuant())) {
			sqlSb.append(" ,PURSUANT = '"+plan.getPursuant()+"'");
		}
		if(plan.getComments() != null && !"".equals(plan.getComments())) {
			sqlSb.append(" ,COMMENTS = '"+plan.getComments()+"'");
		}
		if(plan.getProDesc() != null && !"".equals(plan.getProDesc())) {
			sqlSb.append(" ,PRO_DESC = '"+plan.getProDesc()+"'");
		}
		if(plan.getExamineType() != null ) {
			sqlSb.append(" ,EXAMINETYPE = "+plan.getExamineType() );
		}
		
		if(plan.getUpdateStatus() != null ) {
			sqlSb.append(" ,UPDATESTATUS = "+plan.getUpdateStatus() );
		}
		
		if(plan.getStatus()!=null){
			sqlSb.append(" ,Status = "+plan.getStatus() );

		}
		
		if(plan.getOrgIds() != null && !"".equals(plan.getOrgIds())) {
			sqlSb.append(" ,ORGIDS = '"+plan.getOrgIds()+"'");
		}
		if(plan.getOrgIdNames() != null && !"".equals(plan.getOrgIdNames())) {
			sqlSb.append(" ,ORGIDNAMES = '"+plan.getOrgIdNames()+"'");
		}
		
		//浙资新增字段
		if(plan.getImplementaion() != null && !"".equals(plan.getImplementaion())) {
			sqlSb.append(",IMPLEMENTAION='"+plan.getImplementaion()+"'");
		}
		if(plan.getCospomsordepartment() != null ) {
			sqlSb.append(",COSPOMSORDEPARTMENT="+plan.getCospomsordepartment());
		}
		if(plan.getImplementaionsteps() != null && !"".equals(plan.getImplementaionsteps())) {
			sqlSb.append(",IMPLEMENTAIONSTEPS='"+plan.getImplementaionsteps()+"'");
		}
		if(plan.getAuditrequirements() != null && !"".equals(plan.getAuditrequirements())) {
			sqlSb.append(",AUDITREQUIREMENTS='"+plan.getAuditrequirements()+"'");
		}
		if(plan.getCntType() != null && !"".equals(plan.getCntType())) {
			sqlSb.append(",CNTTYPE='"+plan.getCntType()+"'");
		}
		
		if(plan.getProjecttype() != null && !"".equals(plan.getProjecttype())) {
			sqlSb.append(",PROJECTTYPE='"+plan.getProjecttype()+"'");
		}
		if(plan.getCospomsordepartmentstaffid() != null ) {
			sqlSb.append(",COSPOMSORDEPARTMENTSTAFFID="+plan.getCospomsordepartmentstaffid());
		}
		
		if(plan.getSjap() != null && !"".equals(plan.getSjap())) {
			sqlSb.append(",SJAP='"+plan.getSjap()+"'");
		}
		if(plan.getXmgs() != null && !"".equals(plan.getXmgs())) {
			sqlSb.append(",XMGS='"+plan.getXmgs()+"'");
		}
		
		if(plan.getProjectorgid() != null && !"".equals(plan.getProjectorgid())) {
			sqlSb.append(" ,PROJECTORGID = '"+plan.getProjectorgid()+"'");
		}
		if(plan.getProjectorgname() != null && !"".equals(plan.getProjectorgname())) {
			sqlSb.append(" ,PROJECTORGNAME = '"+plan.getProjectorgname()+"'");
		}
		if(plan.getAppproyearstart() != null && !"".equals(plan.getAppproyearstart())) {
			sqlSb.append(" ,APPPROYEARSTART = '"+plan.getAppproyearstart()+"'");
		}
		if(plan.getAppproyearend() != null && !"".equals(plan.getAppproyearend())) {
			sqlSb.append(" ,APPPROYEAREND = '"+plan.getAppproyearend()+"'");
		}
		if(plan.getActproyearstart() != null && !"".equals(plan.getActproyearstart())) {
			sqlSb.append(" ,ACTPROYEARSTART = '"+plan.getActproyearstart()+"'");
		}
		if(plan.getActproyearend() != null && !"".equals(plan.getActproyearend())) {
			sqlSb.append(" ,ACTPROYEAREND = '"+plan.getActproyearend()+"'");
		}
		if(plan.getProjectmgdeptid() != null && !"".equals(plan.getProjectmgdeptid())) {
			sqlSb.append(" ,PROJECTMGDEPTID = '"+plan.getProjectmgdeptid()+"'");
		}
		if(plan.getProjectmgdeptname() != null && !"".equals(plan.getProjectmgdeptname())) {
			sqlSb.append(" ,PROJECTMGDEPTNAME = '"+plan.getProjectmgdeptname()+"'");
		}
		if(plan.getProjectorgaddress() != null && !"".equals(plan.getProjectorgaddress())) {
			sqlSb.append(" ,PROJECTORGADDRESS = '"+plan.getProjectorgaddress()+"'");
		}
		if(plan.getProjectlinkman() != null && !"".equals(plan.getProjectlinkman())) {
			sqlSb.append(" ,PROJECTLINKMAN = '"+plan.getProjectlinkman()+"'");
		}
		if(plan.getProjectlinktel() != null && !"".equals(plan.getProjectlinktel())) {
			sqlSb.append(" ,PROJECTLINKTEL = '"+plan.getProjectlinktel()+"'");
		}
		if(plan.getSecretlevel() != null && !"".equals(plan.getSecretlevel())) {
			sqlSb.append(" ,SECRETLEVEL = '"+plan.getSecretlevel()+"'");
		}
		if(plan.getSecrectLevelId() != null ) {
			sqlSb.append(",SECRECTLEVELID = '"+plan.getSecrectLevelId() +"'");
		}
		if(plan.getStaffScopeIds()!= null &&!"".equals(plan.getStaffScopeIds())) {
			sqlSb.append(",STAFFSCOPEIDS = '"+plan.getStaffScopeIds()+"'");
		}
		if(plan.getStaffScopeNames() != null && !"".equals(plan.getStaffScopeNames())){
			sqlSb.append(",STAFFSCOPENAMES = '"+plan.getStaffScopeNames()+"'");
		}
		
		sqlSb.append(" WHERE PROJECTID= "+plan.getProjectId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjProject plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_PROJECT(PROJECTID,CREATETIME,STATUS,CREATESTAFFID,EXAMINETYPE");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0,"+plan.getCreateStaffId()+",0");
		
		if(plan.getPlanId() != null && !"".equals(plan.getPlanId())) {
			colSb.append(",PLANID");
			valSb.append(",'"+plan.getPlanId()+"'");
		}
		if(plan.getPlanProjectId() != null && !"".equals(plan.getPlanProjectId())) {
			colSb.append(",PLANPROJECTID");
			valSb.append(",'"+plan.getPlanProjectId()+"'");
		}
		if(plan.getPprojectName() != null && !"".equals(plan.getPprojectName())) {
			colSb.append(",PPROJECTNAME");
			valSb.append(",'"+plan.getPprojectName()+"'");
		}
		if(plan.getProjectCode() != null && !"".equals(plan.getProjectCode())) {
			colSb.append(",PROJECTCODE");
			valSb.append(",'"+plan.getProjectCode()+"'");
		}
		if(plan.getPrjoectName() != null && !"".equals(plan.getPrjoectName())) {
			colSb.append(",PRJOECTNAME");
			valSb.append(",'"+plan.getPrjoectName()+"'");
		}
		if(plan.getTargetName() != null && !"".equals(plan.getTargetName())) {
			colSb.append(",TARGETNAME");
			valSb.append(",'"+plan.getTargetName()+"'");
		}
		if(plan.getAuditOrgId() != null && !"".equals(plan.getAuditOrgId())) {
			colSb.append(",AUDITORGID");
			valSb.append(",'"+plan.getAuditOrgId()+"'");
		}
		if(plan.getAuditStaffId() != null && !"".equals(plan.getAuditStaffId())) {
			colSb.append(",AUDITSTAFFID");
			valSb.append(",'"+plan.getAuditStaffId()+"'");
		}
		if(plan.getPlanYear() != null && !"".equals(plan.getPlanYear())) {
			colSb.append(",PLANYEAR");
			valSb.append(",'"+plan.getPlanYear()+"'");
		}
		if(plan.getAuditType() != null && !"".equals(plan.getAuditType())) {
			colSb.append(",AUDITTYPE");
			valSb.append(",'"+plan.getAuditType()+"'");
		}
		if(plan.getProjectSource() != null && !"".equals(plan.getProjectSource())) {
			colSb.append(",PROJECTSOURCE");
			valSb.append(",'"+plan.getProjectSource()+"'");
		}
		if(plan.getPmId() != null && !"".equals(plan.getPmId())) {
			colSb.append(",PMID");
			valSb.append(",'"+plan.getPmId()+"'");
		}
		if(plan.getPlanStartDate() != null && !"".equals(plan.getPlanStartDate())) {
			colSb.append(",STARTDATE");
			valSb.append(",TO_DATE('"+plan.getPlanStartDate()+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getPlanEndDate() != null && !"".equals(plan.getPlanEndDate())) {
			colSb.append(",ENDDATE");
			valSb.append(",TO_DATE('"+plan.getPlanEndDate()+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
			colSb.append(",TEMPID");
			valSb.append(",'"+plan.getTempId()+"'");
		}
		if(plan.getCosts() != null && !"".equals(plan.getCosts())) {
			colSb.append(",COSTS");
			valSb.append(",'"+plan.getCosts()+"'");
		}
		if(plan.getTempzyId() != null && !"".equals(plan.getTempzyId())) {
			colSb.append(",TEMPZYID");
			valSb.append(",'"+plan.getTempzyId()+"'");
		}
		if(plan.getProSjfs() != null && !"".equals(plan.getProSjfs())) {
			colSb.append(",PRO_SJFS");
			valSb.append(",'"+plan.getProSjfs()+"'");
		}
		if(plan.getExternAlassig() != null && !"".equals(plan.getExternAlassig())) {
			colSb.append(",EXTERNALASSIG");
			valSb.append(",'"+plan.getExternAlassig()+"'");
		}
		if(plan.getPurpose() != null && !"".equals(plan.getPurpose())) {
			colSb.append(",PURPOSE");
			valSb.append(",'"+plan.getPurpose()+"'");
		}
		if(plan.getScopes() != null && !"".equals(plan.getScopes())) {
			colSb.append(",SCOPES");
			valSb.append(",'"+plan.getScopes()+"'");
		}
		if(plan.getPursuant() != null && !"".equals(plan.getPursuant())) {
			colSb.append(",PURSUANT");
			valSb.append(",'"+plan.getPursuant()+"'");
		}
		if(plan.getComments() != null && !"".equals(plan.getComments())) {
			colSb.append(",COMMENTS");
			valSb.append(",'"+plan.getComments()+"'");
		}
		if(plan.getProDesc() != null && !"".equals(plan.getProDesc())) {
			colSb.append(",PRO_DESC");
			valSb.append(",'"+plan.getProDesc()+"'");
		}
		if(plan.getOrgId() != null && !"".equals(plan.getOrgId())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgId()+"'");
		}
		
		if(plan.getOrgIds() != null && !"".equals(plan.getOrgIds())) {
			colSb.append(",ORGIDS");
			valSb.append(",'"+plan.getOrgIds()+"'");
		}
		if(plan.getOrgIdNames() != null && !"".equals(plan.getOrgIdNames())) {
			colSb.append(",ORGIDNAMES");
			valSb.append(",'"+plan.getOrgIdNames()+"'");
		}
		if(plan.getUpdateStatus() != null && !"".equals(plan.getUpdateStatus())) {
			colSb.append(",UPDATESTATUS");
			valSb.append(",'"+plan.getUpdateStatus()+"'");
		}
		//浙资新增字段
		if(plan.getImplementaion() != null && !"".equals(plan.getImplementaion())) {
			colSb.append(",IMPLEMENTAION");
			valSb.append(",'"+plan.getImplementaion()+"'");
		}
		if(plan.getCospomsordepartment() != null ) {
			colSb.append(",COSPOMSORDEPARTMENT");
			valSb.append(","+plan.getCospomsordepartment());
		}
		if(plan.getImplementaionsteps() != null && !"".equals(plan.getImplementaionsteps())) {
			colSb.append(",IMPLEMENTAIONSTEPS");
			valSb.append(",'"+plan.getImplementaionsteps()+"'");
		}
		if(plan.getAuditrequirements() != null && !"".equals(plan.getAuditrequirements())) {
			colSb.append(",AUDITREQUIREMENTS");
			valSb.append(",'"+plan.getAuditrequirements()+"'");
		}
		if(plan.getCntType() != null && !"".equals(plan.getCntType())) {
			colSb.append(",CNTTYPE");
			valSb.append(",'"+plan.getCntType()+"'");
		}
		
		if(plan.getProjecttype() != null && !"".equals(plan.getProjecttype())) {
			colSb.append(",PROJECTTYPE");
			valSb.append(",'"+plan.getProjecttype()+"'");
		}
		if(plan.getCospomsordepartmentstaffid() != null ) {
			colSb.append(",COSPOMSORDEPARTMENTSTAFFID");
			valSb.append(","+plan.getCospomsordepartmentstaffid());
		}
		if(plan.getSjap() != null && !"".equals(plan.getSjap())) {
			colSb.append(",SJAP");
			valSb.append(",'"+plan.getSjap()+"'");
		}
		if(plan.getXmgs() != null && !"".equals(plan.getXmgs())) {
			colSb.append(",XMGS");
			valSb.append(",'"+plan.getXmgs()+"'");
		}
		
		if(plan.getProjectorgid() != null && !"".equals(plan.getProjectorgid())) {
			colSb.append(",PROJECTORGID");
			valSb.append(",'"+plan.getProjectorgid()+"'");
		}
		if(plan.getProjectorgname() != null && !"".equals(plan.getProjectorgname())) {
			colSb.append(",PROJECTORGNAME");
			valSb.append(",'"+plan.getProjectorgname()+"'");
		}
		//批复的起止年限
		if(plan.getAppproyearstart() != null && !"".equals(plan.getAppproyearstart())) {
			colSb.append(",APPPROYEARSTART");
			valSb.append(",'"+plan.getAppproyearstart()+"'");
		}
		if(plan.getAppproyearend() != null && !"".equals(plan.getAppproyearend())) {
			colSb.append(",APPPROYEAREND");
			valSb.append(",'"+plan.getAppproyearend()+"'");
		}
		//实际的起止年限
		if(plan.getActproyearstart() != null && !"".equals(plan.getActproyearstart())) {
			colSb.append(",ACTPROYEARSTART");
			valSb.append(",'"+plan.getActproyearstart()+"'");
		}
		if(plan.getActproyearend() != null && !"".equals(plan.getActproyearend())) {
			colSb.append(",ACTPROYEAREND");
			valSb.append(",'"+plan.getActproyearend()+"'");
		}
		if(plan.getProjectmgdeptid() != null && !"".equals(plan.getProjectmgdeptid())) {
			colSb.append(",PROJECTMGDEPTID");
			valSb.append(",'"+plan.getProjectmgdeptid()+"'");
		}
		if(plan.getProjectmgdeptname() != null && !"".equals(plan.getProjectmgdeptname())) {
			colSb.append(",PROJECTMGDEPTNAME");
			valSb.append(",'"+plan.getProjectmgdeptname()+"'");
		}
		if(plan.getProjectorgaddress() != null && !"".equals(plan.getProjectorgaddress())) {
			colSb.append(",PROJECTORGADDRESS");
			valSb.append(",'"+plan.getProjectorgaddress()+"'");
		}
		if(plan.getProjectlinkman() != null && !"".equals(plan.getProjectlinkman())) {
			colSb.append(",PROJECTLINKMAN");
			valSb.append(",'"+plan.getProjectlinkman()+"'");
		}
		if(plan.getProjectlinktel() != null && !"".equals(plan.getProjectlinktel())) {
			colSb.append(",PROJECTLINKTEL");
			valSb.append(",'"+plan.getProjectlinktel()+"'");
		}
		if(plan.getSecretlevel() != null && !"".equals(plan.getSecretlevel())) {
			colSb.append(",SECRETLEVEL");
			valSb.append(",'"+plan.getSecretlevel()+"'");
		}
		if(plan.getSecrectLevelId() != null && !"".equals(plan.getSecrectLevelId())){
			colSb.append(",SECRECTLEVELID");
			valSb.append(",'"+plan.getSecrectLevelId()+"'");
		}
		if(plan.getStaffScopeIds() != null && !"".equals(plan.getStaffScopeIds())){
			colSb.append(",STAFFSCOPEIDS");
			valSb.append(",'"+plan.getStaffScopeIds()+"'");
		}
		if(plan.getStaffScopeNames() != null && !"".equals(plan.getStaffScopeNames())) {
			colSb.append(",STAFFSCOPENAMES");
			valSb.append(",'"+plan.getStaffScopeNames()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
//==审计项目管理=====================END
	
	
//==项目任务分配===========================BEGIN
	public String selectPJRwfpListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo, String sortFields, String sortFlag,TblStaffUtil loginStaff)throws Exception  {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TNA.AUDITORGNAME AS ORGNAME "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ " WHERE TNA.STATUS != 4 "
				+ " AND TNA.examineType=6 ");
		
		//安全保密SQL
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", 
				"TNA.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(tblnbsjProjectRwfpVo.getPmId() != null ) {
			sqlSb.append(" AND TNA.PMID = "+tblnbsjProjectRwfpVo.getPmId());
		}
		
		if(tblnbsjProjectRwfpVo.getPrjoectName() != null && !"".equals(tblnbsjProjectRwfpVo.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectRwfpVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectRwfpVo.getAuditedObject() != null && !"".equals(tblnbsjProjectRwfpVo.getAuditedObject())) {
			sqlSb.append(" AND TNA.ORGIDNAMES LIKE '%"+tblnbsjProjectRwfpVo.getAuditedObject()+"%'");
		}
		//字段排序处理start
		sqlSb.append(BaseVo.getOrderSqlByFields(sortFields, sortFlag, "PROJECTID"));
//字段排序处理end
		sqlSb.append(" ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectPJRwfpCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectRwfpVo tblnbsjProjectRwfpVo,TblStaffUtil loginStaff)throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM ( SELECT TNA.* FROM TBL_NBSJ_PROJECT TNA ");
		sqlSb.append(" WHERE TNA.STATUS != 4 "
				+ " AND TNA.examineType=6 ");
		
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", 
				"TNA.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(tblnbsjProjectRwfpVo.getPmId() != null ) {
			sqlSb.append(" AND TNA.PMID = "+tblnbsjProjectRwfpVo.getPmId());
		}
		
		if(tblnbsjProjectRwfpVo.getPrjoectName() != null && !"".equals(tblnbsjProjectRwfpVo.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectRwfpVo.getPrjoectName()+"%'");
		}
		
		if(tblnbsjProjectRwfpVo.getAuditedObject() != null && !"".equals(tblnbsjProjectRwfpVo.getAuditedObject())) {
			sqlSb.append(" AND TNA.ORGIDNAMES LIKE '%"+tblnbsjProjectRwfpVo.getAuditedObject()+"%'");
		}
		
		sqlSb.append(")");
		return sqlSb.toString();
	}
//==项目任务分配===========================END
	
//==项目执行一览===========================BEGIN
	public String selectPJZxylListByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TA.PLANNAME,TA.PLANCODE "
				+ "FROM TBL_NBSJ_PROJECT TNA "
				+ " LEFT JOIN TBL_NBSJ_AUDITPLAN TA on TNA.PLANID = TA.PLANID "
				+ " WHERE TNA.ORGID = "+tblnbsjProjectZXYLVo.getOrgId());
		
		if(tblnbsjProjectZXYLVo.getPrjoectName() != null && !"".equals(tblnbsjProjectZXYLVo.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectZXYLVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getProjectCode() != null && !"".equals(tblnbsjProjectZXYLVo.getProjectCode())) {
			sqlSb.append(" AND TNA.PROJECTCODE LIKE '%"+tblnbsjProjectZXYLVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanYear() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanYear())) {
			sqlSb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectZXYLVo.getPlanYear()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanName() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanName())) {
			sqlSb.append(" AND TA.PLANNAME LIKE '%"+tblnbsjProjectZXYLVo.getPlanName()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanNumber() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanNumber())) {
			sqlSb.append(" AND TA.PLANCODE LIKE '%"+tblnbsjProjectZXYLVo.getPlanNumber()+"%'");
		}
		
		sqlSb.append(" ORDER BY TNA.PROJECTID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectPJZxylCountByPageInfo(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, TblnbsjProjectZXYLVo tblnbsjProjectZXYLVo) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM ( SELECT TNA.* FROM TBL_NBSJ_PROJECT TNA ");
		sqlSb.append(" LEFT JOIN TBL_NBSJ_AUDITPLAN TA on TNA.PLANID = TA.PLANID "
				+ " WHERE TNA.ORGID = "+tblnbsjProjectZXYLVo.getOrgId());
		
		if(tblnbsjProjectZXYLVo.getPrjoectName() != null && !"".equals(tblnbsjProjectZXYLVo.getPrjoectName())) {
			sqlSb.append(" AND TNA.PRJOECTNAME LIKE '%"+tblnbsjProjectZXYLVo.getPrjoectName()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getProjectCode() != null && !"".equals(tblnbsjProjectZXYLVo.getProjectCode())) {
			sqlSb.append(" AND TNA.PROJECTCODE LIKE '%"+tblnbsjProjectZXYLVo.getProjectCode()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanYear() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanYear())) {
			sqlSb.append(" AND TNA.PLANYEAR LIKE '%"+tblnbsjProjectZXYLVo.getPlanYear()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanName() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanName())) {
			sqlSb.append(" AND TA.PLANNAME LIKE '%"+tblnbsjProjectZXYLVo.getPlanName()+"%'");
		}
		if(tblnbsjProjectZXYLVo.getPlanNumber() != null && !"".equals(tblnbsjProjectZXYLVo.getPlanNumber())) {
			sqlSb.append(" AND TA.PLANCODE LIKE '%"+tblnbsjProjectZXYLVo.getPlanNumber()+"%'");
		}
		sqlSb.append(")");
		return sqlSb.toString();
	}
//==项目执行一览===========================END
	
	
	
	
	//项目实施
	public String updatePjSS(Integer projectid) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECT SET STATUS = 2 ");
		
		sqlSb.append(" WHERE PROJECTID= "+projectid);
		return sqlSb.toString();
	}
	
	//==修改项目经理
	public String updatePjPm(BigDecimal pmId,BigDecimal projectid) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECT SET PMID = "+pmId);
		
		sqlSb.append(" WHERE PROJECTID= "+projectid);
		return sqlSb.toString();
	}
	
	//==
	public String updatePjStart(BigDecimal projectid) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECT SET STATUS = 1,FPSTATUS=3 ");
		
		sqlSb.append(" WHERE PROJECTID= "+projectid);
		return sqlSb.toString();
	}
	
	
	
	//==
	public String updateFpStatus(Integer fpStatus,BigDecimal projectId) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECT SET FPSTATUS='"+fpStatus+"' ");
		
		sqlSb.append(" WHERE PROJECTID= "+projectId);
		return sqlSb.toString();
	}
	
	
	
	
	
	
	
	//==
	public String selectAuditPlanListByPageInfo(PageInfo<TblNbsjProject> pageInfo,String  projectname,BigDecimal orgid) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,CRESTAFF.REALNAME REALNAME,ORG.ORGNAME auditOrgName,TS.REALNAME auditStaffName "
				+ "FROM TBL_NBSJ_PROJECT TNA "
//				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_STAFF TS ON TS.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 "
				+ " AND TNA.STATUS="+TblNbsjProject.GD_STATUS
				+ " AND TNA.orgId="+orgid);
		
		if(projectname!=null && projectname.length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+projectname+"%'");
		}
		
		sb.append(" ORDER BY TNA.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	public String selectAuditPlanCountByPageInfo(PageInfo<TblNbsjProject> pageInfo,String  projectname,BigDecimal orgid) {
//		TblNbsjProject plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROJECT TNA "
//				+ "left join TBL_NBSJ_AUDIT_PROJECDATA dd on TNA.PROJECTID = dd.OLDPROJECTID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.PMID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.AUDITORGID "
				+ "WHERE 1=1 "
				+ " AND TNA.STATUS="+TblNbsjProject.GD_STATUS
				+ " AND TNA.orgId="+orgid);
		
		if(projectname!=null && projectname.length()>0) {
			sb.append(" AND TNA.PRJOECTNAME LIKE '%"+projectname+"%'");
		}
		return sb.toString();
	}
	public String getGkProjectInfoList(com.hbfk.util.PageInfo<Map<String, Object>> pageInfo,TblGkProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT DISTINCT TP.PROJECTID, TP.PROJECTCODE, TP.PRJOECTNAME,TP.STATUS, TA.ORGNAME AUDITORGNAME," +
				" TP.ORGIDNAMES ORGNAME, TC.REALNAME, TD.REALNAME PMNAME, TP.PLANYEAR, TP.STARTDATE, TP.ENDDATE," +
				" (TP.ENDDATE-TP.STARTDATE) DAYNUMBER, TP.COSTS " +
				"FROM TBL_NBSJ_PROJECT TP " +
				"LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID " +
				"LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID " +
				"LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID " +
				"LEFT JOIN TBL_STAFF TD ON TP.PMID = TD.STAFFID " +
				"WHERE 1 = 1 AND TP.ORGID IN " +
				"( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 START WITH ORGID = "+project.getORGID()+" " +
				"CONNECT BY PRIOR ORGID = FATHERORGID)");
		if(StrUtil.isNotBlank(project.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+project.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+project.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+project.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+project.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+project.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPMNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+project.getPMNAME()+"%'");
		}
		if(project.getStarttime() != null) {
			sqlSb.append(" AND TP.STARTDATE >=TO_DATE('"+DateUtil.parseDate(project.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(project.getEndtime() != null) {
			sqlSb.append(" AND TP.ENDDATE  <= TO_DATE('"+DateUtil.parseDate(project.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String getGkProjectInfoListCount(TblGkProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("");
		sqlSb.append("SELECT count(0) FROM TBL_NBSJ_PROJECT TP LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID LEFT JOIN TBL_STAFF TD ON TP.PMID = TD.STAFFID WHERE 1 = 1 AND TP.ORGID IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 START WITH ORGID = 116821 CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(project.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+project.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+project.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+project.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+project.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+project.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPMNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+project.getPMNAME()+"%'");
		}
		if(project.getStarttime() != null) {
			sqlSb.append(" AND TP.STARTDATE >=TO_DATE('"+DateUtil.parseDate(project.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(project.getEndtime() != null) {
			sqlSb.append(" AND TP.ENDDATE  <= TO_DATE('"+DateUtil.parseDate(project.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC");
		return sqlSb.toString();
	}
	public String getGkQuestionInfoList(com.hbfk.util.PageInfo<Map<String, Object>>  pageInfo,TblGkQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT QU.QUESTIONID,TP.PROJECTCODE,TP.PROJECTID, TP.PRJOECTNAME, TP.PLANYEAR, TP.AUDITTYPE, SH.QUESTITLE," +
				"TO_CHAR(SH.AUDITDISCOVERABLE) AUDITDISCOVERABLE, TA.ORGNAME AUDITORGNAME, SH.ORGIDNAMES ORGNAME, TC.REALNAME," +
				" TD.REALNAME FINDREALNAME " +
				"FROM TBL_NBSJ_QUESTION qu " +
				"LEFT JOIN TBL_NBSJ_SHEET SH ON qu.sheetid = SH.sheetid " +
				"LEFT JOIN TBL_NBSJ_PROJECT TP ON TP.PROJECTID = SH.PROJECTID " +
				"LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID " +
				"LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID " +
				"LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID " +
				"LEFT JOIN TBL_STAFF TD ON SH.CREATESTAFF = TD.STAFFID " +
				"WHERE tp.orgid  IN " +
				"( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" " +
				"CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+question.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+question.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getFINDREALNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+question.getFINDREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getQUESTITLE())){
			sqlSb.append(" AND SH.QUESTITLE LIKE'%"+question.getQUESTITLE()+"%'");
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String getGkQuestionInfoListCount(TblGkQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer("");
		sqlSb.append("SELECT count(0) FROM TBL_NBSJ_QUESTION qu LEFT JOIN TBL_NBSJ_SHEET SH ON qu.sheetid = SH.sheetid LEFT JOIN TBL_NBSJ_PROJECT TP ON TP.PROJECTID = SH.PROJECTID LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID LEFT JOIN TBL_STAFF TD ON SH.CREATESTAFF = TD.STAFFID WHERE tp.orgid IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+question.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+question.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getFINDREALNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+question.getFINDREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getQUESTITLE())){
			sqlSb.append(" AND SH.QUESTITLE LIKE'%"+question.getQUESTITLE()+"%'");
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String getGkZgContentInfoList(com.hbfk.util.PageInfo<Map<String, Object>>  pageInfo,TblGkZgQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT DISTINCT TP.PROJECTID, TP.PROJECTCODE, TP.PRJOECTNAME, TA.ORGNAME AUDITORGNAME, NVL(TP.ORGIDNAMES, TE.REALNAME) ORGNAME, " +
				"TP.PLANYEAR, ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET TS WHERE TS.PROJECTID = TP.PROJECTID AND TS.RISKLEVEL='是' ) wtzs," +
				" ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET TS LEFT JOIN TBL_NBSJ_QUESTION nq ON TS.SHEETID = nq.SHEETID " +
				"WHERE TS.PROJECTID = TP.PROJECTID AND NQ.RECSTATUS = 1 AND TS.RISKLEVEL='是' ) fqzgs," +
				" ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID " +
				" WHERE SH.PROJECTID = TP.PROJECTID AND SH.RISKLEVEL='是'  AND NQ.QUESTIONID in " +
				"(SELECT DISTINCT TC.QUESTIONID from TBL_NBSJ_REFOPM TC  WHERE  TC.LASTREFORMSTATUS = 1 AND TC.STATUS = 3 ) ) yzg, " +
				"( SELECT COUNT (*) FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID " +
				" WHERE SH.PROJECTID = TP.PROJECTID AND SH.RISKLEVEL='是' " +
				" AND NQ.QUESTIONID in (SELECT DISTINCT TC.QUESTIONID " +
				"from TBL_NBSJ_REFOPM TC  WHERE   TC.LASTREFORMSTATUS = 1 AND ( TC.STATUS < 3 OR TC.STATUS IS NULL))) wzg " +
				"FROM TBL_NBSJ_PROJECT TP LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID " +
				"LEFT JOIN TBL_ORGANIZATION TD ON TP.AUDITORGID = TD.ORGID " +
				"LEFT JOIN TBL_STAFF TE ON TE.STAFFID = TP.AUDITSTAFFID WHERE TP.ORGID IN" +
				" ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" " +
				"CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){ 
			sqlSb.append(" AND (TD.ORGNAME LIKE'%"+question.getORGNAME()+"%'");
			sqlSb.append(" OR TE.REALNAME LIKE'%"+question.getORGNAME()+"%')");			
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord()); 
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String getGkZgContentInfoListCount(TblGkZgQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer("");
		sqlSb.append("SELECT COUNT(0) FROM TBL_NBSJ_PROJECT TP LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TD ON TP.AUDITORGID = TD.ORGID LEFT JOIN TBL_STAFF TE ON TE.STAFFID = TP.AUDITSTAFFID WHERE TP.ORGID IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){  
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){
			sqlSb.append(" AND (TD.ORGNAME LIKE'%"+question.getORGNAME()+"%'");
			sqlSb.append(" OR TE.REALNAME LIKE'%"+question.getORGNAME()+"%')");			
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
		
	// TNA.STATUS="+TblNbsjProject.GD_STATUS+" and  
	public String selectAuditItems(BigDecimal staffid) {
		StringBuffer sb = new StringBuffer("SELECT distinct TNA.*  FROM TBL_NBSJ_PROJECT TNA    WHERE 1=1  "
				+ " AND (PMID='"+staffid+"' or (TNA.projectid in ( SELECT distinct T.PROJECTID "
						+ " FROM TBL_NBSJ_PROJECTTEAM TNA  "
			        + "  LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID  "
			        + "  LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID   "
			         + "  WHERE TNS.STAFFID ='"+staffid+"') ) )");
		return sb.toString();
	}
	
	//判断是否为当前项目组成员或者项目经理
	public String selectIfGroup(BigDecimal staffid,Integer projectid) {
		StringBuffer sb = new StringBuffer("SELECT distinct TNA.*  FROM TBL_NBSJ_PROJECT TNA   "
				+ " WHERE 1=1   AND (PMID='"+staffid+"' or (TNA.projectid in ( "
						+ "SELECT distinct T.PROJECTID FROM TBL_NBSJ_PROJECTTEAM TNA    "
						+ "LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID  "
						+ " LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID  "
						+ " WHERE TNS.STAFFID ='"+staffid+"') ) ) and projectid='"+projectid+"'");
		return sb.toString();
	}
	
	
	
	
	
	
	
	
	
	public String insertEntityar(TblNbsjArchiveEntity ar){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_ARCHIVE(ARCID,ARCTIME,STAFFID,ARCLEVEL,MENUCODE,PROJECTID,ENDTIME,PRICE,WORKTIME");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(ar.getAcrtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+ar.getObjTblStaff().getStaffid()+","+ar.getLevel()+",'"+ar.getMenucode()+"'"
				+ ","+ar.getObjTblnbsjProject().getProjectId()+",TO_DATE('"+DateUtil.parseDate(ar.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+ar.getPrice()+","+ar.getWorktime());
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	public String findProjectItemReport(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo,Integer year) {
		String sql = "SELECT ORGNAME,PRJOECTNAME,PLANYEAR,AUDITTYPE,REALNAME,EXAMINETYPE,PROJECTSOURCE,COSTS,RN,ORGIDNAMES,PROJECTCODE,EXTERNALASSIG,STARTDATE,ENDDATE,PROJECTID FROM (SELECT ORGNAME,PRJOECTNAME,PLANYEAR,AUDITTYPE,REALNAME,EXAMINETYPE,PROJECTSOURCE,COSTS,ROWNUM RN,ORGIDNAMES,PROJECTCODE,EXTERNALASSIG,STARTDATE,ENDDATE,PROJECTID FROM (" +
				"SELECT ORG.ORGNAME,TNP.PRJOECTNAME,TNP.PLANYEAR,TNP.AUDITTYPE,TS.REALNAME,CASE WHEN TNP.EXAMINETYPE = 1 THEN '审批中' WHEN TNP.EXAMINETYPE = 2 THEN '审批驳回' WHEN TNP.EXAMINETYPE = 6 THEN '审批通过'   WHEN TNP.STATUS = 1 THEN '启动' ELSE '未审批' END EXAMINETYPE," +
				"TNP.PROJECTSOURCE,NVL(TNP.COSTS,0) COSTS,TNP.ORGIDNAMES,PROJECTCODE,EXTERNALASSIG,STARTDATE,ENDDATE,TNP.PROJECTID FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_ORGANIZATION ORG ON TNP.AUDITORGID = ORG.ORGID LEFT JOIN TBL_STAFF TS ON TNP.PMID = TS.STAFFID WHERE  TO_CHAR(TNP.CREATETIME,'YYYY') = "+year;
		sql += "ORDER BY PROJECTID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord();
		return sql;
	}
	
	public String selectProjectItemReportCount(Integer year) {
		String countSql = "SELECT COUNT(0) FROM TBL_NBSJ_PROJECT TNP LEFT JOIN TBL_ORGANIZATION ORG ON TNP.AUDITORGID = ORG.ORGID LEFT JOIN TBL_STAFF TS ON TNP.PMID = TS.STAFFID WHERE TO_CHAR(TNP.CREATETIME,'YYYY') = "+year;
		return countSql;
	}
	
	
	
	
	public String selectListBytjsj(String starttime,String endtime){
		StringBuffer sql = new StringBuffer("SELECT ");
		//查询所有项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE  in ('贯彻落实国家重大政策措施审计','财政财务收支审计','固定资产投资审计','内部控制和风险管理审计','经济责任审计','信息系统审计','境外审计','其他专项审计')   ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("   ) allprjject, ");
		
		//贯彻落实国家重大政策措施审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examinetype=6 AND AUDITTYPE='贯彻落实国家重大政策措施审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) gczdproject, ");
		
		//财政财务收支审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='财政财务收支审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) czszproject, ");
		
		//固定资产投资审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='固定资产投资审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) gdzcproject, ");
		//内部控制和风险管理审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='内部控制和风险管理审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) nbkzproject, ");
		//经济责任审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='经济责任审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) jjzrproject, ");
		//信息系统审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='信息系统审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) xxproject, ");
		//境外审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='境外审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) jwproject, ");
		//其他专项审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND AUDITTYPE='其他专项审计'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) qtproject, ");
		
		//委托外包项目审计项目数
		sql.append("  (select nvl(count(*),0) from TBL_NBSJ_PROJECT where examineType=6 AND externalAssig='2'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND STARTDATE>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND ENDDATE<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) wtproject, ");
		
		
		//金额类
		sql.append("  (select nvl(sum(relatedMoney),0) from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1'  ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND CREATETIME>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND CREATETIME<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) je, ");
		
		//绩效类问题金额
		sql.append(" (select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='1'   ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND CREATETIME>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND CREATETIME<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) jx, ");	
		
		//合规性问题金额
		sql.append(" (select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2'    ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND CREATETIME>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND CREATETIME<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) hg, ");	
		
		//会计核算方面
		sql.append(" (select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='1'   ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND CREATETIME>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND CREATETIME<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) kjhs, ");
		
		//违规使用资金
		sql.append(" (select nvl(sum(relatedMoney),0)  from TBL_NBSJ_SHEET where risklevel='是' and belongtype='1' and detailtype='2' and HGDETAILTYPE='2'   ");
		if(starttime!=null && starttime.length()>0) {
			sql.append("    AND CREATETIME>=to_date('"+starttime+"' ,'yyyy-mm-dd')  ");
		}
		if(endtime!=null && endtime.length()>0) {
			sql.append("   AND CREATETIME<=to_date('"+endtime+"' ,'yyyy-mm-dd') ");
		}
		sql.append("  ) wgsy, ");
		
		sql.append("  from dual ");
		
		return sql.toString();
	}
	
	public String getObjBySql(String sql){
		return sql;
	}
	
	
	
	public String getGkProjectInfoExport(TblGkProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("");
		sqlSb.append("SELECT DISTINCT TP.PROJECTID, TP.PROJECTCODE, TP.PRJOECTNAME,CASE  to_char(TP.STATUS) WHEN '1' THEN '已启动'   WHEN '4' THEN '已归档' ELSE '未启动' END AS STATUS, TA.ORGNAME AUDITORGNAME, TP.ORGIDNAMES ORGNAME, TC.REALNAME, TD.REALNAME PMNAME, TP.PLANYEAR, to_char(TP.STARTDATE,'yyyy-mm-dd') STARTDATE, to_char(TP.ENDDATE,'yyyy-mm-dd') ENDDATE, (TP.ENDDATE-TP.STARTDATE) DAYNUMBER, TP.COSTS FROM TBL_NBSJ_PROJECT TP LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID LEFT JOIN TBL_STAFF TD ON TP.PMID = TD.STAFFID WHERE 1 = 1 AND TP.ORGID IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 START WITH ORGID = "+project.getORGID()+" CONNECT BY PRIOR ORGID = FATHERORGID)");
		if(StrUtil.isNotBlank(project.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+project.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+project.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+project.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+project.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+project.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(project.getPMNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+project.getPMNAME()+"%'");
		}
		if(project.getStarttime() != null) {
			sqlSb.append(" AND TP.STARTDATE >=TO_DATE('"+DateUtil.parseDate(project.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(project.getEndtime() != null) {
			sqlSb.append(" AND TP.ENDDATE  <= TO_DATE('"+DateUtil.parseDate(project.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	
	public String getGkQuestionInfoExport(TblGkQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT QU.QUESTIONID,TP.PROJECTCODE,TP.PROJECTID, TP.PRJOECTNAME, TP.PLANYEAR, TP.AUDITTYPE, SH.QUESTITLE,TO_CHAR(SH.AUDITDISCOVERABLE) AUDITDISCOVERABLE, TA.ORGNAME AUDITORGNAME, nvl(SH.ORGIDNAMES,TC.REALNAME) ORGNAME, TC.REALNAME, TD.REALNAME FINDREALNAME,NQTYPE.AUDITTYPE  as INTERNALTYPE FROM TBL_NBSJ_QUESTION qu LEFT JOIN TBL_NBSJ_SHEET SH ON qu.sheetid = SH.sheetid LEFT JOIN TBL_NBSJ_PROJECT TP ON TP.PROJECTID = SH.PROJECTID LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TB ON TP.AUDITORGID = TB.ORGID LEFT JOIN TBL_STAFF TC ON TP.AUDITSTAFFID = TC.STAFFID LEFT JOIN TBL_STAFF TD ON SH.CREATESTAFF = TD.STAFFID LEFT JOIN TBL_NBSJ_QUESTION_TYPE NQTYPE ON NQTYPE.TYPEID = SH.INTERNALTYPE WHERE tp.orgid  IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){
			sqlSb.append(" AND TB.ORGNAME LIKE'%"+question.getORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getREALNAME())){
			sqlSb.append(" AND TC.REALNAME LIKE'%"+question.getREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getFINDREALNAME())){
			sqlSb.append(" AND TD.REALNAME LIKE'%"+question.getFINDREALNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getQUESTITLE())){
			sqlSb.append(" AND SH.QUESTITLE LIKE'%"+question.getQUESTITLE()+"%'");
		}
		
		if(StrUtil.isNotBlank(question.getInternalType())){
			sqlSb.append(" AND NQTYPE.AUDITTYPE LIKE'%"+question.getInternalType()+"%'");
		}
		
		
		sqlSb.append(" ORDER BY TP.PROJECTID,QU.questionid  DESC ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String getGkZgContentInfoExport(TblGkZgQuestionVo question) {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT DISTINCT TP.PROJECTID, TP.PROJECTCODE, TP.PRJOECTNAME, TA.ORGNAME AUDITORGNAME, NVL(TP.ORGIDNAMES, TE.REALNAME) ORGNAME, TP.PLANYEAR, ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET TS WHERE TS.PROJECTID = TP.PROJECTID AND TS.RISKLEVEL='是' ) WTZS, ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET TS LEFT JOIN TBL_NBSJ_QUESTION nq ON TS.SHEETID = nq.SHEETID WHERE TS.PROJECTID = TP.PROJECTID AND NQ.RECSTATUS = 1 AND TS.RISKLEVEL='是' ) FQZGS, ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID LEFT JOIN TBL_NBSJ_REFOPM TC ON nq.QUESTIONID = TC.QUESTIONID WHERE SH.PROJECTID = TP.PROJECTID AND SH.RISKLEVEL='是' AND TC.LASTREFORMSTATUS = 1 AND TC.STATUS = 3 ) YZG, ( SELECT COUNT (*) FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID LEFT JOIN TBL_NBSJ_REFOPM TC ON nq.QUESTIONID = TC.QUESTIONID WHERE SH.PROJECTID = TP.PROJECTID AND SH.RISKLEVEL='是' AND TC.LASTREFORMSTATUS = 1 AND ( TC.STATUS < 3 OR TC.STATUS IS NULL )) WZG FROM TBL_NBSJ_PROJECT TP LEFT JOIN TBL_ORGANIZATION TA ON TP.ORGID = TA.ORGID LEFT JOIN TBL_ORGANIZATION TD ON TP.AUDITORGID = TD.ORGID LEFT JOIN TBL_STAFF TE ON TE.STAFFID = TP.AUDITSTAFFID WHERE TP.ORGID IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 START WITH ORGID = "+question.getORGID()+" CONNECT BY PRIOR ORGID = FATHERORGID )");
		if(StrUtil.isNotBlank(question.getPROJECTCODE())){
			sqlSb.append(" AND PROJECTCODE LIKE '%"+question.getPROJECTCODE()+"%'");
		}
		if(StrUtil.isNotBlank(question.getPRJOECTNAME())){
			sqlSb.append(" AND PRJOECTNAME LIKE'%"+question.getPRJOECTNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getAUDITORGNAME())){
			sqlSb.append(" AND TA.ORGNAME LIKE'%"+question.getAUDITORGNAME()+"%'");
		}
		if(StrUtil.isNotBlank(question.getORGNAME())){
			sqlSb.append(" AND (TP.ORGIDNAMES LIKE'%"+question.getORGNAME()+"%'");
			sqlSb.append(" OR TE.REALNAME LIKE'%"+question.getORGNAME()+"%')");			
		}
		sqlSb.append(" ORDER BY TP.PROJECTID DESC  ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectPageInfoListByRectification(TblZgzzProjectVo project) {
		StringBuffer sqlSb = new StringBuffer("SELECT PROJECTID,PROJECTCODE,PRJOECTNAME FROM TBL_NBSJ_PROJECT WHERE PROJECTID IN (SELECT PROJECTID FROM TBL_ZGZZ_ISSUESILIST WHERE ISSUESTYPE = 1)");
		
		if(StringUtils.isNotBlank(project.getPlanCode())) {
			sqlSb.append(" AND PROJECTCODE LIKE '%").append(project.getPlanCode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(project.getPlanName())) {
			sqlSb.append(" AND PRJOECTNAME LIKE '%").append(project.getPlanName()).append("%'");
		}
		
		sqlSb.append(" ORDER BY PROJECTID DESC ");
		return sqlSb.toString();
	}

	/**
	 * 审计分析-审计项目类型详细数据查询
	 */
	public String findAuditTypeDetail(com.hbfk.util.PageInfo<TblNbsjProject> pageInfo, String year, String auditType) {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT * FROM (SELECT T1.*, ROWNUM RN FROM (");
		sqlSb.append("SELECT TBL_NBSJ_PROJECT.PROJECTCODE AS projectCode, ");
		sqlSb.append("TBL_NBSJ_PROJECT.PRJOECTNAME AS prjoectName, ");
		sqlSb.append("TBL_NBSJ_PROJECT.ORGIDNAMES AS orgIdNames, ");
		sqlSb.append("TBL_NBSJ_PROJECT.AUDITTYPE AS auditType, ");
		sqlSb.append("TBL_NBSJ_PROJECT.PLANYEAR AS planYear, ");
		sqlSb.append("TBL_STAFF.REALNAME AS realname, ");
		sqlSb.append("TBL_NBSJ_PROJECT.EXAMINETYPE AS examineType ");
		sqlSb.append("FROM TBL_NBSJ_PROJECT ");
		sqlSb.append("LEFT JOIN TBL_STAFF ON TBL_NBSJ_PROJECT.PMID = TBL_STAFF.STAFFID ");
		sqlSb.append("WHERE TO_CHAR(TBL_NBSJ_PROJECT.CREATETIME, 'YYYY') = '").append(year).append("' ");

		if(StringUtils.isNotBlank(auditType)) {
			sqlSb.append("AND TBL_NBSJ_PROJECT.AUDITTYPE = '").append(auditType).append("' ");
		}

		sqlSb.append("ORDER BY TBL_NBSJ_PROJECT.PROJECTCODE DESC) T1 WHERE ROWNUM <= ")
		  .append((pageInfo.getCurrentPage() - 1) * pageInfo.getPageSize() + pageInfo.getPageSize())
		  .append(") T2 WHERE RN > ").append((pageInfo.getCurrentPage() - 1) * pageInfo.getPageSize());

		System.out.println("审计项目类型详细数据SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}

	/**
	 * 审计分析-审计项目类型详细数据总数查询
	 */
	public String selectAuditTypeDetailCount(String year, String auditType) {
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT COUNT(TBL_NBSJ_PROJECT.PROJECTCODE) FROM TBL_NBSJ_PROJECT ");
		sqlSb.append("WHERE TO_CHAR(TBL_NBSJ_PROJECT.CREATETIME, 'YYYY') = '").append(year).append("' ");

		if(StringUtils.isNotBlank(auditType)) {
			sqlSb.append("AND TBL_NBSJ_PROJECT.AUDITTYPE = '").append(auditType).append("' ");
		}

		System.out.println("审计项目类型详细数据总数SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}

	/**
	 * 整改问题详细数据查询SQL
	 * 支持按主管部门、项目ID、审计类型和查询类型筛选
	 */
/*	public String findZgwtDetail(@Param("year") Integer year,
								  @Param("orgName") String orgName,
								  @Param("projectId") String projectId,
								  @Param("audittype") String audittype,
								  @Param("projectsource") String projectsource,
								  @Param("queryType") String queryType,
								  @Param("pageInfo") com.hbfk.util.PageInfo<ZgwtDetailVo> pageInfo) {
		StringBuffer sqlSb = new StringBuffer();

		// 根据用户需求重写SQL - 支持整改状态(CONCLUSION)动态筛选,并关联整改评价表获取resultStatus
		sqlSb.append("SELECT ISSUECODE,ISSUENAME," +
				"DBMS_LOB.SUBSTR(QUESTIONMEMO, 4000, 1) AS QUESTIONMEMO," +
				"PLANDEADLINE," +
				"ORGNAME," +
				"DBMS_LOB.SUBSTR(CONCLUSION, 4000, 1) AS CONCLUSION FROM (");
		sqlSb.append("  SELECT ROWNUM AS RN, TEMP.* FROM (");
		sqlSb.append("    SELECT DISTINCT ");
		sqlSb.append("           i.ISSUESID AS issueId,");
		sqlSb.append("           i.ISSUESCODE AS issueCode,");
		sqlSb.append("           i.ISSUESNAME AS issueName,");
		sqlSb.append("           i.ISSUESTITLE AS issueTitle,");
		sqlSb.append("           i.QUESTIONMEMO AS questionMemo,");
		sqlSb.append("           p.PROJECTID AS projectId,");
		sqlSb.append("           p.ORGIDNAMES AS orgIdNames,");
		sqlSb.append("           p.PRJOECTNAME AS projectName,");
		sqlSb.append("           p.STARTDATE AS startDate,");
		sqlSb.append("           p.AUDITTYPE AS auditType,");
		sqlSb.append("           p.STATUS AS status,");
		sqlSb.append("           p.PROJECTSOURCE AS projectSource,");
		sqlSb.append("           p.ENDDATE AS endDate,");
		sqlSb.append("           s.STAFFID AS staffId,");
		sqlSb.append("           s.REALNAME AS realName,");
		sqlSb.append("           s.MIBLEPHONE AS mobilePhone,");
		sqlSb.append("           s.EMAIL AS email,");
		sqlSb.append("           o.ORGID AS orgId,");
		sqlSb.append("           o.ORGNAME AS orgName,");
		sqlSb.append("           o.FATHERORGID AS fatherOrgId,");
		sqlSb.append("           r.RELAID AS relaId,");
		sqlSb.append("           r.RECTIFICATIONPLAN AS rectificationPlan,");
		sqlSb.append("           r.RECTIFICATIONMEASURES AS rectificationMeasures,");
		sqlSb.append("           r.DEADLINE AS planDeadline,");
		sqlSb.append("           z.IMPLID AS implId,");
		sqlSb.append("           z.RECTIFICATIONMEASURES AS implRectMeasures,");
		sqlSb.append("           z.SITUATIONOVERVIEW AS situationOverview,");
		sqlSb.append("           z.ACHIVEMENT AS achievement,");
		sqlSb.append("           z.CONCLUSION AS conclusion,");
		sqlSb.append("           z.DEADLINE AS implDeadline,");
		sqlSb.append("           z.FINISHTIME AS finishTime,");
		sqlSb.append("           e.RESULTSTATUS AS resultStatus");
		sqlSb.append("    FROM TBL_ZGZZ_ISSUESILIST i");
		sqlSb.append("         LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID");
		sqlSb.append("         LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID");
		sqlSb.append("         LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID");
		sqlSb.append("         LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID");
		sqlSb.append("         LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID");
		sqlSb.append("         LEFT JOIN TBL_ZGZZ_RCTEVALUATION e ON z.IMPLID = e.IMPLID");
		sqlSb.append("    WHERE 1=1");

		// 年度条件 - 根据项目结束年份筛选
		if (year != null) {
			sqlSb.append(" AND EXTRACT(YEAR FROM p.CREATETIME) = #{year}");
		}

		// 主管部门名称条件 - 模糊匹配
		if (StringUtils.isNotBlank(orgName)) {
			sqlSb.append(" AND o.ORGNAME LIKE '%' || #{orgName} || '%'");
		}

		// 项目ID条件 - 精确匹配
		if (StringUtils.isNotBlank(projectId)) {
			sqlSb.append(" AND p.PROJECTID = #{projectId}");
		}

		// 审计类型条件 - 精确匹配
		if (StringUtils.isNotBlank(audittype)) {
			sqlSb.append(" AND p.AUDITTYPE = #{audittype}");
		}

		// 审计来源条件 - 精确匹配
		if (StringUtils.isNotBlank(projectsource)) {
			sqlSb.append(" AND p.PROJECTSOURCE = #{projectsource}");
		}

		// 整改结论条件 - 根据queryType动态生成
		if (StringUtils.isNotBlank(queryType)) {
			if ("wzg".equals(queryType)) {
				// 未整改: 未整改 + 已整改未到位
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('未整改', '已整改未到位')");
			} else if ("yzg".equals(queryType)) {
				// 已整改: 已整改 + 已整改到位
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('已整改', '已整改到位')");
			} else if ("zs".equals(queryType)) {
				// 总数: 所有有效整改结论
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('已整改', '已整改到位', '未整改', '已整改未到位')");
			} else if ("yxh".equals(queryType)) {
				// 已销号
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) = '已销号'");
			} else if ("wxh".equals(queryType)) {
				// 未销号
				sqlSb.append(" AND (DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IS NULL OR DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) != '已销号')");
			} else if ("xhzs".equals(queryType)) {
				// 销号总数: 不过滤CONCLUSION
				// 不添加CONCLUSION条件
			}
		}

		sqlSb.append("    ORDER BY p.ENDDATE DESC, i.ISSUESID DESC");
		sqlSb.append("  ) TEMP WHERE ROWNUM <= ").append(pageInfo.getCurrentRecord() + pageInfo.getPageSize());
		sqlSb.append(") WHERE RN > ").append(pageInfo.getCurrentRecord());

		System.out.println("整改问题详细数据SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}*/
	public String findZgwtDetail(@Param("year") Integer year,
								 @Param("orgName") String orgName,
								 @Param("projectId") String projectId,
								 @Param("audittype") String audittype,
								 @Param("projectsource") String projectsource,
								 @Param("queryType") String queryType,
								 @Param("pageInfo") com.hbfk.util.PageInfo<ZgwtDetailVo> pageInfo) {
		StringBuffer sqlSb = new StringBuffer();

		// 根据用户需求重写SQL - 支持整改状态(CONCLUSION)动态筛选,并关联整改评价表获取resultStatus
		sqlSb.append("SELECT ISSUECODE,ISSUENAME," +
				"SUBSTR(QUESTIONMEMO, 1, 4000) AS QUESTIONMEMO," +
				"PLANDEADLINE," +
				"ORGNAME," +
				"SUBSTR(CONCLUSION, 1, 4000) AS CONCLUSION FROM (");
		sqlSb.append("  SELECT ROWNUM AS RN, TEMP.* FROM (");
		sqlSb.append("    SELECT DISTINCT ");
		sqlSb.append("           i.ISSUESID AS issueId,");
		sqlSb.append("           i.ISSUESCODE AS issueCode,");
		sqlSb.append("           i.ISSUESNAME AS issueName,");
		sqlSb.append("           i.ISSUESTITLE AS issueTitle,");
		sqlSb.append("           i.QUESTIONMEMO AS questionMemo,");
		sqlSb.append("           p.PROJECTID AS projectId,");
		sqlSb.append("           p.ORGIDNAMES AS orgIdNames,");
		sqlSb.append("           p.PRJOECTNAME AS projectName,");
		sqlSb.append("           p.STARTDATE AS startDate,");
		sqlSb.append("           p.AUDITTYPE AS auditType,");
		sqlSb.append("           p.STATUS AS status,");
		sqlSb.append("           p.PROJECTSOURCE AS projectSource,");
		sqlSb.append("           p.ENDDATE AS endDate,");
		sqlSb.append("           s.STAFFID AS staffId,");
		sqlSb.append("           s.REALNAME AS realName,");
		sqlSb.append("           s.MIBLEPHONE AS mobilePhone,");
		sqlSb.append("           s.EMAIL AS email,");
		sqlSb.append("           o.ORGID AS orgId,");
		sqlSb.append("           o.ORGNAME AS orgName,");
		sqlSb.append("           o.FATHERORGID AS fatherOrgId,");
		sqlSb.append("           r.RELAID AS relaId,");
		sqlSb.append("           r.RECTIFICATIONPLAN AS rectificationPlan,");
		sqlSb.append("           r.RECTIFICATIONMEASURES AS rectificationMeasures,");
		sqlSb.append("           r.DEADLINE AS planDeadline,");
		sqlSb.append("           z.IMPLID AS implId,");
		sqlSb.append("           z.RECTIFICATIONMEASURES AS implRectMeasures,");
		sqlSb.append("           z.SITUATIONOVERVIEW AS situationOverview,");
		sqlSb.append("           z.ACHIVEMENT AS achievement,");
		sqlSb.append("           z.CONCLUSION AS conclusion,");
		sqlSb.append("           z.DEADLINE AS implDeadline,");
		sqlSb.append("           z.FINISHTIME AS finishTime,");
		sqlSb.append("           e.RESULTSTATUS AS resultStatus");
		sqlSb.append("    FROM TBL_ZGZZ_ISSUESILIST i");
		sqlSb.append("         LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID");
		sqlSb.append("         LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID");
		sqlSb.append("         LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID");
		sqlSb.append("         LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID");
		sqlSb.append("         LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID");
		sqlSb.append("         LEFT JOIN TBL_ZGZZ_RCTEVALUATION e ON z.IMPLID = e.IMPLID");
		sqlSb.append("    WHERE 1=1");

		// 年度条件 - 根据项目结束年份筛选
		if (year != null) {
			sqlSb.append(" AND TO_CHAR(p.CREATETIME, 'YYYY') = TO_CHAR(#{year})");
		}

		// 主管部门名称条件 - 模糊匹配
		if (StringUtils.isNotBlank(orgName)) {
			sqlSb.append(" AND o.ORGNAME LIKE '%' || #{orgName} || '%'");
		}

		// 项目ID条件 - 精确匹配
		if (StringUtils.isNotBlank(projectId)) {
			sqlSb.append(" AND p.PROJECTID = #{projectId}");
		}

		// 审计类型条件 - 精确匹配
		if (StringUtils.isNotBlank(audittype)) {
			sqlSb.append(" AND p.AUDITTYPE = #{audittype}");
		}

		// 审计来源条件 - 精确匹配
		if (StringUtils.isNotBlank(projectsource)) {
			sqlSb.append(" AND p.PROJECTSOURCE = #{projectsource}");
		}

		// 整改结论条件 - 根据queryType动态生成
		if (StringUtils.isNotBlank(queryType)) {
			if ("wzg".equals(queryType)) {
				// 未整改: 未整改 + 已整改未到位
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('未整改', '已整改未到位')");
			} else if ("yzg".equals(queryType)) {
				// 已整改: 已整改 + 已整改到位
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('已整改', '已整改到位')");
			} else if ("zs".equals(queryType)) {
				// 总数: 所有有效整改结论
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('已整改', '已整改到位', '未整改', '已整改未到位')");
			} else if ("yxh".equals(queryType)) {
				// 已销号
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) = '已销号'");
			} else if ("wxh".equals(queryType)) {
				// 未销号
				sqlSb.append(" AND (SUBSTR(z.CONCLUSION, 1, 4000) IS NULL OR SUBSTR(z.CONCLUSION, 1, 4000) != '已销号')");
			} else if ("xhzs".equals(queryType)) {
				// 销号总数: 不过滤CONCLUSION
				// 不添加CONCLUSION条件
			}
		}

		sqlSb.append("    ORDER BY p.ENDDATE DESC, i.ISSUESID DESC");
		sqlSb.append("  ) TEMP WHERE ROWNUM <= ").append(pageInfo.getCurrentRecord() + pageInfo.getPageSize());
		sqlSb.append(") WHERE RN > ").append(pageInfo.getCurrentRecord());

		System.out.println("整改问题详细数据SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}
	/**
	 * 整改问题详细数据总数统计SQL
	 */
/*	public String countZgwtDetail(@Param("year") Integer year,
								   @Param("orgName") String orgName,
								   @Param("projectId") String projectId,
								   @Param("audittype") String audittype,
								   @Param("projectsource") String projectsource,
								   @Param("queryType") String queryType) {
		StringBuffer sqlSb = new StringBuffer();

		// 根据用户需求重写SQL - 统计总数(与详情SQL保持一致的WHERE条件)
		sqlSb.append("SELECT COUNT(0)");
		sqlSb.append(" FROM TBL_ZGZZ_ISSUESILIST i");
		sqlSb.append("      LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID");
		sqlSb.append("      LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID");
		sqlSb.append("      LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID");
		sqlSb.append("      LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID");
		sqlSb.append("      LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID");
		sqlSb.append(" WHERE 1=1");

		// 年度条件 - 根据项目结束年份筛选
		if (year != null) {
			sqlSb.append(" AND EXTRACT(YEAR FROM p.CREATETIME) = #{year}");
		}

		// 主管部门名称条件 - 模糊匹配
		if (StringUtils.isNotBlank(orgName)) {
			sqlSb.append(" AND o.ORGNAME LIKE '%' || #{orgName} || '%'");
		}

		// 项目ID条件 - 精确匹配
		if (StringUtils.isNotBlank(projectId)) {
			sqlSb.append(" AND p.PROJECTID = #{projectId}");
		}

		// 审计类型条件 - 精确匹配
		if (StringUtils.isNotBlank(audittype)) {
			sqlSb.append(" AND p.AUDITTYPE = #{audittype}");
		}

		// 审计来源条件 - 精确匹配
		if (StringUtils.isNotBlank(projectsource)) {
			sqlSb.append(" AND p.PROJECTSOURCE = #{projectsource}");
		}

		// 整改结论条件 - 根据queryType动态生成
		if (StringUtils.isNotBlank(queryType)) {
			if ("wzg".equals(queryType)) {
				// 未整改: 未整改 + 已整改未到位
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('未整改', '已整改未到位')");
			} else if ("yzg".equals(queryType)) {
				// 已整改: 已整改 + 已整改到位
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('已整改', '已整改到位')");
			} else if ("zs".equals(queryType)) {
				// 总数: 所有有效整改结论
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IN ('已整改', '已整改到位', '未整改', '已整改未到位')");
			} else if ("yxh".equals(queryType)) {
				// 已销号
				sqlSb.append(" AND DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) = '已销号'");
			} else if ("wxh".equals(queryType)) {
				// 未销号
				sqlSb.append(" AND (DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) IS NULL OR DBMS_LOB.SUBSTR(z.CONCLUSION, 4000, 1) != '已销号')");
			} else if ("xhzs".equals(queryType)) {
				// 销号总数: 不过滤CONCLUSION
				// 不添加CONCLUSION条件
			}
		}

		System.out.println("整改问题详细数据总数SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}*/
	public String countZgwtDetail(@Param("year") Integer year,
								  @Param("orgName") String orgName,
								  @Param("projectId") String projectId,
								  @Param("audittype") String audittype,
								  @Param("projectsource") String projectsource,
								  @Param("queryType") String queryType) {
		StringBuffer sqlSb = new StringBuffer();

		// 根据用户需求重写SQL - 统计总数(与详情SQL保持一致的WHERE条件)
		sqlSb.append("SELECT COUNT(*)");
		sqlSb.append(" FROM TBL_ZGZZ_ISSUESILIST i");
		sqlSb.append("      LEFT JOIN TBL_NBSJ_PROJECT p ON i.PROJECTID = p.PROJECTID");
		sqlSb.append("      LEFT JOIN TBL_STAFF s ON s.STAFFID = p.PMID");
		sqlSb.append("      LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = p.ORGID");
		sqlSb.append("      LEFT JOIN TBL_RECTIFICATION_ISSUES r ON i.ISSUESID = r.ISSUESID");
		sqlSb.append("      LEFT JOIN TBL_ZGZZ_RECTIFICATIONIMPL z ON r.RELAID = z.RELAID");
		sqlSb.append(" WHERE 1=1");

		// 年度条件 - 根据项目结束年份筛选
		if (year != null) {
			sqlSb.append(" AND TO_CHAR(p.CREATETIME, 'YYYY') = TO_CHAR(#{year})");
		}

		// 主管部门名称条件 - 模糊匹配
		if (StringUtils.isNotBlank(orgName)) {
			sqlSb.append(" AND o.ORGNAME LIKE '%' || #{orgName} || '%'");
		}

		// 项目ID条件 - 精确匹配
		if (StringUtils.isNotBlank(projectId)) {
			sqlSb.append(" AND p.PROJECTID = #{projectId}");
		}

		// 审计类型条件 - 精确匹配
		if (StringUtils.isNotBlank(audittype)) {
			sqlSb.append(" AND p.AUDITTYPE = #{audittype}");
		}

		// 审计来源条件 - 精确匹配
		if (StringUtils.isNotBlank(projectsource)) {
			sqlSb.append(" AND p.PROJECTSOURCE = #{projectsource}");
		}

		// 整改结论条件 - 根据queryType动态生成
		if (StringUtils.isNotBlank(queryType)) {
			if ("wzg".equals(queryType)) {
				// 未整改: 未整改 + 已整改未到位
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('未整改', '已整改未到位')");
			} else if ("yzg".equals(queryType)) {
				// 已整改: 已整改 + 已整改到位
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('已整改', '已整改到位')");
			} else if ("zs".equals(queryType)) {
				// 总数: 所有有效整改结论
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) IN ('已整改', '已整改到位', '未整改', '已整改未到位')");
			} else if ("yxh".equals(queryType)) {
				// 已销号
				sqlSb.append(" AND SUBSTR(z.CONCLUSION, 1, 4000) = '已销号'");
			} else if ("wxh".equals(queryType)) {
				// 未销号
				sqlSb.append(" AND (SUBSTR(z.CONCLUSION, 1, 4000) IS NULL OR SUBSTR(z.CONCLUSION, 1, 4000) != '已销号')");
			} else if ("xhzs".equals(queryType)) {
				// 销号总数: 不过滤CONCLUSION
				// 不添加CONCLUSION条件
			}
		}

		System.out.println("整改问题详细数据总数SQL: " + sqlSb.toString());
		return sqlSb.toString();
	}
}
