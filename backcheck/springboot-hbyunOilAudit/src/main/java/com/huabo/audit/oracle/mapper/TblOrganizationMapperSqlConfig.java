package com.huabo.audit.oracle.mapper;

import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.util.PageInfo;

public class TblOrganizationMapperSqlConfig {
public String selectListByPageInfo(PageInfo<TblOrganization> pageInfo) {
		
		StringBuffer sb = new StringBuffer("SELECT * FROM "
				+ "(SELECT T1.*,ROWNUM RN  FROM "
				+ "(SELECT TNA.* "
				+ "FROM TBL_ORGANIZATION TNA "
				+ "WHERE 1=1 ");
		
//		if(tblNbsjDoubtfulpointVo.getDpnumber()!=null && tblNbsjDoubtfulpointVo.getDpnumber().length()>0) {
//			sb.append(" AND TNA.DPNUMBER LIKE '%"+tblNbsjDoubtfulpointVo.getDpnumber()+"%'");
//		}
		
		sb.append(" ORDER BY TNA.ORGID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectCountByPageInfo(PageInfo<TblOrganization> pageInfo) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(0) "
				+ "FROM TBL_ORGANIZATION TNA "
				+ "WHERE 1=1 ");
		
//		if(tblNbsjDoubtfulpointVo.getDpnumber()!=null && tblNbsjDoubtfulpointVo.getDpnumber().length()>0) {
//			sb.append(" AND TNA.DPNUMBER LIKE '%"+tblNbsjDoubtfulpointVo.getDpnumber()+"%'");
//		}
		
		return sb.toString();
	}

	public String selectByIds(String ids){
		return "SELECT * FROM TBL_ORGANIZATION WHERE ORGID in ("+ids+")";
	}
}
