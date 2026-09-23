package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblMonitorSolutionMySql;

import java.math.BigDecimal;

public class TblMonitorSolutionMapperSqlMySqlConfig {


    public String tblMonitorSolutionService(PageInfo<TblMonitorSolutionMySql> pageInfo, BigDecimal staffid, String type) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM (SELECT TMS.* from TBL_MONITOR_SOLUTION TMS where SOLUTIONID in ( SELECT SOLUTIONID FROM TBL_MONITOR_SOLUTION_STAFF WHERE STAFFID= '" + staffid + "' ) and TYPE= '" + type + "' ");

        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize()  + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }

    public String tblMonitorSolutionServiceCount(PageInfo<TblMonitorSolutionMySql> pageInfo,BigDecimal staffid, String type) {
        String sql = "SELECT count(*) from TBL_MONITOR_SOLUTION where SOLUTIONID in ( SELECT SOLUTIONID FROM TBL_MONITOR_SOLUTION_STAFF WHERE STAFFID= "+ staffid +" ) and TYPE= '"+ type + "' ";
        return sql;
    }
}
