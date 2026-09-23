package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblCounterpartBankinfo;

public class TblCounterpartBankinfoMapperSqlConfig {

	public String findAllListByBankInfo(IPage<TblCounterpartBankinfo> page, TblCounterpartBankinfo bank) throws Exception {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE 1 = 1");
        if (bank.getBudgetid() != null) {
            sbSql.append(" AND BUDGETID = " + bank.getBudgetid());
        }
        if(bank.getBankstatus() != null) {
            sbSql.append(" AND BANKSTATUS = " + bank.getBankstatus());
        }
        if(bank.getBankaccount() != null && !"".equals(bank.getBankaccount())) {
            sbSql.append(" AND BANKACCOUNT LIKE '%"+bank.getBankaccount()+"%'");
        }
        if(bank.getBankaccname() != null && !"".equals(bank.getBankaccname())) {
            sbSql.append(" AND BANKACCNAME LIKE '"+bank.getBankaccname()+"'");
        }
        sbSql.append(" ORDER BY BANKID ASC");
        String sql = sbSql.toString();
        return sql;
    }

	public String findListByPageInfo(IPage<TblCounterpartBankinfo> page, TblCounterpartBankinfo bank) {
    	StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_COUNTERPART_BANKINFO WHERE BANKSTATUS=1 ");
        if (bank.getBudgetid() != null) {
            sbSql.append(" AND BUDGETID = " + bank.getBudgetid());
        }
        if (bank.getBankstatus() != null) {
            sbSql.append(" AND BANKSTATUS = " + bank.getBankstatus());
        }
        if (bank.getBankaccount() != null && !"".equals(bank.getBankaccount())) {
            sbSql.append(" AND BANKACCOUNT LIKE '%" + bank.getBankaccount() + "%'");
        }
        if (bank.getBankaccname() != null && !"".equals(bank.getBankaccname())) {
            sbSql.append(" AND BANKACCNAME LIKE '%" + bank.getBankaccname() + "%'");
        }

        sbSql.append(" ORDER BY BANKID ASC ");
        String sql = sbSql.toString();
        return sql;
    }
	
	public String saveBank(TblCounterpartBankinfo bank) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_COUNTERPART_BANKINFO (BANKID");
        StringBuffer value = new StringBuffer(" VALUES ("+bank.getBankid());

        if(bank.getBankaccount() != null) {
            column.append(",BANKACCOUNT");
            value.append(",'"+bank.getBankaccount()+"'");
        }
        if(bank.getBankaccname() != null) {
            column.append(",BANKACCNAME");
            value.append(",'"+bank.getBankaccname()+"'");
        }
        if(bank.getBankkhyh() != null) {
            column.append(",BANKKHYH");
            value.append(",'"+bank.getBankkhyh()+"'");
        }
        if(bank.getBanktype() != null) {
            column.append(",BANKTYPE");
            value.append(",'"+bank.getBanktype()+"'");
        }
        if(bank.getBanknature() != null) {
            column.append(",BANKNATURE");
            value.append(",'"+bank.getBanknature()+"'");
        }
        if(bank.getBankstatus() != null) {
            column.append(",BANKSTATUS");
            value.append(",'"+bank.getBankstatus()+"'");
        }
        if(bank.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'"+bank.getCreatestaff()+"'");
        }
        if(bank.getOutsideid() != null) {
            column.append(",OUTSIDEID");
            value.append(",'"+bank.getOutsideid()+"'");
        }
        if(bank.getBudgetid() != null) {
            column.append(",BUDGETID");
            value.append(",'"+bank.getBudgetid()+"'");
        }
        if(bank.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(bank.getCreatetime()));
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
