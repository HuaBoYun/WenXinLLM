package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorModel;

public class TblMonitorModelMapperSqlConfig {


    public String findByModelJKZX(String solutionid, IPage<TblMonitorModel> page) {
        StringBuffer sbSql = new StringBuffer("SELECT * from TBL_MONITOR_MODEL TMM LEFT JOIN TBL_MONITOR_SOLUTION_MODEL TMSM ON TMM.MODELID = TMSM.MODELID LEFT JOIN TBL_MONITOR_SOLUTION TMS ON TMSM.SOLUTIONID = TMS.SOLUTIONID WHERE TMS.SOLUTIONID = "+ solutionid);
        String sql = sbSql.toString();
        return sql;
    }
}
