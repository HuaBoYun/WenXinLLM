package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;

public class TblYqnsOtherAuditMapperSqlConfig {
	
	public String findListByAnalysis(Integer xmnd, String auditItemName) {
		String sql = " SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
				" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID WHERE ";
		
		sql += " TYO.DRAFTPLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+")";
		sql += " AND TYO.DRAFTPLANID NOT IN (SELECT GLJHXMID FROM TBL_YQNS_XMQD WHERE PLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLJHXMLX = '41')";
		
		if(auditItemName!=null && auditItemName.length()>0) {
			sql+=" and TYO.AUDITITEMNAME like '%"+auditItemName+"%'";
		}
		
		sql += " ORDER BY TYO.AUDITID ASC";
		return sql;
	}
	
	 public String selectListByDraftPlanIdForChoose(BigDecimal draftPlanId, Integer chooseType,
				String auditIdStrs) throws Exception{
		 String sql = " SELECT TYO.*,CTS.REALNAME AS CREATESTAFFNAME,IMORG.ORGNAME AS IMPLORGNAME,IMDEP.ORGNAME AS ITEMDEPTNAME,UNIT.ORGNAME AS PROJECTINITUNITNAME FROM TBL_YQNS_OTHERAUDIT TYO LEFT JOIN TBL_STAFF CTS ON TYO.CREATESTAFFID = CTS.STAFFID" + 
					" LEFT JOIN TBL_ORGANIZATION IMORG ON TYO.IMPLORGID = IMORG.ORGID LEFT JOIN TBL_ORGANIZATION IMDEP ON TYO.ITEMDEPTID = IMDEP.ORGID LEFT JOIN TBL_ORGANIZATION UNIT ON TYO.PROJECTINITUNIT = UNIT.ORGID" + 
					" WHERE TYO.DRAFTPLANID = "+draftPlanId;
		 
		 if(chooseType == 1) {
			 sql += " AND TYO.FIRSTDRAFTPLANID IS NULL";
		 }else if (chooseType == 2) {
			 sql += " AND TYO.SECONDRAFTPLANID IS NULL";
		 }
		 
		 if(StringUtils.isNotBlank(auditIdStrs)) {
			 sql += " AND TYO.AUDITID NOT IN ("+auditIdStrs+")";
		 }
		 
		 return sql;
	 }
}
