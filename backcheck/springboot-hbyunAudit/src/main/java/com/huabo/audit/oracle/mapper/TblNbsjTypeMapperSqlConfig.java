package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjType;

public class TblNbsjTypeMapperSqlConfig {
	public String selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjType> pageInfo,BigDecimal orgid,String auditType) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT * from TBL_NBSJ_TYPE  WHERE ORGID="+orgid);

		if(auditType!=null && auditType.length()>0) {
			sqlSb.append(" and auditType like '%"+auditType+"%'");
		}
		if (pageInfo.getPageSize()!= 15){
			sqlSb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord()+" ORDER BY typeid DESC");
		}else {
			sqlSb.append("and status = 2 ) T1) T2 ORDER BY typeid DESC");
		}
		return sqlSb.toString();
	}
	public String selectNbsjTempleteListCountByPageInfo(PageInfo<TblNbsjType> pageInfo, BigDecimal orgid,String auditType){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_TYPE  WHERE ORGID="+orgid);
		if(auditType!=null && auditType.length()>0) {
			sqlSb.append(" and auditType like '%"+auditType+"%'");
		}
		if (pageInfo.getPageSize() == 15){
			sqlSb.append(" AND STATUS =2 ");
		}
		return sqlSb.toString();
	}
}
