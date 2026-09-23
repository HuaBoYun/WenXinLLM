package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblIndicator;
import com.huabo.compliance.mysql.entity.TblIndicatorMySql;


import java.math.BigDecimal;

public class TblIndicatorMapperSqlMySqlConfig {


    public String findIndicatorByUseridAndSlouid(PageInfo<TblIndicator> pageInfo, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( SELECT ind.* from TBL_INDICATOR ind LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR msi on IND.INDICATORID=MSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMS.SOLUTIONID = TMS.SOLUTIONID LEFT JOIN TBL_MONITOR_SOLUTION_STAFF TMSS ON TMS.SOLUTIONID = TMSS.SOLUTIONID where TMSS.STAFFID= '" + staffid + "' and TMS.TYPE=2 ");


        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findIndicatorByJKZX(String solutionid, PageInfo<TblIndicatorMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( select TI.* from TBL_INDICATOR TI LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR TMSI ON TI.INDICATORID =TMSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSI.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID = " + solutionid);


        sbSql.append(" ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }
}
