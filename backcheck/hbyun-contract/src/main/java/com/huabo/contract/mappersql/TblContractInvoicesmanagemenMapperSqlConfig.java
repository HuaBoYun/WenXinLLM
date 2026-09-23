package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractInvoicesmanagemen;

public class TblContractInvoicesmanagemenMapperSqlConfig {
	
	public String saveMergenEntity(TblContractInvoicesmanagemen oldInvoice) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_INVOICESMANAGEMEN (INVOICEID");
        StringBuffer value = new StringBuffer(" VALUES ("+oldInvoice.getInvoiceid());

        if(oldInvoice.getBudgetId()!= null && !"".equals(oldInvoice.getBudgetId())) {
            column.append(",BUDGETID");
            value.append(",'"+oldInvoice.getBudgetId()+"'");
        }
        if(oldInvoice.getInvoiceno()!= null && !"".equals(oldInvoice.getInvoiceno())) {
            column.append(",INVOICENO");
            value.append(",'"+oldInvoice.getInvoiceno()+"'");
        }
        if(oldInvoice.getInvoicemoney() != null && !"".equals(oldInvoice.getInvoicemoney())) {
            column.append(",INVOICEMONEY");
            value.append(",'"+oldInvoice.getInvoicemoney()+"'");
        }
        if(oldInvoice.getInvoicedate() != null && !"".equals(oldInvoice.getInvoicedate())) {
            column.append(",INVOICEDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(oldInvoice.getInvoicedate()));
        }
        if(oldInvoice.getInvoiceheadtext() != null && !"".equals(oldInvoice.getInvoiceheadtext())) {
            column.append(",INVOICEHEADTEXT");
            value.append(",'"+oldInvoice.getInvoiceheadtext()+"'");
        }
        if(oldInvoice.getInvoicepost() != null && !"".equals(oldInvoice.getInvoicepost())) {
            column.append(",INVOICEPOST");
            value.append(",'"+oldInvoice.getInvoicepost()+"'");
        }
        if(oldInvoice.getInvoicecontent() != null && !"".equals(oldInvoice.getInvoicecontent())) {
            column.append(",INVOICECONTENT");
            value.append(",'"+oldInvoice.getInvoicecontent()+"'");
        }
        if(oldInvoice.getInvoicetype() != null && !"".equals(oldInvoice.getInvoicetype())) {
            column.append(",INVOICETYPE");
            value.append(",'"+oldInvoice.getInvoicetype()+"'");
        }
        if(oldInvoice.getInvoicestatus() != null && !"".equals(oldInvoice.getInvoicestatus())) {
            column.append(",INVOICESTATUS");
            value.append(",'"+oldInvoice.getInvoicestatus()+"'");
        }
        if(oldInvoice.getInvoicespdate() != null && !"".equals(oldInvoice.getInvoicespdate())) {
            column.append(",INVOICESPDATE");
            value.append(",TO_DATE('"+ DataBaseSqlConfig.getDateStrFormat(oldInvoice.getInvoicespdate()));        
        }
        if(oldInvoice.getInvoicesporg() != null && !"".equals(oldInvoice.getInvoicesporg())) {
            column.append(",INVOICESPORG");
            value.append(",'"+oldInvoice.getInvoicesporg()+"'");
        }
        if(oldInvoice.getInvoicekporg() != null && !"".equals(oldInvoice.getInvoicekporg())) {
            column.append(",INVOICEKPORG");
            value.append(",'"+oldInvoice.getInvoicekporg()+"'");
        }
        if(oldInvoice.getInvoiceogr() != null && !"".equals(oldInvoice.getInvoiceogr())) {
            column.append(",INVOICEOGR");
            value.append(",'"+oldInvoice.getInvoiceogr()+"'");
        }
        if(oldInvoice.getCreatestaff() != null && !"".equals(oldInvoice.getCreatestaff())) {
            column.append(",CREATESTAFF");
            value.append(",'"+oldInvoice.getCreatestaff()+"'");
        }
        if(oldInvoice.getTinumber() != null && !"".equals(oldInvoice.getTinumber())) {
            column.append(",TINUMBER");
            value.append(",'"+oldInvoice.getTinumber()+"'");
        }
        if(oldInvoice.getOutsideid() != null && !"".equals(oldInvoice.getOutsideid())) {
            column.append(",OUTSIDEID");
            value.append(",'"+oldInvoice.getOutsideid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

	public String findInvoiceInfoListForCollection(IPage<TblContractInvoicesmanagemen> page,TblContractInvoicesmanagemen invoice) {
        StringBuffer sbSql = new StringBuffer("SELECT INVOICEID,INVOICENO,INVOICEMONEY,INVOICETYPE,INVOICEDATE,INVOICESTATUS,INVOICEKPORG,INVOICEHEADTEXT  FROM TBL_CONTRACT_INVOICESMANAGEMEN WHERE INVOICEOGR = " + invoice.getInvoiceogr() + " AND INVOICESTATUS != 5 AND INVOICEID NOT IN (SELECT INVOICEID FROM TBL_CONTRACT_COLLECTION WHERE LINKORG = " + invoice.getInvoiceogr() + " AND INVOICEID IS NOT NULL UNION ALL SELECT INVOICEID FROM TBL_CONTRACT_PAYMENT WHERE LINKORG = "+invoice.getInvoiceogr()+" AND INVOICEID IS NOT NULL)");
        if (invoice.getInvoiceno() != null && !"".equals(invoice.getInvoiceno())) {
            sbSql.append(" AND INVOICENO LIKE '%" + invoice.getInvoiceno() + "%'");
        }
        if (invoice.getInvoiceheadtext() != null && !"".equals(invoice.getInvoiceheadtext())) {
            sbSql.append(" AND INVOICEHEADTEXT LIKE '%" + invoice.getInvoiceheadtext() + "%'");
        }
        if (invoice.getInvoicekporg() != null && !"".equals(invoice.getInvoicekporg())) {
            sbSql.append(" AND INVOICEKPORG LIKE '%" + invoice.getInvoicekporg() + "%'");
        }
        if (invoice.getInvoicetype() != null && !"".equals(invoice.getInvoicetype())) {
            sbSql.append(" AND INVOICETYPE = '" + invoice.getInvoicetype() + "'");
        }
        if (invoice.getBudgetId() != null) {
            sbSql.append(" AND BUDGETID = " + invoice.getBudgetId());
        }

        sbSql.append(" ORDER BY INVOICEID DESC");
        String sql = sbSql.toString();
        return sql;
    }
	
	public String findContractInvociesManaeMenPageInfo(IPage<TblContractInvoicesmanagemen> page,TblContractInvoicesmanagemen invoice) {
		StringBuffer sbSql = new StringBuffer("SELECT tcp.BUDGETNAME budgetName, ts.REALNAME realname, tci.*  FROM TBL_CONTRACT_INVOICESMANAGEMEN tci" +
                " left join TBL_STAFF ts on tci.CREATESTAFF = ts.STAFFID LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON tci.BUDGETID = tcp.BUDGETID " +
                " WHERE INVOICEOGR = " + invoice.getInvoiceogr());
        if (invoice.getCreatestaff() != null) {
            sbSql.append(" AND CREATESTAFF = " + invoice.getCreatestaff());
        }
        if (invoice.getInvoiceno() != null && !"".equals(invoice.getInvoiceno())) {
            sbSql.append(" AND INVOICENO LIKE '%" + invoice.getInvoiceno() + "%'");
        }
        if (invoice.getInvoiceheadtext() != null && !"".equals(invoice.getInvoiceheadtext() )) {
            sbSql.append(" AND INVOICEHEADTEXT LIKE '%" + invoice.getInvoiceheadtext()  + "%'");
        }
        if (invoice.getInvoicecontent() != null &&  !"".equals(invoice.getInvoicecontent() )){
            sbSql.append(" AND INVOICECONTENT LIKE '%" + invoice.getInvoicecontent()  + "%'");
        }
        if (invoice.getInvoicetype() != null && !"".equals(invoice.getInvoicetype())) {
            sbSql.append(" AND INVOICETYPE = '" + invoice.getInvoicetype() + "'");
        }
        if (invoice.getInvoicestatus() != null) {
            sbSql.append(" AND INVOICESTATUS = " + invoice.getInvoicestatus());
        }
        sbSql.append(" ORDER BY INVOICEID DESC");
        String sql = sbSql.toString();
        return sql;
	}
	
	public String selectListCount(TblContractInvoicesmanagemen invoice) {
		StringBuffer sbSql = new StringBuffer("SELECT COUNT(*) FROM TBL_CONTRACT_INVOICESMANAGEMEN WHERE INVOICEOGR = " + invoice.getInvoiceogr() + " AND INVOICENO = '" + invoice.getInvoiceno() + "'");
        if (invoice.getInvoiceid() != null) {
            sbSql.append(" AND INVOICEID != " + invoice.getInvoiceid());
        }
        String sql = sbSql.toString();
        return sql;
	}
    
}
