package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;

public class TblNbsjStaffscoreDetailsMapperSqlconfig  {
	
	public String insertEntity(TblNbsjStaffscoreDetails re){
		StringBuffer colSb = new StringBuffer("insert into TBL_NBSJ_STAFFSCORE_DETAILS (staffScore_details_id, staffScore_id,Project,Consideration,Grading,AuditTeamLeaderScore,Remark");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+re.getStaffScore_id()+",'"+re.getProject()+"','"+re.getConsideration()+"','"+re.getGrading()+"','"+re.getAuditTeamLeaderScore()+"','"+re.getRemark()+"'");
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	public String updateEntity(TblNbsjStaffscoreDetails re){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_STAFFSCORE_DETAILS set staffScore_details_id=staffScore_details_id ");
		
		if(re.getAuditTeamLeaderScore()!= null && !"".equals(re.getAuditTeamLeaderScore().toString())) {
			colSb.append(" ,AuditTeamLeaderScore = '"+re.getAuditTeamLeaderScore()+"'");
		}
		
		if(re.getRemark()!= null && !"".equals(re.getRemark().toString())) {
			colSb.append(" ,Remark = '"+re.getRemark()+"'");
		}
		
		
		if(re.getConsideration()!= null && !"".equals(re.getConsideration().toString())) {
			colSb.append(" ,Consideration = '"+re.getConsideration()+"'");
		}
		
		if(re.getGrading()!= null && !"".equals(re.getGrading().toString())) {
			colSb.append(" ,Grading = '"+re.getGrading()+"'");
		}
		
		if(re.getProject()!= null && !"".equals(re.getProject().toString())) {
			colSb.append(" ,Project = '"+re.getProject()+"'");
		}
		colSb.append(" WHERE staffScore_details_id = "+re.getStaffScore_details_id());
		return colSb.toString();
	}
	
}
