package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;

public class TblNbsjBorrowRecordMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjBorrowRecordEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_BORROWRECORD WHERE 1=1 ");
		if(plan.getBorrowid() != null) {
			sb.append(" AND BORROWID != "+plan.getBorrowid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjBorrowRecordEntity> pageInfo,Integer projectid) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.BORROWID,TNA.CREATEDATE,TNA.RETURNDATE,TNA.MEMO,staff.realname "
				+ "FROM TBL_NBSJ_BORROWRECORD TNA "
				+ "LEFT JOIN TBL_STAFF staff ON staff.STAFFID = TNA.STAFFID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE TNA.PROJECTID= "+projectid);
		
//		if(plan.getPrincipalid() != null) {
//			sb.append(" AND TNA.PRINCIPALID =  "+plan.getPrincipalid());
//		}
		
		sb.append(" ORDER BY TNA.BORROWID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjBorrowRecordEntity> pageInfo,Integer projectid) {
//		TblNbsjBorrowRecordEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_BORROWRECORD TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE TNA.PROJECTID= "+projectid);
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjBorrowRecordEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_BORROWRECORD SET ");
		if(plan.getMemo()!= null && !"".equals(plan.getMemo())) {
			sqlSb.append(" MEMO = '"+plan.getMemo()+"'");
		}
		if(plan.getBorrowDate() != null && !"".equals(plan.getBorrowDate())) {
			sqlSb.append(", CREATEDATE = TO_DATE('"+plan.getBorrowDate()+"','yyyy-MM-dd HH:MI:SS')");
		}
		if(plan.getBackDate() != null && !"".equals(plan.getBackDate())) {
			sqlSb.append(", RETURNDATE = TO_DATE('"+plan.getBackDate()+"','yyyy-MM-dd HH:MI:SS')");
		}
		sqlSb.append(" WHERE BORROWID= "+plan.getBorrowid());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjBorrowRecordEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_BORROWRECORD(BORROWID,STAFFID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+plan.getStaffid()+",'0'");
		
		if(plan.getProjectid() != null && !"".equals(plan.getProjectid())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectid()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+plan.getMemo()+"'");
		}
		if(plan.getBorrowDate() != null && !"".equals(plan.getBorrowDate())) {
			colSb.append(",CREATEDATE");
			valSb.append(",TO_DATE('"+plan.getBorrowDate()+"','yyyy-MM-dd')");
		}
		if(plan.getBackDate() != null && !"".equals(plan.getBackDate())) {
			colSb.append(",RETURNDATE");
			valSb.append(",TO_DATE('"+plan.getBackDate()+"','yyyy-MM-dd')");
		}
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
