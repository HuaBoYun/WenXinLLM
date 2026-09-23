package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblOrgNoMySql;

import java.math.BigDecimal;

public class TblOrgNoMapperSqlMySqlConfig {

    public String selectListByPageInfo(PageInfo<TblOrgNoMySql> pageInfo, BigDecimal orgid, BigDecimal fatherrightid) {
//        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (SELECT * from TBL_LOGINTYPE WHERE ORGID =" +orgid);
//        sqlSb.append(" ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
//        return sqlSb.toString();

        StringBuffer sqlSb = new StringBuffer("");
        if (fatherrightid.toString().equals("1")) {
            sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT DISTINCT TON.ORGID,TON.NOID,TON.NOCODE,TON.NOSEPARTOR,TON.NONUMBER,TON.ISUSEDEFAULT,TAI.NONAME FROM TBL_ORG_NO TON LEFT JOIN TBL_AUTONO_INFO TAI ON TON.NOID=TAI.NOID where TON.ORGID = " + orgid + " and getParentList(TAI.PARENTID IN (SELECT NOID FROM TBL_AUTONO_INFO WHERE PARENTID=1 AND NOID IN ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = " + orgid + "))) ");
            sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        } else {
            sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (SELECT DISTINCT TON.ORGID,TON.NOID,TON.NOCODE,TON.NOSEPARTOR,TON.NONUMBER,TON.ISUSEDEFAULT,TAI.NONAME FROM TBL_ORG_NO TON LEFT JOIN TBL_AUTONO_INFO TAI ON TON.NOID=TAI.NOID where TON.ORGID = " + orgid + " and getParentList(TAI.PARENTID = " + fatherrightid + ") ");
            sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " )as a ");
        }
        return sqlSb.toString();
    }

    public String selectCountByPageInfo(BigDecimal orgid, BigDecimal fatherrightid) {
        String sqlCount = "";
        if (fatherrightid.toString().equals("1")) {
            sqlCount = "SELECT count(DISTINCT TON.NOID) FROM TBL_ORG_NO TON LEFT JOIN TBL_AUTONO_INFO TAI ON TON.NOID=TAI.NOID  where TON.ORGID =" + orgid + " and getParentList(TAI.PARENTID IN (SELECT NOID FROM TBL_AUTONO_INFO WHERE PARENTID=1 AND NOID IN ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = " + orgid + ")))";
        } else {
            sqlCount = "SELECT count(DISTINCT TON.NOID) FROM TBL_ORG_NO TON LEFT JOIN TBL_AUTONO_INFO TAI ON TON.NOID=TAI.NOID  where TON.ORGID =" + orgid + " and getParentList(TAI.PARENTID = " + fatherrightid + ") ";
        }
        return sqlCount;
    }


//    public String save(TblOrgNo orgNo) {
//        StringBuffer column = new StringBuffer("INSERT INTO TBL_ORG_NO (NOID");
//        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");
//
//        if(orgNo.getOrgid() != null) {
//            column.append(",ORGID");
//            value.append(",'"+orgNo.getOrgid()+"'");
//        }
//        if(orgNo.getNocode() != null) {
//            column.append(",nocode");
//            value.append(",'"+orgNo.getNocode()+"'");
//        }
//        if(orgNo.getNoSepartor() != null) {
//            column.append(",NOSEPARTOR");
//            value.append(",'"+orgNo.getNoSepartor()+"'");
//        }
//        if(orgNo.getNoNumber() != null) {
//            column.append(",NONUMBER");
//            value.append(",'"+orgNo.getNoNumber()+"'");
//        }
//        if(orgNo.getIsusedefault() != null) {
//            column.append(",ISUSEDEFAULT");
//            value.append(",'"+orgNo.getIsusedefault()+"'");
//        }
//        if(orgNo.getNoSuffix() != null) {
//            column.append(",NOSUFFIX");
//            value.append(",'"+orgNo.getNoSuffix()+"'");
//        }
//        if(orgNo.getNewnumber() != null) {
//            column.append(",NEWNUMBER");
//            value.append(",'"+orgNo.getNewnumber()+"'");
//        }
//
//        column.append(")");
//        value.append(")");
//        String sql = column.toString()+value.toString();
//        return sql;
//    }
}
