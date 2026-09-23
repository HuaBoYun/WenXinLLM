package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblMonitorRuleMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-27
 */
public interface TblMonitorRuleMySqlMapper extends BaseMapper<TblMonitorRuleMySql> {

    @SelectProvider(type = TblMonitorRuleSqlMySqlConfig.class, method = "findTblMonitorRuleByUser")
    List<TblMonitorRuleMySql> findTblMonitorRuleByUser(PageInfo<TblMonitorRuleMySql> pageInfo, BigDecimal staffid);

    @Select(" SELECT count(*) from TBL_MONITOR_RULE mr  \n" +
            " LEFT JOIN TBL_MONITOR_SOLUTION_RULE msr on mr.RULEID=msr.RULEID  \n" +
            " LEFT JOIN TBL_MONITOR_SOLUTION tms ON msr.SOLUTIONID = tms.SOLUTIONID \n" +
            " LEFT JOIN TBL_MONITOR_SOLUTION_STAFF  sta on tms.SOLUTIONID = sta.SOLUTIONID\n" +
            " where tms.STAFFID= #{staffid} and tms.TYPE=1 ORDER BY MR.RULEID\n ")
    Integer findTblMonitorRuleByUserCount(BigDecimal staffid);

    @Select("SELECT * FROM TBL_MONITOR_RULE WHERE RULEID = #{ruleid}")
    TblMonitorRuleMySql findRuleid(BigDecimal ruleid);

    @SelectProvider(type = TblMonitorRuleSqlMySqlConfig.class, method = "findAll")
    List<TblMonitorRuleMySql> findAll(String solutionid, PageInfo<TblMonitorRuleMySql> pageInfo);

    //@Select("select COUNT(*) from TBL_MONITOR_RULE tmr left join TBL_MONITOR_PREWARNING tm on tmr.RULEID = tm.RULEID where tm.SIGNID like '% #{solutionid} %' and tm.resultCount >0  order by tmr.ruleid desc")
    @SelectProvider(type = TblMonitorRuleSqlMySqlConfig.class, method = "findAllCount")
    Integer findAllCount(String solutionid);
}
