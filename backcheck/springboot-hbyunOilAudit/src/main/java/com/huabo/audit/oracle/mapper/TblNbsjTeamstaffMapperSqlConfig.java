package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjTeamstaffEntity;

public class TblNbsjTeamstaffMapperSqlConfig {

	public String insertEntity(TblNbsjTeamstaffEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_TEAMSTAFF(ID,TEAMID,STAFFID,STAFFTYPE");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+plan.getTeamid()+","+plan.getStaffid()+","+plan.getStafftype()+"");
		
//		if(plan.getTeamName() != null && !"".equals(plan.getTeamName())) {
//			colSb.append(",TEAMNAME");
//			valSb.append(",'"+plan.getTeamName()+"'");
//		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String updateEntity(TblNbsjTeamstaffEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_TEAMSTAFF SET STAFFID='"+plan.getStaffid()+"' ");
//		if(plan.getPprojectName() != null && !"".equals(plan.getPprojectName())) {
//			sqlSb.append(" ,PPROJECTNAME = '"+plan.getPprojectName()+"'");
//		}
		
		sqlSb.append(" WHERE ID= "+plan.getId());
		return sqlSb.toString();
	}
}
