package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblNbsjQuestionEntity;
import com.huabo.audit.oracle.vo.TblNbsjQuestionVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjQuestionMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjQuestionEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_QUESTION WHERE 1=1 ");
//		if(plan.getPlanid() != null) {
//			sb.append(" AND PLANID != "+plan.getPlanid());
//		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) {
//		TblNbsjQuestionEntity plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,SHEET.BUSINESSAFFILIATION,SHEET.AUDITDISCOVERABLE,SHEET.SHEETCODE,STAFF.REALNAME, ORG.ORGNAME,SHEET.HZDG,SHEET.ORGIDS,SHEET.ORGIDNAMES "
				+ "FROM TBL_NBSJ_QUESTION TNA "
				+ "LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(null != projectId) {
			sb.append(" AND SHEET.PROJECTID = "+projectId+"");
		}
		
		if(tblNbsjQuestionVo.getStatus() != null && !"".equals(tblNbsjQuestionVo.getStatus()) ) {
			sb.append(" AND TNA.STATUS =  "+tblNbsjQuestionVo.getStatus());
		}
		
		if(tblNbsjQuestionVo.getRecStatus() != null && !"".equals(tblNbsjQuestionVo.getRecStatus())  ) {
			sb.append(" AND TNA.RECSTATUS =  "+tblNbsjQuestionVo.getRecStatus());
		}
		
		if(tblNbsjQuestionVo.getBusinessAffiliation()!=null && tblNbsjQuestionVo.getBusinessAffiliation().length()>0) {
			sb.append(" AND BUSINESSAFFILIATION LIKE '%"+tblNbsjQuestionVo.getBusinessAffiliation()+"%'");
		}
		
		if(tblNbsjQuestionVo.getFindPeople()!=null && tblNbsjQuestionVo.getFindPeople().length()>0) {
			sb.append(" AND STAFF.STAFFID = "+tblNbsjQuestionVo.getFindPeople()+"");
		}
		
		sb.append(" ORDER BY TNA.QUESTIONID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) {
//		TblNbsjQuestionEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_QUESTION TNA "
				+ "LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
				+ "WHERE 1=1 ");
		
		if(null != projectId) {
			sb.append(" AND SHEET.PROJECTID = "+projectId+"");
		}
		
		if(tblNbsjQuestionVo.getStatus() != null && !"".equals(tblNbsjQuestionVo.getStatus()) ) {
			sb.append(" AND TNA.STATUS =  "+tblNbsjQuestionVo.getStatus());
		}
		
		if(tblNbsjQuestionVo.getRecStatus() != null && !"".equals(tblNbsjQuestionVo.getRecStatus())  ) {
			sb.append(" AND TNA.RECSTATUS =  "+tblNbsjQuestionVo.getRecStatus());
		}
		
		if(tblNbsjQuestionVo.getBusinessAffiliation()!=null && tblNbsjQuestionVo.getBusinessAffiliation().length()>0) {
			sb.append(" AND BUSINESSAFFILIATION LIKE '%"+tblNbsjQuestionVo.getBusinessAffiliation()+"%'");
		}
		
		if(tblNbsjQuestionVo.getFindPeople()!=null && tblNbsjQuestionVo.getFindPeople().length()>0) {
			sb.append(" AND STAFF.STAFFID = "+tblNbsjQuestionVo.getFindPeople()+"");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjQuestionEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_QUESTION SET SHEETID = "+plan.getSheetId());
//		if(plan.getSheetName() != null && !"".equals(plan.getSheetName())) {
//			sqlSb.append(" ,SHEETNAME = '"+plan.getSheetName()+"'");
//		}
		
		
		if(plan.getStatus() != null ) {
			sqlSb.append(" ,status = "+plan.getStatus());
		}
		
		if(plan.getRecStatus() != null ) {
			sqlSb.append(" ,recStatus = "+plan.getRecStatus());
		}
		
		sqlSb.append(" WHERE QUESTIONID= "+plan.getQuestionId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjQuestionEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_QUESTION(QUESTIONID,recStatus");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,0");
		
		if(plan.getStatus() != null && !"".equals(plan.getStatus())) {
			colSb.append(",STATUS");
			valSb.append(",'"+plan.getStatus()+"'");
		}
		if(plan.getGroupStatus() != null && !"".equals(plan.getGroupStatus())) {
			colSb.append(",groupStatus");
			valSb.append(",'"+plan.getGroupStatus()+"'");
		}
		if(plan.getNbsjSheet().getSheetId() != null && !"".equals(plan.getNbsjSheet().getSheetId())) {
			colSb.append(",SHEETID");
			valSb.append(",'"+plan.getNbsjSheet().getSheetId()+"'");
		}
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	
	
	
	
	
	
	
	public String selectConfirmationListByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,SHEET.BUSINESSAFFILIATION,SHEET.AUDITDISCOVERABLE,SHEET.SHEETCODE,STAFF.REALNAME, ORG.ORGNAME "
				+ "FROM TBL_NBSJ_QUESTION TNA "
				+ "LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 "
				+ " AND TNA.STATUS <> '2'"
//				+ " and SHEET.STATE=4 and TNA.STATUS!=2  AND SHEET.RISKLEVEL='是' "
//				+ " AND QUESTIONID not in (SELECT QUESTIONID FROM TBL_NBSJ_QUESTIONAFFIRM "
//				+ " where FACTID IN (SELECT FACTID  FROM TBL_NBSJ_FACTBOOK WHERE PROJECTID = "+projectId+") )"
				+ "");
		
		if(null != projectId) {
			sb.append(" AND SHEET.PROJECTID = "+projectId+"");
		}
		if(tblNbsjQuestionVo.getBusinessAffiliation()!=null && tblNbsjQuestionVo.getBusinessAffiliation().length()>0) {
			sb.append(" AND SHEET.BUSINESSAFFILIATION LIKE '%"+tblNbsjQuestionVo.getBusinessAffiliation()+"%'");
		}
		if(tblNbsjQuestionVo.getFindPeople()!=null && tblNbsjQuestionVo.getFindPeople().length()>0) {
			sb.append(" AND STAFF.STAFFID = "+tblNbsjQuestionVo.getFindPeople()+"");
		}
		sb.append(" ORDER BY TNA.QUESTIONID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectConfirmationCountByPageInfo(PageInfo<TblNbsjQuestionEntity> pageInfo,TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) {
//		TblNbsjQuestionEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_QUESTION TNA "
				+ "LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
				+ "WHERE 1=1 "
				+ " and SHEET.STATE=6 and TNA.STATUS!=2  AND SHEET.RISKLEVEL='是'  AND QUESTIONID not in (SELECT QUESTIONID FROM TBL_NBSJ_QUESTIONAFFIRM where FACTID IN (SELECT FACTID  FROM TBL_NBSJ_FACTBOOK WHERE PROJECTID = "+projectId+") )"
				+ "");
		if(null != projectId) {
			sb.append(" AND SHEET.PROJECTID = "+projectId+"");
		}
		if(tblNbsjQuestionVo.getBusinessAffiliation()!=null && tblNbsjQuestionVo.getBusinessAffiliation().length()>0) {
			sb.append(" AND BUSINESSAFFILIATION LIKE '%"+tblNbsjQuestionVo.getBusinessAffiliation()+"%'");
		}
		if(tblNbsjQuestionVo.getFindPeople()!=null && tblNbsjQuestionVo.getFindPeople().length()>0) {
			sb.append(" AND STAFF.STAFFID = "+tblNbsjQuestionVo.getFindPeople()+"");
		}
		return sb.toString();
	}
	
	
	
	//==
	public String selectConfirmationListByFact(PageInfo<TblNbsjQuestionEntity> pageInfo,Integer factid) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(select SHEET.*,QUE.QUESTIONID from TBL_NBSJ_SHEET SHEET "
				+ "LEFT JOIN TBL_NBSJ_QUESTION QUE ON SHEET.SHEETID = QUE.SHEETID "
				+ "where SHEET.SHEETID in (select SHEETID from TBL_NBSJ_QUESTIONAFFIRM f left join TBL_NBSJ_QUESTION q on f.QUESTIONID = q.QUESTIONID where q.QUESTIONID is not null and f.FACTID = "+factid+") ");
		
		sb.append(" ORDER BY SHEET.SHEETID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	
	public String getNbsjQuestList(TblNbsjQuestionVo tblNbsjQuestionVo,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT P.PPROJECTNAME projectname,SHEET.SHEETCODE,SHEET.SHEETNAME,ORG.ORGNAME,SHEET.businessAffiliation,SHEET.TargetName,SHEET.BusinessType,SHEET.QuesTitle,SHEET.SuditProcess,SHEET.Sjbwl,STAFF.REALNAME,TNA.RECSTATUS  as sfssqr,TNA.STATUS as sfzg"
				+ " FROM TBL_NBSJ_QUESTION TNA   LEFT JOIN TBL_NBSJ_SHEET SHEET ON SHEET.SHEETID = TNA.SHEETID  "
                + " LEFT JOIN TBL_NBSJ_project p on p.projectid=SHEET.projectid "
                + " LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = SHEET.AUDITORG  "
                + " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = SHEET.CREATESTAFF "
				+ " WHERE 1=1 ");
		if(null != projectId) {
			sb.append(" AND SHEET.PROJECTID = "+projectId+"");
		}
		
		if(tblNbsjQuestionVo.getStatus() != null && !"".equals(tblNbsjQuestionVo.getStatus()) ) {
			sb.append(" AND TNA.STATUS =  "+tblNbsjQuestionVo.getStatus());
		}
		
		if(tblNbsjQuestionVo.getRecStatus() != null && !"".equals(tblNbsjQuestionVo.getRecStatus())  ) {
			sb.append(" AND TNA.RECSTATUS =  "+tblNbsjQuestionVo.getRecStatus());
		}
		
		if(tblNbsjQuestionVo.getBusinessAffiliation()!=null && tblNbsjQuestionVo.getBusinessAffiliation().length()>0) {
			sb.append(" AND BUSINESSAFFILIATION LIKE '%"+tblNbsjQuestionVo.getBusinessAffiliation()+"%'");
		}
		
		if(tblNbsjQuestionVo.getFindPeople()!=null && tblNbsjQuestionVo.getFindPeople().length()>0) {
			sb.append(" AND STAFF.STAFFID = "+tblNbsjQuestionVo.getFindPeople()+"");
		}
		
		sb.append(" ORDER BY TNA.QUESTIONID DESC ");
		return sb.toString();
	}
	
}
