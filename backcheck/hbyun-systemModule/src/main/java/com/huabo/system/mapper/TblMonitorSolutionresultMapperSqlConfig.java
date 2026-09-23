package com.huabo.system.mapper;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblMonitorSolutionresult;

public class TblMonitorSolutionresultMapperSqlConfig {

    public String findBySoultionIdZKZX(String zt, String table, BigDecimal soultionId, String rulid, IPage<TblMonitorSolutionresult> page) throws Exception {
        StringBuffer sbSql = new StringBuffer("select *  from '"+zt+"'.'"+table+"' where  EXECTIME = (SELECT SIGNID FROM TBL_MONITOR_PREWARNING WHERE SOLUTIONRESULTID = ( select SOLUTIONRESULTID from ( SELECT SOLUTIONRESULTID FROM TBL_MONITOR_SOLUTIONRESULT  WHERE SOURCE = 3 AND SOLUTIONID = '"+soultionId+"' order by savetime desc) where "+DataBaseSqlConfig.getRowLimitSql(0, 1)+" ) and RULEID= '"+rulid+"') ");
        String sql = sbSql.toString();
        return sql;
    }

}
