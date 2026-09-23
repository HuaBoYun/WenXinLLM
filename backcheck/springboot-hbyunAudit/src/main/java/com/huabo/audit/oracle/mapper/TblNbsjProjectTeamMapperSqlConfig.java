package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjProjectTeamEntity;

import java.math.BigDecimal;

public class TblNbsjProjectTeamMapperSqlConfig {
	
	public String insertEntity(TblNbsjProjectTeamEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_PROJECTTEAM(TEAMID,CREATETIME,CREATEUSERID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getCreateUserId()+"");
		
		if(plan.getTeamName() != null && !"".equals(plan.getTeamName())) {
			colSb.append(",TEAMNAME");
			valSb.append(",'"+plan.getTeamName()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String updateEntity(TblNbsjProjectTeamEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROJECTTEAM SET TEAMNAME='"+plan.getTeamName()+"' ");
//		if(plan.getPprojectName() != null && !"".equals(plan.getPprojectName())) {
//			sqlSb.append(" ,PPROJECTNAME = '"+plan.getPprojectName()+"'");
//		}
		
		sqlSb.append(" WHERE TEAMID= "+plan.getTeamId());
		return sqlSb.toString();
	}
	
	public String insertProTeam(BigDecimal teamId, BigDecimal projectid){
		
		return "INSERT INTO TBL_NBSJ_PRO_TEAM(TEAMID,PROJECTID) VALUES ("+teamId+","+projectid+")";
		
//		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_PROJECTTEAM(TEAMID,CREATETIME,CREATEUSERID");
//		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getCreateUserId()+"");
//		
//		if(plan.getTeamName() != null && !"".equals(plan.getTeamName())) {
//			colSb.append(",TEAMNAME");
//			valSb.append(",'"+plan.getTeamName()+"'");
//		}
//		
//		String sql = colSb.toString()+")"+valSb.toString()+")";
//		return sql;
	}
	
}
