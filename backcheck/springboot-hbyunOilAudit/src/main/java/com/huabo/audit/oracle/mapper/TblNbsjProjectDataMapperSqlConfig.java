package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjProjectDataEntity;
import com.huabo.audit.oracle.vo.DataProVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjProjectDataMapperSqlConfig {
	public String selectPlanCodeByOrgid(TblNbsjProjectDataEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_PROJECT_DATAPRE WHERE 1=1 AND PROJECT_DATAPRE_ID='"+plan.getProjectDatapreId()+"' ");
		if(plan.getId() != null) {
			sb.append(" AND ID != "+plan.getId());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblNbsjProjectDataEntity> pageInfo,DataProVo dataProVo,Integer orgid,Integer projectId,BigDecimal staffid) {
		
		StringBuffer sb = new StringBuffer("SELECT TNA.* "
				+ "FROM TBL_PROJECT_DATAPRE TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
//				+ " AND TNA. ");
		
		if(projectId!=null) {
			sb.append(" AND TNA.projectId = '"+projectId+"'");
		}
		if(staffid!=null){
			sb.append("and id in (select dataid from TBL_DATAPROJECT_ISSUE where staffid='"+staffid+"') ");
		}
		if(orgid!=null) {
			sb.append(" AND TNA.ORGID = '"+orgid+"'");
		}
		
		if(dataProVo.getDatacode()!=null && dataProVo.getDatacode().length()>0) {
			sb.append(" AND TNA.PROJECT_DATAPRE_ID LIKE '%"+dataProVo.getDatacode()+"%'");
		}
		
		if(dataProVo.getDataname()!=null && dataProVo.getDataname().length()>0) {
			sb.append(" AND TNA.DATA_NAME LIKE '%"+dataProVo.getDataname()+"%'");
		}
		
		sb.append(" ORDER BY TNA.ID DESC");
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjProjectDataEntity> pageInfo,DataProVo dataProVo,Integer orgid,Integer projectId,BigDecimal type) {
//		TblNbsjProjectDataEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_PROJECT_DATAPRE TNA "
//				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.PRINCIPALID "
//				+ "LEFT JOIN TBL_STAFF LEADER ON LEADER.STAFFID = TNA.LEADERID "
//				+ "LEFT JOIN TBL_ORGANIZATION AUDITORG ON AUDITORG.ORGID = TNA.AUDITORGID "
//				+ "LEFT JOIN TBL_STAFF CRESTAFF ON CRESTAFF.STAFFID = TNA.CREATESTAFFID "
				+ "WHERE 1=1 ");
		
		if(projectId!=null) {
			sb.append(" AND TNA.projectId = '"+projectId+"'");
		}
		if(type!=null){
			sb.append("and id in (select dataid from TBL_DATAPROJECT_ISSUE where staffid='"+type+"') ");
		}
		
		if(dataProVo.getDatacode()!=null && dataProVo.getDatacode().length()>0) {
			sb.append(" AND TNA.PROJECT_DATAPRE_ID LIKE '%"+dataProVo.getDatacode()+"%'");
		}
		
		if(dataProVo.getDataname()!=null && dataProVo.getDataname().length()>0) {
			sb.append(" AND TNA.DATA_NAME LIKE '%"+dataProVo.getDataname()+"%'");
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjProjectDataEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_PROJECT_DATAPRE SET DATA_NAME = '"+plan.getDataName()+"' ");
		
		if(plan.getDataCapacity() != null && !"".equals(plan.getDataCapacity())) {
			sqlSb.append(" ,DATA_CAPACITY = '"+plan.getDataCapacity()+"'");
		}
		
		if(plan.getProjectDatapreId() != null && !"".equals(plan.getProjectDatapreId())) {
			sqlSb.append(" ,PROJECT_DATAPRE_ID = '"+plan.getProjectDatapreId()+"'");
		}
		if(plan.getFristuserid() != null && !"".equals(plan.getFristuserid())) {
			sqlSb.append(" ,FRISTUSERID = '"+plan.getFristuserid()+"'");
		}
		if(plan.getProjectname() != null && !"".equals(plan.getProjectname())) {
			sqlSb.append(" ,PROJECTNAME = '"+plan.getProjectname()+"'");
		}
//		if(plan.getProjectId() != null && !"".equals(plan.getProjectId())) {
//			sqlSb.append(" ,PROJECTID = '"+plan.getProjectId()+"'");
//		}
		
		sqlSb.append(" WHERE ID= "+plan.getId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjProjectDataEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_PROJECT_DATAPRE(ID,DATA_DATE,PROJECTID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getDataDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getProjectid());
		
		
		if(plan.getProjectDatapreId() != null && !"".equals(plan.getProjectDatapreId())) {
			colSb.append(",PROJECT_DATAPRE_ID");
			valSb.append(",'"+plan.getProjectDatapreId()+"'");
		}
		
		if(plan.getDataName() != null && !"".equals(plan.getDataName())) {
			colSb.append(",DATA_NAME");
			valSb.append(",'"+plan.getDataName()+"'");
		}
		
		if(plan.getDataCapacity() != null && !"".equals(plan.getDataCapacity())) {
			colSb.append(",DATA_CAPACITY");
			valSb.append(",'"+plan.getDataCapacity()+"'");
		}
		
		if(plan.getProjectname() != null && !"".equals(plan.getProjectname())) {
			colSb.append(",PROJECTNAME");
			valSb.append(",'"+plan.getProjectname()+"'");
		}
		
		if(plan.getUsername() != null && !"".equals(plan.getUsername())) {
			colSb.append(",USERNAME");
			valSb.append(",'"+plan.getUsername()+"'");
		}
		if(plan.getOrgid() != null && !"".equals(plan.getOrgid())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgid()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String saveOld(TblNbsjProjectDataEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDIT_PROJECDATA(DATAID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		
		if(plan.getProjectid() != null && !"".equals(plan.getProjectid())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectid()+"'");
		}
		
		if(plan.getOldProjectId() != null && !"".equals(plan.getOldProjectId())) {
			colSb.append(",OLDPROJECTID");
			valSb.append(",'"+plan.getOldProjectId()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
