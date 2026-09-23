package com.huabo.monitor.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblMonitorSolutionresultMySql;

import java.math.BigDecimal;

public class TblMonitorSolutionresultMapperSqlMySqlConfig {


    public String findBySoultionIdZKZX(String zt, String table, BigDecimal soultionId, String rulid, PageInfo<TblMonitorSolutionresultMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( select *  from '" + zt + "'.'" + table + "' where  EXECTIME = (SELECT SIGNID FROM TBL_MONITOR_PREWARNING WHERE SOLUTIONRESULTID = ( select SOLUTIONRESULTID from ( SELECT SOLUTIONRESULTID FROM TBL_MONITOR_SOLUTIONRESULT  WHERE SOURCE = 3 AND SOLUTIONID = '" + soultionId + "' order by savetime desc)) and RULEID= '" + rulid + "') ");


        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize());
        String sql = sbSql.toString();
        return sql;
    }

}
