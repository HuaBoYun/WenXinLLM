package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblAcctBookMySql;

import java.math.BigDecimal;

public class TblAcctBookMySqlDaoSqlConfig {

    public String findByTypeNewZB(PageInfo<TblAcctBookMySql> pageInfo, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ACCBOOK tb left join TBL_ORGANIZATION toz on tb.ORGID = toz.ORGID WHERE toz.ORGID = " + orgid + " AND tb.BOOKDESC IS NULL ORDER BY BOOKYEAR DESC LIMIT " + pageInfo.getCurrentRecord() + "," + pageInfo.getPageSize());
        return sbSql.toString();
    }


//    public String selectCountt(PageInfo<TblAcctBook> pageInfo) {
//        TblAcctBook tblAcctBook = new TblAcctBook();
//        StringBuffer sbSql = new StringBuffer("select COUNT(*) from (SELECT BUDGET.*,ROWNUM RNUM FROM  (SELECT * FROM TBL_ACCBOOK tab left JOIN TBL_ORGANIZATION toz on tab.ORGID =  "+tblAcctBook.getOrgId()+" WHERE toz.orgid = "+tblAcctBook.getOrgId()+"");
//        sbSql.append(") BUDGET WHERE ROWNUM <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RNUM > "+pageInfo.getCurrentRecord());
//        return sbSql.toString();
//    }
}
