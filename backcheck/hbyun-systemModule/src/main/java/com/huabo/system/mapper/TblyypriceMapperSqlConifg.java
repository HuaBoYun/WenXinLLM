package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.Tblyyprice;
import com.huabo.system.service.impl.SysConfig;


import java.math.BigDecimal;

public class TblyypriceMapperSqlConifg {
	
	
	public String selectListByPageInfo(IPage<Tblyyprice> page, Find find, BigDecimal orgid) {
		StringBuffer sqlSb = new StringBuffer("SELECT * from TBL_YY_PRICE where COMPANYID = "+orgid);
		if(find!=null && find.getInterfacename()!=null && find.getInterfacename().length()>0){
			sqlSb.append(" AND INTERFACENAME = '" + find.getInterfacename() + "' ");
		}
		sqlSb.append(" ORDER BY PRICEID ");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectRepearBudgetName(String budgetname, BigDecimal orgid, String budgetId) {
		String sql = "SELECT COUNT(0) FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME ='"+budgetname+"' AND ORGID = '"+orgid+"'";
		if(budgetId != null) {
			sql += " AND BUDGETID != "+budgetId;
		}
		return sql;
	}
	

	
}
