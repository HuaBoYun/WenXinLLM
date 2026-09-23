package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjType;

public class TblNbsjTypeMapperSqlConfig {
	public String selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjType> pageInfo,BigDecimal orgid) throws Exception{
		StringBuffer sqlSb = new StringBuffer();
		sqlSb.append("SELECT * from TBL_NBSJ_TYPE  WHERE ORGID="+orgid);
		sqlSb.append(" ORDER BY typeid DESC");
		return sqlSb.toString();
	}
	public String selectNbsjTempleteListCountByPageInfo(PageInfo<TblNbsjType> pageInfo, BigDecimal orgid){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_TYPE  WHERE ORGID="+orgid);
		return sqlSb.toString();
	}
}
