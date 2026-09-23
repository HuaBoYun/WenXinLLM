package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblLoginType;
import com.huabo.system.entity.TblVideoType;

public class TblVideoTypeMapperSqlConfig {

	
	public String selectListByPageInfo(IPage<TblVideoType> page, Integer orgid) {

		StringBuffer sqlSb = new StringBuffer("select * from TBL_VIDEO_TYPE  WHERE 1=1 AND ORGID = "+orgid);
		sqlSb.append(" ORDER BY TYPEID DESC ");
		String sql = sqlSb.toString();
		return sql;
	}

	public String insertTblVideoType(TblVideoType tnt) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_VIDEO_TYPE (TYPEID");
		StringBuffer value = new StringBuffer(" VALUES (").append(tnt.getTypeId());

		if(tnt.getTypename() != null) {
			column.append(",TYPENAME");
			value.append(",'"+tnt.getTypename()+"'");
		}
		if(tnt.getVersion() != null) {
			column.append(",VERSION");
			value.append(",'"+tnt.getVersion()+"'");
		}
		if(tnt.getOrgid() != null) {
			column.append(",ORGID");
			value.append(",'"+tnt.getOrgid()+"'");
		}
		if(tnt.getType() != null) {
			column.append(",TYPE");
			value.append(",'"+tnt.getType()+"'");
		}
		
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String updateTblVideoType(TblVideoType tnt) {
		StringBuffer sql = new StringBuffer("UPDATE TBL_VIDEO_TYPE SET TYPENAME = '"+tnt.getTypename()+"'");

		if(tnt.getVersion() != null && !"".equals(tnt.getVersion())) {
			sql.append(" , VERSION = '"+tnt.getVersion()+"'");
		}
		if(tnt.getOrgid() != null && !"".equals(tnt.getOrgid())) {
			sql.append(" , ORGID = '"+tnt.getOrgid()+"'");
		}
		if(tnt.getType() != null && !"".equals(tnt.getType())) {
			sql.append(" , TYPE = '"+tnt.getType()+"'");
		}
		sql.append(" WHERE TYPEID = '"+tnt.getTypeId()+"'");
		return sql.toString();
	}
}
