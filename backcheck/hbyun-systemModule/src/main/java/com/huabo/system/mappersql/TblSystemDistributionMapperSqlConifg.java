package com.huabo.system.mappersql;


import com.hbfk.util.DateUtil;

import com.hbfk.util.PageInfo;
import com.huabo.system.vo.TblSystemDistributionVo;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

public class TblSystemDistributionMapperSqlConifg {
	
	public String selectDistributionAllType(TblSystemDistributionVo distribution) throws Exception{
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT MODULETYPE,DISTRIBUTIONTYPE FROM TBL_SYSTEM_DISTRIBUTION WHERE RECIVER = ").append(distribution.getReciver());
		
		if(StringUtils.isNotBlank(distribution.getModuleType())) {
			sqlSb.append(" AND MODULETYPE =  '").append(distribution.getModuleType()).append("'");
		}
		if(distribution.getIsread() != null) {
			sqlSb.append(" AND ISREAD = ").append(distribution.getIsread());
		}
		
		if(StringUtils.isNotBlank(distribution.getDistributionType())){
			sqlSb.append(" AND DISTRIBUTIONTYPE = '").append(distribution.getDistributionType()).append("'");
		}
		
		
		sqlSb.append(" GROUP BY MODULETYPE,DISTRIBUTIONTYPE ORDER BY MODULETYPE,DISTRIBUTIONTYPE DESC");
		String sql = sqlSb.toString();
		return sql;
	}

	public String selectDistributionListPageInfo(PageInfo<TblSystemDistributionVo> pageInfo) {
		TblSystemDistributionVo condition = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("SELECT T2.* FROM (SELECT T1.*,ROWNUM ROWNO FROM (")
		.append("SELECT TSD.*,CTS.REALNAME AS CREATESTAFFNAME FROM TBL_SYSTEM_DISTRIBUTION TSD LEFT JOIN TBL_STAFF CTS ON TSD.CREATESTAFF = CTS.STAFFID WHERE RECIVER = ").append(condition.getReciver());
		
		if(StringUtils.isNotBlank(condition.getCreateStaffName())) {
			sqlSb.append(" AND CTS.REALNAME LIKE '%").append(condition.getCreateStaffName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getDistributionTitle())) {
			sqlSb.append(" AND TSD.DISTRIBUTIONTITLE LIKE '%").append(condition.getDistributionTitle()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getModuleType())) {
			sqlSb.append(" AND TSD.MODULETYPE =  '").append(condition.getModuleType()).append("'");
		}
		if(condition.getIsread() != null) {
			sqlSb.append(" AND TSD.ISREAD = ").append(condition.getIsread());
		}
		
		if(StringUtils.isNotBlank(condition.getDistributionType())){
			sqlSb.append(" AND TSD.DISTRIBUTIONTYPE = '").append(condition.getDistributionType()).append("'");
		}
		
		if(condition.getCreateStartDate() != null) {
			sqlSb.append(" AND TSD.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(condition.getCreateStartDate(), DateUtil.DATE_SMALL_STR)).append("', 'YYYY-MM-DD')");
		}
		if(condition.getCreateEndDate() != null) {
			sqlSb.append(" AND TSD.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(condition.getCreateEndDate(), DateUtil.DATE_SMALL_STR)).append("', 'YYYY-MM-DD')");
		}
		
		sqlSb.append(" ORDER BY TSD.ISREAD , TSD.CREATETIME DESC ) T1 WHERE ROWNUM <= ").append((pageInfo.getCurrentRecord()+pageInfo.getPageSize())).append(") T2 WHERE ROWNO > ").append(pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	
	public String selectDistributionCountPageInfo(PageInfo<TblSystemDistributionVo> pageInfo) {
		TblSystemDistributionVo condition = pageInfo.getCondition();
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) FROM TBL_SYSTEM_DISTRIBUTION TSD LEFT JOIN TBL_STAFF CTS ON TSD.CREATESTAFF = CTS.STAFFID WHERE RECIVER = ").append(condition.getReciver());
		
		if(StringUtils.isNotBlank(condition.getCreateStaffName())) {
			sqlSb.append(" AND CTS.REALNAME LIKE '%").append(condition.getCreateStaffName()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getDistributionTitle())) {
			sqlSb.append(" AND TSD.DISTRIBUTIONTITLE LIKE '%").append(condition.getDistributionTitle()).append("%'");
		}
		if(StringUtils.isNotBlank(condition.getModuleType())) {
			sqlSb.append(" AND TSD.MODULETYPE =  '").append(condition.getModuleType()).append("'");
		}
		if(condition.getIsread() != null) {
			sqlSb.append(" AND TSD.ISREAD = ").append(condition.getIsread());
		}
		if(StringUtils.isNotBlank(condition.getDistributionType())){
			sqlSb.append(" AND TSD.DISTRIBUTIONTYPE = '").append(condition.getDistributionType()).append("'");
		}
		if(condition.getCreateStartDate() != null) {
			sqlSb.append(" AND TSD.CREATETIME >= TO_DATE('").append(DateUtil.parseDate(condition.getCreateStartDate(), DateUtil.DATE_SMALL_STR)).append("', 'YYYY-MM-DD')");
		}
		if(condition.getCreateEndDate() != null) {
			sqlSb.append(" AND TSD.CREATETIME <= TO_DATE('").append(DateUtil.parseDate(condition.getCreateEndDate(), DateUtil.DATE_SMALL_STR)).append("', 'YYYY-MM-DD')");
		}
		
		return sqlSb.toString();
	}
	
	
	public String getDistributionCount(BigDecimal staffid) {
		StringBuffer sqlSb = new StringBuffer("SELECT count(1) FROM TBL_SYSTEM_DISTRIBUTION TSD LEFT JOIN TBL_STAFF CTS ON TSD.CREATESTAFF = CTS.STAFFID WHERE  MODULETYPE IN ('znsj','nkhg','fxgk') and isread=0 ");
		if(staffid!=null) {
			sqlSb.append(" and RECIVER =").append(staffid);
		}
		return sqlSb.toString();
	}
}
