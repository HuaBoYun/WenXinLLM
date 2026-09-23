package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjLeavemeetingEntity;
import com.huabo.audit.oracle.vo.TblNbsjLeavemeetingVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjLeavemeetingMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjLeavemeetingEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_LEAVEMEETING WHERE LEAVECOED = '"+plan.getLeavecoed()+"'");
		if(plan.getLeaveid() != null) {
			sb.append(" AND LEAVEID != "+plan.getLeaveid());
		}
		return sb.toString();
	}
	
	public String selectNbsjLeavemeetingListByPageInfo(PageInfo<TblNbsjLeavemeetingEntity> pageInfo,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.LEAVEID,TNA.LEAVECOED,TNA.LEAVENAME,TNA.CREATRTIME,TNA.CREATESTAFFID,TNA.STATUS "
				+ "FROM TBL_NBSJ_LEAVEMEETING TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjLeavemeetingVo.getProgectid() != null) {
			sb.append(" AND TNA.PROGECTID =  "+tblNbsjLeavemeetingVo.getProgectid());
		}
		
		if(tblNbsjLeavemeetingVo.getLeavecoed()!=null && tblNbsjLeavemeetingVo.getLeavecoed().length()>0) {
			sb.append(" AND TNA.LEAVECOED LIKE '%"+tblNbsjLeavemeetingVo.getLeavecoed()+"%'");
		}
		if(tblNbsjLeavemeetingVo.getLeavename()!=null && tblNbsjLeavemeetingVo.getLeavename().length()>0) {
			sb.append(" AND TNA.LEAVENAME LIKE '%"+tblNbsjLeavemeetingVo.getLeavename()+"%'");
		}
		
		if(tblNbsjLeavemeetingVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjLeavemeetingVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjLeavemeetingVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjLeavemeetingVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.LEAVEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectNbsjLeavemeetingCountByPageInfo(PageInfo<TblNbsjLeavemeetingEntity> pageInfo,TblNbsjLeavemeetingVo tblNbsjLeavemeetingVo) {
//		TblNbsjLeavemeetingEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_LEAVEMEETING TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(tblNbsjLeavemeetingVo.getProgectid() != null) {
			sb.append(" AND TNA.PROGECTID =  "+tblNbsjLeavemeetingVo.getProgectid());
		}
		
		if(tblNbsjLeavemeetingVo.getLeavecoed()!=null && tblNbsjLeavemeetingVo.getLeavecoed().length()>0) {
			sb.append(" AND TNA.LEAVECOED LIKE '%"+tblNbsjLeavemeetingVo.getLeavecoed()+"%'");
		}
		if(tblNbsjLeavemeetingVo.getLeavename()!=null && tblNbsjLeavemeetingVo.getLeavename().length()>0) {
			sb.append(" AND TNA.LEAVENAME LIKE '%"+tblNbsjLeavemeetingVo.getLeavename()+"%'");
		}
		
		if(tblNbsjLeavemeetingVo.getStartDate() !=null){
			sb.append(" AND TNA.CREATRTIME >= TO_DATE('"+tblNbsjLeavemeetingVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjLeavemeetingVo.getEndDate() !=null){
			sb.append(" AND TNA.CREATRTIME <= TO_DATE('"+tblNbsjLeavemeetingVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjLeavemeetingEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_LEAVEMEETING SET LEAVECOED = '"+plan.getLeavecoed()+"'");
		if(plan.getLeavename() != null && !"".equals(plan.getLeavename())) {
			sqlSb.append(" ,LEAVENAME = '"+plan.getLeavename()+"'");
		}
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			sqlSb.append(" ,CONTENT = '"+plan.getContent()+"'");
		}
		sqlSb.append(" WHERE LEAVEID = "+plan.getLeaveid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjLeavemeetingEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_LEAVEMEETING(LEAVEID,CREATRTIME,CREATESTAFFID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreatrtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'), "+plan.getCreatestaffid()+",0");
		
		if(plan.getLeavecoed() != null && !"".equals(plan.getLeavecoed())) {
			colSb.append(",LEAVECOED");
			valSb.append(",'"+plan.getLeavecoed()+"'");
		}
		
		if(plan.getLeavename() != null && !"".equals(plan.getLeavename())) {
			colSb.append(",LEAVENAME");
			valSb.append(",'"+plan.getLeavename()+"'");
		}
		
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			colSb.append(",CONTENT");
			valSb.append(",'"+plan.getContent()+"'");
		}
		
		if(plan.getProject().getProjectId() != null) {
			colSb.append(",PROGECTID");
			valSb.append(",'"+plan.getProject().getProjectId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
