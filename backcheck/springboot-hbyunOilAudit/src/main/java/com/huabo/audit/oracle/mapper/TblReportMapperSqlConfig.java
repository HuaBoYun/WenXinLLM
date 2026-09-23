package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblReportEntity;
import com.huabo.audit.oracle.vo.TblReportVo;

import cn.hutool.core.util.StrUtil;

public class TblReportMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblReportEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_REPORT WHERE 1=1 AND REPORTNAME='"+plan.getReportname()+"' ");
		if(plan.getReportid() != null) {
			sb.append(" AND REPORTID != "+plan.getReportid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT TNA.* "
				+ "FROM TBL_REPORT TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1"
				+ " AND ORGID='"+orgid+"' ");
		
		if(projectId != null) {
			sb.append(" AND TNA.PROJECTID =  "+projectId);
		}
		if(tblReportVo.getType()!=null && tblReportVo.getType().length()>0) {
			sb.append(" AND TYPE = '"+tblReportVo.getType()+"'");
		}
		
		if(tblReportVo.getReportname()!=null && tblReportVo.getReportname().length()>0) {
			sb.append(" AND TNA.REPORTNAME LIKE '%"+tblReportVo.getReportname()+"%'");
		}
		
		if(tblReportVo.getStartDate() !=null && tblReportVo.getStartDate().length()>0){
			sb.append(" AND TNA.REPORTTIME >= TO_DATE('"+tblReportVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblReportVo.getEndDate() !=null && tblReportVo.getEndDate().length()>0){
			sb.append(" AND TNA.REPORTTIME <= TO_DATE('"+tblReportVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.REPORTID DESC");
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblReportEntity> pageInfo,TblReportVo tblReportVo,Integer orgid,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_REPORT TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1"
				+ " AND ORGID='"+orgid+"' ");
		
		if(projectId != null) {
			sb.append(" AND TNA.PROJECTID =  "+projectId);
		}
		
		if(tblReportVo.getType()!=null && tblReportVo.getType().length()>0) {
			sb.append(" AND TYPE = '"+tblReportVo.getType()+"'");
		}
		
		if(tblReportVo.getReportname()!=null && tblReportVo.getReportname().length()>0) {
			sb.append(" AND TNA.REPORTNAME LIKE '%"+tblReportVo.getReportname()+"%'");
		}
		
		if(tblReportVo.getStartDate() !=null && tblReportVo.getStartDate().length()>0){
			sb.append(" AND TNA.REPORTTIME >= TO_DATE('"+tblReportVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblReportVo.getEndDate() !=null && tblReportVo.getEndDate().length()>0){
			sb.append(" AND TNA.REPORTTIME <= TO_DATE('"+tblReportVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblReportEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_REPORT SET REPORTNAME = '"+plan.getReportname()+"' ");
		if(plan.getReporttime() != null ) {
			sqlSb.append(" ,REPORTTIME = TO_DATE('"+DateUtil.parseDate(plan.getReporttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getReporttype() != null && !"".equals(plan.getReporttype())) {
			sqlSb.append(" ,REPORTTYPE = '"+plan.getReporttype()+"'");
		}
		if(plan.getReportmode() != null && !"".equals(plan.getReportmode())) {
			sqlSb.append(" ,REPORTMODE = '"+plan.getReportmode()+"'");
		}
		if(plan.getReporter() != null && !"".equals(plan.getReporter())) {
			sqlSb.append(" ,REPORTER = '"+plan.getReporter()+"'");
		}
		if(plan.getReportdepartment() != null && !"".equals(plan.getReportdepartment())) {
			sqlSb.append(" ,REPORTDEPARTMENT = '"+plan.getReportdepartment()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			sqlSb.append(" ,REPDESC = '"+plan.getRepdesc()+"'");
		}
		if(plan.getReportstatus()!= null && !"".equals(plan.getReportstatus())) {
			sqlSb.append(" ,reportstatus = '"+plan.getReportstatus()+"'");
		}
		if(plan.getFhstaffid()!= null) {
			sqlSb.append(" ,FHSTAFFID = "+plan.getFhstaffid());
		}
		if(StrUtil.isNotBlank(plan.getFhstaffname())) {
			sqlSb.append(" ,FHSTAFFNAME = '"+plan.getFhstaffname()+"'");
		}
		if(plan.getZqyjstaffid()!= null) {
			sqlSb.append(" ,ZQYJSTAFFID = "+plan.getZqyjstaffid());
		}
		if(StrUtil.isNotBlank(plan.getZqyjstaffname())) {
			sqlSb.append(" ,ZQYJSTAFFNAME = '"+plan.getZqyjstaffname()+"'");
		}
		if(plan.getReporterid()!= null && !"".equals(plan.getReporterid())) {
			sqlSb.append(" ,REPORTERID = '"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid()!= null && !"".equals(plan.getReportdepartmentid())) {
			sqlSb.append(" ,REPORTDEPARTMENTID = '"+plan.getReportdepartmentid()+"'");
		}
		
		sqlSb.append(" WHERE REPORTID= "+plan.getReportid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblReportEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_REPORT(REPORTID,SENDTIME,REPORTSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getSendTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0");
		
		if(plan.getReportname() != null && !"".equals(plan.getReportname())) {
			colSb.append(",REPORTNAME");
			valSb.append(",'"+plan.getReportname()+"'");
		}
		if(plan.getReporttime() != null && !"".equals(plan.getReporttime())) {
			colSb.append(",REPORTTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getReporttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getReporttype() != null && !"".equals(plan.getReporttype())) {
			colSb.append(",REPORTTYPE");
			valSb.append(",'"+plan.getReporttype()+"'");
		}
		if(plan.getReportmode() != null && !"".equals(plan.getReportmode())) {
			colSb.append(",REPORTMODE");
			valSb.append(",'"+plan.getReportmode()+"'");
		}
		if(plan.getReporter() != null && !"".equals(plan.getReporter())) {
			colSb.append(",REPORTER");
			valSb.append(",'"+plan.getReporter()+"'");
		}
		if(plan.getReportdepartment() != null && !"".equals(plan.getReportdepartment())) {
			colSb.append(",REPORTDEPARTMENT");
			valSb.append(",'"+plan.getReportdepartment()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			colSb.append(",REPDESC");
			valSb.append(",'"+plan.getRepdesc()+"'");
		}
		if(plan.getFhstaffid()!= null) {
			colSb.append(",FHSTAFFID");
			valSb.append(",'"+plan.getFhstaffid()+"'");
		}
		if(StrUtil.isNotBlank(plan.getFhstaffname())) {
			colSb.append(",FHSTAFFNAME");
			valSb.append(",'"+plan.getFhstaffname()+"'");
		}
		if(plan.getZqyjstaffid()!= null) {
			colSb.append(",ZQYJSTAFFID");
			valSb.append(",'"+plan.getZqyjstaffid()+"'");
		}
		if(StrUtil.isNotBlank(plan.getZqyjstaffname())) {
			colSb.append(",ZQYJSTAFFNAME");
			valSb.append(",'"+plan.getZqyjstaffname()+"'");
		}
		if(plan.getOrgid() != null && !"".equals(plan.getOrgid())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgid()+"'");
		}
		if(plan.getType() != null && !"".equals(plan.getType())) {
			colSb.append(",TYPE");
			valSb.append(",'"+plan.getType()+"'");
		}
		
		if(plan.getReporterid() != null && !"".equals(plan.getReporterid())) {
			colSb.append(",REPORTERID");
			valSb.append(",'"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid() != null && !"".equals(plan.getReportdepartmentid())) {
			colSb.append(",REPORTDEPARTMENTID");
			valSb.append(",'"+plan.getReportdepartmentid()+"'");
		}
		
		if(plan.getProjectId() != null) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
