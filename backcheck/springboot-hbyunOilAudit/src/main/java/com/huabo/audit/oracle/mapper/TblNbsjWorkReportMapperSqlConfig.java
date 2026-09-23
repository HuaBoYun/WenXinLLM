package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjWorkReportEntity;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjWorkReportMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjWorkReportEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_WORKREPORT WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjWorkReportEntity> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo) {
//		TblNbsjWorkReportEntity plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
				+ "FROM TBL_NBSJ_WORKREPORT TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.REPORTER "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.REPORTDEPARTMENT "
				+ "WHERE 1=1 ");
		
//		if(plan.getPrincipalid() != null) {
//			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
//		}
		if(tblNbsjWorkReportVo.getProjectId()!=null){
			sb.append("AND PROJECTID ="+tblNbsjWorkReportVo.getProjectId()+"");
		}
		if(tblNbsjWorkReportVo.getReportname()!=null && tblNbsjWorkReportVo.getReportname().length()>0) {
			sb.append(" AND TNA.REPORTNAME LIKE '%"+tblNbsjWorkReportVo.getReportname()+"%'");
		}
		if(tblNbsjWorkReportVo.getReporttype()!=null && tblNbsjWorkReportVo.getReporttype().length()>0){
			sb.append(" AND TNA.REPORTTYPE LIKE '%"+tblNbsjWorkReportVo.getReporttype()+"%'");
		}
		if(tblNbsjWorkReportVo.getStarttime()!=null && tblNbsjWorkReportVo.getStarttime().length()>0){
			sb.append(" AND REPORTTIME >= TO_DATE ('"+tblNbsjWorkReportVo.getStarttime()+"', 'yyyy-MM-dd')");
		}
		if(tblNbsjWorkReportVo.getEndtime()!=null && tblNbsjWorkReportVo.getEndtime().length()>0){
			sb.append(" AND REPORTTIME <= TO_DATE ('"+tblNbsjWorkReportVo.getEndtime()+"', 'yyyy-MM-dd')");
		}
		
		sb.append(" ORDER BY TNA.REPORTID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjWorkReportEntity> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo) {
		TblNbsjWorkReportEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_WORKREPORT TNA "
				+ "WHERE 1=1 ");
		
		if(tblNbsjWorkReportVo.getProjectId()!=null){
			sb.append("AND PROJECTID ="+tblNbsjWorkReportVo.getProjectId()+"");
		}
		if(tblNbsjWorkReportVo.getReportname()!=null && tblNbsjWorkReportVo.getReportname().length()>0) {
			sb.append(" AND TNA.REPORTNAME LIKE '%"+tblNbsjWorkReportVo.getReportname()+"%'");
		}
		if(tblNbsjWorkReportVo.getReporttype()!=null && tblNbsjWorkReportVo.getReporttype().length()>0){
			sb.append(" AND TNA.REPORTTYPE LIKE '%"+tblNbsjWorkReportVo.getReporttype()+"%'");
		}
		if(tblNbsjWorkReportVo.getStarttime()!=null && tblNbsjWorkReportVo.getStarttime().length()>0){
			sb.append(" AND REPORTTIME >= TO_DATE ('"+tblNbsjWorkReportVo.getStarttime()+"', 'yyyy-MM-dd')");
		}
		if(tblNbsjWorkReportVo.getEndtime()!=null && tblNbsjWorkReportVo.getEndtime().length()>0){
			sb.append(" AND REPORTTIME <= TO_DATE ('"+tblNbsjWorkReportVo.getEndtime()+"', 'yyyy-MM-dd')");
		}
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjWorkReportEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_WORKREPORT SET REPORTNAME = '"+plan.getReportname()+"' ");
		if(plan.getReporttype() != null && !"".equals(plan.getReporttype())) {
			sqlSb.append(" ,REPORTTYPE = '"+plan.getReporttype()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			sqlSb.append(" ,MEMO = '"+plan.getMemo()+"'");
		}
		
		if(plan.getReporttime() != null && !"".equals(plan.getReporttime())) {
			sqlSb.append(" ,REPORTTIME = TO_DATE('"+DateUtil.parseDate(plan.getReporttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getReporterid() != null && !"".equals(plan.getReporterid())) {
			sqlSb.append(" ,REPORTER = '"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid() != null && !"".equals(plan.getReportdepartmentid())) {
			sqlSb.append(" ,REPORTDEPARTMENT = '"+plan.getReportdepartmentid()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			sqlSb.append(" ,REPDESC = '"+plan.getRepdesc()+"'");
		}
		if(plan.get_projectid() != null && !"".equals(plan.get_projectid())) {
			sqlSb.append(" ,PROJECTID = '"+plan.get_projectid()+"'");
		}
		
		sqlSb.append(" WHERE REPORTID= "+plan.getReportid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjWorkReportEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_WORKREPORT(REPORTID,REPORTSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,0");
		
		if(plan.getReportname() != null && !"".equals(plan.getReportname())) {
			colSb.append(",REPORTNAME");
			valSb.append(",'"+plan.getReportname()+"'");
		}
		
		if(plan.getReporttype() != null && !"".equals(plan.getReporttype())) {
			colSb.append(",REPORTTYPE");
			valSb.append(",'"+plan.getReporttype()+"'");
		}
		
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+plan.getMemo()+"'");
		}
		
		if(plan.getReporttime() != null && !"".equals(plan.getReporttime())) {
			colSb.append(",REPORTTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getReporttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(plan.getReporterid() != null && !"".equals(plan.getReporterid())) {
			colSb.append(",REPORTER");
			valSb.append(",'"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid() != null && !"".equals(plan.getReportdepartmentid())) {
			colSb.append(",REPORTDEPARTMENT");
			valSb.append(",'"+plan.getReportdepartmentid()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			colSb.append(",REPDESC");
			valSb.append(",'"+plan.getRepdesc()+"'");
		}
		if(plan.get_projectid() != null && !"".equals(plan.get_projectid())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.get_projectid()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
