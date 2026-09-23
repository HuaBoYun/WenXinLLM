package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-21
 */
public interface MonitorSolutionMapper extends BaseMapper<MonitorSolution> {


//SOLUTIONID
    //TBL_MONITOR_SOLUTION
    //TBL_MONITOR_SOLUTION_RULE
    @Select("select * from TBL_MONITOR_SOLUTION a left  join  TBL_MONITOR_SOLUTION_RULE b  on a.SOLUTIONID = b.SOLUTIONID")
    List<Indicator>  getMonitorSolutionIndicators();


   @Select("select tms.*,ts.REALNAME  from TBL_MONITOR_SOLUTION tms left join TBL_STAFF TS on tms.STAFFID = TS.STAFFID\n" +
           "where SOLUTIONID=#{id}")

     MonitorSolution queryOne(@Param("id") String selectedid);


     @Select("select * from TBL_MONITOR_RULE  tmr where tmr.RULEID in" +
             " (select RULEID from TBL_MONITOR_SOLUTION_RULE where SOLUTIONID=#{id}) ")
     List<MonitorRule> queryMonitorRuleList(@Param("id") String selectedid);
}
