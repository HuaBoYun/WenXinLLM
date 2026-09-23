package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblYyUserQueryMySql;

public class TblYyOrgDepositMapperSqlMySqlConifg {


    public String selectListByPageInfo(PageInfo<TblYyUserQueryMySql> pageInfo) {
        TblYyUserQueryMySql yuqMySql = pageInfo.getCondition();
        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.* FROM (Select uq.*,s.REALNAME FROM TBL_YY_USER_QUERY uq " +
                " LEFT JOIN TBL_ORGANIZATION o on uq.ORGID = o.ORGID " +
                " LEFT JOIN TBL_STAFF s ON uq.QUERYSTAFF = s.STAFFID WHERE uq.ORGID =" + yuqMySql.getOrgid());
        if (yuqMySql.getStaffid() != null) {
            sqlSb.append(" AND QUERYSTAFF = '" + yuqMySql.getStaffid() + "'");
        }
        if (yuqMySql.getReportName() != null) {
            yuqMySql.setReportName("%" + yuqMySql.getReportName() + "%");
            sqlSb.append(" AND uq.REPORTNAME LIKE '%" + yuqMySql.getReportName() + "%'");
        }
        sqlSb.append(" ORDER BY uq.QUERYTIME DESC ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + ") AS A ");
        String sql = sqlSb.toString();
        return sql;
    }

    public String selectCountByPageInfo(PageInfo<TblYyUserQueryMySql> pageInfo) {
        TblYyUserQueryMySql yuq = pageInfo.getCondition();
        String hqlCount = "Select COUNT(*) FROM TBL_YY_USER_QUERY uq " +
                " LEFT JOIN TBL_ORGANIZATION o on uq.ORGID = o.ORGID " +
                " LEFT JOIN TBL_STAFF s ON uq.QUERYSTAFF = s.STAFFID WHERE uq.ORGID = '" + yuq.getOrgid() + "'";
        if (yuq.getStaffid() != null) {
            hqlCount += " AND QUERYSTAFF ='" + yuq.getStaffid() + "'   ";
        }
        if (yuq.getReportName() != null) {
            yuq.setReportName("%" + yuq.getReportName() + "%");
            hqlCount += " AND uq.REPORTNAME LIKE '%" + yuq.getReportName() + "%'";
        }
        return hqlCount;
    }


}
