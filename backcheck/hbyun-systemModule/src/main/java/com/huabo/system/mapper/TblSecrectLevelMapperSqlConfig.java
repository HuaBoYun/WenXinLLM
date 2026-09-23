package com.huabo.system.mapper;

import java.math.BigDecimal;

import com.hbfk.util.database.DataBaseSqlConfig;

public class TblSecrectLevelMapperSqlConfig {

	public String selectListByLoginUser(BigDecimal secrectLevelId) throws Exception {
		
		StringBuffer sqlSb = new StringBuffer("SELECT LEVELID,LEVELNAME FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 2 AND ").append(DataBaseSqlConfig.getWhereColumnInStr("SECRECYSTAFFSCOPE", secrectLevelId.toString(), ","));
		
		String sql = sqlSb.toString();
		
		return sql;
	}
	
}