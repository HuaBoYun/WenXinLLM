package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractPayment;
import com.huabo.contract.entity.TblCyhwUnit;

public class TblContractPaymentMapperSqlConfig {

	public String selectPaymentManagemenByPageInfo(IPage<TblContractPayment> page, String orgid,TblContractPayment payment, TblCyhwUnit unit) {
        StringBuffer sbSql = new StringBuffer("SELECT TCP.*,TCU.CONTRACTNAME,TCU.CONTRACTNO,TCPB.BUDGETNAME FROM TBL_CONTRACT_PAYMENT TCP " +
                "LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.CONTRACTID = TCU.CONTRACTID " +
                "LEFT JOIN TBL_CYHW_PROJECTBUDGET TCPB ON TCP.BUDGETID = TCPB.BUDGETID WHERE TCP.LINKORG = "+orgid+"");
        if (payment.getCreatestaff() != null) {
            sbSql.append(" AND TCP.CREATESTAFF = " + payment.getCreatestaff());
        }
        if (payment.getPaymenttitle() != null && !"".equals(payment.getPaymenttitle())) {
            sbSql.append(" AND TCP.PAYMENTTITLE LIKE '%" + payment.getPaymenttitle() + "%'");
        }
        if (payment.getContractno() != null && !"".equals(payment.getContractno())) {
            sbSql.append(" AND TCU.CONTRACTNO LIKE '%" + payment.getContractno() + "%'");
        }

        if (payment.getContractname() != null && !"".equals(payment.getContractname())) {
            sbSql.append(" AND TCU.CONTRACTNAME LIKE '%" + payment.getContractname() + "%'");
        }

        if (payment.getBudgetname() != null && !"".equals(payment.getBudgetname())) {
            sbSql.append(" AND TCPB.BUDGETNAME LIKE '%" + payment.getBudgetname() + "%'");
        }

        sbSql.append(" ORDER BY TCP.PAYMENTID DESC ");
        String sql = sbSql.toString();
        return sql;
    }
	
	public String savePaymenInfo(TblContractPayment payment) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_CONTRACT_PAYMENT (PAYMENTID");
        StringBuffer value = new StringBuffer(" VALUES ("+payment.getPaymentid());

        if(payment.getNodeid() != null) {
            column.append(",NODEID");
            value.append(",'"+payment.getNodeid()+"'");
        }
        if(payment.getBudgetid() != null) {
            column.append(",BUDGETID");
            value.append(",'"+payment.getBudgetid()+"'");
        }
        if(payment.getContractid() != null) {
            column.append(",CONTRACTID");
            value.append(",'"+payment.getContractid()+"'");
        }
        if(payment.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'"+payment.getCreatestaff()+"'");
        }
        if(payment.getLinkorg() != null) {
            column.append(",LINKORG");
            value.append(",'"+payment.getLinkorg()+"'");
        }
        if(payment.getPaymenmoney() != null) {
            column.append(",PAYMENMONEY");
            value.append(",'"+payment.getPaymenmoney()+"'");
        }
        if(payment.getPaymenttitle() != null) {
            column.append(",PAYMENTTITLE");
            value.append(",'"+payment.getPaymenttitle()+"'");
        }
        if(payment.getApplystaff() != null) {
            column.append(",APPLYSTAFF");
            value.append(",'"+payment.getApplystaff()+"'");
        }
        if(payment.getApplyorg() != null) {
            column.append(",APPLYORG");
            value.append(",'"+payment.getApplyorg()+"'");
        }
        if(payment.getPaymentrecord() != null) {
            column.append(",PAYMENTRECORD");
            value.append(",'"+payment.getPaymentrecord()+"'");
        }
        if(payment.getPaymenttype() != null) {
            column.append(",PAYMENTTYPE");
            value.append(",'"+payment.getPaymenttype()+"'");
        }
        if(payment.getPaymentlatedate() != null) {
            column.append(",PAYMENTLATEDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(payment.getPaymentlatedate()));
        }
        if(payment.getPaymentmemo() != null) {
            column.append(",PAYMENTMEMO");
            value.append(",'"+payment.getPaymentmemo()+"'");
        }
        if(payment.getPaymentstatus() != null) {
            column.append(",PAYMENTSTATUS");
            value.append(",'"+payment.getPaymentstatus()+"'");
        }
        if(payment.getApplydate() != null) {
            column.append(",APPLYDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(payment.getApplydate()));
        }
        if(payment.getCounterbank() != null) {
            column.append(",COUNTERBANK");
            value.append(",'"+payment.getCounterbank()+"'");
        }
        if(payment.getOrgbank() != null) {
            column.append(",ORGBANK");
            value.append(",'"+payment.getOrgbank()+"'");
        }
        if(payment.getInvoiceid() != null) {
            column.append(",INVOICEID");
            value.append(",'"+payment.getInvoiceid()+"'");
        }
        if(payment.getNoPaymoney() != null) {
            column.append(",NOPAYMONEY");
            value.append(","+payment.getNoPaymoney());
        }
        
        if(payment.getAccumulatedpayments() != null) {
            column.append(",Accumulatedpayments");
            value.append(","+payment.getAccumulatedpayments());
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
