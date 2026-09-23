package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblIndicator;
import com.huabo.system.entity.TblMonitorRule;

import java.math.BigDecimal;

public class TblMonitorRuleSqlConfig {

    public String findTblMonitorRuleByUser(IPage<TblMonitorRule> page, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("SELECT * from TBL_MONITOR_RULE mr " +
                " LEFT JOIN TBL_MONITOR_SOLUTION_RULE msr on mr.RULEID=msr.RULEID " +
                " LEFT JOIN TBL_MONITOR_SOLUTION tms ON msr.SOLUTIONID = tms.SOLUTIONID " +
                " LEFT JOIN TBL_MONITOR_SOLUTION_STAFF  sta on tms.SOLUTIONID = sta.SOLUTIONID" +
                " where tms.STAFFID= '"+staffid+"' and tms.TYPE=1 ORDER BY MR.RULEID " );
        String sql = sbSql.toString();
        return sql;
    }

    public String findAll(String solutionid, IPage<TblMonitorRule> page) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_MONITOR_RULE tmr left join TBL_MONITOR_PREWARNING tm on tmr.RULEID = tm.RULEID where tm.SIGNID like '% "+solutionid+" %' and tm.resultCount >0" );
        sbSql.append(" order by tmr.ruleid desc ");
        String sql = sbSql.toString();
        return sql;
    }

}
