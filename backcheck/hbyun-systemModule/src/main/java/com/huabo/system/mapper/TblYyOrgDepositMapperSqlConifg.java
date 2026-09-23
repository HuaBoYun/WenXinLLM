package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblYyUserQuery;
import com.huabo.system.service.impl.SysConfig;

import java.math.BigDecimal;

public class TblYyOrgDepositMapperSqlConifg {
	
	
	public String selectListByPageInfo(IPage<TblYyUserQuery> page, TblYyUserQuery yuq) {
		StringBuffer sqlSb = new StringBuffer("Select uq.*,s.REALNAME FROM TBL_YY_USER_QUERY uq " +
				" LEFT JOIN TBL_ORGANIZATION o on uq.ORGID = o.ORGID " +
				" LEFT JOIN TBL_STAFF s ON uq.QUERYSTAFF = s.STAFFID WHERE uq.ORGID ="+yuq.getOrgid());
		if(yuq.getStaffid() != null){
			sqlSb.append(" AND QUERYSTAFF = '"+yuq.getStaffid()+"'");
		}
		if(yuq.getReportName() != null){
			sqlSb.append(" AND uq.REPORTNAME LIKE '%"+yuq.getReportName()+"%'");
		}
		sqlSb.append(" ORDER BY uq.QUERYTIME DESC ");
		String sql = sqlSb.toString();
		return sql;
	}
	
}
