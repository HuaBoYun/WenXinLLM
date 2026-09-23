package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;

public class TblNbsjStaffscoreMapperSqlConfig  { 
	
public String   selectNbsjStaffscoreByPageCount( BigDecimal orgId, TblNbsjStaffscore re,TblStaffUtil loginStaff) throws Exception {
		
		StringBuffer sb = new StringBuffer("select count(*) from ( SELECT * FROM TBL_NBSJ_STAFFSCORE where 1=1   " );
		

		if(re.getAuditors() != null ) {
			sb.append("  and auditors = "+re.getAuditors());
		}
		
		if(re.getAuditProjectName() != null && !"".equals(re.getAuditProjectName())) {
			sb.append("  and auditProjectName like '%"+re.getAuditProjectName()+"%'");
		}
		
		if(orgId!=null) {
			sb.append("  and unit = '"+orgId+"'");
		}
		
		sb.append(" ) ORDER BY staffScore_id  ");
		return sb.toString();
	}
	
	
	public String   selectNbsjStaffscoreByPageInfo(PageInfo<TblNbsjStaffscore> pageInfo, BigDecimal orgId, TblNbsjStaffscore re,TblStaffUtil loginStaff) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (select * from ( SELECT SS.STAFFSCORE_ID,STA.REALNAME,SS.AUDITORS,"
				+ "SS.AUDITPROJECTNAME,SS.TOTALSCORE,SS.PROJECTID,ss.STATUS,SS.SECRECTLEVELID,SS.STAFFSCOPEIDS,SS.STAFFSCOPENAMES,SS.CREATESTAFFID "
				+ "FROM TBL_NBSJ_STAFFSCORE ss LEFT JOIN TBL_STAFF sta ON SS.auditors = sta.STAFFID where 1=1   " );
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "SS.UNIT", "SS.UNIT",
				"SS.CREATESTAFFID", "SS.SECRECTLEVELID", "SS.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));

		if(re.getAuditors() != null ) {
			sb.append("  and auditors = "+re.getAuditors());
		}
		
		if(re.getAuditProjectName() != null && !"".equals(re.getAuditProjectName())) {
			sb.append("  and auditProjectName like '%"+re.getAuditProjectName()+"%'");
		}
		
		if(orgId!=null) {
			sb.append("  and unit = '"+orgId+"'");
		}
		
		sb.append(" ) ORDER BY staffScore_id ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	
	
	public String insertEntity(TblNbsjStaffscore re){
		StringBuffer colSb = new StringBuffer("insert into TBL_NBSJ_STAFFSCORE (staffScore_id,unit,assessmentResults,auditors,auditProjectName,totalScore,PROJECTID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+re.getUnit()+",'"+re.getAssessmentResults()+"',"+re.getAuditors()+",'"+re.getAuditProjectName()+"','"+re.getTotalScore()+"',"+re.getProjectid()+",0");
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	public String updateEntity(TblNbsjStaffscore re){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_STAFFSCORE  SET staffScore_id=staffScore_id ");
		if(re.getAssessmentResults()!= null && !"".equals(re.getAssessmentResults().toString())) {
			colSb.append(" ,assessmentResults = '"+re.getAssessmentResults()+"'");
		}
		
		if(re.getStatus()!= null && !"".equals(re.getStatus().toString())) {
			colSb.append(" ,STATUS = "+re.getStatus());
		}
		
		
		if(re.getAuditors() != null ) {
			colSb.append(" ,auditors = "+re.getAuditors());
		}
		
		if(re.getAuditProjectName() != null && !"".equals(re.getAuditProjectName())) {
			colSb.append(" ,auditProjectName = '"+re.getAuditProjectName()+"'");
		}
		
		if(re.getTotalScore() != null && !"".equals(re.getTotalScore())) {
			colSb.append(" ,totalScore = '"+re.getTotalScore()+"'");
		}
		if(re.getProjectid() != null ) {
			colSb.append(" ,PROJECTID = "+re.getProjectid());
		}
		
		
		
		colSb.append(" WHERE staffScore_id = "+re.getStaffScoreid());
		return colSb.toString();
	}
	
	
	
public String   selectNbsjStaffscoreList(BigDecimal orgId, TblNbsjStaffscore re) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT SS.STAFFSCORE_ID,STA.REALNAME,SS.AUDITORS,SS.AUDITPROJECTNAME,SS.TOTALSCORE,SS.PROJECTID,ss.STATUS FROM TBL_NBSJ_STAFFSCORE ss LEFT JOIN TBL_STAFF sta ON SS.auditors = sta.STAFFID where ss.status=3  " );
		if(re.getAuditors() != null ) {
			sb.append("  and auditors = "+re.getAuditors());
		}
		if(orgId!=null) {
			sb.append("  and unit = '"+orgId+"'");
		}
		sb.append(" ORDER BY staffScore_id ");
		return sb.toString();
	}
	
}
