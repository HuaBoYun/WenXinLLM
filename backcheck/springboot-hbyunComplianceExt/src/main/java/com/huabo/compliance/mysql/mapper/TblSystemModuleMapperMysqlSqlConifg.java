package com.huabo.compliance.mysql.mapper;

public class TblSystemModuleMapperMysqlSqlConifg {

	public String selectAllRoleListToYM(Integer orgId) {
		String sql = "SELECT RID,RNAME,RDESC,RSTATUS,PKYMROLEID,ORG.PKYMORGID,ORG.ORGNAME FROM TBL_ROLE TR LEFT JOIN TBL_ORGANIZATION ORG ON TR.COMPANYID = ORG.ORGID";
		if(orgId != null) {
			sql += " WHERE TR.COMPANYID = "+orgId;
		}
		return sql;
	}
    
}