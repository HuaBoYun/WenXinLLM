package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblImplog;

public class TblImplogMapperSqlConfig {


	public String findByTblImplogList(PageInfo<TblImplog> pageInfo, String username, String type) {

		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT * FROM TBL_IMP_LOG  WHERE IMPTYPE = '"+type+ "' and IMPCREATEUSERNAME = '"+username+"' ");
		sqlSb.append(" order by IMPCREATETIME desc ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
		String sql = sqlSb.toString();
		return sql;
	}
}
