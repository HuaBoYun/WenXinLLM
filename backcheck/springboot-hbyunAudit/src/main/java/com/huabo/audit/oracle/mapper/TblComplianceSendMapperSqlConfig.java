package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblTransferWorkUtils;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblComplianceWeekly;

public class TblComplianceSendMapperSqlConfig {
	
 
	
	public String selectWeeklyListByPageInfo(PageInfo<TblComplianceWeekly> pageInfo,TblComplianceWeekly tblComplianceWeekly,TblStaffUtil loginStaff) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.*,STAFF.REALNAME creatorName "
				+ "FROM TBL_COMPLIANCE_WEEKLY TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.CREATOR "
				+ "WHERE 1=1 ");
		
		
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.BELONGGROUP", "TNA.WORKUNIT",
				"TNA.CREATOR", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
//		if(TblComplianceWeeklyVo.getProjectcode()!=null && TblComplianceWeeklyVo.getProjectcode().length()>0) {
//			sb.append(" AND TNA.PROJECTCODE LIKE '%"+TblComplianceWeeklyVo.getProjectcode()+"%'");
//		}
		if(tblComplianceWeekly.getWeeklytitle()!=null) {
			sb.append(" AND TNA.WEEKLYTITLE LIKE '%"+tblComplianceWeekly.getWeeklytitle()+"%'");
		}
		if(tblComplianceWeekly.getLytype()!=null) {
			sb.append(" AND TNA.LYTYPE = "+tblComplianceWeekly.getLytype()+"");
		}
		if(tblComplianceWeekly.getEffstatus()!=null) {
			sb.append(" AND TNA.EFFSTATUS = "+tblComplianceWeekly.getEffstatus()+"");
		}
		
		
		sb.append(" ORDER BY TNA.ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectWeeklyCountByPageInfo(PageInfo<TblComplianceWeekly> pageInfo,TblComplianceWeekly tblComplianceWeekly,TblStaffUtil loginStaff) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_COMPLIANCE_WEEKLY TNA "
				+ "WHERE 1=1 ");
		
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "TNA.BELONGGROUP", "TNA.WORKUNIT",
				"TNA.CREATOR", "TNA.SECRECTLEVELID", "TNA.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(tblComplianceWeekly.getWeeklytitle()!=null) {
			sb.append(" AND TNA.WEEKLYTITLE LIKE '%"+tblComplianceWeekly.getWeeklytitle()+"%'");
		}
		if(tblComplianceWeekly.getLytype()!=null) {
			sb.append(" AND TNA.LYTYPE = "+tblComplianceWeekly.getLytype()+"");
		}
		if(tblComplianceWeekly.getEffstatus()!=null) {
			sb.append(" AND TNA.EFFSTATUS = "+tblComplianceWeekly.getEffstatus()+"");
		}
		
		return sb.toString();
	}
	
	
	public String updateWeeklyEntity(TblComplianceWeekly tblComplianceWeekly) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_COMPLIANCE_WEEKLY SET ID = '"+tblComplianceWeekly.getId()+"' ");
		if(tblComplianceWeekly.getWeeklytitle() != null && !"".equals(tblComplianceWeekly.getWeeklytitle())) {
			sqlSb.append(" ,WEEKLYTITLE = '"+tblComplianceWeekly.getWeeklytitle()+"'");
		}
		if(tblComplianceWeekly.getMemo() != null && !"".equals(tblComplianceWeekly.getMemo())) {
			sqlSb.append(" ,MEMO = '"+tblComplianceWeekly.getMemo()+"'");
		}
		if(tblComplianceWeekly.getEffdate() != null && !"".equals(tblComplianceWeekly.getEffdate())) {
			sqlSb.append(" ,EFFDATE = TO_DATE('"+DateUtil.parseDate(tblComplianceWeekly.getEffdate(), "yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(tblComplianceWeekly.getEffstatus() != null && !"".equals(tblComplianceWeekly.getEffstatus())) {
			sqlSb.append(" ,EFFSTATUS = '"+tblComplianceWeekly.getEffstatus()+"'");
		}
		if(tblComplianceWeekly.getFileIds() != null && !"".equals(tblComplianceWeekly.getFileIds())) {
			sqlSb.append(" ,FILEIDS = '"+tblComplianceWeekly.getFileIds()+"'");
		}
		if(tblComplianceWeekly.getStatus() != null && !"".equals(tblComplianceWeekly.getStatus())) {
			sqlSb.append(" ,STATUS = '"+tblComplianceWeekly.getStatus()+"'");
		}
		if(tblComplianceWeekly.getLytype() != null && !"".equals(tblComplianceWeekly.getLytype())) {
			sqlSb.append(" ,LYTYPE = '"+tblComplianceWeekly.getLytype()+"'");
		}
		if(tblComplianceWeekly.getSecrectLevelId() != null) {
			sqlSb.append(" ,SECRECTLEVELID = "+tblComplianceWeekly.getSecrectLevelId());
		}
		
		if(StringUtils.isNotBlank(tblComplianceWeekly.getStaffScopeIds())) {
			sqlSb.append(" ,STAFFSCOPEIDS = '"+tblComplianceWeekly.getStaffScopeIds()+"'");
		}
		if(StringUtils.isNotBlank(tblComplianceWeekly.getStaffScopeNames())) {
			sqlSb.append(" ,STAFFSCOPENAMES = '"+tblComplianceWeekly.getStaffScopeNames()+"'");
		}
		sqlSb.append(" WHERE ID= "+tblComplianceWeekly.getId());
		return sqlSb.toString();
	}
	
	public String insertWeeklyEntity(TblComplianceWeekly tblComplianceWeekly){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_COMPLIANCE_WEEKLY(ID,CREATEDTIME,CREATOR,WORKUNIT,BELONGGROUP");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"
				+ "TO_DATE('"+DateUtil.parseDate(tblComplianceWeekly.getCreatedTime(), "yyyy-MM-dd")+"','YYYY-MM-DD'),"
						+ tblComplianceWeekly.getCreator()+","+tblComplianceWeekly.getWorkUnit()+","+tblComplianceWeekly.getBelongGroup());
		
		if(tblComplianceWeekly.getWeeklytitle() != null && !"".equals(tblComplianceWeekly.getWeeklytitle())) {
			colSb.append(",WEEKLYTITLE");
			valSb.append(",'"+tblComplianceWeekly.getWeeklytitle()+"'");
		}
		
		if(tblComplianceWeekly.getMemo() != null && !"".equals(tblComplianceWeekly.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+tblComplianceWeekly.getMemo()+"'");
		}
		
		if(tblComplianceWeekly.getEffdate() != null && !"".equals(tblComplianceWeekly.getEffdate())) {
			colSb.append(",EFFDATE");
			valSb.append(",TO_DATE('"+DateUtil.parseDate(tblComplianceWeekly.getEffdate(), "yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(tblComplianceWeekly.getEffstatus() != null && !"".equals(tblComplianceWeekly.getEffstatus())) {
			colSb.append(",EFFSTATUS");
			valSb.append(",'"+tblComplianceWeekly.getEffstatus()+"'");
		}
		
		if(tblComplianceWeekly.getFileIds() != null && !"".equals(tblComplianceWeekly.getFileIds())) {
			colSb.append(",FILEIDS");
			valSb.append(",'"+tblComplianceWeekly.getFileIds()+"'");
		}
		if(tblComplianceWeekly.getStatus() != null && !"".equals(tblComplianceWeekly.getStatus())) {
			colSb.append(",STATUS");
			valSb.append(","+tblComplianceWeekly.getStatus()+"");
		}
		if(tblComplianceWeekly.getLytype() != null && !"".equals(tblComplianceWeekly.getLytype())) {
			colSb.append(",LYTYPE");
			valSb.append(","+tblComplianceWeekly.getLytype());
		}
		
		if(tblComplianceWeekly.getSecrectLevelId() != null) {
			colSb.append(",SECRECTLEVELID");
			valSb.append(","+tblComplianceWeekly.getSecrectLevelId()+"");
		}
		
		if(StringUtils.isNotBlank(tblComplianceWeekly.getStaffScopeIds())) {
			colSb.append(",STAFFSCOPEIDS");
			valSb.append(",'"+tblComplianceWeekly.getStaffScopeIds()+"'");
		}
		if(StringUtils.isNotBlank(tblComplianceWeekly.getStaffScopeNames())) {
			colSb.append(",STAFFSCOPENAMES");
			valSb.append(",'"+tblComplianceWeekly.getStaffScopeNames()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	
}
