package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblIndicator;
import com.huabo.system.entity.TblMonitorRule;
import com.huabo.system.entity.TblMonitorSolution;

import java.math.BigDecimal;

public class TblMonitorSolutionMapperSqlConfig {

	public String tblMonitorSolutionService(IPage<TblMonitorSolution> page, BigDecimal staffid, String type) {
        StringBuffer sbSql = new StringBuffer("SELECT * from TBL_MONITOR_SOLUTION where SOLUTIONID in ( SELECT SOLUTIONID FROM TBL_MONITOR_SOLUTION_STAFF WHERE STAFFID= '"+staffid+"' ) and TYPE= '"+type+"' ");
        String sql = sbSql.toString();
        return sql;
    }
}
