package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblOrgBankaccount;

public class TblOrgBankaccountMapperSqlConfig {

	public String findPlanNodeListForCollection(IPage<TblOrgBankaccount> page, TblOrgBankaccount bank) {
    	StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORG_BANKACCOUNT "
        		+ " WHERE ORGID = " + bank.getOrgid()+""
        		+ " AND BANKSTATE=0 "
        		+ " AND BANKSTATUS=1 ");
        if (bank.getBankaccname() != null && !"".equals(bank.getBankaccname())) {
            sbSql.append(" AND bankAccName LIKE '%" + bank.getBankaccname() + "%'");
        }
        if (bank.getBankaccnum() != null && !"".equals(bank.getBankaccnum())) {
            sbSql.append(" AND bankAccNum LIKE '%" + bank.getBankaccnum() + "%'");
        }
        if (bank.getBankcode() != null && !"".equals(bank.getBankcode())) {
            sbSql.append(" AND bankCode LIKE '%" + bank.getBankcode() + "%'");
        }
        if (bank.getBankkhyh() != null && !"".equals(bank.getBankkhyh())) {
            sbSql.append(" AND bankKhyh LIKE '%" + bank.getBankkhyh() + "%'");
        }
        if (bank.getBankname() != null && !"".equals(bank.getBankname())) {
            sbSql.append(" AND bankName LIKE '%" + bank.getBankname() + "%'");
        }
        if (bank.getBankyhlb() != null && !"".equals(bank.getBankyhlb())) {
            sbSql.append(" AND bankYhlb LIKE '%" + bank.getBankyhlb() + "%'");
        }

        sbSql.append(" ORDER BY BANKID ASC ");
        String sql = sbSql.toString();
        return sql;
    }
	
	public String findListByPageInfoPid(IPage<TblOrgBankaccount> page, BigDecimal pid,TblOrgBankaccount bank) {
		StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ORG_BANKACCOUNT WHERE ORGID = " +pid);

        if (bank.getBankaccname() != null && !"".equals(bank.getBankaccname())) {
            sbSql.append(" AND bankAccName LIKE '%" + bank.getBankaccname() + "%'");
        }
        if (bank.getBankaccnum() != null && !"".equals(bank.getBankaccnum())) {
            sbSql.append(" AND bankAccNum LIKE '%" + bank.getBankaccnum() + "%'");
        }
        if (bank.getBankcode() != null && !"".equals(bank.getBankcode())) {
            sbSql.append(" AND bankCode LIKE '%" + bank.getBankcode() + "%'");
        }
        if (bank.getBankkhyh() != null && !"".equals(bank.getBankkhyh())) {
            sbSql.append(" AND bankKhyh LIKE '%" + bank.getBankkhyh() + "%'");
        }
        if (bank.getBankname() != null && !"".equals(bank.getBankname())) {
            sbSql.append(" AND bankName LIKE '%" + bank.getBankname() + "%'");
        }
        if (bank.getBankyhlb() != null && !"".equals(bank.getBankyhlb())) {
            sbSql.append(" AND bankYhlb LIKE '%" + bank.getBankyhlb() + "%'");
        }

        sbSql.append(" ORDER BY BANKID DESC ");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String savebankInfo(TblOrgBankaccount bank) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORG_BANKACCOUNT (BANKID");
        StringBuffer value = new StringBuffer(" VALUES ("+bank.getBankid());

        if(bank.getBankaccnum() != null && !"".equals(bank.getBankaccnum())) {
            column.append(",BANKACCNUM");
            value.append(",'"+bank.getBankaccnum()+"'");
        }
        if(bank.getBankaccname() != null && !"".equals(bank.getBankaccname())) {
            column.append(",BANKACCNAME");
            value.append(",'"+bank.getBankaccname()+"'");
        }
        if(bank.getBankcode() != null && !"".equals(bank.getBankcode())) {
            column.append(",BANKCODE");
            value.append(",'"+bank.getBankcode()+"'");
        }
        if(bank.getBankkhyh() != null && !"".equals(bank.getBankkhyh())) {
            column.append(",BANKKHYH");
            value.append(",'"+bank.getBankkhyh()+"'");
        }
        if(bank.getBankyhlb() != null && !"".equals(bank.getBankyhlb())) {
            column.append(",BANKYHLB");
            value.append(",'"+bank.getBankyhlb()+"'");
        }
        if(bank.getBankstate() != null && !"".equals(bank.getBankstate())) {
            column.append(",BANKSTATE");
            value.append(",'"+bank.getBankstate()+"'");
        }
        if(bank.getBankstatus() != null && !"".equals(bank.getBankstatus())) {
            column.append(",BANKSTATUS");
            value.append(",'"+bank.getBankstatus()+"'");
        }
        if(bank.getOrgid() != null && !"".equals(bank.getOrgid())) {
            column.append(",ORGID");
            value.append(",'"+bank.getOrgid()+"'");
        }
        if(bank.getBankname() != null && !"".equals(bank.getBankname())) {
            column.append(",BANKNAME");
            value.append(",'"+bank.getBankname()+"'");
        }
        if(bank.getCreatedate() != null && !"".equals(bank.getCreatedate())) {
            column.append(",CREATEDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(bank.getCreatedate()));
        }
        if(bank.getOutsideid() != null && !"".equals(bank.getOutsideid())) {
            column.append(",OUTSIDEID");
            value.append(",'"+bank.getOutsideid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
