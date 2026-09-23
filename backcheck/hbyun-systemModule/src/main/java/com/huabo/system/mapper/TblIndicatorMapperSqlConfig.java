package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblHomePageModel;
import com.huabo.system.entity.TblIndicator;

import java.math.BigDecimal;

public class TblIndicatorMapperSqlConfig {


    public String findIndicatorByUseridAndSlouid(IPage<TblIndicator> page, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("SELECT * from TBL_INDICATOR ind LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR msi on IND.INDICATORID=MSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMS.SOLUTIONID = TMS.SOLUTIONID LEFT JOIN TBL_MONITOR_SOLUTION_STAFF TMSS ON TMS.SOLUTIONID = TMSS.SOLUTIONID where TMSS.STAFFID= '"+staffid+"' and TMS.TYPE=2 " );


        sbSql.append(" ORDER BY ind.INDICATORID desc ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findIndicatorByJKZX(String solutionid, IPage<TblIndicator> page) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_INDICATOR TI LEFT JOIN TBL_MONITOR_SOLUTION_INDICATOR TMSI ON TI.INDICATORID =TMSI.INDICATORID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSI.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID ="+solutionid );
        String sql = sbSql.toString();
        return sql;
    }
}
