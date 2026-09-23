package com.huabo.finance.mappersql;

import java.util.Arrays;

import com.hbfk.util.database.DataBaseSqlConfig;

public class GatherFinanceDataMapperSqlConfig {
	
	public String selectExistPrimaryKey(String primarycol, String pkValStr, String tableName, String fplanid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(primarycol).append(" FROM ").append(tableName).append(" WHERE DATAORIGINFLAG = -2 AND FPLANID = '").append(fplanid).append("'");
		
		sqlSb.append(" AND ").append(DataBaseSqlConfig.getInOrNotSql(primarycol, "IN", "OR", Arrays.asList(pkValStr.split(","))));
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
}
