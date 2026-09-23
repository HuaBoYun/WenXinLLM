package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.MonitorRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-22
 */
public interface MonitorRuleMapper extends BaseMapper<MonitorRule> {


    /**
     *
     * @param start 起始
     * @param end   结束
     * @param solutionid 编号
     */
    @Select("select * from (" +
            "select *,rownum st from TblMonitorRule t left join fetch  t.tblMonitorSolutionRules tm where tm.tblMonitorSolution.solutionid = #{solutionid}" +
            ") t where t.st >= #{start} and t.st < #{end}")
    List<Map<String,Object>> findRuleBySolut(@Param("start") long start, @Param("end") long end, @Param("solutionid") String solutionid);

    @Select("select count(1) num from TblMonitorRule t left join fetch  t.tblMonitorSolutionRules tm where tm.tblMonitorSolution.solutionid = #{solutionid}")
    int findCount( @Param("solutionid") String solutionid);

}
