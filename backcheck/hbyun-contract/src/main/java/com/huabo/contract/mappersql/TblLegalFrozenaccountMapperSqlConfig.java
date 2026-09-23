package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalFrozenaccount;

public class TblLegalFrozenaccountMapperSqlConfig {

    public String findListByPageInfo(IPage<TblLegalFrozenaccount> page,TblLegalFrozenaccount frozenAccount, BigDecimal disputeid) {
        StringBuffer sqlSb = new StringBuffer("select " +
                "tlf.*,\n" +
                "ts.STAFFID,\n" +
                "ts.REALNAME,\n" +
                "tlp.PROCEEDID,\n" +
                "tlp.PROCEEDNO,\n" +
                "tlp.COURT,\n" +
                "tlp.PORCEEDSTAGE,\n" +
                "tll.ACTIONOBJECT,\n" +
                "tll.DISPUTEID,\n" +
                "tll.DISPUTEINFO,\n" +
                "tll.DISPUTENAME,\n" +
                "tld.PLAINTIFF,\n" +
                "tld.DEFENDANT,\n" +
                "tld.DISPUTEITEM " +
                " from TBL_LEGAL_FROZENACCOUNT tlf " +
                "LEFT JOIN TBL_STAFF ts on tlf.CREATESTAFF = ts.STAFFID  " +
                "LEFT JOIN TBL_LEGAL_PROCEEDINGSRECORD tlp on tlf.PROCEEDINFO = tlp.PROCEEDID " +
                "LEFT JOIN TBL_LEGAL_LITIGATIONSETTLEMENT tll ON tlp.LITIGATIONINFO = tll.LITIGATIONID " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tll.DISPUTEINFO = tld.DISPUTEID " +
                "WHERE tlf.LINKORG = "+ frozenAccount.getLinkorg() );

        if (disputeid != null){
            sqlSb.append(" AND tld.DISPUTEID = " + disputeid);
        }
        if (frozenAccount.getRealname() != null) {
            sqlSb.append(" AND ts.REALNAME like '%" + frozenAccount.getRealname() + "%' ");
        }
        if (frozenAccount.getFrozenblank() != null && !"".equals(frozenAccount.getFrozenblank())) {
            sqlSb.append(" AND tlf.FROZENBLANK LIKE '%" + frozenAccount.getFrozenblank() + "%'");
        }
        if (frozenAccount.getFrozenaccount() != null && !"".equals(frozenAccount.getFrozenaccount())) {
            sqlSb.append(" AND tlf.FROZENACCOUNT LIKE '%" + frozenAccount.getFrozenaccount() + "%'");
        }
        if (frozenAccount.getAccountnature() != null && !"".equals(frozenAccount.getAccountnature())) {
            sqlSb.append(" AND tlf.ACCOUNTNATURE LIKE '%" + frozenAccount.getAccountnature() + "%'");
        }
        if (frozenAccount.getApplyamount() != null) {
            sqlSb.append(" AND tlf.APPLYAMOUNT  >= " + frozenAccount.getApplyamount());
        }
        if (frozenAccount.getMaxApplyAmount() != null) {
            sqlSb.append(" AND tlf.APPLYAMOUNT  <= " + frozenAccount.getMaxApplyAmount());
        }
        if (frozenAccount.getFrozenamount() != null) {
            sqlSb.append(" AND tlf.FROZENAMOUNT  <= " + frozenAccount.getFrozenamount());
        }
        if (frozenAccount.getMinFrozenAmount() != null) {
            sqlSb.append(" AND tlf.FROZENAMOUNT  >= " + frozenAccount.getMinFrozenAmount());
        }
        if (frozenAccount.getKouhuaamount() != null) {
            sqlSb.append(" AND tlf.KOUHUAAMOUNT  <= " + frozenAccount.getKouhuaamount());
        }
        if (frozenAccount.getMinKouhuaAmount() != null) {
            sqlSb.append(" AND tlf.KOUHUAAMOUNT  >= " + frozenAccount.getMinKouhuaAmount());
        }

        sqlSb.append(" ORDER BY inforId DESC");
        String sql = sqlSb.toString();
        return sql;
    }

    public String saveFrozenAccount(TblLegalFrozenaccount frozen) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_FROZENACCOUNT (INFORID");
        StringBuffer value = new StringBuffer(" VALUES ("+frozen.getInforid());

        if(frozen.getProceedinfo() != null) {
            column.append(",PROCEEDINFO");
            value.append(",'"+frozen.getProceedinfo()+"'");
        }
        if(frozen.getCreatetime() != null) {
            column.append(",CREATETIME");
            value.append(","+DataBaseSqlConfig.getDateStrFormat(frozen.getCreatetime()));
        }
        if(frozen.getCreatestaff() != null ) {
            column.append(",CREATESTAFF");
            value.append(",'"+frozen.getCreatestaff()+"'");
        }
        if(frozen.getLinkorg() != null) {
            column.append(",LINKORG");
            value.append(",'"+frozen.getLinkorg()+"'");
        }
        if(frozen.getFrozenblank() != null && !"".equals(frozen.getFrozenblank())) {
            column.append(",FROZENBLANK");
            value.append(",'"+frozen.getFrozenblank()+"'");
        }
        if(frozen.getFrozenaccount() != null && !"".equals(frozen.getFrozenaccount())) {
            column.append(",FROZENACCOUNT");
            value.append(",'"+frozen.getFrozenaccount()+"'");
        }
        if(frozen.getApplyamount() != null) {
            column.append(",APPLYAMOUNT");
            value.append(",'"+frozen.getApplyamount()+"'");
        }
        if(frozen.getFrozenamount() != null) {
            column.append(",FROZENAMOUNT");
            value.append(",'"+frozen.getFrozenamount()+"'");
        }
        if(frozen.getKouhuaamount() != null) {
            column.append(",KOUHUAAMOUNT");
            value.append(",'"+frozen.getKouhuaamount()+"'");
        }
        if(frozen.getAccountnature() != null && !"".equals(frozen.getAccountnature())) {
            column.append(",ACCOUNTNATURE");
            value.append(",'"+frozen.getAccountnature()+"'");
        }
        if(frozen.getStartdate() != null) {
            column.append(",STARTDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(frozen.getStartdate()));
        }
        if(frozen.getEnddate() != null) {
            column.append(",ENDDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(frozen.getEnddate()));
        }
        if(frozen.getFrozenmemo() != null && !"".equals(frozen.getFrozenmemo())) {
            column.append(",FROZENMEMO");
            value.append(",'"+frozen.getFrozenmemo()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateModifyFrozenAccount(TblLegalFrozenaccount oldFrozen) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_FROZENACCOUNT SET FROZENBLANK = '"+oldFrozen.getFrozenblank()+"'");

        if(oldFrozen.getProceedinfo() != null) {
            sql.append(" , PROCEEDINFO = '"+oldFrozen.getProceedinfo()+"'");
        }
        if(oldFrozen.getCreatetime() != null) {
            sql.append(" ,CREATETIME = "+DataBaseSqlConfig.getDateStrFormat(oldFrozen.getCreatetime()));
        }
        if(oldFrozen.getCreatestaff() != null) {
            sql.append(" , CREATESTAFF = '"+oldFrozen.getCreatestaff()+"'");
        }
        if(oldFrozen.getLinkorg() != null) {
            sql.append(" , LINKORG = '"+oldFrozen.getLinkorg()+"'");
        }
//        if(oldFrozen.getFrozenblank() != null && !"".equals(oldFrozen.getFrozenblank())) {
//            sql.append(" , FROZENBLANK = '"+oldFrozen.getFrozenblank()+"'");
//        }
        if(oldFrozen.getFrozenaccount() != null && !"".equals(oldFrozen.getFrozenaccount())) {
            sql.append(" , FROZENACCOUNT = '"+oldFrozen.getFrozenaccount()+"'");
        }
        if(oldFrozen.getApplyamount() != null) {
            sql.append(" , APPLYAMOUNT = '"+oldFrozen.getApplyamount()+"'");
        }
        if(oldFrozen.getFrozenamount() != null) {
            sql.append(" , FROZENAMOUNT = '"+oldFrozen.getFrozenamount()+"'");
        }
        if(oldFrozen.getKouhuaamount() != null ) {
            sql.append(" , KOUHUAAMOUNT = '"+oldFrozen.getKouhuaamount()+"'");
        }
        if(oldFrozen.getAccountnature() != null && !"".equals(oldFrozen.getAccountnature())) {
            sql.append(" , ACCOUNTNATURE = '"+oldFrozen.getAccountnature()+"'");
        }
        if(oldFrozen.getStartdate() != null) {
            sql.append(" ,STARTDATE = "+DataBaseSqlConfig.getDateStrFormat(oldFrozen.getCreatetime()));
        }
        if(oldFrozen.getEnddate() != null) {
            sql.append(" ,ENDDATE = "+DataBaseSqlConfig.getDateStrFormat(oldFrozen.getCreatetime()));
        }
        if(oldFrozen.getFrozenmemo() != null && !"".equals(oldFrozen.getFrozenmemo())) {
            sql.append(" , FROZENMEMO = '"+oldFrozen.getFrozenmemo()+"'");
        }

        sql.append(" WHERE INFORID = '"+oldFrozen.getInforid()+"'");
        return sql.toString();
    }
}
