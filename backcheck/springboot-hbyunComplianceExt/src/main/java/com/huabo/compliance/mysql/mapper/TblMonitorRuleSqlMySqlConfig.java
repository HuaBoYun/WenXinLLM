package com.huabo.compliance.mysql.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblMonitorRuleMySql;

import java.math.BigDecimal;

public class TblMonitorRuleSqlMySqlConfig {

    public String findTblMonitorRuleByUser(PageInfo<TblMonitorRuleMySql> pageInfo, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM (SELECT mr.* from TBL_MONITOR_RULE mr  \n" +
                " LEFT JOIN TBL_MONITOR_SOLUTION_RULE msr on mr.RULEID=msr.RULEID  \n" +
                " LEFT JOIN TBL_MONITOR_SOLUTION tms ON msr.SOLUTIONID = tms.SOLUTIONID \n" +
                " LEFT JOIN TBL_MONITOR_SOLUTION_STAFF  sta on tms.SOLUTIONID = sta.SOLUTIONID\n" +
                " where tms.STAFFID= '" + staffid + "' and tms.TYPE=1 ORDER BY MR.RULEID \n");


        sbSql.append(" ) BUDGET LIMIT "  + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() +  " ) as a ");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAll(String solutionid, PageInfo<TblMonitorRuleMySql> pageInfo) {
        StringBuffer sbSql = new StringBuffer("SELECT * FROM ( SELECT BUDGET.* FROM ( select tmr.* from TBL_MONITOR_RULE tmr left join TBL_MONITOR_PREWARNING tm on tmr.RULEID = tm.RULEID where tm.SIGNID like '% " + solutionid + " %' and tm.resultCount >0");


        sbSql.append(" order by tmr.ruleid desc ) BUDGET LIMIT " + pageInfo.getCurrentRecord() + " , " + pageInfo.getPageSize() + " ) as a");
        String sql = sbSql.toString();
        return sql;
    }

    public String findAllCount(String solutionid) {
        StringBuffer sbSql = new StringBuffer("select count(*) from TBL_MONITOR_RULE tmr left join TBL_MONITOR_PREWARNING tm on tmr.RULEID = tm.RULEID where tm.SIGNID like '% " + solutionid + " %' and tm.resultCount >0 order by tmr.ruleid desc ");


        // sbSql.append(" order by tmr.ruleid desc ");
        String sql = sbSql.toString();
        return sql;
    }
}
