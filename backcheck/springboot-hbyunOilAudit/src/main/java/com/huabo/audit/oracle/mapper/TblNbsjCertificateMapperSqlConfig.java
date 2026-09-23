package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.hbfk.util.PageInfo;

public class TblNbsjCertificateMapperSqlConfig {
	
	public String selectNbsjCertificateListByPageInfo(PageInfo<TblNbsjCertificate> pageInfo,BigDecimal orgid,String projectName,
			String auditMatter,String auditAbstract,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,TT.ORGNAME,TS.REALNAME,STAFF.REALNAME SJRY,PJ.PRJOECTNAME "
				+ " FROM TBL_NBSJ_CERTIFICATE TNA "
				+ " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID "
				+ " LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId "
				+ " LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID "
				+ " LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID "
				+ " WHERE 1=1 ");
		
		if(projectId!=null && !"".equals(projectId)) {
			sb.append(" AND PJ.projectId = '"+projectId+"'");
		}
		if(projectName!=null && !"".equals(projectName)) {
			sb.append(" AND PJ.PRJOECTNAME LIKE '%"+projectName+"%'");
		}
		if(auditMatter!=null && !"".equals(auditMatter)) {
			sb.append(" AND TNA.auditMatter LIKE '%"+auditMatter+"%'");
		}
		if(auditAbstract!=null && !"".equals(auditAbstract)) {
			sb.append(" AND TNA.auditAbstract LIKE '%"+auditAbstract+"%'");
		}
		
		sb.append(" ORDER BY TNA.CERTIFICATEID DESC");
		return sb.toString();
	}
	
	public String selectNbsjCertificateListByPageInfoDg(PageInfo<TblNbsjCertificate> pageInfo,String sheetid) {
		TblNbsjCertificate cate=pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TT.ORGNAME,TS.REALNAME,STAFF.REALNAME SJRY,PJ.PRJOECTNAME "
				+ " FROM TBL_NBSJ_CERTIFICATE TNA "
				+ " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID "
				+ " LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId "
				+ " LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID "
				+ " LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID "
				//+ "WHERE 1=1 and TNA.orgid='"+cate.getOrgId()+"' and TNA.projectid='"+cate.getProjectId()+"' and certificateid not in (select distinct certificateid from tbl_SHEET_CERTIFICATE) ");
				+ "WHERE 1=1 and   TNA.projectid='"+cate.getProjectId()+"' ");
		if(sheetid!=null && !sheetid.equals("")) {
			sb.append("  and certificateid not in (select distinct certificateid from tbl_SHEET_CERTIFICATE WHERE SHEETID="+sheetid+") ");
		}
		if(cate.getProjectName()!=null && !"".equals(cate.getProjectName())) {
			
		}
		if(cate.getAuditMatter()!=null && !"".equals(cate.getAuditMatter())) {
			sb.append(" AND TNA.auditMatter LIKE '%"+cate.getAuditMatter()+"%'");
		}
		if(cate.getAuditAbstract()!=null && !"".equals(cate.getAuditAbstract())) {
			sb.append(" AND TNA.auditAbstract LIKE '%"+cate.getAuditAbstract()+"%'");
		}
		
		sb.append(" ORDER BY TNA.CERTIFICATEID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCertificateList(Integer sheetid) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,TT.ORGNAME,TS.REALNAME,STAFF.REALNAME SJRY,PJ.PRJOECTNAME "
				+ " FROM TBL_NBSJ_CERTIFICATE TNA "
				+ " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID "
				+ " LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId "
				+ " LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID "
				+ " LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID "
				+ "WHERE 1=1 and certificateid  in (select distinct certificateid from tbl_SHEET_CERTIFICATE where SHEETID="+sheetid+") ");
		return sb.toString();
	}
	
 
	public String selectNbsjCertificateListCountByPageInfoDg(PageInfo<TblNbsjCertificate> pageInfo,String sheetid) {
		TblNbsjCertificate cate=pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_CERTIFICATE TNA "
				//+ "WHERE 1=1 and orgid='"+cate.getOrgId()+"' and projectid='"+cate.getProjectId()+"' and certificateid not in (select distinct certificateid from tbl_SHEET_CERTIFICATE)");
				+ "WHERE 1=1 and   TNA.projectid='"+cate.getProjectId()+"' ");
				if(sheetid!=null && !sheetid.equals("")) {
					sb.append("  and certificateid not in (select distinct certificateid from tbl_SHEET_CERTIFICATE WHERE SHEETID="+sheetid+") ");
				}
			return sb.toString();
	}
	
	public String selectNbsjCertificateListCountByPageInfo(PageInfo<TblNbsjCertificate> pageInfo,BigDecimal orgid,String projectName,
			String auditMatter,String auditAbstract,Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ " FROM TBL_NBSJ_CERTIFICATE TNA "
				+ " LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.AUDITUSERID "
				+ " LEFT JOIN TBL_NBSJ_PROJECT PJ ON PJ.projectId =TNA.projectId "
				+ " LEFT JOIN TBL_ORGANIZATION TT ON PJ.AUDITORGID =TT.ORGID "
				+ " LEFT JOIN TBL_STAFF TS ON PJ.AUDITSTAFFID = TS.STAFFID "
				+ "WHERE 1=1 ");
		
		if(projectId!=null ) {
			sb.append(" AND PJ.projectId = "+projectId);
		}
		
		if(projectName!=null && !"".equals(projectName)) {
			sb.append(" AND PJ.PRJOECTNAME LIKE '%"+projectName+"%'");
		}
		if(auditMatter!=null && !"".equals(auditMatter)) {
			sb.append(" AND TNA.auditMatter LIKE '%"+auditMatter+"%'");
		}
		if(auditAbstract!=null && !"".equals(projectName)) {
			sb.append(" AND TNA.auditAbstract LIKE '%"+auditAbstract+"%'");
		}
		return sb.toString();
	}
	
	
	public String updateEntity(TblNbsjCertificate plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_CERTIFICATE SET AUDITABSTRACT = '"+plan.getAuditAbstract()+"' ");
		if(plan.getProjectId() != null && !"".equals(plan.getProjectId())) {
			sqlSb.append(" ,PROJECTID = '"+plan.getProjectId()+"'");
		}
		if(plan.getOrgId() != null && !"".equals(plan.getOrgId())) {
			sqlSb.append(" ,ORGID = '"+plan.getOrgId()+"'");
		}
		if(plan.getCertificateDate() != null && !"".equals(plan.getCertificateDate())) {
			sqlSb.append(" ,CERTIFICATEDATE = TO_DATE('"+DateUtil.parseDate(plan.getCertificateDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getEvidenceOpinion() != null && !"".equals(plan.getEvidenceOpinion())) {
			sqlSb.append(" ,EVIDENCEOPINION = '"+plan.getEvidenceOpinion()+"'");
		}
		if(plan.getCertificateStaffId() != null ) {
			sqlSb.append(" ,CERTIFICATESTAFFID = '"+plan.getCertificateStaffId()+"'");
		}
		if(plan.getCertificateUser() != null && !"".equals(plan.getCertificateUser())) {
			sqlSb.append(" ,CERTIFICATEUSER = '"+plan.getCertificateUser()+"'");
		}
		if(plan.getAuditMatter() != null && !"".equals(plan.getAuditMatter())) {
			sqlSb.append(" ,AUDITMATTER = '"+plan.getAuditMatter()+"'");
		}
		if(plan.getAssistedbmfzr() != null && !"".equals(plan.getAssistedbmfzr())) {
			sqlSb.append(" ,ASSISTEDBMFZR = '"+plan.getAssistedbmfzr()+"'");
		}
		if(plan.getAssistedbmfzrid() != null ) {
			sqlSb.append(" ,ASSISTEDBMFZRID = '"+plan.getAssistedbmfzrid()+"'");
		}
		if(plan.getAssistedfgld() != null && !"".equals(plan.getAssistedfgld())) {
			sqlSb.append(" ,ASSISTEDFGLD = '"+plan.getAssistedfgld()+"'");
		}
		if(plan.getAssistedfgldid() != null ) {
			sqlSb.append(" ,ASSISTEDFGLDID = '"+plan.getAssistedfgldid()+"'");
		}
		if(plan.getAssistedzbuser() != null && !"".equals(plan.getAssistedzbuser())) {
			sqlSb.append(" ,ASSISTEDZBUSER = '"+plan.getAssistedzbuser()+"'");
		}
		if(plan.getAssistedzbuserid() != null ) {
			sqlSb.append(" ,ASSISTEDZBUSERID = '"+plan.getAssistedzbuserid()+"'");
		}
		
		if(plan.getFirststaffid() != null ) {
			sqlSb.append(" ,FIRSTSTAFFID = '"+plan.getFirststaffid()+"'");
		}
		
		if(plan.getSecondstaffid() != null ) {
			sqlSb.append(" ,SECONDSTAFFID = '"+plan.getSecondstaffid()+"'");
		}
		
		if(plan.getYjfh()!= null && !"".equals(plan.getYjfh())) {
			sqlSb.append(" ,YJFH = '"+plan.getYjfh()+"'");
		}
		
		if(plan.getEjfh()!= null && !"".equals(plan.getEjfh())) {
			sqlSb.append(" ,EJFH = '"+plan.getEjfh()+"'");
		}
		if(plan.getOrgIds()!= null && !"".equals(plan.getOrgIds())) {
			sqlSb.append(" ,ORGIDS = '"+plan.getOrgIds()+"'");
		}
		if(plan.getOrgIdNames()!= null && !"".equals(plan.getOrgIdNames())) {
			sqlSb.append(" ,ORGIDNAMES = '"+plan.getOrgIdNames()+"'");
		}
		
		sqlSb.append(" WHERE CERTIFICATEID= "+plan.getCertificateId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjCertificate plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_CERTIFICATE(CERTIFICATEID,STATUS,createDate,auditUserId");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,0,TO_DATE('"+DateUtil.parseDate(plan.getCreateDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getAuditUserId()+"");
		
		if(plan.getProjectId() != null && !"".equals(plan.getProjectId())) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectId()+"'");
		}
		if(plan.getOrgId() != null && !"".equals(plan.getOrgId())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgId()+"'");
		}
		if(plan.getAuditAbstract() != null && !"".equals(plan.getAuditAbstract())) {
			colSb.append(",AUDITABSTRACT");
			valSb.append(",'"+plan.getAuditAbstract()+"'");
		}
		if(plan.getEvidenceOpinion() != null && !"".equals(plan.getEvidenceOpinion())) {
			colSb.append(",EVIDENCEOPINION");
			valSb.append(",'"+plan.getEvidenceOpinion()+"'");
		}
		if(plan.getCertificateStaffId() != null ) {
			colSb.append(",CERTIFICATESTAFFID");
			valSb.append(",'"+plan.getCertificateStaffId()+"'");
		}
		if(plan.getCertificateUser() != null && !"".equals(plan.getCertificateUser())) {
			colSb.append(",CERTIFICATEUSER");
			valSb.append(",'"+plan.getCertificateUser()+"'");
		}
		if(plan.getCertificateDate() != null && !"".equals(plan.getCertificateDate())) {
			colSb.append(",CERTIFICATEDATE");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(plan.getCertificateDate(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		if(plan.getAuditMatter() != null && !"".equals(plan.getAuditMatter())) {
			colSb.append(",AUDITMATTER");
			valSb.append(",'"+plan.getAuditMatter()+"'");
		}
		if(plan.getAssistedbmfzr() != null && !"".equals(plan.getAssistedbmfzr())) {
			colSb.append(",ASSISTEDBMFZR");
			valSb.append(",'"+plan.getAssistedbmfzr()+"'");
		}
		if(plan.getAssistedbmfzrid() != null ) {
			colSb.append(",ASSISTEDBMFZRID");
			valSb.append(",'"+plan.getAssistedbmfzrid()+"'");
		}
		if(plan.getAssistedfgld() != null && !"".equals(plan.getAssistedfgld())) {
			colSb.append(",ASSISTEDFGLD");
			valSb.append(",'"+plan.getAssistedfgld()+"'");
		}
		if(plan.getAssistedfgldid() != null ) {
			colSb.append(",ASSISTEDFGLDID");
			valSb.append(",'"+plan.getAssistedfgldid()+"'");
		}
		if(plan.getAssistedzbuser() != null && !"".equals(plan.getAssistedzbuser())) {
			colSb.append(",ASSISTEDZBUSER");
			valSb.append(",'"+plan.getAssistedzbuser()+"'");
		}
		if(plan.getAssistedzbuserid() != null ) {
			colSb.append(",ASSISTEDZBUSERID");
			valSb.append(",'"+plan.getAssistedzbuserid()+"'");
		}
		if(plan.getFirststaffid() != null ) {
			colSb.append(",FIRSTSTAFFID");
			valSb.append(",'"+plan.getFirststaffid()+"'");
		}
		
		if(plan.getSecondstaffid() != null ) {
			colSb.append(",SECONDSTAFFID");
			valSb.append(",'"+plan.getSecondstaffid()+"'");
		}
		
		if(plan.getYjfh()!= null && !"".equals(plan.getYjfh())) {
			colSb.append(",YJFH");
			valSb.append(",'"+plan.getYjfh()+"'");
		}
		
		if(plan.getEjfh()!= null && !"".equals(plan.getEjfh())) {
			colSb.append(",EJFH");
			valSb.append(",'"+plan.getEjfh()+"'");
		}
		
		if(plan.getOrgIds()!= null && !"".equals(plan.getOrgIds())) {
			colSb.append(",ORGIDS");
			valSb.append(",'"+plan.getOrgIds()+"'");
		}
		if(plan.getOrgIdNames()!= null && !"".equals(plan.getOrgIdNames())) {
			colSb.append(",ORGIDNAMES");
			valSb.append(",'"+plan.getOrgIdNames()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

}
