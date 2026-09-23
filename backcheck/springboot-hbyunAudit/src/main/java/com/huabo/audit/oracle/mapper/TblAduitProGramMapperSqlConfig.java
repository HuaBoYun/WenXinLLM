package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblAduitProGramEntity;

import java.math.BigDecimal;

public class TblAduitProGramMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblAduitProGramEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_AUDITPROGRAM WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo,BigDecimal projectId,BigDecimal tempId,BigDecimal targetId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
				+ "LEFT JOIN TBL_NBSJ_TEMPLETE ntemp ON ntemp.templeteId = TNA.TEMPID "
				+ "WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND ntemp.templeteId =  "+tempId);
		}
		
		sb.append(" ORDER BY TNA.TARGETID,TNA.TEMPID ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblAduitProGramEntity> pageInfo, BigDecimal projectId, BigDecimal tempId, BigDecimal targetId) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_AUDITPROGRAM TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(null != targetId) {
			sb.append(" AND TNA.TARGETID =  "+targetId);
		}else {
			sb.append(" AND TNA.TEMPID =  "+tempId);
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblAduitProGramEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_AUDITPROGRAM SET UPDATETIME=TO_DATE('"+DateUtil.parseDate(plan.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss') ");
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			sqlSb.append(" ,BUSINESSTYPE = '"+plan.getBusinessType()+"'");
		}
		if(plan.getRiskSource() != null && !"".equals(plan.getRiskSource())) {
			sqlSb.append(" ,RISKSOURCE = '"+plan.getRiskSource()+"'");
		}
		if(plan.getRiskPoint() != null && !"".equals(plan.getRiskPoint())) {
			sqlSb.append(" ,RISKPOINT = '"+plan.getRiskPoint()+"'");
		}
		if(plan.getControl() != null && !"".equals(plan.getControl())) {
			sqlSb.append(" ,CONTROL = '"+plan.getControl()+"'");
		}
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			sqlSb.append(" ,SUDITPROCESS = '"+plan.getSuditProcess()+"'");
		}
		if(plan.getBioData() != null && !"".equals(plan.getBioData())) {
			sqlSb.append(" ,BIODATA = '"+plan.getBioData()+"'");
		}
		
		
//		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
//			sqlSb.append(" ,TEMPID = '"+plan.getTargetId()+"'");
//		}
//		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
//			sqlSb.append(" ,TARGETID = '"+plan.getTempId()+"'");
//		}
		
		sqlSb.append(" WHERE PROGRAMID= "+plan.getProgramId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblAduitProGramEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID,CREATETIME,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),0");
		
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			colSb.append(",BUSINESSTYPE");
			valSb.append(",'"+plan.getBusinessType()+"'");
		}
		if(plan.getRiskSource() != null && !"".equals(plan.getRiskSource())) {
			colSb.append(",RISKSOURCE");
			valSb.append(",'"+plan.getRiskSource()+"'");
		}
		if(plan.getRiskPoint() != null && !"".equals(plan.getRiskPoint())) {
			colSb.append(",RISKPOINT");
			valSb.append(",'"+plan.getRiskPoint()+"'");
		}
		if(plan.getControl() != null && !"".equals(plan.getControl())) {
			colSb.append(",CONTROL");
			valSb.append(",'"+plan.getControl()+"'");
		}
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			colSb.append(",SUDITPROCESS");
			valSb.append(",'"+plan.getSuditProcess()+"'");
		}
		if(plan.getBioData() != null && !"".equals(plan.getBioData())) {
			colSb.append(",BIODATA");
			valSb.append(",'"+plan.getBioData()+"'");
		}
		
		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
			colSb.append(",TARGETID");
			valSb.append(",'"+plan.getTargetId()+"'");
		}
		if(plan.getTempId() != null && !"".equals(plan.getTempId())) {
			colSb.append(",TEMPID");
			valSb.append(",'"+plan.getTempId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
