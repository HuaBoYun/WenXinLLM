package com.huabo.system.mapper;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblRiskcategory;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblRiskcategoryMapperSqlConifg {

	public String findBysql(String orgid, String type) {
		String sql = "select * from TBL_RISKCATEGORY where RISKCATNAME in ('企业风险','业务风险','专项风险') and unit = '"+ orgid + "'";
		if (StringUtils.isNotEmpty(type)) {
			sql += " and moduletype = '"+type+"'";
		}
		return sql;
	}

	public String save(TblRiskcategory cat) {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_RISKCATEGORY (RISKCATID");
		StringBuffer value = new StringBuffer(" VALUES (").append(cat.getRiskcatid());

		if(cat.getRiskcatnumber() != null) {
			column.append(",RISKCATNUMBER");
			value.append(",'"+cat.getRiskcatnumber()+"'");
		}
		if(cat.getRiskcatname() != null) {
			column.append(",RISKCATNAME");
			value.append(",'"+cat.getRiskcatname()+"'");
		}
		if(cat.getRiskcatdes() != null) {
			column.append(",RISKCATDES");
			value.append(",'"+cat.getRiskcatdes()+"'");
		}
		if(cat.getRiskstatus() != null) {
			column.append(",RISKSTATUS");
			value.append(",'"+cat.getRiskstatus()+"'");
		}
		if(cat.getFatherriskcatid() != null) {
			column.append(",FATHERRISKCATID");
			value.append(",'"+cat.getFatherriskcatid()+"'");
		}
		if(cat.getFullpath() != null) {
			column.append(",FULLPATH");
			value.append(",'"+cat.getFullpath()+"'");
		}
		if(cat.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+cat.getMemo()+"'");
		}
		if(cat.getIsleaf() != null) {
			column.append(",ISLEAF");
			value.append(",'"+cat.getIsleaf()+"'");
		}
		if(cat.getUnit() != null ) {
			column.append(",UNIT");
			value.append(",'"+cat.getUnit()+"'");
		}
		if(cat.getModuletype() != null) {
			column.append(",MODULETYPE");
			value.append(",'"+cat.getModuletype()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}
	
}
