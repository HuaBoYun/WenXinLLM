package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjProposalEntity;
import com.huabo.audit.oracle.vo.TblNbsjProposalVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjProposalMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjProposalEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_PROPOSAL WHERE 1=1 AND PROCODE='"+plan.getProcode()+"' ");
		if(plan.getProid() != null) {
			sb.append(" AND PROID != "+plan.getProid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjProposalEntity> pageInfo,TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) {
//		TblNbsjProposalEntity plan = pageInfo.getCondition();
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,STAFF.REALNAME "
				+ "FROM TBL_NBSJ_PROPOSAL TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATESTAFFID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(projectId != null) {
			sb.append(" AND TNA.PROJECTID =  "+projectId);
		}
		if(tblNbsjProposalVo.getProcode()!=null && tblNbsjProposalVo.getProcode().length()>0) {
			sb.append(" AND TNA.PROCODE LIKE '%"+tblNbsjProposalVo.getProcode()+"%'");
		}
		
		if(tblNbsjProposalVo.getProname()!=null && tblNbsjProposalVo.getProname().length()>0) {
			sb.append(" AND TNA.PRONAME LIKE '%"+tblNbsjProposalVo.getProname()+"%'");
		}
		
		if(tblNbsjProposalVo.getStartDate() !=null && tblNbsjProposalVo.getStartDate().length()>0){
			sb.append(" AND TNA.CREATETIME >= TO_DATE('"+tblNbsjProposalVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjProposalVo.getEndDate() !=null && tblNbsjProposalVo.getEndDate().length()>0){
			sb.append(" AND TNA.CREATETIME <= TO_DATE('"+tblNbsjProposalVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		sb.append(" ORDER BY TNA.PROID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjProposalEntity> pageInfo,TblNbsjProposalVo tblNbsjProposalVo,Integer projectId) {
//		TblNbsjProposalEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_PROPOSAL TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(projectId != null) {
			sb.append(" AND TNA.PROJECTID =  "+projectId);
		}
		
		if(tblNbsjProposalVo.getProcode()!=null && tblNbsjProposalVo.getProcode().length()>0) {
			sb.append(" AND TNA.PROCODE LIKE '%"+tblNbsjProposalVo.getProcode()+"%'");
		}
		
		if(tblNbsjProposalVo.getProname()!=null && tblNbsjProposalVo.getProname().length()>0) {
			sb.append(" AND TNA.PRONAME LIKE '%"+tblNbsjProposalVo.getProname()+"%'");
		}
		
		if(tblNbsjProposalVo.getStartDate() !=null && tblNbsjProposalVo.getStartDate().length()>0){
			sb.append(" AND TNA.CREATETIME >= TO_DATE('"+tblNbsjProposalVo.getStartDate()+"','yyyy-MM-dd')");
		}
		
		if(tblNbsjProposalVo.getEndDate() !=null && tblNbsjProposalVo.getEndDate().length()>0){
			sb.append(" AND TNA.CREATETIME <= TO_DATE('"+tblNbsjProposalVo.getEndDate()+" 23:59:59','yyyy-mm-dd HH24:MI:SS')");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjProposalEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_PROPOSAL SET PROCODE = '"+plan.getProcode()+"' ");
		if(plan.getProname() != null && !"".equals(plan.getProname())) {
			sqlSb.append(" ,PRONAME = '"+plan.getProname()+"'");
		}
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			sqlSb.append(" ,CONTENT = '"+plan.getContent()+"'");
		}
		sqlSb.append(" WHERE PROID= "+plan.getProid());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjProposalEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_PROPOSAL(PROID,CREATETIME,CREATESTAFFID,STATUS");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getStaffid()+",0");
		
		if(plan.getProcode() != null && !"".equals(plan.getProcode())) {
			colSb.append(",PROCODE");
			valSb.append(",'"+plan.getProcode()+"'");
		}
		if(plan.getProname() != null && !"".equals(plan.getProname())) {
			colSb.append(",PRONAME");
			valSb.append(",'"+plan.getProname()+"'");
		}
		if(plan.getContent() != null && !"".equals(plan.getContent())) {
			colSb.append(",CONTENT");
			valSb.append(",'"+plan.getContent()+"'");
		}
		if(plan.getProjectId() != null && !"".equals(plan.getProjectId())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
