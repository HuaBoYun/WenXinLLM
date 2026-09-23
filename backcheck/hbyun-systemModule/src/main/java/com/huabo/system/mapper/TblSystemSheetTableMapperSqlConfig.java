package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblSystemSheetTable;


public class TblSystemSheetTableMapperSqlConfig {
	
	public String selectEntityByProcessId(String processId) throws Exception{
		String sql = "SELECT FF.FORMID,SS.PRIMARYCOLUMN,SS.SECRECTCOLUMN,SS.TABLENAME,SS.STAFFSCOPECOLUMN FROM TBL_SYSTEM_FORMFLOW FF LEFT JOIN TBL_SYSTEM_SHEETTABLE SS ON FF.TABLEID = SS.TABLEID" + 
				" WHERE FF.SUBTIME = (SELECT MAX(SUBTIME) FROM TBL_SYSTEM_FORMFLOW WHERE YMFORMID = '"+processId+"') AND FF.YMFORMID = '"+processId+"' AND " + DataBaseSqlConfig.getRowLimitSql(0, 1);
		return sql;
	}
	
	public String selectEntityById(BigDecimal tableId) throws Exception {
		String sql = "SELECT TABLEID,TABLETYPE,"+DataBaseSqlConfig.getNullColumn("RI.NAME", "FLOW.FLOWNAME")+" AS YMWORKNAME FROM TBL_SYSTEM_SHEETTABLE SHEET LEFT JOIN TBL_SYSTEM_RIGHT RI ON RI.ID = SHEET.RIGHTID LEFT JOIN TBL_FLOW FLOW ON SHEET.FLOWID = FLOW.FLOWID WHERE TABLEID = "+tableId;
		
		return sql;
	}
	
	public String selectWorkFlowList(BigDecimal tableId, BigDecimal orgid, String workName, IPage<TblSystemSheetTable> page) {
		String sql = "SELECT YMWORKFROM,YMWORKNAME,QYSTATS,VERSION,FLOWTEMPLATEID FROM TBL_SYSTEM_YMWORK WHERE TABLEID = "+tableId+" AND ORGID = "+orgid;
		if(workName != null && !"".equals(workName)) {
			sql += " AND YMWORKNAME LIKE '%"+workName+"%'";
		}
		sql += " ORDER BY QYSTATS DESC,YMWORKFROM ASC ";
		return sql;
	}

	public String selectWorkFlowListCount(BigDecimal tableId, BigDecimal orgid, String workName) {
		String sql = "SELECT COUNT(*) FROM TBL_SYSTEM_YMWORK WHERE TABLEID = "+tableId+" AND ORGID = "+orgid;
		if(workName != null && !"".equals(workName)) {
			sql += " AND YMWORKNAME LIKE '%"+workName+"%'";
		}
		sql += " ORDER BY QYSTATS DESC,YMWORKFROM ASC ";
		return sql;
	}
	
	public String selectSystemFlowList(TblSystemSheetTable sheet, BigDecimal orgid) throws Exception {
		String sql = "SELECT TABLEID,TABLETYPE,"+DataBaseSqlConfig.getNullColumn("RI.NAME", "FLOW.FLOWNAME")+" AS YMWORKNAME,(SELECT COUNT(0) FROM TBL_SYSTEM_YMWORK WHERE TABLEID = SHEET.TABLEID AND ORGID = "+orgid+") WORKCOUNT FROM TBL_SYSTEM_SHEETTABLE SHEET LEFT JOIN TBL_SYSTEM_RIGHT RI ON RI.ID = SHEET.RIGHTID LEFT JOIN TBL_FLOW FLOW ON SHEET.FLOWID = FLOW.FLOWID WHERE 1=1";
		
		if(sheet.getTableType() != null && !"".equals(sheet.getTableType())) {
			sql += " AND TABLETYPE = '"+sheet.getTableType()+"' ";
		}
		
		if(sheet.getYmWorkName() != null && !"".equals(sheet.getYmWorkName())) {
			sql += " AND (RI.NAME LIKE '%"+sheet.getYmWorkName()+"%' OR FLOW.FLOWNAME LIKE '%"+sheet.getYmWorkName()+"%')";
		}
		sql += " ORDER BY TABLEID ASC";
		return sql;
	}
	
	public String insertFormFlowTable(BigDecimal fromId, String flowId, String ymflowid, BigDecimal staffId, String deptId, BigDecimal tableId, BigDecimal orgId) throws Exception{
		StringBuffer col = new StringBuffer("INSERT INTO TBL_SYSTEM_FORMFLOW(FORMID,FLOWID,YMFORMID,USERID,TABLEID,ORGID,SUBTIME");
		StringBuffer value = new StringBuffer(" VALUES( ").append(fromId).append(",'").append(flowId).append("','").append(ymflowid).append("',").append(staffId).append(",")
				.append(tableId).append(",").append(orgId).append(",").append(DataBaseSqlConfig.getDateHmsStrFormat(new Date()));
		if(StringUtils.isNotBlank(deptId)) {
			col.append(",DEPTID");
			value.append(",").append(deptId);
		}
		return col.toString()+") "+value.toString() +")";
	}
	
}