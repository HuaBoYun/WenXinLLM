package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractLend;

public class TblContractLendMapperSqlConfig {

	public String updateTblContractLead(TblContractLend lend) throws Exception {
		StringBuffer sql = new StringBuffer("UPDATE TBL_CONTRACT_LEND SET CONTRACTID = '"+lend.getContractid()+"'");
		if(lend.getUserid() != null ) {
			sql.append(" , USERID = '"+lend.getUserid()+"'");
		}
		if(lend.getMemo() != null && !"".equals(lend.getMemo())) {
			sql.append(" , MEMO = '"+lend.getMemo()+"'");
		}
		if(lend.getLendstatus() != null ) {
			sql.append(" , LENDSTATUS = '"+lend.getLendstatus()+"'");
		}
		if(lend.getLenddate() != null) {
			sql.append(" ,LENDDATE = "+ DataBaseSqlConfig.getDateStrFormat(lend.getLenddate()));
		}
		if(lend.getReturndate() != null) {
			sql.append(" ,RETURNDATE = "+ DataBaseSqlConfig.getDateStrFormat(lend.getReturndate()));
		}
		sql.append(" WHERE LENDID = '"+lend.getLendid()+"'");
		return sql.toString();
	}
	
	
	public String saveTblContractLead(TblContractLend lend) throws Exception {
		StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_LEND (LENDID");
		StringBuffer value = new StringBuffer(" VALUES ("+lend.getLendid());

		if(lend.getContractid() != null) {
			column.append(",CONTRACTID");
			value.append(",'"+lend.getContractid()+"'");
		}
		if(lend.getUserid() != null) {
			column.append(",USERID");
			value.append(",'"+lend.getUserid()+"'");
		}
		if(lend.getMemo() != null) {
			column.append(",MEMO");
			value.append(",'"+lend.getMemo()+"'");
		}
		if(lend.getLendstatus() != null) {
			column.append(",LENDSTATUS");
			value.append(",'"+lend.getLendstatus()+"'");
		}
		if(lend.getLenddate() != null) {
			column.append(",LENDDATE");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(lend.getLenddate()));
		}
		if(lend.getReturndate() != null) {
			column.append(",RETURNDATE");
			value.append(","+ DataBaseSqlConfig.getDateStrFormat(lend.getLenddate()));
		}

		column.append(")");
		value.append(")");
		String sql = column.toString()+value.toString();
		return sql;
	}

	public String findByContractId(IPage<TblContractLend> page, TblContractLend tblContractLead) throws Exception {
		StringBuffer sbSql = new StringBuffer("SELECT TC.LENDID,TC.LENDDATE,TC.RETURNDATE,TC.MEMO,TC.LENDSTATUS,TC.CONTRACTID,TCY.CONTRACTNAME,TCY.CONTRACTNO,TCY.CONTRACTTYPE,TS.STAFFID,TS.REALNAME FROM TBL_CONTRACT_LEND TC LEFT JOIN TBL_CYHW_UNIT TCY ON TC.CONTRACTID = TCY.CONTRACTID LEFT JOIN TBL_STAFF TS ON TC.USERID = TS.STAFFID WHERE 1 = 1");
		if (tblContractLead.getContractid() != null && !"".equals(tblContractLead.getContractid())) {
			sbSql.append(" AND TC.CONTRACTID = " + tblContractLead.getContractid());
		}
		if(tblContractLead.getUserid()!=null && !"".equals(tblContractLead.getUserid())) {
			sbSql.append(" AND TC.USERID = " + tblContractLead.getUserid());
		}
		sbSql.append(" ORDER BY TC.LENDID DESC ");
		String sql = sbSql.toString();
		return sql;
	}

}
