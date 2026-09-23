package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblMonitorIndicatorresult;

import java.math.BigDecimal;

public class TblMonitorIndicatorresultMapperSqlConfig {

    public String getResultListJKZX(IPage<TblMonitorIndicatorresult> page, BigDecimal indicatorid, BigDecimal solutionresultid) throws Exception {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_MONITOR_INDICATORRESULT where RESULTID =(select RESULTID from (SELECT RESULTID FROM TBL_MONITOR_INDICATORRESULT WHERE SOLUTIONRESULTID= '"+solutionresultid+"' and INDICATORID= '"+indicatorid+"' and SOURCE=3 order by savetime desc) where "+DataBaseSqlConfig.getRowLimitSql(0, 1)+" ) ");

        String sql = sbSql.toString();
        return sql;
    }
}
