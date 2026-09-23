package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjWorkreport;
import com.huabo.audit.oracle.vo.TblNbsjWorkReportVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjWorkReportMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjWorkreport plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_WORKREPORT WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjWorkreport> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo,TblStaffUtil loginStaff)throws Exception {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.REPORTID, TNA.REPORTNAME,TNA.REPORTTIME,TNA.REPORTTYPE,TNA.REPORTMODE,TNA.REPORTSTATUS," +
				"    TNA.REPORTFILE,TNA.MEMO,TNA.ITEMTYPE,TNA.REPDESC,TNA.REPORTERID,TNA.REPORTDEPARTMENTID," +
				"    TNA.PROJECTID,TNA.SECRECTLEVELID,TNA.STAFFSCOPEIDS," +
				"    TNA.STAFFSCOPENAMES,STAFF.REALNAME,STAFF.STAFFID CREATESTAFFID,ORG.ORGNAME "
				+ "FROM TBL_NBSJ_WORKREPORT TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.REPORTERID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.REPORTDEPARTMENTID "
				+ "WHERE 1=1 ");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.REPORTDEPARTMENTID", "TNA.REPORTDEPARTMENTID",
				"TNA.REPORTERID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
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
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjWorkreport> pageInfo,TblNbsjWorkReportVo tblNbsjWorkReportVo,TblStaffUtil loginStaff)throws Exception {
		TblNbsjWorkreport plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_WORKREPORT TNA "
				+ "WHERE 1=1 ");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), true, "TNA.REPORTDEPARTMENTID", "TNA.REPORTDEPARTMENTID",
				"TNA.REPORTERID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
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
	
	public String updateEntity(TblNbsjWorkreport plan) {
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
			sqlSb.append(" ,REPORTERID = '"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid() != null && !"".equals(plan.getReportdepartmentid())) {
			sqlSb.append(" ,REPORTDEPARTMENTID = '"+plan.getReportdepartmentid()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			sqlSb.append(" ,REPDESC = '"+plan.getRepdesc()+"'");
		}
		/*if(plan.get_projectid() != null && !"".equals(plan.get_projectid())) {
			sqlSb.append(" ,PROJECTID = '"+plan.get_projectid()+"'");
		}*/
		
		sqlSb.append(" WHERE REPORTID= "+plan.getReportid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjWorkreport plan){
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
			colSb.append(",REPORTERID");
			valSb.append(",'"+plan.getReporterid()+"'");
		}
		if(plan.getReportdepartmentid() != null && !"".equals(plan.getReportdepartmentid())) {
			colSb.append(",REPORTDEPARTMENTID");
			valSb.append(",'"+plan.getReportdepartmentid()+"'");
		}
		if(plan.getRepdesc() != null && !"".equals(plan.getRepdesc())) {
			colSb.append(",REPDESC");
			valSb.append(",'"+plan.getRepdesc()+"'");
		}
	/*	if(plan.get_projectid() != null && !"".equals(plan.get_projectid())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.get_projectid()+"'");
		}*/
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
