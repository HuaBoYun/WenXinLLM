package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractTypeof;

import java.math.BigDecimal;

public class TblContractTypeofMapperSqlConfig {


	public String findAllList(BigDecimal orgid, Integer parentId) {//ORGID = " + orgid;
		String sql = "SELECT * FROM TBL_CONTRACT_TYPEOF ";
		if(parentId != null && parentId == -1) {
				sql += " WHERE  PARENTID IS NOT NULL";
		}
		return sql.toString();
	}

	public String selectLedgerListPageInfo(IPage<TblContractTypeof> page, TblContractTypeof condition){
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM TBL_CONTRACT_TYPEOF WHERE 1=1 ");//
		if(condition.getTypename() != null && !"".equals(condition.getTypename())) {
			sqlSb.append(" AND TYPENAME LIKE '%"+condition.getTypename()+"%'");
		}
		if(condition.getTypeid() != null ) {
			sqlSb.append(" AND PARENTID = "+condition.getTypeid()+"");
		}else {
			sqlSb.append(" AND PARENTID IS NULL");
		}
		sqlSb.append(" ORDER BY TYPEID ASC ");
		return sqlSb.toString();
	}

	public String saveContractTypeof(TblContractTypeof typeof) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_TYPEOF (TYPEID");
		StringBuffer value = new StringBuffer(" VALUES ("+typeof.getTypeid());

		if(typeof.getTypename() != null) {
			column.append(",TYPENAME");
			value.append(",'"+typeof.getTypename()+"'");
		}
		if(typeof.getCreatestaff() != null) {
			column.append(",CREATESTAFF");
			value.append(",'"+typeof.getCreatestaff()+"'");
		}
		if(typeof.getCreatetime() != null) {
			column.append(",CREATETIME");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(typeof.getCreatetime()));
		}
		if(typeof.getOrgid() != null) {
			column.append(",ORGID");
			value.append(",'"+typeof.getOrgid()+"'");
		}
		if(typeof.getUpdatestaff() != null) {
			column.append(",UPDATESTAFF");
			value.append(",'"+typeof.getUpdatestaff()+"'");
		}
		if(typeof.getSettingid() != null) {
			column.append(",SETTINGID");
			value.append(",'"+typeof.getSettingid()+"'");
		}
		if(typeof.getPageurl() != null) {
			column.append(",PAGEURL");
			value.append(",'"+typeof.getPageurl()+"'");
		}
		if(typeof.getUpdatetime() != null) {
			column.append(",UPDATETIME");
			value.append(","+DataBaseSqlConfig.getDateStrFormat(typeof.getUpdatetime()));
		}
		if(typeof.getParentid() != null) {
			column.append(",PARENTID");
			value.append(",'"+typeof.getParentid()+"'");
		}
		if(typeof.getType() != null) {
			column.append(",TYPE");
			value.append(",'"+typeof.getType()+"'");
		}
		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String selectRepeatTypeName(BigDecimal orgid, String typeNameStr) {
		String sql = "SELECT * FROM TBL_CONTRACT_TYPEOF WHERE ORGID = " + orgid + " AND TYPENAME IN (" + typeNameStr + ")";
		return sql.toString();
	}



	


}
