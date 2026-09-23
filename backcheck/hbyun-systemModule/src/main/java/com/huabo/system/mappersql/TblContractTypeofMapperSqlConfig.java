package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblContractTypeof;

public class TblContractTypeofMapperSqlConfig {
	
	public String selectPageInfoList(IPage<TblContractTypeof> page, TblContractTypeof condition) {
		StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_CONTRACT_TYPEOF WHERE ORGID = "+condition.getOrgId());

		if(condition.getTypeName() != null && !"".equals(condition.getTypeName())) {
			sqlSb.append(" AND TYPENAME LIKE '%"+condition.getTypeName()+"%'");
		}
		sqlSb.append(" AND PARENTID IS NOT NULL");
		sqlSb.append(" ORDER BY TYPEID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
}
