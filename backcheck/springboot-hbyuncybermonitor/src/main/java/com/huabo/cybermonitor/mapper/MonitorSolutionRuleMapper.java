package com.huabo.cybermonitor.mapper;

import com.huabo.cybermonitor.entity.MonitorRule;
import com.huabo.cybermonitor.entity.MonitorSolutionRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-26
 */
public interface MonitorSolutionRuleMapper extends BaseMapper<MonitorSolutionRule> {


    @Select("select * from TBL_MONITOR_RULE where ruleid in ( " +
            "   select ruleid from TBL_MONITOR_SOLUTION_RULE where solutionid=#{solutionid} " +
            ")")
    public List<MonitorRule> queryRuleBySoluid(BigDecimal solutionid) ;
    
    

	@SelectProvider(method="selectListPageInfo",type=MonitorSolutionRuleMapperSqlConfig.class)
	List<Map<String,Object>> selectListPageInfo(PageInfo<Map<String,Object>> pageInfo, String sql) throws Exception;
	
	@SelectProvider(method="selectListPageInfocount",type=MonitorSolutionRuleMapperSqlConfig.class)
	Integer selectListPageInfocount( String sql) throws Exception; 



}
