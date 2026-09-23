package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.FaAccbookinfoUtil;
import com.huabo.finance.vo.OrgOrgsVo;

public class OrgOrgsMapperSqlConfig {
	
	public String selectAllList(OrgOrgsVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM ORG_ORGS WHERE 1 = 1 ");
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			sqlSb.append(" AND DATAORIGINFLAG = -2 AND FPLANID = '").append(bookInfo.getPkFinanplanid()).append("'");
		}
		
		String sql = sqlSb.toString();
		return sql;
	}
}
