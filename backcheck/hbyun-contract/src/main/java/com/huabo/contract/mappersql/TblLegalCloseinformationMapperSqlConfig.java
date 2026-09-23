package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblLegalCloseinformation;

public class TblLegalCloseinformationMapperSqlConfig {

    public String findListByPageInfo(IPage<TblLegalCloseinformation> page, TblLegalCloseinformation closeInfo,BigDecimal disputeid) throws Exception {
        StringBuffer sqlSb = new StringBuffer("SELECT " +
                "tlc.*,\n" +
                "tld.DISPUTENO,\n" +
                "tld.DISPUTETYPE,\n" +
                "tld.DISPUTEITEM,\n" +
                "tld.CONTRACTINFO,\n" +
                "tld.DISPUTECOURS,\n" +
                "tld.WHETHERSUED,\n" +
                "tld.LITIGATIONPOS,\n" +
                "tld.DISPUTEID DISPUTEIDONE,\n" +
                "tld.DISPUTEID,\n" +
                "ts.REALNAME" +
                " FROM TBL_LEGAL_CLOSEINFORMATION tlc " +
                "LEFT JOIN TBL_STAFF ts on tlc.CREATESTAFF = ts.STAFFID " +
                "LEFT JOIN TBL_LEGAL_DISPUTREGISTRATION tld on tlc.DISPUTINFO = tld.DISPUTEID " +
                "WHERE tlc.LINKORG = "+ closeInfo.getLinkorg() );

        if (disputeid != null){
            sqlSb.append(" AND tld.DISPUTEID = " + disputeid );
        }
        if (closeInfo.getRealname() != null && !"".equals(closeInfo.getRealname())) {
            sqlSb.append(" AND ts.REALNAME LIKE '%" + closeInfo.getRealname() + "%'");
        }
        if (closeInfo.getDisputeitem() != null && !"".equals(closeInfo.getDisputeitem())) {
            sqlSb.append(" AND tld.DISPUTEITEM LIKE '%" + closeInfo.getDisputeitem() + "%'");
        }
        if (closeInfo.getMinmoney() != null) {
            sqlSb.append(" AND JUDGEMENTAMOUNT >= " + closeInfo.getMinmoney());
        }

        if (closeInfo.getMaxmoney() != null) {
            sqlSb.append(" AND JUDGEMENTAMOUNT <= " + closeInfo.getMaxmoney());
        }

        sqlSb.append(" ORDER BY closeId DESC");
        String sql = sqlSb.toString();
        return sql;
    }



    public String addDisputeSettlement(TblLegalCloseinformation closeInfo) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LEGAL_CLOSEINFORMATION (CLOSEID,CLOSEDATE,CREATETIME");
        StringBuffer value = new StringBuffer(" VALUES ("+closeInfo.getCloseid()+","+DataBaseSqlConfig.getDateStrFormat(closeInfo.getClosedate()) +","+DataBaseSqlConfig.getDateStrFormat(closeInfo.getCreatetime()));

//        if(closeInfo.getClosedate() != null && !"".equals(closeInfo.getClosedate())) {
//            column.append(",CLOSEDATE");
//            value.append(",TO_DATE('"+ DateUtil.parseDate(closeInfo.getClosedate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
//        }
        if(closeInfo.getJudgementamount() != null) {
            column.append(",JUDGEMENTAMOUNT");
            value.append(",'"+closeInfo.getJudgementamount()+"'");
        }
        if(closeInfo.getCloseresult() != null && !"".equals(closeInfo.getCloseresult())) {
            column.append(",CLOSERESULT");
            value.append(",'"+closeInfo.getCloseresult()+"'");
        }
        if(closeInfo.getManagerecommond() != null && !"".equals(closeInfo.getManagerecommond())) {
            column.append(",MANAGERECOMMOND");
            value.append(",'"+closeInfo.getManagerecommond()+"'");
        }
        if(closeInfo.getDisputinfo() != null) {
            column.append(",DISPUTINFO");
            value.append(",'"+closeInfo.getDisputinfo()+"'");
        }
        if(closeInfo.getCreatestaff() != null) {
            column.append(",CREATESTAFF");
            value.append(",'"+closeInfo.getCreatestaff()+"'");
        }
//        if(closeInfo.getCreatetime() != null && !"".equals(closeInfo.getCreatetime())) {
//            column.append(",CREATETIME");
//            value.append(",'"+closeInfo.getCreatetime()+"'");
//        }
        if(closeInfo.getLinkorg() != null) {
            column.append(",LINKORG");
            value.append(",'"+closeInfo.getLinkorg()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateModifyDisputeSettlementModify(TblLegalCloseinformation oldCloseInfo) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LEGAL_CLOSEINFORMATION SET JUDGEMENTAMOUNT = '"+oldCloseInfo.getJudgementamount()+"'");
        if(oldCloseInfo.getClosedate() != null) {
            sql.append(" ,CLOSEDATE = "+ DataBaseSqlConfig.getDateStrFormat(oldCloseInfo.getClosedate()));
        }
        if(oldCloseInfo.getCloseresult() != null && !"".equals(oldCloseInfo.getCloseresult())) {
            sql.append(" , CLOSERESULT = '"+oldCloseInfo.getCloseresult()+"'");
        }
        if(oldCloseInfo.getManagerecommond() != null && !"".equals(oldCloseInfo.getManagerecommond())) {
            sql.append(" , MANAGERECOMMOND = '"+oldCloseInfo.getManagerecommond()+"'");
        }
        if(oldCloseInfo.getDisputinfo() != null) {
            sql.append(" , DISPUTINFO = '"+oldCloseInfo.getDisputinfo()+"'");
        }
        if(oldCloseInfo.getCreatestaff() != null) {
            sql.append(" , CREATESTAFF = '"+oldCloseInfo.getCreatestaff()+"'");
        }
        if(oldCloseInfo.getCreatetime() != null) {
            sql.append(" ,CREATETIME = "+ DataBaseSqlConfig.getDateStrFormat(oldCloseInfo.getCreatetime()));
            //sql.append(" , CREATETIME = '"+oldCloseInfo.getCreatetime()+"'");
        }
        if(oldCloseInfo.getLinkorg() != null) {
            sql.append(" , LINKORG = '"+oldCloseInfo.getLinkorg()+"'");
        }


        sql.append(" WHERE CLOSEID = '"+oldCloseInfo.getCloseid()+"'");
        return sql.toString();
    }
}
