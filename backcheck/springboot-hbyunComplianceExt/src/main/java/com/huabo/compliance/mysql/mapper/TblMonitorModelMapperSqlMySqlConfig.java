package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblMonitorModelMySql;

public class TblMonitorModelMapperSqlMySqlConfig {


    public String findByModelJKZX(String solutionid, PageInfo<TblMonitorModelMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( SELECT TMM.* from TBL_MONITOR_MODEL TMM LEFT JOIN TBL_MONITOR_SOLUTION_MODEL TMSM ON TMM.MODELID = TMSM.MODELID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSM.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID = " + solutionid);

        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }
}
