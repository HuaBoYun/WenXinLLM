package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.config.SystemStaticValue;
import com.hbfk.util.database.DataBaseSqlConfig;

public class TblUserOrgRelationMapperSqlConfig {

	public String selectUserOrgRelationInfoListByStaffId(BigDecimal staffid, String orgname) throws Exception {
		String sql = "SELECT UOR.*,"+DataBaseSqlConfig.getConcatColumn(DataBaseSqlConfig.getConcatColumn("ORG.ORGNAME", "'/'"),"DEPT.ORGNAME");
		sql += " AS LONGNAME , ORG.ORGNAME AS ORGNAME,DEPT.ORGNAME AS DETPNAME FROM TBL_USER_ORGRELATION UOR LEFT JOIN TBL_ORGANIZATION ORG ON UOR.ORGID = ORG.ORGID LEFT JOIN TBL_ORGANIZATION DEPT ON UOR.DEPTID = DEPT.ORGID WHERE UOR.STAFFID = "+staffid;
		if(StringUtils.isNotBlank(orgname)) {
			sql += " AND (ORG.ORGNAME LIKE '%"+orgname+"%' OR DEPT.ORGNAME LIKE '%"+orgname+"%')";
		}
		sql += " ORDER BY UOR.NUMNO ASC";
		return sql;
	}
}
