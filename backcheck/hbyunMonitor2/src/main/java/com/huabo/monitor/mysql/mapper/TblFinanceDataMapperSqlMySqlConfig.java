package com.huabo.monitor.mysql.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblFinanceDataMySql;

import java.math.BigDecimal;

public class TblFinanceDataMapperSqlMySqlConfig {


    public String selectListByPageInfo(PageInfo<TblFinanceDataMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sqlSb = new StringBuffer("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID where 1=1 and A.STATUS=1 AND A.COMPANYID= " + orgid);
        sqlSb.append(" order by A.STARTDATE  LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        return sqlSb.toString();
    }

    public String saveFirst(TblFinanceDataMySql tlf) {
        StringBuffer column = new StringBuffer("INSERT INTO BATHDATA.TBL_FINANCEDATA (ORDERID");
        StringBuffer value = new StringBuffer(" VALUES (" + tlf.getOrderId() + " ");

        if (tlf.getOperType() != null) {
            column.append(",OPERTYPE");
            value.append(",'" + tlf.getOperType() + "'");
        }
        if (tlf.getSourceDbType() != null) {
            column.append(",SOURCEDBTYPE");
            value.append(",'" + tlf.getSourceDbType() + "'");
        }
        if (tlf.getSourceConn() != null) {
            column.append(",SOURCECONN");
            value.append(",'" + tlf.getSourceConn() + "'");
        }
        if (tlf.getSourceUserId() != null) {
            column.append(",SOURCEUSERID");
            value.append(",'" + tlf.getSourceUserId() + "'");
        }
        if (tlf.getSourcePassWord() != null) {
            column.append(",SOURCEPASSWORD");
            value.append(",'" + tlf.getSourcePassWord() + "'");
        }
        if (tlf.getDestDbType() != null) {
            column.append(",DESTDBTYPE");
            value.append(",'" + tlf.getDestDbType() + "'");
        }
        if (tlf.getDestConn() != null) {
            column.append(",DESTCONN");
            value.append(",'" + tlf.getDestConn() + "'");
        }
        if (tlf.getDestUserId() != null) {
            column.append(",DESTUSERID");
            value.append(",'" + tlf.getDestUserId() + "'");
        }
        if (tlf.getDestPassWord() != null) {
            column.append(",DESTPASSWORD");
            value.append(",'" + tlf.getDestPassWord() + "'");
        }
        if (tlf.getCompanyId() != null) {
            column.append(",COMPANYID");
            value.append(",'" + tlf.getCompanyId() + "'");
        }
        if (tlf.getCompanyName() != null) {
            column.append(",COMPANYNAME");
            value.append(",'" + tlf.getCompanyName() + "'");
        }
        if (tlf.getStatus() != null) {
            column.append(",STATUS");
            value.append(",'" + tlf.getStatus() + "'");
        }
        if (tlf.getFid() != null) {
            column.append(",FID");
            value.append(",'" + tlf.getFid() + "'");
        }
        if (tlf.getFvendor() != null) {
            column.append(",FVENDOR");
            value.append(",'" + tlf.getFvendor() + "'");
        }
        if (tlf.getStartdate() != null) {
            column.append(",STARTDATE");
            value.append(",'" + tlf.getStartdate() + "'");
        }
        if (tlf.getEnddate() != null) {
            column.append(",ENDDATE");
            value.append(",'" + tlf.getEnddate() + "'");
        }
        if (tlf.getDestcompanyid() != null) {
            column.append(",DESTCOMPANYID");
            value.append(",'" + tlf.getDestcompanyid() + "'");
        }
        if (tlf.getDestbookid() != null) {
            column.append(",DESTBOOKID");
            value.append(",'" + tlf.getDestbookid() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }

    public String updateFin(TblFinanceDataMySql tlf) {
        StringBuffer sql = new StringBuffer("UPDATE BATHDATA.TBL_FINANCEDATA SET OPERTYPE = '" + tlf.getOperType() + "'");

        if (tlf.getSourceDbType() != null && !"".equals(tlf.getSourceDbType())) {
            sql.append(" , SOURCEDBTYPE = '" + tlf.getSourceDbType() + "'");
        }
        if (tlf.getSourceConn() != null && !"".equals(tlf.getSourceConn())) {
            sql.append(" , SOURCECONN = '" + tlf.getSourceConn() + "'");
        }
        if (tlf.getSourceUserId() != null && !"".equals(tlf.getSourceUserId())) {
            sql.append(" , SOURCEUSERID = '" + tlf.getSourceUserId() + "'");
        }
        if (tlf.getSourcePassWord() != null && !"".equals(tlf.getSourcePassWord())) {
            sql.append(" , SOURCEPASSWORD = '" + tlf.getSourcePassWord() + "'");
        }
        if (tlf.getDestDbType() != null && !"".equals(tlf.getDestDbType())) {
            sql.append(" , DESTDBTYPE = '" + tlf.getDestDbType() + "'");
        }
        if (tlf.getDestConn() != null && !"".equals(tlf.getDestConn())) {
            sql.append(" , DESTCONN = '" + tlf.getDestConn() + "'");
        }
        if (tlf.getDestUserId() != null && !"".equals(tlf.getDestUserId())) {
            sql.append(" , DESTUSERID = '" + tlf.getDestUserId() + "'");
        }
        if (tlf.getDestPassWord() != null && !"".equals(tlf.getDestPassWord())) {
            sql.append(" , DESTPASSWORD = '" + tlf.getDestPassWord() + "'");
        }
        if (tlf.getCompanyId() != null && !"".equals(tlf.getCompanyId())) {
            sql.append(" , COMPANYID = '" + tlf.getCompanyId() + "'");
        }
        if (tlf.getStatus() != null && !"".equals(tlf.getStatus())) {
            sql.append(" , STATUS = '" + tlf.getStatus() + "'");
        }
        if (tlf.getFid() != null && !"".equals(tlf.getFid())) {
            sql.append(" , FID = '" + tlf.getFid() + "'");
        }
        if (tlf.getStartdate() != null && !"".equals(tlf.getStartdate())) {
            sql.append(" , STARTDATE = '" + tlf.getStartdate() + "'");
        }
        if (tlf.getEnddate() != null && !"".equals(tlf.getEnddate())) {
            sql.append(" , ENDDATE = '" + tlf.getEnddate() + "'");
        }
        if (tlf.getDestcompanyid() != null && !"".equals(tlf.getDestcompanyid())) {
            sql.append(" , DESTCOMPANYID = '" + tlf.getDestcompanyid() + "'");
        }
        if (tlf.getDestbookid() != null && !"".equals(tlf.getDestbookid())) {
            sql.append(" , DESTBOOKID = '" + tlf.getDestbookid() + "'");
        }
        if (tlf.getCompanyName() != null && !"".equals(tlf.getCompanyName())) {
            sql.append(" , COMPANYNAME = '" + tlf.getCompanyName() + "'");
        }
        sql.append(" WHERE ORDERID = '" + tlf.getOrderId() + "'");
        return sql.toString();
    }


}
