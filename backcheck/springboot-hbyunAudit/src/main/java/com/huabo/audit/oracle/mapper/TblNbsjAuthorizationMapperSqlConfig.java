package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjAuthorizationEntity;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;

public class TblNbsjAuthorizationMapperSqlConfig {

	public String insertEntity(TblNbsjAuthorizationEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUTHORIZATION(AUTHID,AUTHSTAFFID,TEAMSTAFFID,AUTHTIME");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+plan.getTeamStaff().getStaffid()+","+plan.getTeamStaff().getId()+",TO_DATE('"+DateUtil.parseDate(plan.getAuthTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss') ");
		
		if(plan.getProject().getProjectId() != null && !"".equals(plan.getProject().getProjectId())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProject().getProjectId()+"'");
		}
		
		if(plan.getAduitProGram().getProgramId() != null && !"".equals(plan.getAduitProGram().getProgramId())) {
			colSb.append(",PROGRAMID");
			valSb.append(",'"+plan.getAduitProGram().getProgramId()+"'");
		}
		
//		if(plan.getw.getContent() != null && !"".equals(plan.getContent())) {
//			colSb.append(",WASTETIME");
//			valSb.append(",'"+plan.getContent()+"'");
//		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String updateEntity(TblNbsjAuthorizationEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_AUTHORIZATION SET AUTHTIME =TO_DATE('"+DateUtil.parseDate(plan.getAuthTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss') ");
		if(plan.getTeamStaff().getStaffid()!= null && !"".equals(plan.getTeamStaff().getStaffid())) {
			sqlSb.append(",TEAMSTAFFID = '"+plan.getTeamStaff().getId()+"'");
		}
		sqlSb.append(" WHERE authId= "+plan.getAuthId());
		return sqlSb.toString();
	}
}
