package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblMonitorIndicatorresultMySql;

import java.math.BigDecimal;

public class TblMonitorIndicatorresultMapperSqlMySqlConfig {


    public String getResultListJKZX(PageInfo<TblMonitorIndicatorresultMySql> pageInfo, BigDecimal indicatorid, BigDecimal solutionresultid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( SELECT TMI.* FROM TBL_MONITOR_INDICATORRESULT TMI where RESULTID =(select RESULTID RS from (SELECT RESULTID RS FROM TBL_MONITOR_INDICATORRESULT WHERE SOLUTIONRESULTID= '" + solutionresultid + "' and INDICATORID= '" + indicatorid + "' and SOURCE=3 order by savetime desc) as b ) ");


        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }
}
