package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.PageInfo;

public class TblNbsjSheetMapperSqlConfig {
	
	public String  selectListByPageInfoxml(TBlNbsjSheetVo vo, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.STAFFID CREATESTAFFID,PRINCIPAL.REALNAME,ORG.ORGNAME,st.REALNAME auditrealname  FROM TBL_NBSJ_SHEET TNA " +
				" LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF  LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG LEFT JOIN TBL_STAFF st ON st.STAFFID = TNA.AUDITSTAFFID LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		//安全保密SQL
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID",
				"TNA.CREATESTAFF", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));

		if(vo.getProjectid() != null) {
			sqlSb.append(" AND TNA.PROJECTID = ").append(vo.getProjectid());
		}
		
		if(StringUtils.isNotBlank(vo.getSheetcode())) {
			sqlSb.append("  AND TNA.SHEETCODE LIKE '%").append(vo.getSheetcode()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getSheetname())) {
			sqlSb.append("  AND TNA.SHEETNAME LIKE '%").append(vo.getSheetname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getProjectName())) {
			sqlSb.append("  AND TNP.PRJOECTNAME LIKE '%").append(vo.getProjectName()).append("%'");
		}
		
		if(vo.getAuditStaffId() != null) {
			sqlSb.append(" AND TNA.AUDITSTAFFID = ").append(vo.getAuditStaffId());
		}
		
		if(StringUtils.isNotBlank(vo.getStatus())) {
			sqlSb.append(" AND TNA.STATE = ").append(vo.getStatus());
		}
		
		if(StringUtils.isNotBlank(vo.getStaffid())) {
			sqlSb.append(" AND PRINCIPAL.STAFFID = ").append(vo.getStaffid());
		}
		
		if(vo.getOperateid() != null) {
			sqlSb.append(" AND TNA.TARGETID = ").append(vo.getOperateid());
		}
		
		if(StringUtils.isNotBlank(vo.getInternalType())) {
			sqlSb.append("  AND TNA.INTERNALTYPE LIKE '%").append(vo.getInternalType()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getStartDate())) {
			sqlSb.append("  AND TNA.createtime >= '").append(vo.getStartDate()).append("'");
		}
		if(StringUtils.isNotBlank(vo.getEndDate())) {
			sqlSb.append("  AND TNA.createtime <= '").append(vo.getEndDate()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getOrgName())) {
			String[] orgnames = vo.getOrgName().split(",");
			sqlSb.append(" AND ( TNA.ORGIDNAMES LIKE '%").append(orgnames[0]).append("%' ");
			if(orgnames.length > 1) {
				for (int i = 1; i < orgnames.length; i++) {
					sqlSb.append(" OR TNA.ORGIDNAMES LIKE '%").append(orgnames[i]).append("%' ");
				}
			}
			sqlSb.append(" )");
		}
		
		sqlSb.append(" ORDER BY TNA.SHEETID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}

	public String  selectManageListByPageInfoxml(TBlNbsjSheetVo vo, TblStaffUtil loginStaff) throws Exception {
		StringBuffer sqlSb = new StringBuffer("SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.REALNAME,ORG.ORGNAME,st.REALNAME auditrealname  FROM TBL_NBSJ_SHEET TNA " +
				" LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF  LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG LEFT JOIN TBL_STAFF st ON st.STAFFID = TNA.AUDITSTAFFID LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID");

		sqlSb.append(" WHERE 1 = 1 ");

		//安全保密SQL
		sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID",
				"TNP.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));

		if(vo.getProjectid() != null) {
			sqlSb.append(" AND TNA.PROJECTID = ").append(vo.getProjectid());
		}

		if(StringUtils.isNotBlank(vo.getSheetcode())) {
			sqlSb.append("  AND TNA.SHEETCODE LIKE '%").append(vo.getSheetcode()).append("%'");
		}

		if(StringUtils.isNotBlank(vo.getSheetname())) {
			sqlSb.append("  AND TNA.SHEETNAME LIKE '%").append(vo.getSheetname()).append("%'");
		}

		if(StringUtils.isNotBlank(vo.getProjectName())) {
			sqlSb.append("  AND TNP.PRJOECTNAME LIKE '%").append(vo.getProjectName()).append("%'");
		}

		if(vo.getAuditStaffId() != null) {
			sqlSb.append(" AND TNA.AUDITSTAFFID = ").append(vo.getAuditStaffId());
		}

		if(StringUtils.isNotBlank(vo.getStatus())) {
			sqlSb.append(" AND TNA.STATE = ").append(vo.getStatus());
		}

		if(StringUtils.isNotBlank(vo.getStaffid())) {
			sqlSb.append(" AND PRINCIPAL.STAFFID = ").append(vo.getStaffid());
		}

		if(vo.getOperateid() != null) {
			sqlSb.append(" AND TNA.TARGETID = ").append(vo.getOperateid());
		}

		if(StringUtils.isNotBlank(vo.getOrgName())) {
			String[] orgnames = vo.getOrgName().split(",");
			sqlSb.append(" AND ( TNA.ORGIDNAMES LIKE '%").append(orgnames[0]).append("%' ");
			if(orgnames.length > 1) {
				for (int i = 1; i < orgnames.length; i++) {
					sqlSb.append(" OR TNA.ORGIDNAMES LIKE '%").append(orgnames[i]).append("%' ");
				}
			}
			sqlSb.append(" )");
		}

		sqlSb.append(" ORDER BY TNA.SHEETID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String findByProjectIdAndPmUserId(com.hbfk.util.PageInfo<TblNbsjSheetEntity> pageInfo,
			Integer projectId) {
		StringBuffer sb = new StringBuffer("SELECT * FROM ( SELECT SHEETID,SHEETCODE,SHEETNAME,SHEETTARGET,CREATETIME,REALNAME,PROJECTID,AUDITDEST,ROWNUM RN " +
				"FROM ( SELECT SHEET.SHEETID,SHEET.SHEETCODE,SHEET.SHEETNAME,SHEET.SHEETTARGET,SHEET.CREATETIME,CSTAFF.REALNAME,PROJECT.PROJECTID," +
				"NVL(ASTAFF.REALNAME,AORG.ORGNAME) AUDITDEST " +
				"FROM TBL_NBSJ_SHEET SHEET " +
				"LEFT JOIN TBL_STAFF CSTAFF ON SHEET.CREATESTAFF = CSTAFF.STAFFID " +
				"LEFT JOIN TBL_NBSJ_PROJECT PROJECT ON SHEET.PROJECTID = PROJECT.PROJECTID " +
				"LEFT JOIN TBL_STAFF ASTAFF ON PROJECT.AUDITSTAFFID = ASTAFF.STAFFID " +
				"LEFT JOIN TBL_ORGANIZATION AORG ON AORG.ORGID = PROJECT.AUDITORGID");
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
	
	public String selectListByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.REALNAME,ORG.ORGNAME,st.REALNAME auditrealname "
				+ "FROM TBL_NBSJ_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "LEFT JOIN TBL_STAFF st ON st.STAFFID = TNA.AUDITSTAFFID "
				+ "LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
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
	
	
	public String selectCountByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) {
//		TblNbsjSheetEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		if(tBlNbsjSheetVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
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
		
		if(plan.getSecrectLevelId()!= null && !"".equals(plan.getSecrectLevelId())) {
			sqlSb.append(" ,SECRECTLEVELID = '"+plan.getSecrectLevelId()+"'");
		}
		if(plan.getStaffScopeIds()!= null && !"".equals(plan.getStaffScopeIds())) {
			sqlSb.append(" ,STAFFSCOPEIDS = '"+plan.getStaffScopeIds()+"'");
		}
		if(plan.getStaffScopeNames()!= null && !"".equals(plan.getStaffScopeNames())) {
			sqlSb.append(" ,STAFFSCOPENAMES = '"+plan.getStaffScopeNames()+"'");
		}
		if(plan.getSheettype()!= null && !"".equals(plan.getSheettype())) {
			sqlSb.append(" ,SHEETTYPE = '"+plan.getSheettype()+"'");
		}
		
		sqlSb.append(" WHERE SHEETID= "+plan.getSheetId());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjSheetEntity plan){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SHEET(SHEETID,CREATETIME,CREATESTAFF,STATUS,STATE");
		StringBuffer valSb = new StringBuffer("  VALUES ("+plan.getSheetId()+",TO_DATE('"+DateUtil.parseDate(plan.getCreateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),"+plan.getAuditStaffId()+",0,"+plan.getState());
		
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
		
		if(plan.getSecrectLevelId()!= null && !"".equals(plan.getSecrectLevelId())) {
			colSb.append(",SECRECTLEVELID");
			valSb.append(",'"+plan.getSecrectLevelId()+"'");
		}
		if(plan.getStaffScopeIds()!= null && !"".equals(plan.getStaffScopeIds())) {
			colSb.append(",STAFFSCOPEIDS");
			valSb.append(",'"+plan.getStaffScopeIds()+"'");
		}
		if(plan.getStaffScopeNames()!= null && !"".equals(plan.getStaffScopeNames())) {
			colSb.append(",STAFFSCOPENAMES");
			valSb.append(",'"+plan.getStaffScopeNames()+"'");
		}
		if(plan.getOrgid()!= null && !"".equals(plan.getOrgid())) {
			colSb.append(",ORGID");
			valSb.append(",'"+plan.getOrgid()+"'");
		}
		if(plan.getSheettype()!= null && !"".equals(plan.getSheettype())) {
			colSb.append(",SHEETTYPE");
			valSb.append(",'"+plan.getSheettype()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	//==
	public String getExportList(BigDecimal projectid ,TblNbsjSheetEntity tblSheet,TblStaffUtil staff) throws Exception {
		StringBuffer sb = new StringBuffer(""
				+ "SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,pr.PRJOECTNAME,AUS.REALNAME approver  "
				+ "FROM TBL_NBSJ_SHEET TNA "
				+ " LEFT JOIN TBL_NBSJ_PROJECT pr ON TNA.PROJECTID=PR.PROJECTID "
				+ " LEFT JOIN TBL_STAFF aus ON aus.STAFFID = PR.AUDITSTAFFID "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 ");
		
		//安全保密SQL
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(staff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID",
				"pr.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds()));

		
		if(projectid!=null) {
			sb.append(" AND TNA.PROJECTID = '"+projectid+"'");
		}
		if(tblSheet.getSheetCode() != null && !"".equals(tblSheet.getSheetCode()) ){
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tblSheet.getSheetCode()+"%'");
		}
		if(tblSheet.getSheetName()!= null && !"".equals(tblSheet.getSheetName())){
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tblSheet.getSheetName()+"%'");
		}
		if(tblSheet.getStatus()!=null){
			sb.append(" AND TNA.STATE = '"+tblSheet.getStatus()+"'");
		}
		if(tblSheet.getCreatestaff()!=null && !"".equals(tblSheet.getCreatestaff())){
			sb.append(" AND TNA.CREATESTAFF = '"+tblSheet.getCreatestaff()+"'");
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
	
	public String selectSheetByprojectId(BigDecimal projectid) {
		StringBuffer sb = new StringBuffer("select count(*) from TBL_NBSJ_SHEET where  PROJECTID = "+projectid+" and  STATE!=6");
		return sb.toString();
	}
	
	
	//==
		public String getExportListstaff(BigDecimal projectid,TblStaffUtil staff,TblNbsjSheetEntity tblSheet) throws Exception {
			StringBuffer sqlSb = new StringBuffer("SELECT TNA.*,TNP.prjoectname projectName,PRINCIPAL.REALNAME,ORG.ORGNAME,st.REALNAME auditrealname  FROM TBL_NBSJ_SHEET TNA " +
					" LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF  LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG LEFT JOIN TBL_STAFF st ON st.STAFFID = TNA.AUDITSTAFFID LEFT JOIN TBL_NBSJ_PROJECT TNP  ON TNP.PROJECTID = TNA.PROJECTID");

			sqlSb.append(" WHERE 1 = 1 ");

			//安全保密SQL
			sqlSb.append(GeneralSQLConcatConfig.concatSecrectSql(staff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID",
					"TNP.PMID", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", staff.getStaffid(), staff.getDeptIds(), staff.getSecrectScopeIds()));

			if(projectid != null) {
				sqlSb.append(" AND TNA.PROJECTID = ").append(projectid);
			}

			if(StringUtils.isNotBlank(tblSheet.getSheetCode())) {
				sqlSb.append("  AND TNA.SHEETCODE LIKE '%").append(tblSheet.getSheetCode()).append("%'");
			}

			if(StringUtils.isNotBlank(tblSheet.getSheetName())) {
				sqlSb.append("  AND TNA.SHEETNAME LIKE '%").append(tblSheet.getSheetName()).append("%'");
			}

			if(StringUtils.isNotBlank(tblSheet.getProjectName())) {
				sqlSb.append("  AND TNP.PRJOECTNAME LIKE '%").append(tblSheet.getProjectName()).append("%'");
			}

			if(tblSheet.getAuditStaffId() != null) {
				sqlSb.append(" AND TNA.AUDITSTAFFID = ").append(tblSheet.getAuditStaffId());
			}

			if(tblSheet.getStatus() != null) {
				sqlSb.append(" AND TNA.STATE = ").append(tblSheet.getStatus());
			}

			if(tblSheet.getOperateId() != null) {
				sqlSb.append(" AND TNA.TARGETID = ").append(tblSheet.getOperateId());
			}

			if(StringUtils.isNotBlank(tblSheet.getOrgname())) {
				String[] orgnames = tblSheet.getOrgname().split(",");
				sqlSb.append(" AND ( TNA.ORGIDNAMES LIKE '%").append(orgnames[0]).append("%' ");
				if(orgnames.length > 1) {
					for (int i = 1; i < orgnames.length; i++) {
						sqlSb.append(" OR TNA.ORGIDNAMES LIKE '%").append(orgnames[i]).append("%' ");
					}
				}
				sqlSb.append(" )");
			}

			sqlSb.append(" ORDER BY TNA.SHEETID DESC ");
			String sql = sqlSb.toString();
			return sql;
		}
		
		
		public String selectListByhzPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,TblStaffUtil loginStaff)throws Exception  {
			StringBuffer sb = new StringBuffer("SELECT * FROM "
					+ "(SELECT T1.*,ROWNUM RN  FROM "
					+ "(SELECT TNA.*,"
					+ " PRO.PRJOECTNAME projectName, "
					+ " PRO.PROJECTCODE projectCode, "
					+ " PRINCIPAL.REALNAME,ORG.ORGNAME,PRO.AUDITORGNAME auditorgname,PRO.PURPOSE purpose "
					+ "FROM TBL_NBSJ_SHEET TNA "
					+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
					+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
		    		+ " LEFT JOIN  TBL_NBSJ_PROJECT pro on TNA.PROJECTID=PRO.PROJECTID "  
					+ "WHERE TNA.STATE = 6 AND TNA.RISKLEVEL = '是' and ISTYPE is null and (IFSBG <> 1 OR IFSBG IS NULL)");
			
			sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", "TNA.CREATESTAFF", "TNA.STAFFSCOPEIDS", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
			
			if(tBlNbsjSheetVo.getProjectid()!=null) {
				sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getProjectName())) {
				sb.append(" AND PRO.PRJOECTNAME LIKE  '%").append(tBlNbsjSheetVo.getProjectName()).append("%'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getBsusinessAffiliation())) {
				sb.append(" AND TNA.BUSINESSAFFILIATION LIKE '%"+tBlNbsjSheetVo.getBsusinessAffiliation()+"%'");
			}
			
			if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
				sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
			}
			
			if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
				sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
			}
			
			if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
				sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
			}
			
			if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
				sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
			}
			
			sb.append(" ORDER BY TNA.SHEETID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
			return sb.toString();
		}
		
		
		public String selectCountByhzPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,TblStaffUtil loginStaff)throws Exception  {
			StringBuffer sb = new StringBuffer("SELECT COUNT(0) FROM TBL_NBSJ_SHEET TNA "
					+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
					+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
		    		+ " LEFT JOIN  TBL_NBSJ_PROJECT pro on TNA.PROJECTID=PRO.PROJECTID "  
					+ "WHERE TNA.STATE = 6 AND TNA.RISKLEVEL = '是' and ISTYPE is null and (IFSBG <> 1 OR IFSBG IS NULL)");
			
			sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.ORGID", "TNA.ORGID", "TNA.CREATESTAFF", "TNA.STAFFSCOPEIDS", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
			
			
			if(tBlNbsjSheetVo.getProjectid()!=null) {
				sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getProjectName())) {
				sb.append(" AND PRO.PRJOECTNAME LIKE  '%").append(tBlNbsjSheetVo.getProjectName()).append("%'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getBsusinessAffiliation())) {
				sb.append(" AND TNA.BUSINESSAFFILIATION LIKE '%"+tBlNbsjSheetVo.getBsusinessAffiliation()+"%'");
			}
			
			
			if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
				sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
			}
			
			if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
				sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
			}
			
			if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
				sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
			}
			
			if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
				sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
			}
			
			return sb.toString();
		}

		public String selectPageInfoListByIssues(TBlNbsjSheetVo tBlNbsjSheetVo) {
			StringBuffer sb = new StringBuffer("SELECT TNA.*,PRO.PRJOECTNAME projectName,PRO.PROJECTCODE projectCode,PRINCIPAL.REALNAME,ORG.ORGNAME,PRO.AUDITORGNAME auditorgname,PRO.PURPOSE purpose,2 AS AUDITOBJECTTYPE ")
					.append("FROM TBL_NBSJ_SHEET TNA LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG LEFT JOIN  TBL_NBSJ_PROJECT pro on TNA.PROJECTID=PRO.PROJECTID ")
					.append("WHERE TNA.RISKLEVEL = '是' AND TNA.STATE = 6 AND TNA.ISTYPE IS NULL and (IFSBG <> 1 OR IFSBG IS NULL) ");
			//AND TNA.SHEETID NOT IN (SELECT QUESITIONID FROM TBL_ZGZZ_ISSUESILIST WHERE QUESITIONID IS NOT NULL AND ISSUESTYPE = 1) 
			
			if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
				sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getBsusinessAffiliation())) {
				sb.append(" AND TNA.BUSINESSAFFILIATION LIKE '%"+tBlNbsjSheetVo.getBsusinessAffiliation()+"%'");
			}
			
			if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
				sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
			}
			
			if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
				sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getProjectName())) {
				sb.append(" AND PRO.PRJOECTNAME LIKE '%").append(tBlNbsjSheetVo.getProjectName()).append("%'");
			}
			
			if(StringUtils.isNotBlank(tBlNbsjSheetVo.getProjectNo())) {
				sb.append(" AND PRO.PROJECTCODE LIKE '%").append(tBlNbsjSheetVo.getProjectNo()).append("%'");
			}
			
			sb.append(" ORDER BY TNA.SHEETID DESC");
			return sb.toString();
		}

		public String selectListByZgPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
			
			StringBuffer sb = new StringBuffer("SELECT * FROM "
					+ "(SELECT T1.*,ROWNUM RN  FROM "
					+ "(SELECT TNA.*,"
					+ " case when PRO.PRJOECTNAME is not NULL  then  PRO.PRJOECTNAME     when pl.PLANNAME is not NULL   then pl.PLANNAME  else wpro.projectname END projectName, "
					+ " PRINCIPAL.REALNAME,ORG.ORGNAME,PRO.AUDITORGNAME auditorgname,PRO.PURPOSE purpose "
					+ "FROM TBL_NBSJ_SHEET TNA "
					+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
					+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
					+ " LEFT JOIN  TBL_TESTPLAN pl on TNA.PROJECTID=pl.TESTPLANID  "
		    		+ " LEFT JOIN  TBL_NBSJ_PROJECT pro on TNA.PROJECTID=PRO.PROJECTID "  
		    		+ " LEFT JOIN  TBL_NBSJ_WBPROJECT wpro on TNA.PROJECTID=wpro.PROJECTID "
					+ "WHERE 1=1  and ISTYPE is not null ");
			
			if(tBlNbsjSheetVo.getProjectid()!=null) {
				sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
			}
			
			if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
				sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
			}
			
			if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
				sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
			}
			
			if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
				sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
			}
			
			if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
				sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
			}
			
			sb.append(" ORDER BY TNA.SHEETID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
			return sb.toString();
		}


	public String selectCountByZgPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) {
//		TblNbsjSheetEntity plan = pageInfo.getCondition();
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_NBSJ_SHEET TNA "
				+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
				+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
				+ "WHERE 1=1 and ISTYPE is not null  ");
		
		if(tBlNbsjSheetVo.getProjectid()!=null) {
			sb.append(" AND TNA.PROJECTID = '"+tBlNbsjSheetVo.getProjectid()+"'");
		}
		
		if(tBlNbsjSheetVo.getSheetcode()!=null && tBlNbsjSheetVo.getSheetcode().length()>0) {
			sb.append(" AND TNA.SHEETCODE LIKE '%"+tBlNbsjSheetVo.getSheetcode()+"%'");
		}
		
		if(tBlNbsjSheetVo.getSheetname()!=null && tBlNbsjSheetVo.getSheetname().length()>0) {
			sb.append(" AND TNA.SHEETNAME LIKE '%"+tBlNbsjSheetVo.getSheetname()+"%'");
		}
		
		if(tBlNbsjSheetVo.getStatus() != null && tBlNbsjSheetVo.getStatus().length()>0) {
			sb.append(" AND TNA.STATE =  "+tBlNbsjSheetVo.getStatus());
		}
		
		if(tBlNbsjSheetVo.getStaffid() != null && tBlNbsjSheetVo.getStaffid().length()>0) {
			sb.append(" AND PRINCIPAL.STAFFID =  "+tBlNbsjSheetVo.getStaffid());
		}
		
		return sb.toString();
	}
	
	public String selectPlanCodeByDGOrgid(TblNbsjSheetEntity plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_NBSJ_SHEET WHERE 1=1 and ISTYPE is not null  AND SHEETCODE='"+plan.getSheetCode()+"' ");
		if(plan.getSheetId() != null) {
			sb.append(" AND SHEETID != "+plan.getSheetId());
		}
		return sb.toString();
	}
	
		
}
