package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjSheetMapperSqlConfig {
	
	public String findByProjectIdAndPmUserId(com.hbfk.util.PageInfo<TblNbsjSheetEntity> pageInfo,
			Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM ( SELECT SHEETID,SHEETCODE,SHEETNAME,SHEETTARGET,CREATETIME,REALNAME,PROJECTID,AUDITDEST,ROWNUM RN FROM ( SELECT SHEET.SHEETID,SHEET.SHEETCODE,SHEET.SHEETNAME,SHEET.SHEETTARGET,SHEET.CREATETIME,CSTAFF.REALNAME,PROJECT.PROJECTID,NVL(ASTAFF.REALNAME,AORG.ORGNAME) AUDITDEST FROM TBL_NBSJ_SHEET SHEET LEFT JOIN TBL_STAFF CSTAFF ON SHEET.CREATESTAFF = CSTAFF.STAFFID LEFT JOIN TBL_NBSJ_PROJECT PROJECT ON SHEET.PROJECTID = PROJECT.PROJECTID LEFT JOIN TBL_STAFF ASTAFF ON PROJECT.AUDITSTAFFID = ASTAFF.STAFFID LEFT JOIN TBL_ORGANIZATION AORG ON AORG.ORGID = PROJECT.AUDITORGID");
		sb.append(" WHERE SHEET.PROJECTID = "+projectId);
		
		TblNbsjSheetEntity sheet = pageInfo.getCondition();
		
		if(sheet.getSheetCode() != null && !"".equals(sheet.getSheetCode())) {
			sb.append(" AND SHEET.SHEETCODE LIKE '%"+sheet.getSheetCode()+"%'");
		}
		
		if(sheet.getSheetName() != null && !"".equals(sheet.getSheetName())) {
			sb.append(" AND SHEET.SHEETNAME LIKE '%"+sheet.getSheetName()+"%'");
		}
		
		if(sheet.getState()!=null && sheet.getState()>0){
			sb.append(" AND SHEET.STATE ="+sheet.getState());
		}
		
		if(sheet.getAuditStaffId()!=null){
			sb.append(" AND SHEET.CREATESTAFF ="+sheet.getAuditStaffId());
		}
		
		sb.append(" ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return null;
	}
	
	
	
	public String selectPlanCodeByOrgid(TblNbsjSheetEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_SHEET WHERE 1=1 AND SHEETCODE='"+plan.getSheetCode()+"' ");
		if(plan.getSheetId() != null) {
			sb.append(" AND SHEETID != "+plan.getSheetId());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo, TBlNbsjSheetVo tBlNbsjSheetVo, Integer operateid) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.REALNAME,ORG.ORGNAME,st.REALNAME auditrealname "
				+ "FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "LEFT JOIN TBL_STAFF st ON st.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_YQNS_IMPLEMENT_PLAN TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getProjectName()!=null && tBlNbsjSheetVo.getProjectName().length()>0) {
			sb.append(" AND TNP.prjoectname LIKE '%"+tBlNbsjSheetVo.getProjectName()+"%'");
		}
		
		if(tBlNbsjSheetVo.getAuditOrgId()!=null) {
			sb.append(" AND TNA.AUDITORGID = '"+tBlNbsjSheetVo.getAuditOrgId()+"'");
		}
		if(tBlNbsjSheetVo.getAuditStaffId()!=null) {
			sb.append(" AND TNA.AUDITSTAFFID = '"+tBlNbsjSheetVo.getAuditStaffId()+"'");
		}
		
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		if(operateid != null ) {
			sb.append(" AND TNA.TARGETID=  "+operateid);
		}
		
		sb.append(" ORDER BY TNA.SHEETID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	
	public String selectCountByPageInfo(PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) {
//		TblNbsjSheetEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectId()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectId()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		if(tBlNbsjSheetVo.getAuditOrgId()!=null) {
			sb.append(" AND TNA.AUDITORG = '"+tBlNbsjSheetVo.getAuditOrgId()+"'");
		}
		if(tBlNbsjSheetVo.getAuditStaffId()!=null) {
			sb.append(" AND TNA.AUDITSTAFFID = '"+tBlNbsjSheetVo.getAuditStaffId()+"'");
		}
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		if(operateid != null ) {
			sb.append(" AND TNA.TARGETID=  "+operateid);
		}
		
		return sb.toString();
	}
	
	public String updateEntity(TblNbsjSheetEntity plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_NBSJ_SHEET SET SHEETCODE = '"+plan.getSheetCode()+"' ");
		if(plan.getSheetName() != null && !"".equals(plan.getSheetName())) {
			sqlSb.append(" ,SHEETNAME = '"+plan.getSheetName()+"'");
		}
		if(plan.getSheetTarget() != null && !"".equals(plan.getSheetTarget())) {
			sqlSb.append(" ,SHEETTARGET = '"+plan.getSheetTarget()+"'");
		}
		if(plan.getAuditOrg() != null && !"".equals(plan.getAuditOrg())) {
			sqlSb.append(" ,AUDITORG = '"+plan.getAuditOrg()+"'");
		}
		if(plan.getRiskAttrbution() != null && !"".equals(plan.getRiskAttrbution())) {
			sqlSb.append(" ,RISKATTRBUTION = '"+plan.getRiskAttrbution()+"'");
		}
		if(plan.getBusinessAffiliation() != null && !"".equals(plan.getBusinessAffiliation())) {
			sqlSb.append(" ,BUSINESSAFFILIATION = '"+plan.getBusinessAffiliation()+"'");
		}
		if(plan.getApprover() != null && !"".equals(plan.getApprover())) {
			sqlSb.append(" ,APPROVER = '"+plan.getApprover()+"'");
		}
		if(plan.getRiskLevel() != null && !"".equals(plan.getRiskLevel())) {
			sqlSb.append(" ,RISKLEVEL = '"+plan.getRiskLevel()+"'");
		}
		if(plan.getQuesTitle() != null && !"".equals(plan.getQuesTitle())) {
			sqlSb.append(" ,QUESTITLE = '"+plan.getQuesTitle()+"'");
		}
		if(plan.getTargetName() != null && !"".equals(plan.getTargetName())) {
			sqlSb.append(" ,TARGETNAME = '"+plan.getTargetName()+"'");
		}
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			sqlSb.append(" ,BUSINESSTYPE = '"+plan.getBusinessType()+"'");
		}
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			sqlSb.append(" ,SUDITPROCESS = '"+plan.getSuditProcess()+"'");
		}
		if(plan.getState() != null) {
			sqlSb.append(" ,state = '"+plan.getState()+"'");
		}
		if(plan.getYjfh() != null && !"".equals(plan.getYjfh())) {
			sqlSb.append(" ,YJFH = '"+plan.getYjfh()+"'");
		}
		if(plan.getEjfh() != null && !"".equals(plan.getEjfh())) {
			sqlSb.append(" ,EJFH = '"+plan.getEjfh()+"'");
		}
		if(plan.getFirststaffid() != null && !"".equals(plan.getFirststaffid())) {
			sqlSb.append(" ,FIRSTSTAFFID = '"+plan.getFirststaffid()+"'");
		}
		if(plan.getSecondstaffid() != null && !"".equals(plan.getSecondstaffid())) {
			sqlSb.append(" ,SECONDSTAFFID = '"+plan.getSecondstaffid()+"'");
		}
		
		if(plan.getAuditDesc() != null && !"".equals(plan.getAuditDesc())) {
			sqlSb.append(" ,AUDITDESC = '"+plan.getAuditDesc()+"'");
		}
		if(plan.getAuditCourse() != null && !"".equals(plan.getAuditCourse())) {
			sqlSb.append(" ,AUDITCOURSE = '"+plan.getAuditCourse()+"'");
		}else {
			sqlSb.append(" ,AUDITCOURSE = null ");
		}
		if(plan.getSjbwl() != null && !"".equals(plan.getSjbwl())) {
			sqlSb.append(" ,SJBWL = '"+plan.getSjbwl()+"'");
		}else {
			sqlSb.append(" ,SJBWL = null ");
		}
		if(plan.getAuditDiscoverable() != null && !"".equals(plan.getAuditDiscoverable())) {
			sqlSb.append(" ,AUDITDISCOVERABLE = '"+plan.getAuditDiscoverable()+"'");
		}
		if(plan.getInternalType()!= null && !"".equals(plan.getInternalType())) {
			sqlSb.append(" ,INTERNALTYPE = '"+plan.getInternalType()+"'");
		}else {
			sqlSb.append(" ,INTERNALTYPE = null ");
		}
		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
			sqlSb.append(" ,TARGETID = '"+plan.getTargetId()+"'");
		}
		//判断是否发现问题  
		if(plan.getRiskLevel()!= null && !"".equals(plan.getDetailType())&&plan.getRiskLevel().equals("是")){
		if(plan.getBelongType() != null && !"".equals(plan.getBelongType())) {
			sqlSb.append(" ,belongType = '"+plan.getBelongType()+"'");
		}
		
		if(plan.getDetailType() != null && !"".equals(plan.getDetailType())) {
			sqlSb.append(" ,detailType = '"+plan.getDetailType()+"'");
		}
		
		if(plan.getRelatedMoney()!= null && !"".equals(plan.getRelatedMoney())) {
			sqlSb.append(" ,relatedMoney = '"+plan.getRelatedMoney()+"'");
		}
		if(plan.getHgDetailType()!= null && !"".equals(plan.getHgDetailType())) {
			sqlSb.append(" ,hgDetailType = '"+plan.getHgDetailType()+"'");
		}
		
		if(plan.getOrgIds()!= null && !"".equals(plan.getOrgIds())) {
			sqlSb.append(" ,ORGIDS = '"+plan.getOrgIds()+"'");
		}
		if(plan.getOrgIdNames()!= null && !"".equals(plan.getOrgIdNames())) {
			sqlSb.append(" ,ORGIDNAMES = '"+plan.getOrgIdNames()+"'");
		}
		
		}else{ //发现问题为否的时候 直接赋值为空
			sqlSb.append(" ,hgDetailType = null");
			sqlSb.append(" ,relatedMoney = 0 ");
			sqlSb.append(" ,detailType = null ");
			sqlSb.append(" ,belongType = null ");
			sqlSb.append(" ,auditDiscoverable = null ");
		}
		if(plan.getHzdg()!= null && !"".equals(plan.getHzdg())) {
			sqlSb.append(" ,hzdg = '"+plan.getHzdg()+"'");
		}
		if(plan.getRelationsheetids()!= null && !"".equals(plan.getRelationsheetids())) {
			sqlSb.append(" ,relationsheetids = '"+plan.getRelationsheetids()+"'");
		}
		sqlSb.append(" WHERE SHEETID= "+plan.getSheetId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjSheetEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SHEET(SHEETID,CREATETIME,CREATESTAFF,STATUS,STATE");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getAuditStaffId()+",0,"+plan.getState());
		
		if(plan.getSheetCode() != null && !"".equals(plan.getSheetCode())) {
			colSb.append(",SHEETCODE");
			valSb.append(",'"+plan.getSheetCode()+"'");
		}
		
		if(plan.getSheetName() != null && !"".equals(plan.getSheetName())) {
			colSb.append(",SHEETNAME");
			valSb.append(",'"+plan.getSheetName()+"'");
		}
		
		if(plan.getSheetTarget() != null && !"".equals(plan.getSheetTarget())) {
			colSb.append(",SHEETTARGET");
			valSb.append(",'"+plan.getSheetTarget()+"'");
		}
		
		if(plan.getAuditOrg() != null && !"".equals(plan.getAuditOrg())) {
			colSb.append(",AUDITORG");
			valSb.append(",'"+plan.getAuditOrg()+"'");
		}
		
		if(plan.getRiskAttrbution() != null && !"".equals(plan.getRiskAttrbution())) {
			colSb.append(",RISKATTRBUTION");
			valSb.append(",'"+plan.getRiskAttrbution()+"'");
		}
		
		if(plan.getBusinessAffiliation() != null && !"".equals(plan.getBusinessAffiliation())) {
			colSb.append(",BUSINESSAFFILIATION");
			valSb.append(",'"+plan.getBusinessAffiliation()+"'");
		}
		
		if(plan.getApprover() != null && !"".equals(plan.getApprover())) {
			colSb.append(",APPROVER");
			valSb.append(",'"+plan.getApprover()+"'");
		}
		
		if(plan.getRiskLevel() != null && !"".equals(plan.getRiskLevel())) {
			colSb.append(",RISKLEVEL");
			valSb.append(",'"+plan.getRiskLevel()+"'");
		}
		
		if(plan.getQuesTitle() != null && !"".equals(plan.getQuesTitle())) {
			colSb.append(",QUESTITLE");
			valSb.append(",'"+plan.getQuesTitle()+"'");
		}
		
		if(plan.getTargetName() != null && !"".equals(plan.getTargetName())) {
			colSb.append(",TARGETNAME");
			valSb.append(",'"+plan.getTargetName()+"'");
		}
		
		if(plan.getBusinessType() != null && !"".equals(plan.getBusinessType())) {
			colSb.append(",BUSINESSTYPE");
			valSb.append(",'"+plan.getBusinessType()+"'");
		}
		
		if(plan.getSuditProcess() != null && !"".equals(plan.getSuditProcess())) {
			colSb.append(",SUDITPROCESS");
			valSb.append(",'"+plan.getSuditProcess()+"'");
		}
		
		if(plan.getYjfh() != null && !"".equals(plan.getYjfh())) {
			colSb.append(",YJFH");
			valSb.append(",'"+plan.getYjfh()+"'");
		}
		if(plan.getEjfh() != null && !"".equals(plan.getEjfh())) {
			colSb.append(",EJFH");
			valSb.append(",'"+plan.getEjfh()+"'");
		}
		if(plan.getFirststaffid() != null && !"".equals(plan.getFirststaffid())) {
			colSb.append(",FIRSTSTAFFID");
			valSb.append(",'"+plan.getFirststaffid()+"'");
		}
		if(plan.getSecondstaffid() != null && !"".equals(plan.getSecondstaffid())) {
			colSb.append(",SECONDSTAFFID");
			valSb.append(",'"+plan.getSecondstaffid()+"'");
		}
		if(plan.getAuditDesc()!= null && !"".equals(plan.getAuditDesc())) {
			colSb.append(",AUDITDESC");
			valSb.append(",'"+plan.getAuditDesc()+"'");
		}
		if(plan.getAuditCourse()!= null && !"".equals(plan.getAuditCourse())) {
			colSb.append(",AUDITCOURSE");
			valSb.append(",'"+plan.getAuditCourse()+"'");
		}
		if(plan.getSjbwl()!= null && !"".equals(plan.getSjbwl())) {
			colSb.append(",SJBWL");
			valSb.append(",'"+plan.getSjbwl()+"'");
		}
		if(plan.getProjectId()!= null) {
			colSb.append(",PROJECTID");
			valSb.append(",'"+plan.getProjectId()+"'");
		}
		if(plan.getAuditDiscoverable()!= null && !"".equals(plan.getAuditDiscoverable())) {
			colSb.append(",AUDITDISCOVERABLE");
			valSb.append(",'"+plan.getAuditDiscoverable()+"'");
		}
		
		
		if(plan.getBelongType() != null && !"".equals(plan.getBelongType())) {
			colSb.append(",belongType");
			valSb.append(",'"+plan.getBelongType()+"'");
		}
		
		if(plan.getRiskLevel()!= null && !"".equals(plan.getDetailType())&&plan.getRiskLevel().equals("是")){
		
		if(plan.getDetailType() != null && !"".equals(plan.getDetailType())) {
			colSb.append(",detailType");
			valSb.append(",'"+plan.getDetailType()+"'");
		}
		
		if(plan.getRelatedMoney()!= null && !"".equals(plan.getRelatedMoney())) {
			colSb.append(",relatedMoney");
			valSb.append(",'"+plan.getRelatedMoney()+"'");
		}
		if(plan.getHgDetailType()!= null && !"".equals(plan.getHgDetailType())) {
			colSb.append(",hgDetailType");
			valSb.append(",'"+plan.getHgDetailType()+"'");
		}
		
		}
		if(plan.getInternalType()!= null && !"".equals(plan.getInternalType())) {
			colSb.append(",INTERNALTYPE");
			valSb.append(",'"+plan.getInternalType()+"'");
		}
		
		if(plan.getRelationsheetids()!= null && !"".equals(plan.getRelationsheetids())) {
			colSb.append(",relationsheetids");
			valSb.append(",'"+plan.getRelationsheetids()+"'");
		}
		
		if(plan.getHzdg()!= null && !"".equals(plan.getHzdg())) {
			colSb.append(",hzdg");
			valSb.append(",'"+plan.getHzdg()+"'");
		}
		
		if(plan.getTargetId() != null && !"".equals(plan.getTargetId())) {
			colSb.append(",TARGETID");
			valSb.append(",'"+plan.getTargetId()+"'");
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
	
	
	
	//==
	public String getExportList(Integer projectid) {
		StringBuffer sb = new StringBuffer(""
				+ "SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,pr.PRJOECTNAME,AUS.REALNAME approver  "
				+ "FROM TBL_NBSJ_SHEET TNA "
				+ " LEFT JOIN TBL_NBSJ_PROJECT pr ON TNA.PROJECTID=PR.PROJECTID "
				+ " LEFT JOIN TBL_STAFF aus ON aus.STAFFID = PR.AUDITSTAFFID "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		if(projectid!=null) {
			sb.append(" AND TNA.PROJECTID = '"+projectid+"'");
		}
		
		sb.append(" ORDER BY TNA.SHEETID DESC ");
		return sb.toString();
	}
	
	public String insertSheetReport(TblNbsjSheetReportEntity sr){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SHEET_REPORT(REPORTID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		
		if(sr.getSheetId() != null && !"".equals(sr.getSheetId())) {
			colSb.append(",SHEETID");
			valSb.append(",'"+sr.getSheetId()+"'");
		}
		if(sr.getReportConcent() != null && !"".equals(sr.getReportConcent())) {
			colSb.append(",REPORTCONTENT");
			valSb.append(",'"+sr.getReportConcent()+"'");
		}
		if(sr.getSjdeptIds() != null && !"".equals(sr.getSjdeptIds())) {
			colSb.append(",REPORTORGIDS");
			valSb.append(",'"+sr.getSjdeptIds()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	public String selectifPmOrLeader(BigDecimal staffid,Integer projectid) {
		StringBuffer sb = new StringBuffer("select count(*) from tbl_nbsj_project where (projectid='"+projectid+"' and pmid='"+staffid+"') or projectid in ("
				+ "  SELECT  t.projectid  FROM TBL_NBSJ_PROJECTTEAM TNA  "+
                 " LEFT JOIN TBL_NBSJ_PRO_TEAM T ON TNA.TEAMID = T.TEAMID  "+
				"  LEFT JOIN TBL_NBSJ_TEAMSTAFF TNS ON T.TEAMID = TNS.TEAMID    "+
				"   WHERE  t.projectid='"+projectid+"' and TNS.stafftype=0 and TNS.staffid='"+staffid+"')");
		return sb.toString();
	}
	
	public String selectSheetByprojectId(Integer projectid) {
		StringBuffer sb = new StringBuffer("select count(*) from TBL_NBSJ_SHEET where  PROJECTID = "+projectid+" and  STATE!=4");
		return sb.toString();
	}
	
	
	//==
		public String getExportListstaff(Integer projectid,BigDecimal staffid) {
			StringBuffer sb = new StringBuffer(""
					+ "SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,pr.PRJOECTNAME,AUS.REALNAME approver  "
					+ "FROM TBL_NBSJ_SHEET TNA "
					+ " LEFT JOIN TBL_NBSJ_PROJECT pr ON TNA.PROJECTID=PR.PROJECTID "
					+ " LEFT JOIN TBL_STAFF aus ON aus.STAFFID = PR.AUDITSTAFFID "
					+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
					+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
					+ "WHERE 1=1 ");
			
			if(projectid!=null) {
				sb.append(" AND TNA.PROJECTID = '"+projectid+"'");
			}
			if(staffid!=null) {
				sb.append(" AND TNA.CREATESTAFF = '"+staffid+"'");
			}
			
			sb.append(" ORDER BY TNA.SHEETID DESC ");
			return sb.toString();
		}
		
}
