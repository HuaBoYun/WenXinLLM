package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.Date;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;

import cn.hutool.core.util.StrUtil;

public class TblNbsjAuditplanMapperSqlConfig {
	
	
	public String selectAuditPlanListView(PageInfo<TblNbsjAuditplan> pageInfo) throws Exception {
		TblNbsjAuditplan plan = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("SELECT PLANID,PLANCODE,PLANNAME,PALNYEAR,"
				+ "(SELECT COUNT(0) FROM TBL_NBSJ_PROJECT WHERE PLANID = T2.PLANID) TOTALITEM,"
				+ "(SELECT COUNT(0) FROM TBL_NBSJ_PROJECT WHERE PLANID = T2.PLANID AND STATUS = 0) UNENFORCEDITEM,"
				+ "(SELECT COUNT(0) FROM TBL_NBSJ_PROJECT WHERE PLANID = T2.PLANID AND STATUS IN (1,2)) CONDUCTITEM,"
				+ "(SELECT COUNT(0) FROM TBL_NBSJ_PROJECT WHERE PLANID = T2.PLANID AND STATUS IN (3,4)) COMPLETEITEM "
				+ "FROM (SELECT PLANID,PLANCODE,PLANNAME,PALNYEAR,ROWNUM RN FROM (SELECT TNAP.PLANID,TNAP.PLANCODE,TNAP.PLANNAME,PALNYEAR FROM TBL_NBSJ_AUDITPLAN TNAP WHERE TNAP.AUDITORGID = "+plan.getAuditorgid());
		if(plan.getPlanname() != null && !"".equals(plan.getPlanname())) {
			sqlSb.append(" AND PLANNAME LIKE '%"+plan.getPlanname()+"%'");
		}
		if(plan.getPalnyear() != null && !"".equals(plan.getPalnyear())) {
			sqlSb.append(" AND PALNYEAR = '"+plan.getPalnyear()+"'");
		}
		sqlSb.append(" ORDER BY PLANID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectAuditPlanCountView(PageInfo<TblNbsjAuditplan> pageInfo) throws Exception {
		TblNbsjAuditplan plan = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_NBSJ_AUDITPLAN TNAP WHERE TNAP.AUDITORGID = "+plan.getAuditorgid());
		if(plan.getPlanname() != null && !"".equals(plan.getPlanname())) {
			sqlSb.append(" AND PLANNAME LIKE '%"+plan.getPlanname()+"%'");
		}
		if(plan.getPalnyear() != null && !"".equals(plan.getPalnyear())) {
			sqlSb.append(" AND PALNYEAR = '"+plan.getPalnyear()+"'");
		}
		return sqlSb.toString();
	}
	
	
	public String selectAuditPlanByIds(String planId, boolean stateFlag) {
		String sql=" SELECT * from TBL_NBSJ_AUDITPLAN where PLANID in ("+planId+")";
		if(stateFlag) {
			sql += " AND OPINIONSTATUS = 0";
		}
		sql += " ORDER BY PLANID desc";
		return sql;
	}
	
	public String updateEntity(TblNbsjAuditplan plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_AUDITPLAN SET UPDATETIMR = TO_DATE('"+DateUtil.parseDate(new Date(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		if(plan.getPlancode() != null && !"".equals(plan.getPlancode())) {
			sqlSb.append(" ,PLANCODE = '"+plan.getPlancode()+"'");
		}
		if(plan.getPlanname() != null && !"".equals(plan.getPlanname())) {
			sqlSb.append(" ,PLANNAME = '"+plan.getPlanname()+"'");
		}
		if(plan.getPalnyear() != null && !"".equals(plan.getPalnyear())) {
			sqlSb.append(" ,PALNYEAR = '"+plan.getPalnyear()+"'");
		}
		if(plan.getPlantype() != null && !"".equals(plan.getPlantype())) {
			sqlSb.append(" ,PLANTYPE = '"+plan.getPlantype()+"'");
		}
		if(plan.getAuditorgid() != null) {
			sqlSb.append(" ,AUDITORGID = '"+plan.getAuditorgid()+"'");
		}
		if(plan.getPalncost() != null) {
			sqlSb.append(" ,PALNCOST = '"+plan.getPalncost()+"'");
		}
		if(plan.getStarttime() != null) {
			sqlSb.append(" ,STARTTIME = TO_DATE('"+DateUtil.parseDate(plan.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getEndtime() != null) {
			sqlSb.append(" ,ENDTIME = TO_DATE('"+DateUtil.parseDate(plan.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getPrincipalid() != null) {
			sqlSb.append(" ,PRINCIPALID = '"+plan.getPrincipalid()+"'");
		}
		if(plan.getLeaderid() != null) {
			sqlSb.append(" ,LEADERID = '"+plan.getLeaderid()+"'");
		}
		if(plan.getStatus() != null) {
			sqlSb.append(" ,STATUS = '"+plan.getStatus()+"'");
		}
		if(plan.getOpinionstatus() != null) {
			sqlSb.append(" ,OPINIONSTATUS = '"+plan.getOpinionstatus()+"'");
		}
		if(plan.getRemarks() != null && !"".equals(plan.getRemarks())) {
			sqlSb.append(" ,REMARKS = '"+plan.getRemarks()+"'");
		}
		if(plan.getIsauditor() != null && !"".equals(plan.getIsauditor())) {
			sqlSb.append(" ,ISAUDITOR = '"+plan.getIsauditor()+"'");
		}
		sqlSb.append(" WHERE PLANID = "+plan.getPlanid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjAuditplan plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDITPLAN(PLANID,CREATESTAFFID,CREATETIME,STATUS,OPINIONSTATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+plan.getCreatestaffid()+",TO_DATE('"+DateUtil.parseDate(plan.getCreatetime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0,0");
		if(plan.getPlancode() != null && !"".equals(plan.getPlancode())) {
			colSb.append(",PLANCODE");
			valSb.append(",'"+plan.getPlancode()+"'");
		}
		
		if(plan.getPlanname() != null && !"".equals(plan.getPlanname())) {
			colSb.append(",PLANNAME");
			valSb.append(",'"+plan.getPlanname()+"'");
		}
		
		if(plan.getPalnyear() != null && !"".equals(plan.getPalnyear())) {
			colSb.append(",PALNYEAR");
			valSb.append(",'"+plan.getPalnyear()+"'");
		}
		if(plan.getPlantype() != null && !"".equals(plan.getPlantype())) {
			colSb.append(",PLANTYPE");
			valSb.append(",'"+plan.getPlantype()+"'");
		}
		if(plan.getAuditorgid() != null) {
			colSb.append(",AUDITORGID");
			valSb.append(","+plan.getAuditorgid());
		}
		if(plan.getPalncost() != null) {
			colSb.append(",PALNCOST");
			valSb.append(",'"+plan.getPalncost()+"'");
		}
		
		if(plan.getStarttime() != null) {
			colSb.append(",STARTTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getStarttime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(plan.getEndtime() != null) {
			colSb.append(",ENDTIME");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getEndtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		if(plan.getPrincipalid() != null) {
			colSb.append(",PRINCIPALID");
			valSb.append(",'"+plan.getPrincipalid()+"'");
		}
		
		if(plan.getLeaderid() != null) {
			colSb.append(",LEADERID");
			valSb.append(",'"+plan.getLeaderid()+"'");
		}
		
		if(plan.getRemarks() != null && !"".equals(plan.getRemarks())) {
			colSb.append(",REMARKS");
			valSb.append(",'"+plan.getRemarks()+"'");
		}
		if(plan.getIsauditor() != null && !"".equals(plan.getIsauditor())) {
			colSb.append(",ISAUDITOR");
			valSb.append(",'"+plan.getIsauditor()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String selectPlanCodeByOrgid(TblNbsjAuditplan plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_AUDITPLAN WHERE PLANCODE = '"+plan.getPlancode()+"' AND AUDITORGID = "+plan.getAuditorgid());
		if(plan.getPlanid() != null) {
			sb.append(" AND PLANID != "+plan.getPlanid());
		}
		return sb.toString();
	}
	
	public String selectNbsjAuditPlanListByPageInfo(PageInfo<TblNbsjAuditplan> pageInfo, String planStartDate, String planEndDate, BigDecimal orgId, TblNbsjAuditPlanVo plan) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.PLANID,TNA.PLANCODE,TNA.PLANNAME,TNA.PALNYEAR,TNA.PLANTYPE,TNA.PALNCOST,TNA.STARTTIME,TNA.ENDTIME,TNA.REMARKS,"
				+ "TNA.CREATETIME,TNA.STATUS,TNA.OPINIONSTATUS,TNA.ISAUDITOR,PRINCIPAL.STAFFID AS PRISTAFFID,PRINCIPAL.REALNAME AS PRIREALNAME,"
				+ "LEADER.STAFFID LEASTAFFID,LEADER.REALNAME LEAREALNAME,AUDITORG.ORGID AUDORGID,AUDITORG.ORGNAME AUDORGNAME,"
				+ "CRESTAFF.STAFFID CRESTAFFID,CRESTAFF.REALNAME CREREALNAME "
				+ "FROM TBL_NBSJ_AUDITPLAN TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE TNA.AUDITORGID = " +orgId);
		
		if(plan.getPrincipalid() != null) {
			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
		}
		if(plan.getPlancode()!=null && plan.getPlancode().length()>0) { 
			sb.append(" AND TNA.PLANCODE LIKE '%"+plan.getPlancode()+"%'");
		}
		
		if(plan.getPlanname()!=null && plan.getPlanname().length()>0){
			sb.append(" AND TNA.PLANNAME LIKE '%"+plan.getPlanname()+"%'");
		}
		if(plan.getPalnyear()!=null && plan.getPalnyear().length()>0){
			sb.append(" AND TNA.PALNYEAR LIKE '"+plan.getPalnyear()+"%'");
		}
		
		if(StrUtil.isNotBlank(planStartDate)){
			sb.append(" AND TNA.STARTTIME >= TO_DATE('"+planStartDate+"','yyyy-MM-dd')");
		}
		
		if(StrUtil.isNotBlank(planEndDate)){
			sb.append(" AND TNA.ENDTIME <= TO_DATE('"+planEndDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.PLANID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	
	public String selectNbsjAuditPlanCountByPageInfo(PageInfo<TblNbsjAuditplan> pageInfo, String planStartDate,String planEndDate, BigDecimal orgid, TblNbsjAuditPlanVo plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_NBSJ_AUDITPLAN TNA LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID WHERE TNA.AUDITORGID = " +orgid);
		
		if(plan.getPrincipalid() != null) {
			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
		}
		if(plan.getPlancode()!=null && plan.getPlancode().length()>0) {
			sb.append(" AND TNA.PLANCODE LIKE '%"+plan.getPlancode()+"%'");
		}
		
		if(plan.getPlanname()!=null && plan.getPlanname().length()>0){
			sb.append(" AND TNA.PLANNAME LIKE '%"+plan.getPlanname()+"%'");
		}
		if(plan.getPalnyear()!=null && plan.getPalnyear().length()>0){
			sb.append(" AND TNA.PALNYEAR LIKE '"+plan.getPalnyear()+"%'");
		}
		if(StrUtil.isNotBlank(planStartDate)){
			sb.append(" AND TNA.STARTTIME >= TO_DATE('"+planStartDate+"','yyyy-MM-dd')");
		}
		if(StrUtil.isNotBlank(planEndDate)){
			sb.append(" AND TNA.ENDTIME <= TO_DATE('"+planEndDate+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		return sb.toString();
	}
	
}
