package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblYyUserOrderMySql;

public class TblYyUserOrderMapperSqlMySqlConifg {


    public String selectListByPageInfo(PageInfo<TblYyUserOrderMySql> pageInfo) {
        TblYyUserOrderMySql yuoMySql = pageInfo.getCondition();
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (Select uo.*,S.REALNAME FROM TBL_YY_USER_ORDER uo " +
                " LEFT JOIN TBL_ORGANIZATION o on uo.ORGID = o.ORGID " +
                " LEFT JOIN TBL_STAFF S ON uo.CREATESTAFF = S.STAFFID WHERE uo.ORGID = " + yuoMySql.getOrgid() + " ");
        if (yuoMySql.getOrderno() != null && !"".equals(yuoMySql.getOrderno())) {
            sqlSb.append(" AND uo.ORDERNO = '" + yuoMySql.getOrderno() + "'");
        }
        sqlSb.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        return sqlSb.toString();
    }

    public String selectCountByPageInfo(PageInfo<TblYyUserOrderMySql> pageInfo) {
        TblYyUserOrderMySql yuo = pageInfo.getCondition();
        String sqlCount = "Select COUNT(*) FROM TBL_YY_USER_ORDER uo " +
                " LEFT JOIN TBL_ORGANIZATION o on uo.ORGID = o.ORGID " +
                " LEFT JOIN TBL_STAFF S ON uo.CREATESTAFF = S.STAFFID WHERE uo.ORGID = " + yuo.getOrgid() + " ";
        if (yuo.getOrderno() != null && !"".equals(yuo.getOrderno())) {
            sqlCount += " AND uo.ORDERNO = " + yuo.getOrderno();
        }
        return sqlCount;
    }


}
