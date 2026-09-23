package com.huabo.system.mappersql;

import org.apache.commons.lang.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblSystemImportLog;

public class TblSystemImportLogMapperSqlConfig {
	
	public String selectImportLogPage(IPage<TblSystemImportLog> page, String createTime, Integer importType) throws Exception {
		StringBuffer sb = new StringBuffer("SELECT TSI.*,TS.REALNAME FROM TBL_SYSTEM_IMPORTLOG TSI LEFT JOIN TBL_STAFF TS ON TSI.CREATESTAFF = TS.STAFFID WHERE 1 = 1 ");
		if(StringUtils.isNotBlank(createTime)) {
			sb.append(" AND TSI.CREATETIME >= ").append(DataBaseSqlConfig.getDateStrFormat(createTime));
		}
		if(importType != null) {
			sb.append(" AND TSI.IMPORTTYPE = ").append(importType);
		}
		sb.append(" ORDER BY TSI.CREATETIME DESC");
		String sql = sb.toString();
		return sql;
	}
}
