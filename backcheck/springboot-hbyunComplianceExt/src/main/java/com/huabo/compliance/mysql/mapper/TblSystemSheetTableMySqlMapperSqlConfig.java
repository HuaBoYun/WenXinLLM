package com.huabo.compliance.mysql.mapper;


import com.huabo.compliance.entity.TblSystemSheetTable;

import java.math.BigDecimal;


public class TblSystemSheetTableMySqlMapperSqlConfig {
	
	public String selectWorkFlowList(Integer tableId, BigDecimal orgid, String workName) {
		String sql = "SELECT YMWORKFROM,YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = "+tableId+" AND ORGID = "+orgid;
		
		if(workName != null && !"".equals(workName)) {
			sql += " AND YMWORKFROM LIKE '%"+workName+"%'";
		}
		
		return sql;
	}
	
	public String selectSystemFlowList(TblSystemSheetTable sheet) {
		String sql = "SELECT TABLEID,TABLETYPE,IFNULL(RI.NAME,FLOW.FLOWNAME) YMWORKNAME,(SELECT COUNT(0) FROM TBL_SYSTEM_YMWORK WHERE TABLEID = SHEET.TABLEID) WORKCOUNT FROM TBL_SYSTEM_SHEETTABLE SHEET LEFT JOIN TBL_SYSTEM_RIGHT RI ON RI.ID = SHEET.RIGHTID LEFT JOIN TBL_FLOW FLOW ON SHEET.FLOWID = FLOW.FLOWID WHERE 1=1";
		
		if(sheet.getTableType() != null && !"".equals(sheet.getTableType())) {
			sql += " AND TABLETYPE = '"+sheet.getTableType()+"' ";
		}
		
		if(sheet.getYmWorkName() != null && !"".equals(sheet.getYmWorkName())) {
			sql += " AND (RI.NAME LIKE '%"+sheet.getYmWorkName()+"%' OR FLOW.FLOWNAME LIKE '%"+sheet.getYmWorkName()+"%')";
		}
		sql += " ORDER BY TABLEID ASC";
		return sql;
	}
}