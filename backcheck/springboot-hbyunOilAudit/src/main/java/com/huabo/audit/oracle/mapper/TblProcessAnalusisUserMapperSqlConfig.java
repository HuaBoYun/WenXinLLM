package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblProcessAnalusisUser;

public class TblProcessAnalusisUserMapperSqlConfig {
	public String insertSetting(TblProcessAnalusisUser analysisUser) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_PROCESS_ANALUSIS_USER (ID");
		StringBuffer value = new StringBuffer(" VALUES (ANALUSIS_USER_SEQUENCE.nextval");
		
		if(analysisUser.getStaffid() != null && !"".equals(analysisUser.getStaffid())) {
			column.append(",STAFFID");
			value.append(",'"+analysisUser.getStaffid()+"'");
		}
		if(analysisUser.getAnalid() != null && !"".equals(analysisUser.getAnalid())) {
			column.append(",ANALID");
			value.append(",'"+analysisUser.getAnalid()+"'");
		}
		if(analysisUser.getSpdate() != null && !"".equals(analysisUser.getSpdate())) {
			column.append(",SPDATE");
			value.append(",TO_DATE('"+DateUtil.parseDate(analysisUser.getSpdate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
		}
		if(analysisUser.getFromid() != null && !"".equals(analysisUser.getFromid())) {
			column.append(",FROMID");
			value.append(",'"+analysisUser.getFromid()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String updateAnalysisUser(TblProcessAnalusisUser analysisUser) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_PROCESS_ANALUSIS_USER SET SPDATE = "+analysisUser.getSpdate()+" ");

		if(analysisUser.getStaffid() != null && !"".equals(analysisUser.getStaffid())) {
			sql.append(" , STAFFID = '"+analysisUser.getStaffid()+"'");
		}
		if(analysisUser.getAnalid() != null && !"".equals(analysisUser.getAnalid())) {
			sql.append(" , ANALID = '"+analysisUser.getAnalid()+"'");
		}
		if(analysisUser.getFromid() != null && !"".equals(analysisUser.getFromid())) {
			sql.append(" , FROMID = '"+analysisUser.getFromid()+"'");
		}
		sql.append(" WHERE id = '"+analysisUser.getId()+"'");
		return sql.toString();
	}
}
