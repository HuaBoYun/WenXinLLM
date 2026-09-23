package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.system.entity.TblRole;

public class TblRoleMapperSqlConfig {
	
	public String addRoleReturnId(TblRole role) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_ROLE (RID");
		StringBuffer value = new StringBuffer(" VALUES (").append(RandomUtil.uuBigDecimalId());

		if(role.getRname() != null) {
			column.append(",RNAME");
			value.append(",'"+role.getRname()+"'");
		}
		if(role.getRdesc() != null) {
			column.append(",RDESC");
			value.append(",'"+role.getRdesc()+"'");
		}
		if(role.getCompanyid() != null) {
			column.append(",COMPANYID");
			value.append(",'"+role.getCompanyid()+"'");
		}
		if(role.getRstatus() != null) {
			column.append(",RSTATUS");
			value.append(",'"+role.getRstatus()+"'");
		}
		if(StringUtils.isNotBlank(role.getPkYmRoleId())) {
			column.append(",PKYMROLEID");
			value.append(",'"+role.getPkYmRoleId()+"'");
		}
		
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String selectAllRoleListToYM(Integer orgId) {
		String sql = "SELECT RID,RNAME,RDESC,RSTATUS,PKYMROLEID,ORG.PKYMORGID,ORG.ORGNAME FROM TBL_ROLE TR LEFT JOIN TBL_ORGANIZATION ORG ON TR.COMPANYID = ORG.ORGID";
		if(orgId != null) {
			sql += " WHERE TR.COMPANYID = "+orgId;
		}
		return sql;
	}
    
	public String selectAllRoleInfoToYM(BigDecimal rid) {
		String sql = "SELECT RID,RNAME,RDESC,RSTATUS,PKYMROLEID,ORG.PKYMORGID,ORG.ORGNAME FROM TBL_ROLE TR LEFT JOIN TBL_ORGANIZATION ORG ON TR.COMPANYID = ORG.ORGID WHERE TR.RID = "+rid;
		return sql;
	}
}