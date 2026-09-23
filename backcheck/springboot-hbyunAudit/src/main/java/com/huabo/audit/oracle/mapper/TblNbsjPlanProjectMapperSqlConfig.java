package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.vo.TblNbsjPlanProjectVo;

public class TblNbsjPlanProjectMapperSqlConfig {
	
	public String selectPlanProjectListByPageInfo(TblNbsjPlanProjectVo project, PageInfo<TblNbsjPlanProject> pageInfo) {
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* FROM ( SELECT T1.*,ROWNUM RN FROM ( SELECT PLANPROJECTID,PROJECTNAME,TARGETNAME,FINISHTIME,ORGIDNAMES,EXTERNALASSIG FROM TBL_NBSJ_PLANPROJECT WHERE 1=1");
		if(project.getPlanId() != null) {
			sqlSb.append(" AND PLANID = "+project.getPlanId());
		}
		
		if(project.getProjectname() != null && !"".equals(project.getProjectname())) {
			sqlSb.append(" AND PROJECTNAME LIKE '%"+project.getProjectname()+"%'");
		}
		
		if(project.getTargetname() != null && !"".equals(project.getTargetname())) {
			sqlSb.append(" AND TARGETNAME LIKE '%"+project.getTargetname()+"%'");
		}
		
		sqlSb.append("ORDER BY PLANPROJECTID ASC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectPlanProjectCountByPageInfo(TblNbsjPlanProjectVo project, PageInfo<TblNbsjPlanProject> pageInfo) {
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_NBSJ_PLANPROJECT WHERE 1=1");
		if(project.getPlanId() != null) {
			sqlSb.append(" AND PLANID = "+project.getPlanId());
		}
		
		if(project.getProjectname() != null && !"".equals(project.getProjectname())) {
			sqlSb.append(" AND PROJECTNAME LIKE '%"+project.getProjectname()+"%'");
		}
		
		if(project.getTargetname() != null && !"".equals(project.getTargetname())) {
			sqlSb.append(" AND TARGETNAME LIKE '%"+project.getTargetname()+"%'");
		}
		
		return sqlSb.toString();
		
	}
	
	
	public String updateEntity(TblNbsjPlanProject project) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PLANPROJECT SET PLANID = "+project.getPlanid() );
		
		if(project.getProjectname() != null && !"".equals(project.getProjectname())) {
			sqlSb.append(",PROJECTNAME = '"+project.getProjectname()+"'");
		}
		
		if(project.getTargetname() != null && !"".equals(project.getTargetname())) {
			sqlSb.append(",TARGETNAME = '"+project.getTargetname()+"'");
		}
		
		if(project.getFinishtime() != null) {
			sqlSb.append(",FINISHTIME = TO_DATE('"+DateUtil.parseDate(project.getFinishtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(project.getOrgids() != null && !"".equals(project.getOrgids())) {
			sqlSb.append(",ORGIDS = '"+project.getOrgids()+"'");
		}
		
		if(project.getOrgidnames() != null && !"".equals(project.getOrgidnames())) {
			sqlSb.append(",ORGIDNAMES = '"+project.getOrgidnames()+"'");
		}
		
		if(project.getExternalassig() != null && !"".equals(project.getExternalassig())) {
			sqlSb.append(",EXTERNALASSIG = '"+project.getExternalassig()+"'");
		}
		
		if(project.getBsjtype() != null && !"".equals(project.getBsjtype())) {
			sqlSb.append(",BSJTYPE = '"+project.getBsjtype()+"'");
		}
		
		sqlSb.append(" WHERE PLANPROJECTID = "+project.getPlanprojectid());
		return sqlSb.toString();
	}
	
	
	public String insertEntity(TblNbsjPlanProject project) {
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_PLANPROJECT(PLANPROJECTID,PLANID");
		StringBuffer valSb = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval,"+project.getPlanid());
		
		if(project.getProjectname() != null && !"".equals(project.getProjectname())) {
			colSb.append(",PROJECTNAME");
			valSb.append(",'"+project.getProjectname()+"'");
		}
		
		if(project.getTargetname() != null && !"".equals(project.getTargetname())) {
			colSb.append(",TARGETNAME");
			valSb.append(",'"+project.getTargetname()+"'");
		}
		
		if(project.getFinishtime() != null) {
			colSb.append(",FINISHTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(project.getFinishtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(project.getOrgids() != null && !"".equals(project.getOrgids())) {
			colSb.append(",ORGIDS");
			valSb.append(",'"+project.getOrgids()+"'");
		}
		
		if(project.getOrgidnames() != null && !"".equals(project.getOrgidnames())) {
			colSb.append(",ORGIDNAMES");
			valSb.append(",'"+project.getOrgidnames()+"'");
		}
		
		if(project.getExternalassig() != null && !"".equals(project.getExternalassig())) {
			colSb.append(",EXTERNALASSIG");
			valSb.append(",'"+project.getExternalassig()+"'");
		}
		
		if(project.getBsjtype() != null && !"".equals(project.getBsjtype())) {
			colSb.append(",BSJTYPE");
			valSb.append(",'"+project.getBsjtype()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		
		return sql;
	}
}
