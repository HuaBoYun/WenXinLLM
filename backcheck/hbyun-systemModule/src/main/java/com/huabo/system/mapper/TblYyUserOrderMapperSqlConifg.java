package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblYyUserOrder;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblYyUserOrderMapperSqlConifg {
	
	
	public String selectListByPageInfo(IPage<TblYyUserOrder> page, TblYyUserOrder yuo) {
		StringBuffer sqlSb = new StringBuffer("Select uo.*,S.REALNAME FROM TBL_YY_USER_ORDER uo " +
				" LEFT JOIN TBL_ORGANIZATION o on uo.ORGID = o.ORGID " +
				" LEFT JOIN TBL_STAFF S ON uo.CREATESTAFF = S.STAFFID WHERE uo.ORGID = "+yuo.getOrgid()+" ");
		if(yuo.getOrderno() != null && !"".equals(yuo.getOrderno())){
			sqlSb.append(" AND uo.ORDERNO = '"+yuo.getOrderno()+"'");
		}
		sqlSb.append(" ORDER BY ORDERID DESC ");
		return sqlSb.toString();
	}
	
}
